package io.delimeat.core.guide;

public class GuideNotAuthorisedException extends GuideException {

	static final long serialVersionUID = -1;

	public GuideNotAuthorisedException(String message) {
		super(message);
	}

	public GuideNotAuthorisedException(Throwable cause) {
		super(cause);
	}

	public GuideNotAuthorisedException(GuideError error, Throwable cause) {
		super(error.getMessage(), cause);
	}
}
