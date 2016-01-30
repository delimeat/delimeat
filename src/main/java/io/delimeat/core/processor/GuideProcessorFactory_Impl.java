package io.delimeat.core.processor;

import io.delimeat.core.config.Config;
import io.delimeat.core.guide.GuideInfoDao;
import io.delimeat.core.processor.Processor;
import io.delimeat.core.show.Show;
import io.delimeat.core.show.ShowDao;

public class GuideProcessorFactory_Impl implements ProcessorFactory {

	private ShowDao showDao;
	private GuideInfoDao guideDao;

	public ShowDao getShowDao() {
		return showDao;
	}

	public void setShowDao(ShowDao showDao) {
		this.showDao = showDao;
	}

	public GuideInfoDao getGuideDao() {
		return guideDao;
	}

	public void setGuideDao(GuideInfoDao guideDao) {
		this.guideDao = guideDao;
	}

	@Override
	public Processor build(Show show, Config config) {
		
		GuideProcessor_Impl processor = new GuideProcessor_Impl();
		processor.setShow(show);
		processor.setShowDao(showDao);
		processor.setGuideDao(guideDao);
		return processor;
		
	}
}
