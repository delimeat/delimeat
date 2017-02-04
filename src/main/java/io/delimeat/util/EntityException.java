package io.delimeat.util;

public class EntityException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public EntityException(String message) {
		super(message);
	}

	public EntityException(Throwable cause) {
		super(cause);
	}

	public EntityException(String message, Throwable cause) {
		super(message, cause);
	}
}
