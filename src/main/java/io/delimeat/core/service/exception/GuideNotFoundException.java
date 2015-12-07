package io.delimeat.core.service.exception;

import io.delimeat.core.guide.GuideSource;

public class GuideNotFoundException extends ServiceException {

	private static final long serialVersionUID = 1L;

	public GuideNotFoundException(GuideSource source){
		//TODO i18n
		super("GuideSource "+ source + " not found");
	}
}
