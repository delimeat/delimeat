package io.delimeat.core.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.Executor;

import io.delimeat.core.config.ConfigDao;
import io.delimeat.core.processor.ProcessorFactory;
import io.delimeat.core.show.ShowDao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ProcessorService_ImplTest {

	private ProcessorService_Impl service;
	
	@Before
	public void setUp() throws Exception {
		service = new ProcessorService_Impl();
	}
	
	@Test
	public void showDaoTest() {
		Assert.assertNull(service.getShowDao());
		ShowDao mockedShowDao = Mockito.mock(ShowDao.class);
		service.setShowDao(mockedShowDao);
		Assert.assertEquals(mockedShowDao, service.getShowDao());
	}
	
	@Test
	public void configDaoTest() {
		Assert.assertNull(service.getConfigDao());
		ConfigDao mockedDao = Mockito.mock(ConfigDao.class);
		service.setConfigDao(mockedDao);
		Assert.assertEquals(mockedDao, service.getConfigDao());
	}
	
	@Test
	public void executorTest() {
		Assert.assertNull(service.getExecutor());
		Executor executor = Mockito.mock(Executor.class);
		service.setExecutor(executor);
		Assert.assertEquals(executor, service.getExecutor());
	}
	
	@Test
	public void feedProcessorFactoryTest() {
		Assert.assertNull(service.getFeedProcessorFactory());
		ProcessorFactory factory = Mockito.mock(ProcessorFactory.class);
		service.setFeedProcessorFactory(factory);
		Assert.assertEquals(factory, service.getFeedProcessorFactory());
	}
	
	@Test
	public void guideProcessorFactoryTest() {
		Assert.assertNull(service.getGuideProcessorFactory());
		ProcessorFactory factory = Mockito.mock(ProcessorFactory.class);
		service.setGuideProcessorFactory(factory);
		Assert.assertEquals(factory, service.getGuideProcessorFactory());
	}
	
	@Test
	public void test() throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = sdf.parse("2016-01-30");
		
		sdf.setTimeZone(TimeZone.getTimeZone("EST"));
		Date date2 = sdf.parse("2016-01-30");
		
		Date date3 = new Date();
		
		System.out.println(date1);
		System.out.println(date2);
		System.out.println(date3);
		
		long offset = -(5*60*60*1000);
		Date date4 = new Date(date1.getTime() + offset);
		System.out.println(date4);
		
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date5 = sdf.parse("2016-01-30");
		System.out.println(date5);
		
		Date date6 = new Date(date5.getTime() - offset);
		System.out.println(date6);
	}
}
