package io.delimeat.core.show;

public class ShowConcurrencyException extends ShowException {

	private static final long serialVersionUID = 1L;

	public ShowConcurrencyException(String message) {
		super(message);
	}

	public ShowConcurrencyException(Throwable cause) {
		super(cause);
	}

	public ShowConcurrencyException(String message, Throwable cause) {
		super(message, cause);
	}

}
