package io.delimeat.core.guide;

public class GuideNotFoundException extends GuideException {

	static final long serialVersionUID = -1;

	public GuideNotFoundException(String message) {
		super(message);
	}

	public GuideNotFoundException(Throwable cause) {
		super(cause);
	}

	public GuideNotFoundException(GuideError error, Throwable cause) {
		super(error.getMessage(), cause);
	}
}
