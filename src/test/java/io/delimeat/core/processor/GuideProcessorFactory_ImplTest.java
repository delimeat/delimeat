package io.delimeat.core.processor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.delimeat.core.config.Config;
import io.delimeat.core.guide.GuideInfoDao;
import io.delimeat.core.show.Show;
import io.delimeat.core.show.ShowDao;

public class GuideProcessorFactory_ImplTest {
  
	public GuideProcessorFactory_Impl factory;
	
	@Before
	public void setUp() throws Exception {
		factory = new GuideProcessorFactory_Impl();
	}
	
	@Test
	public void showDaoTest(){
		Assert.assertNull(factory.getShowDao());
		ShowDao dao = Mockito.mock(ShowDao.class);
		factory.setShowDao(dao);
		Assert.assertEquals(dao, factory.getShowDao());
	}
  
	@Test
	public void guideDaoTest(){
		Assert.assertNull(factory.getGuideDao());
		GuideInfoDao dao = Mockito.mock(GuideInfoDao.class);
		factory.setGuideDao(dao);
		Assert.assertEquals(dao, factory.getGuideDao());
	}
  
	@Test
	public void buildSeasonTest(){
		ShowDao showDao = Mockito.mock(ShowDao.class);
		factory.setShowDao(showDao);	
	
		GuideInfoDao guideDao = Mockito.mock(GuideInfoDao.class);
		factory.setGuideDao(guideDao);
		
		Show show = new Show();
		
		Config config = new Config();
     
		Processor processor = factory.build(show, config);
		Assert.assertTrue(processor instanceof GuideProcessor_Impl);
		GuideProcessor_Impl castProcessor = (GuideProcessor_Impl)processor;
		Assert.assertEquals(show, castProcessor.getShow());
		Assert.assertEquals(showDao, castProcessor.getShowDao());
      Assert.assertEquals(guideDao, castProcessor.getGuideDao());
	}
}
