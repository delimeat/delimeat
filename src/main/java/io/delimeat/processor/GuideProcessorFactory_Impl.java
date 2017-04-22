package io.delimeat.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import io.delimeat.config.domain.Config;
import io.delimeat.processor.Processor;
import io.delimeat.show.domain.Show;

public class GuideProcessorFactory_Impl implements BeanFactoryAware, GuideProcessorFactory {

  	private BeanFactory beanFactory;
  
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;		
	}
	
	public BeanFactory getBeanFactory(){
		return beanFactory;
	}

	/* (non-Javadoc)
	 * @see io.delimeat.server.processor.GuideProcessorFactory#build(io.delimeat.common.show.model.Show, io.delimeat.common.config.model.Config)
	 */
	@Override
	public Processor build(Show show, Config config) {
		
		GuideProcessor_Impl processor = (GuideProcessor_Impl)beanFactory.getBean("guideProcessorId");
		processor.setProcessEntity(show);
		return processor;
		
	}
}
