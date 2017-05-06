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

import java.util.Comparator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.BeanFactory;

import io.delimeat.config.domain.Config;
import io.delimeat.feed.domain.FeedResult;
import io.delimeat.show.domain.Episode;
import io.delimeat.show.domain.Show;
import io.delimeat.show.domain.ShowType;

public class FeedProcessorFactory_ImplTest {

	public FeedProcessorFactory_Impl factory;
	
	@Before
	public void setUp() throws Exception {
		factory = new FeedProcessorFactory_Impl();
	}

	@Test
	public void beanFactoryTest(){
		Assert.assertNull(factory.getBeanFactory());
		BeanFactory beanFactory = Mockito.mock(BeanFactory.class);
		factory.setBeanFactory(beanFactory);
		Assert.assertEquals(beanFactory, factory.getBeanFactory());
	}
	
	@Test
	public void preferFilesComparatorTest(){
		Assert.assertNull(factory.getPreferFilesComparator());
		@SuppressWarnings("unchecked")
		Comparator<FeedResult> comparator = Mockito.mock(Comparator.class);
		factory.setPreferFilesComparator(comparator);
		Assert.assertEquals(comparator, factory.getPreferFilesComparator());
	}
	
	@Test
	public void maxSeedersComparatorTest(){
		Assert.assertNull(factory.getMaxSeedersComparator());
		@SuppressWarnings("unchecked")
		Comparator<FeedResult> comparator = Mockito.mock(Comparator.class);
		factory.setMaxSeedersComparator(comparator);
		Assert.assertEquals(comparator, factory.getMaxSeedersComparator());
	}
	
	@Test
	public void buildPreferFilesTest(){
		
		BeanFactory beanFactory = Mockito.mock(BeanFactory.class);
		FeedProcessor_Impl actualprocessor = new FeedProcessor_Impl();
		Mockito.when(beanFactory.getBean(FeedProcessor_Impl.class)).thenReturn(actualprocessor);
		factory.setBeanFactory(beanFactory);

		
		@SuppressWarnings("unchecked")
		Comparator<FeedResult> comparator = Mockito.mock(Comparator.class);
		factory.setPreferFilesComparator(comparator);
		
		Show show = new Show();
		show.setShowType(ShowType.SEASON);
		
		Episode episode = new Episode();
		episode.setShow(show);
		
		Config config = new Config();
		config.setIgnoreFolders(true);
		config.setPreferFiles(true);
		
		Processor processor = factory.build(episode, config);
		Assert.assertTrue(processor instanceof FeedProcessor_Impl);
		FeedProcessor_Impl castProcessor = (FeedProcessor_Impl)processor;

     	Assert.assertEquals(episode, castProcessor.getProcessEntity());
		Assert.assertEquals(config, castProcessor.getConfig());
		Assert.assertEquals(comparator, castProcessor.getResultComparator());

	}
  
	@Test
	public void buildMaxSeedersTest(){
		
		BeanFactory beanFactory = Mockito.mock(BeanFactory.class);
		FeedProcessor_Impl actualprocessor = new FeedProcessor_Impl();
		Mockito.when(beanFactory.getBean(FeedProcessor_Impl.class)).thenReturn(actualprocessor);
		factory.setBeanFactory(beanFactory);
		
		@SuppressWarnings("unchecked")
		Comparator<FeedResult> comparator = Mockito.mock(Comparator.class);
		factory.setMaxSeedersComparator(comparator);
		
		Show show = new Show();
		show.setShowType(ShowType.SEASON);
		
		Episode episode = new Episode();
		episode.setShow(show);
		
		Config config = new Config();
		config.setIgnoreFolders(false);
		config.setPreferFiles(false);
		
		Processor processor = factory.build(episode, config);
		Assert.assertTrue(processor instanceof FeedProcessor_Impl);
		FeedProcessor_Impl castProcessor = (FeedProcessor_Impl)processor;
		Assert.assertEquals(episode, castProcessor.getProcessEntity());
		Assert.assertEquals(config, castProcessor.getConfig());

		Assert.assertEquals(0, castProcessor.getTorrentValidators().size());
		Assert.assertEquals(comparator, castProcessor.getResultComparator());

	}
}
