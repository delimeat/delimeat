package io.delimeat.core.processor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.delimeat.core.show.Show;
import io.delimeat.core.show.ShowDao;

public class GuideProcessor_ImplTest {
  
	private GuideProcessor_Impl processor;

	@Before
	public void setUp() throws Exception {
		processor = new GuideProcessor_Impl();
	}

	@Test
	public void showTest() {
		Assert.assertNull(processor.getShow());
		Show show = new Show();
		processor.setShow(show);
		Assert.assertEquals(show, processor.getShow());
	}

	@Test
	public void showDaoTest() {
		Assert.assertNull(processor.getShowDao());
		ShowDao mockedDao = Mockito.mock(ShowDao.class);
		processor.setShowDao(mockedDao);
		Assert.assertEquals(mockedDao, processor.getShowDao());
	}  

	@Test
	public void guideDaoTest() {
		Assert.assertNull(processor.getShowDao());
		ShowDao mockedDao = Mockito.mock(ShowDao.class);
		processor.setShowDao(mockedDao);
		Assert.assertEquals(mockedDao, processor.getShowDao());
	}

  	@Test
	public void abortTest() {
		Assert.assertFalse(processor.isActive());
		processor.setActive(true);
		Assert.assertTrue(processor.isActive());
		processor.abort();
		Assert.assertFalse(processor.isActive());
	}

}
