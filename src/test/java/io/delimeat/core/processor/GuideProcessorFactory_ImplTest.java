package io.delimeat.core.processor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.BeanFactory;

import io.delimeat.core.config.Config;
import io.delimeat.core.show.Show;

public class GuideProcessorFactory_ImplTest {
  
	public GuideProcessorFactory_Impl factory;
	
	@Before
	public void setUp() throws Exception {
		factory = new GuideProcessorFactory_Impl();
	}

	@Test
	public void beanFactoryTest(){
		Assert.assertNull(factory.getBeanFactory());
		BeanFactory beanFactory = Mockito.mock(BeanFactory.class);
		factory.setBeanFactory(beanFactory);
		Assert.assertEquals(beanFactory, factory.getBeanFactory());
	}
  
	@Test
	public void buildTest(){
		BeanFactory beanFactory = Mockito.mock(BeanFactory.class);
		GuideProcessor_Impl actualprocessor = new GuideProcessor_Impl();
		Mockito.when(beanFactory.getBean(Mockito.anyString())).thenReturn(actualprocessor);
		factory.setBeanFactory(beanFactory);
		
		Show show = new Show();
		
		Config config = new Config();
     
		Processor processor = factory.build(show, config);
     	Assert.assertEquals(actualprocessor, processor);
		Assert.assertTrue(processor instanceof GuideProcessor_Impl);
		GuideProcessor_Impl castProcessor = (GuideProcessor_Impl)processor;
		Assert.assertEquals(show, castProcessor.getShow());
	}
}
