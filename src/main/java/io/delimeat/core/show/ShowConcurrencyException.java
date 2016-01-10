package io.delimeat.core.show;

public class ShowConcurrencyException extends ShowException {

	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 */
	public ShowConcurrencyException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ShowConcurrencyException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ShowConcurrencyException(String message, Throwable cause) {
		super(message, cause);
	}

}
