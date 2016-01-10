package io.delimeat.core.service;

import io.delimeat.core.config.Config;
import io.delimeat.core.config.ConfigDao;
import io.delimeat.core.config.ConfigNotFoundException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ConfigService_ImplTest {

	private ConfigService_Impl service;

	@Before
	public void setUp() throws Exception {
		service = new ConfigService_Impl();
	}

	@Test
	public void configDaoTest() {
		Assert.assertNull(service.getConfigDao());
		ConfigDao mockedDao = Mockito.mock(ConfigDao.class);
		service.setConfigDao(mockedDao);
		Assert.assertEquals(mockedDao, service.getConfigDao());
	}
	
	@Test
	public void defaultOutputDirectoryTest() {
		Assert.assertNull(service.getDefaultOutputDir());
		service.setDefaultOutputDir("DEFAULT_OUTPUT_DIR");
		Assert.assertEquals("DEFAULT_OUTPUT_DIR", service.getDefaultOutputDir());
	}
	
	@Test
	public void readTest() throws Exception {
		ConfigDao mockedDao = Mockito.mock(ConfigDao.class);
		Config mockedResult = Mockito.mock(Config.class);
		Mockito.when(mockedDao.read()).thenReturn(mockedResult);

		service.setConfigDao(mockedDao);

		Assert.assertEquals(mockedResult, service.read());
	}

	@Test
	public void readNotFoundTest() throws Exception {
		ConfigDao mockedDao = Mockito.mock(ConfigDao.class);
		ConfigNotFoundException exception = new ConfigNotFoundException(
				"exception");
		Mockito.when(mockedDao.read()).thenThrow(exception);

		service.setConfigDao(mockedDao);

		Assert.assertNotNull(service.read());
		Mockito.verify(mockedDao).update(Mockito.any(Config.class));

	}

	@Test
	public void updateTest() throws Exception {
		ConfigDao mockedDao = Mockito.mock(ConfigDao.class);
		service.setConfigDao(mockedDao);

		Config mockedConfig = Mockito.mock(Config.class);

		Config actualConfig = service.update(mockedConfig);
		Assert.assertEquals(mockedConfig,actualConfig);

		Mockito.verify(mockedDao).update(Mockito.any(Config.class));
	}

}
