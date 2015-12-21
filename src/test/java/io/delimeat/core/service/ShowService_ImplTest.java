package io.delimeat.core.service;

import java.util.ArrayList;
import java.util.List;

import io.delimeat.core.show.Show;
import io.delimeat.core.show.ShowDao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ShowService_ImplTest {

	private ShowService_Impl service;

	@Before
	public void setUp() {
		service = new ShowService_Impl();
	}

	@Test
	public void showDaoTest() {
		Assert.assertNull(service.getShowDao());
		ShowDao mockedShowDao = Mockito.mock(ShowDao.class);
		service.setShowDao(mockedShowDao);
		Assert.assertEquals(mockedShowDao, service.getShowDao());
	}

	@Test
	public void createTest() throws Exception {
		ShowDao mockedShowDao = Mockito.mock(ShowDao.class);
		Show mockedShow = Mockito.mock(Show.class);
		Mockito.when(mockedShowDao.create(Mockito.any(Show.class))).thenReturn(
				mockedShow);
		service.setShowDao(mockedShowDao);

		Show actualShow = service.create(new Show());
		Assert.assertEquals(mockedShow, actualShow);
	}

	@Test
	public void readTest() throws Exception {
		ShowDao mockedShowDao = Mockito.mock(ShowDao.class);
		Show mockedShow = Mockito.mock(Show.class);
		Mockito.when(mockedShowDao.read(Mockito.anyLong())).thenReturn(
				mockedShow);
		service.setShowDao(mockedShowDao);

		Show actualShow = service.read(Long.MAX_VALUE);
		Assert.assertEquals(mockedShow, actualShow);
	}

	@Test
	public void readAllTest() throws Exception {
		ShowDao mockedShowDao = Mockito.mock(ShowDao.class);
		List<Show> expectedShows = new ArrayList<Show>();
		Show mockedShow = Mockito.mock(Show.class);
		expectedShows.add(mockedShow);
		Mockito.when(mockedShowDao.readAll()).thenReturn(expectedShows);
		service.setShowDao(mockedShowDao);

		List<Show> actualShows = service.readAll();
		Assert.assertEquals(1, actualShows.size());
		Assert.assertEquals(mockedShow, actualShows.get(0));
	}

	@Test
	public void updateTest() throws Exception {
		ShowDao mockedShowDao = Mockito.mock(ShowDao.class);
		Show mockedShow = Mockito.mock(Show.class);
		Mockito.when(mockedShowDao.update(Mockito.any(Show.class))).thenReturn(
				mockedShow);
		service.setShowDao(mockedShowDao);

		Show actualShow = service.update(new Show());
		Assert.assertEquals(mockedShow, actualShow);
	}

	@Test
	public void deleteTest() throws Exception {
		ShowDao mockedShowDao = Mockito.mock(ShowDao.class);
		service.setShowDao(mockedShowDao);
		service.delete(Long.MAX_VALUE);
		Mockito.verify(mockedShowDao).delete(Mockito.anyLong());
	}
}