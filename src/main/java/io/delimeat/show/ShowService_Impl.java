package io.delimeat.show;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.delimeat.guide.GuideService;
import io.delimeat.show.domain.Episode;
import io.delimeat.show.domain.EpisodeStatus;
import io.delimeat.show.domain.Show;

@Service("showServiceId")
public class ShowService_Impl implements ShowService {

	private static final String TITLE_REGEX = "(\\(\\d{4}\\))$|[^A-Za-z\\d\\s]";
	
	@Autowired
	private ShowRepository showRepository;
	
	@Autowired
	private EpisodeService episodeService;
	
	@Autowired
	private GuideService guideService;

	/**
	 * Get show dao
	 * @return ShowRepository
	 */
	public ShowRepository getShowRepository() {
		return showRepository;
	}

	/**
	 * Set show dao
	 * @param ShowRepository
	 */
	public void setShowRepository(ShowRepository ShowRepository) {
		this.showRepository = ShowRepository;
	}

	/**
	 * Get guide service
	 * @return guideService
	 */
	public GuideService getGuideService() {
		return guideService;
	}

	/**
	 * Set guide service
	 * @param guideService
	 */
	public void setGuideService(GuideService guideService) {
		this.guideService = guideService;
	}

	/**
	 * Get episode service
	 * @return episodeService
	 */
	public EpisodeService getEpisodeService() {
		return episodeService;
	}

	/**
	 * Set episode service
	 * @param episodeService
	 */
	public void setEpisodeService(EpisodeService episodeService) {
		this.episodeService = episodeService;
	}

	/* (non-Javadoc)
	 * @see io.delimeat.common.show.ShowService#create(io.delimeat.common.show.model.Show)
	 */
	@Override
	@Transactional
	public void create(Show show) throws Exception {

		getShowRepository().save(cleanTitle(show));
		final String guideId = show.getGuideId();

		if (guideId != null && guideId.length() > 0) {
			final List<Episode> episodes = guideService.readEpisodes(guideId).stream()
					.map(ShowUtils::fromGuideEpisode)
					.collect(Collectors.toList());
			
			Instant now = Instant.now();
			for(Episode episode: episodes){
				episode.setShow(show);
				
				Instant airDateTime = ShowUtils.determineAirTime(episode.getAirDate(), show.getAirTime(), show.getTimezone());
				if(airDateTime.isBefore(now)){
					episode.setStatus(EpisodeStatus.SKIPPED);
				}
				
				episodeService.save(episode);
			}

		}
	}

	/* (non-Javadoc)
	 * @see io.delimeat.common.show.ShowService#read(java.lang.Long)
	 */
	@Override
	@Transactional
	public Show read(Long id)  throws Exception  {
		return getShowRepository().findOne(id);
	}

	/* (non-Javadoc)
	 * @see io.delimeat.common.show.ShowService#readAll()
	 */
	@Override
	@Transactional
	public List<Show> readAll() throws Exception {
		return getShowRepository().findAll();
	}

	/* (non-Javadoc)
	 * @see io.delimeat.common.show.ShowService#update(io.delimeat.common.show.model.Show)
	 */
	@Override
	@Transactional
	public Show update(Show show)  throws Exception {
		return getShowRepository().save(show);
	}

	/* (non-Javadoc)
	 * @see io.delimeat.common.show.ShowService#delete(java.lang.Long)
	 */
	@Override
	@Transactional
	public void delete(Long id) throws Exception {
		getShowRepository().delete(id);
	}

	/* (non-Javadoc)
	 * @see io.delimeat.common.show.ShowService#readAllEpisodes(java.lang.Long)
	 */
	@Override
	@Transactional
	public List<Episode> readAllEpisodes(Long id) throws Exception {
		return getEpisodeService().findByShow(read(id));
	}
	
	
	/**
	 * Clean up a title remove any unwanted characters
	 * 
	 * @param show
	 * @return show
	 */
	public Show cleanTitle(Show show){
		final String originalTitle = show.getTitle() != null ? show.getTitle() : "";
		String cleanedTitle = originalTitle.replaceAll(TITLE_REGEX, "").trim();
		show.setTitle(cleanedTitle);
		return show;
	}

}
