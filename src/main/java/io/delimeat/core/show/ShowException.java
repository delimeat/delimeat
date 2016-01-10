package io.delimeat.core.show;

public class ShowException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public ShowException(String message) {
		super(message);
	}

	public ShowException(Throwable cause) {
		super(cause);
	}

	public ShowException(String message, Throwable cause) {
		super(message, cause);
	}

}
