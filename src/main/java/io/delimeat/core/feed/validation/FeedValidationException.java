package io.delimeat.core.feed.validation;

import io.delimeat.core.feed.FeedException;

public class FeedValidationException extends FeedException {

	private static final long serialVersionUID = 1L;

	public FeedValidationException(String message) {
		super(message);
	}

	public FeedValidationException(Throwable cause) {
		super(cause);
	}

	public FeedValidationException(String message, Throwable cause) {
		super(message, cause);
	}

}
