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
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

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
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuideItemProcessor_Impl implements ItemProcessor<Show> {

	@Autowired
	private ShowService showService;

	@Autowired
	private GuideService guideService;

	@Autowired
	private EpisodeService episodeService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	@Transactional
	@Override
	public void process(Show show) throws Exception {
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
			final List<Episode> showEps = episodeService.findByShow(show);

			// add new episodes that don't already exist
			updated = updated | createEpisodes(guideEps, showEps, show);

			// remove any episodes that aren't in the guide any more
			updated = updated | deleteEpisodes(guideEps, showEps);

			// update any episodes if they need an update
			updated = updated | updateEpisodes(guideEps, showEps);
		}

		Instant now = Instant.now();
		// once everything is done update the show
		if (updated == true) {
			show.setLastGuideUpdate(now);
		}
		show.setLastGuideCheck(now);
		showService.update(show);

	}

	public boolean createEpisodes(List<GuideEpisode> guideEps, List<Episode> showEps, Show show) {
		List<Episode> episodesToCreate = guideEps.stream()
											.filter(guideEp -> showEps.stream().noneMatch(ep->DelimeatUtils.equals(guideEp, ep)))
											.map(ShowUtils::fromGuideEpisode).map(ep -> {
												ep.setShow(show);
												return ep;
											})
											.collect(Collectors.toList());

		for (Episode ep : episodesToCreate) {
			episodeService.save(ep);
		}

		return !episodesToCreate.isEmpty();

	}

	public boolean deleteEpisodes(List<GuideEpisode> guideEps, List<Episode> showEps) {
		List<Long> episodesToDelete = showEps.stream()
											.filter(p -> p.getStatus().equals(EpisodeStatus.PENDING))
											.filter(ep-> guideEps.stream().noneMatch(guideEp->DelimeatUtils.equals(guideEp, ep)))
											.map(p ->p.getEpisodeId())
											.collect(Collectors.toList());

		for (Long epId : episodesToDelete) {
			episodeService.delete(epId);
		}

		return !episodesToDelete.isEmpty();
	}

	public boolean updateEpisodes(List<GuideEpisode> guideEps, List<Episode> showEps) {
		List<Episode> episodesToUpdate = guideEps.stream()
											.filter(guideEp -> !showEps.stream().noneMatch(ep->DelimeatUtils.equals(guideEp, ep)))
											.map(guideEp -> {
												Episode showEp = showEps.stream().filter(ep->DelimeatUtils.equals(guideEp, ep)).findFirst().get();
												if ((showEp.getStatus().equals(EpisodeStatus.PENDING)
														&& (!Objects.equals(showEp.getAirDate(), guideEp.getAirDate())
																|| !Objects.equals(showEp.getTitle(), guideEp.getTitle())))) {
													showEp.setAirDate(guideEp.getAirDate());
													showEp.setTitle(guideEp.getTitle());
													return showEp;
												}
												return null;
											})
											.filter(Objects::nonNull)
											.collect(Collectors.toList());

		for (Episode ep : episodesToUpdate) {
			episodeService.save(ep);
		}

		return !episodesToUpdate.isEmpty();
	}

}
