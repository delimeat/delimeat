package io.delimeat.core.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import io.delimeat.core.config.Config;
import io.delimeat.core.processor.Processor;
import io.delimeat.core.show.Show;

public class GuideProcessorFactory_Impl implements ProcessorFactory, BeanFactoryAware {

  	private BeanFactory beanFactory;
  
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;		
	}
	
	public BeanFactory getBeanFactory(){
		return beanFactory;
	}

	@Override
	public Processor build(Show show, Config config) {
		
		GuideProcessor_Impl processor = (GuideProcessor_Impl)beanFactory.getBean("guideProcessorId");
		processor.setShow(show);
		return processor;
		
	}
}
