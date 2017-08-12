/*
 * Copyright 2013-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.delimeat.processor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import io.delimeat.guide.GuideService;
import io.delimeat.guide.domain.GuideEpisode;
import io.delimeat.guide.domain.GuideInfo;
import io.delimeat.show.EpisodeService;
import io.delimeat.show.ShowService;
import io.delimeat.show.ShowUtils;
import io.delimeat.show.domain.Episode;
import io.delimeat.show.domain.EpisodeStatus;
import io.delimeat.show.domain.Show;
import io.delimeat.util.DelimeatUtils;

@Component
@Scope("prototype")
public class GuideItemProcessor_Impl implements ItemProcessor<Show> {

  	private static final Logger LOGGER = LoggerFactory.getLogger(GuideItemProcessor_Impl.class);

	@Autowired
	private ShowService showService;

	@Autowired
	private GuideService guideService;

	@Autowired
	private EpisodeService episodeService;

	/**
	 * @return the showService
	 */
	public ShowService getShowService() {
		return showService;
	}

	/**
	 * @param showService the showService to set
	 */
	public void setShowService(ShowService showService) {
		this.showService = showService;
	}

	/**
	 * @return the guideService
	 */
	public GuideService getGuideService() {
		return guideService;
	}

	/**
	 * @param guideService the guideService to set
	 */
	public void setGuideService(GuideService guideService) {
		this.guideService = guideService;
	}

	/**
	 * @return the episodeService
	 */
	public EpisodeService getEpisodeService() {
		return episodeService;
	}

	/**
	 * @param episodeService the episodeService to set
	 */
	public void setEpisodeService(EpisodeService episodeService) {
		this.episodeService = episodeService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	@Transactional(TxType.REQUIRES_NEW)
	@Override
	public void process(Show show) throws Exception {
    	LOGGER.debug(String.format("starting guide item processor for %s", show.getTitle()));

		GuideInfo info = guideService.read(show.getGuideId());
		boolean updated = false;

		// only update if the info is newer than the last update
		LocalDate infoLastUpdatedDate = Optional.ofNullable(info.getLastUpdated()).orElse(LocalDate.ofEpochDay(0));
		Instant infoLastUpdated = LocalDateTime.of(infoLastUpdatedDate, LocalTime.MIDNIGHT).toInstant(ZoneOffset.UTC);
		Instant showLastUpdated = Optional.ofNullable(show.getLastGuideUpdate()).orElse(Instant.EPOCH);

		// determine if updates exist to process
		if (infoLastUpdated.isAfter(showLastUpdated) == true) {

			// update the airing status
			if (show.isAiring() != info.isAiring()) {
				show.setAiring(info.isAiring());
				updated = true;
			}

			// get the episodes and refresh them
			final List<GuideEpisode> guideEps = guideService.readEpisodes(show.getGuideId());

			// get the existing episodes
			final List<Episode> showEps = episodeService.findByShow(show.getShowId());

			// add new episodes that don't already exist
			updated = updated | createEpisodes(guideEps, showEps, show);

			// remove any episodes that aren't in the guide any more
			updated = updated | deleteEpisodes(guideEps, showEps);

			// update any episodes if they need an update
			updated = updated | updateEpisodes(guideEps, showEps);
		}

		// once everything is done update the show
		Instant now = Instant.now();
		if (updated == true) {
			show.setLastGuideUpdate(now);
		}
		show.setLastGuideCheck(now);
		showService.update(show);

    	LOGGER.debug(String.format("ending guide item processor for %s", show.getTitle()));	
	}

	public boolean createEpisodes(List<GuideEpisode> guideEps, List<Episode> showEps, Show show) {
		List<Episode> episodesToCreate = guideEps.stream()
											.filter(guideEp -> showEps.stream().noneMatch(ep->DelimeatUtils.equals(guideEp, ep)))
											.map(ShowUtils::fromGuideEpisode)
											.map(ep -> {
												ep.setShow(show);
												return ep;
											})
											.collect(Collectors.toList());

		for (Episode ep : episodesToCreate) {
			episodeService.update(ep);
		}

		return !episodesToCreate.isEmpty();

	}

	public boolean deleteEpisodes(List<GuideEpisode> guideEps, List<Episode> showEps) {
		List<Long> episodesToDelete = showEps.stream()
											.filter(ep -> EpisodeStatus.PENDING.equals(ep.getStatus()))
											.filter(ep-> guideEps.stream().noneMatch(guideEp->DelimeatUtils.equals(guideEp, ep)))
											.map(p ->p.getEpisodeId())
											.collect(Collectors.toList());

		for (Long epId : episodesToDelete) {
			episodeService.delete(epId);
		}

		return !episodesToDelete.isEmpty();
	}

	/**
	 * Update pending episodes when the air date or title change
	 * 
	 * @param guideEps
	 * @param showEps
	 * @return if there were any updates
	 */
	public boolean updateEpisodes(List<GuideEpisode> guideEps, List<Episode> showEps) {
		List<Episode> pendingShowEps = showEps.stream()
												.filter(ep->EpisodeStatus.PENDING.equals(ep.getStatus()))
												.collect(Collectors.toList());
		
		List<Episode> episodesToUpdate = guideEps.stream()
											.map(guideEp->determineEpisodeToUpdate(guideEp,pendingShowEps))
											.filter(Objects::nonNull)
											.collect(Collectors.toList());

		for (Episode ep : episodesToUpdate) {
			episodeService.update(ep);
		}

		return !episodesToUpdate.isEmpty();
	}
	
	/**
	 * Determine episode to update when air date or title have changed
	 * 
	 * @param guideEp
	 * @param showEps
	 * @return showEp to update
	 */
	public Episode determineEpisodeToUpdate(GuideEpisode guideEp, List<Episode> showEps){
		try{
			Episode showEp = showEps.stream()
									.filter(ep->DelimeatUtils.equals(guideEp, ep))
									.filter(ep->!Objects.equals(ep.getAirDate(), guideEp.getAirDate())
												|| ! Objects.equals(ep.getTitle(), guideEp.getTitle()))
									.findFirst()
									.get();
			
			showEp.setAirDate(guideEp.getAirDate());
			showEp.setTitle(guideEp.getTitle());
			return showEp;
			
		}catch(NoSuchElementException ex){
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GuideItemProcessor_Impl [" + (showService != null ? "showService=" + showService + ", " : "")
				+ (guideService != null ? "guideService=" + guideService + ", " : "")
				+ (episodeService != null ? "episodeService=" + episodeService : "") + "]";
	}

}
