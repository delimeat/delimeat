/*
 * Copyright 2013-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.delimeat.processor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.BeanFactory;

import io.delimeat.config.domain.Config;
import io.delimeat.processor.GuideProcessorFactory_Impl;
import io.delimeat.processor.GuideProcessor_Impl;
import io.delimeat.processor.Processor;
import io.delimeat.show.domain.Show;

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
		Mockito.when(beanFactory.getBean(GuideProcessor_Impl.class)).thenReturn(actualprocessor);
		factory.setBeanFactory(beanFactory);
		
		Show show = new Show();
		
		Config config = new Config();
     
		Processor processor = factory.build(show, config);
     	Assert.assertEquals(actualprocessor, processor);
		Assert.assertTrue(processor instanceof GuideProcessor_Impl);
		GuideProcessor_Impl castProcessor = (GuideProcessor_Impl)processor;
		Assert.assertEquals(show, castProcessor.getProcessEntity());
	}
}
