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

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import io.delimeat.config.domain.Config;
import io.delimeat.feed.domain.FeedResult;
import io.delimeat.show.domain.Episode;

@Component
public class FeedProcessorFactory_Impl implements BeanFactoryAware, FeedProcessorFactory {

	private BeanFactory beanFactory; 
	
	@Autowired
	@Qualifier("preferFilesComparatorId")
	private Comparator<FeedResult> preferFilesComparator;
	@Autowired
	@Qualifier("maxSeedersComparatorId")
	private Comparator<FeedResult> maxSeedersComparator;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;		
	}
	
	public BeanFactory getBeanFactory(){
		return beanFactory;
	}

	public Comparator<FeedResult> getPreferFilesComparator() {
		return preferFilesComparator;
	}

	public void setPreferFilesComparator(
			Comparator<FeedResult> preferFilesComparator) {
		this.preferFilesComparator = preferFilesComparator;
	}

	public Comparator<FeedResult> getMaxSeedersComparator() {
		return maxSeedersComparator;
	}

	public void setMaxSeedersComparator(
			Comparator<FeedResult> maxSeedersComparator) {
		this.maxSeedersComparator = maxSeedersComparator;
	}

	/* (non-Javadoc)
	 * @see io.delimeat.server.processor.FeedProcessorFactory#build(io.delimeat.common.show.model.Episode, io.delimeat.common.config.model.Config)
	 */
	@Override
	public Processor build(Episode episode, Config config) {
		final FeedProcessor_Impl processor = (FeedProcessor_Impl)beanFactory.getBean(FeedProcessor_Impl.class);
	
		processor.setProcessEntity(episode);
		processor.setConfig(config);

		// set file comparator
		if (config.isPreferFiles() == true) {
			processor.setResultComparator(preferFilesComparator);
		} else {
			processor.setResultComparator(maxSeedersComparator);
		}

		return processor;
	}
	
}
