package io.delimeat.core.service;

import io.delimeat.core.show.Show;
import io.delimeat.core.show.ShowDao;
import io.delimeat.core.show.ShowGuideSource;

import java.util.List;

import javax.transaction.Transactional;

@Transactional
public class ShowService_Impl implements ShowService {

	private ShowDao showDao;

	public ShowDao getShowDao() {
		return showDao;
	}

	public void setShowDao(ShowDao showDao) {
		this.showDao = showDao;
	}

	@Override
	public Show create(Show show) throws Exception {
		return getShowDao().createOrUpdate(mergeShow(show));
	}

	@Override
	public Show read(Long id) throws Exception {
		return getShowDao().read(id);
	}

	@Override
	public List<Show> readAll() throws Exception {
		return getShowDao().readAll();
	}

	@Override
	public Show update(Show show) throws Exception {
		return getShowDao().createOrUpdate(mergeShow(show));
	}

	@Override
	public void delete(Long id) throws Exception {
		getShowDao().delete(id);
	}
	
	private Show mergeShow(Show show){
		if (show.getGuideSources() != null && show.getGuideSources().size() > 0) {
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
