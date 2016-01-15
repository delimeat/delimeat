package io.delimeat.core.guide;

public class GuideException extends Exception {

	static final long serialVersionUID = -1;

	public GuideException(String message) {
		super(message);
	}

	public GuideException(Throwable cause) {
		super(cause);
	}

	public GuideException(String message, Throwable cause) {
		super(message, cause);
	}
}
