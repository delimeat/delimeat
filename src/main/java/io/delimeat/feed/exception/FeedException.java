package io.delimeat.feed.exception;

public class FeedException extends Exception {

	static final long serialVersionUID = -1;

	public FeedException(String message) {
		super(message);
	}

	public FeedException(Throwable cause) {
		super(cause);
	}

	public FeedException(String message, Throwable cause) {
		super(message, cause);
	}
}
