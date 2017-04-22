package io.delimeat.processor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.base.MoreObjects;

import io.delimeat.guide.GuideService;
import io.delimeat.guide.domain.GuideEpisode;
import io.delimeat.guide.domain.GuideInfo;
import io.delimeat.show.EpisodeService;
import io.delimeat.show.ShowService;
import io.delimeat.show.ShowUtils;
import io.delimeat.show.domain.Episode;
import io.delimeat.show.domain.EpisodeStatus;
import io.delimeat.show.domain.Show;

public class GuideProcessor_Impl extends AbstractProcessor<Show> implements Processor {

	private ShowService showService;
	private EpisodeService episodeService;
	private GuideService guideService;

	public ShowService getShowService() {
		return showService;
	}

	public void setShowService(ShowService showService) {
		this.showService = showService;
	}

	
	public EpisodeService getEpisodeService() {
		return episodeService;
	}

	public void setEpisodeService(EpisodeService episodeService) {
		this.episodeService = episodeService;
	}

	public GuideService getGuideService() {
		return guideService;
	}

	public void setGuideService(GuideService guideService) {
		this.guideService = guideService;
	}
	
	@Override
	public void doProcessing() throws Exception {
		final long showId = processEntity.getShowId();
		final Show lockedShow = showService.read(showId);
		final String guideId = lockedShow.getGuideId();
		
		checkThreadStatus();

		boolean updated = false;
		final List<Episode> createEps = new ArrayList<>();
		final List<Episode> removeEps = new ArrayList<>();
		final List<Episode> updateEps = new ArrayList<>();
		
		// get the info
		final GuideInfo info = guideService.read(guideId);
		
		// only update if the info is newer than the last update
		LocalDate infoLastUpdatedDate = Optional.ofNullable(info.getLastUpdated()).orElse(LocalDate.ofEpochDay(0));
		Instant infoLastUpdated = LocalDateTime.of(infoLastUpdatedDate, LocalTime.MIDNIGHT).toInstant(ZoneOffset.UTC);
		Instant showLastUpdated = Optional.ofNullable(lockedShow.getLastGuideUpdate()).orElse(Instant.EPOCH);

		// determine if updates exist to process
		if (infoLastUpdated.isAfter(showLastUpdated) == true) {

			// update the airing status
			if (lockedShow.isAiring() != info.isAiring()) {
				lockedShow.setAiring(info.isAiring());
				updated = true;
			}

			// get the episodes and refresh them
			final List<GuideEpisode> guideEps = guideService.readEpisodes(guideId);
			
			// get the existing episodes
			final List<Episode> showEps = showService.readAllEpisodes(showId);
							
			// add new episodes that don't already exist
			createEps.addAll(getEpisodesToCreate(guideEps, showEps));
		
			// remove any episodes that aren't in the guide any more
			removeEps.addAll(getEpisodesToDelete(guideEps, showEps));
			
			// update any episodes if they need an update
			updateEps.addAll(getEpisodesToUpdate(guideEps, showEps));
		}
		
		checkThreadStatus();
		
		// once everything is done update the show
		Instant now = Instant.now();
		if (updated == true || createEps.isEmpty() == false || removeEps.isEmpty() == false || updateEps.isEmpty() == false) {
			
			for(Episode episode: removeEps){
				episodeService.delete(episode.getEpisodeId());
			}
			
			for(Episode episode: createEps){
				episode.setShow(lockedShow);
				episodeService.save(episode);
			}
			
			for(Episode episode: updateEps){
				episodeService.save(episode);
			}
			
			lockedShow.setLastGuideUpdate(now);
		}
		
		checkThreadStatus();
		
		lockedShow.setLastGuideCheck(now);
		showService.update(lockedShow);
	}
	
	public List<Episode> getEpisodesToCreate(List<GuideEpisode> guideEps, List<Episode> showEps){
		return guideEps.stream()
				.filter(p->showEps.contains(p) == false)
				.map(ShowUtils::fromGuideEpisode)
				.collect(Collectors.toList());	
	}
	
	public List<Episode> getEpisodesToDelete(List<GuideEpisode> guideEps, List<Episode> showEps){
		return showEps.stream()
				.filter(p->p.getStatus().equals(EpisodeStatus.PENDING))
				.filter(p->guideEps.contains(p) == false)
				.collect(Collectors.toList());
	}
	
	public List<Episode> getEpisodesToUpdate(List<GuideEpisode> guideEps, List<Episode> showEps){
		return guideEps.stream()
			.filter(p->showEps.contains(p))
			.map(p->{
				Episode showEp = showEps.get(showEps.indexOf(p));
				if((showEp.getStatus().equals(EpisodeStatus.PENDING) 
						&& (!Objects.equals(showEp.getAirDate(), p.getAirDate()) 
								|| !Objects.equals(showEp.getTitle(), p.getTitle()))
						)){
					showEp.setAirDate(p.getAirDate());
					showEp.setTitle(p.getTitle());
					return showEp;
				}					
				return null;
				
			})
			.filter(Objects::nonNull)
			.collect(Collectors.toList());
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("processEntity", processEntity)
				.add("config", config)
				.add("active", active)
				.add("listeners", listeners)
				.add("showService", showService)
				.add("guideService", guideService)
				.omitNullValues()
				.toString();
	}

}