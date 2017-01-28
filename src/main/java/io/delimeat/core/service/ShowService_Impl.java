package io.delimeat.core.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import io.delimeat.core.guide.GuideDao;
import io.delimeat.core.guide.GuideException;
import io.delimeat.core.show.Episode;
import io.delimeat.core.show.Show;
import io.delimeat.core.show.ShowConcurrencyException;
import io.delimeat.core.show.ShowDao;
import io.delimeat.core.show.ShowException;
import io.delimeat.core.show.ShowNotFoundException;

public class ShowService_Impl implements ShowService {

	private ShowDao showDao;
	private GuideDao guideDao;

	public ShowDao getShowDao() {
		return showDao;
	}

	public void setShowDao(ShowDao showDao) {
		this.showDao = showDao;
	}

	public GuideDao getGuideDao() {
		return guideDao;
	}

	public void setGuideDao(GuideDao guideDao) {
		this.guideDao = guideDao;
	}

	@Override
	@Transactional
	public Show create(Show show) throws GuideException, ShowConcurrencyException, ShowException {

		final Show createdShow = getShowDao().createOrUpdate(prepareShow(show));

		final String guideId = createdShow.getGuideId();

		if (guideId != null && guideId.length() > 0) {
			final Episode nextEp = createdShow.getNextEpisode();
			final Episode prevEp = createdShow.getPreviousEpisode();
			final List<Episode> createEpisodes = guideDao.episodes(guideId).stream()
					.filter(p -> (p.getSeasonNum() != null && p.getSeasonNum() != 0 && p.getAirDate() != null)).sorted()
					.filter(p -> (nextEp == null || !nextEp.equals(p)))
					.filter(p -> (prevEp == null || !prevEp.equals(p)))
					.map(Episode::new)
					.collect(Collectors.toList());
			
			createEpisodes.forEach(p -> p.setShow(createdShow));

			showDao.createOrUpdateEpisodes(createEpisodes);

		}

		return createdShow;
	}

	@Override
	@Transactional
	public Show read(Long id) throws ShowNotFoundException, ShowConcurrencyException, ShowException {
		return getShowDao().read(id);
	}

	@Override
	@Transactional
	public List<Show> readAll() throws ShowException {
		return getShowDao().readAll();
	}

	@Override
	@Transactional
	public Show update(Show show) throws ShowConcurrencyException, ShowException {
		return getShowDao().createOrUpdate(prepareShow(show));
	}

	@Override
	@Transactional
	public void delete(Long id) throws ShowNotFoundException, ShowException {
		getShowDao().delete(id);
	}

	public Show prepareShow(Show show) {
		if (show.getNextEpisode() != null) {
			show.getNextEpisode().setShow(show);
		}

		if (show.getPreviousEpisode() != null) {
			show.getPreviousEpisode().setShow(show);
		}
		return show;
	}

	@Override
	@Transactional
	public List<Episode> readAllEpisodes(Long id) throws ShowNotFoundException, ShowException {
		return getShowDao().readAllEpisodes(id);
	}

}
