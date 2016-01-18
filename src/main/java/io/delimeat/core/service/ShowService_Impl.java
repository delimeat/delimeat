package io.delimeat.core.service;

import io.delimeat.core.show.Show;
import io.delimeat.core.show.ShowConcurrencyException;
import io.delimeat.core.show.ShowDao;
import io.delimeat.core.show.ShowException;
import io.delimeat.core.show.ShowGuideSource;
import io.delimeat.core.show.ShowNotFoundException;
import io.delimeat.util.DelimeatUtils;

import java.util.List;

public class ShowService_Impl implements ShowService {

	private ShowDao showDao;

	public ShowDao getShowDao() {
		return showDao;
	}

	public void setShowDao(ShowDao showDao) {
		this.showDao = showDao;
	}

	@Override
	public Show create(Show show) throws ShowConcurrencyException, ShowException {
		return getShowDao().createOrUpdate(prepareShow(show));
	}

	@Override
	public Show read(Long id) throws ShowNotFoundException, ShowConcurrencyException, ShowException {
		return getShowDao().read(id);
	}

	@Override
	public List<Show> readAll() throws ShowException {
		return getShowDao().readAll();
	}

	@Override
	public Show update(Show show) throws ShowConcurrencyException, ShowException {
		return getShowDao().createOrUpdate(prepareShow(show));
	}

	@Override
	public void delete(Long id) throws ShowNotFoundException, ShowException {
		getShowDao().delete(id);
	}
	
	public Show prepareShow(Show show){
		if (!DelimeatUtils.isCollectionEmpty(show.getGuideSources())) {
			for (ShowGuideSource guideSource : show.getGuideSources()) {
				guideSource.setShow(show);
			}
		}
		
		if (show.getNextEpisode() != null) {
			show.getNextEpisode().setShow(show);
		}
		
		if (show.getPreviousEpisode() != null) {
			show.getPreviousEpisode().setShow(show);
		}
		return show;
	}

}
