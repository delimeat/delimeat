package io.delimeat.core.service;

import io.delimeat.core.show.Show;
import io.delimeat.core.show.ShowDao;

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
		return getShowDao().create(show);
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
		return getShowDao().update(show);
	}

	@Override
	public void delete(Long id) throws Exception {
		getShowDao().delete(id);
	}

}
