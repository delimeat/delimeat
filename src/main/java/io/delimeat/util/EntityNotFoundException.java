package io.delimeat.util;

public class EntityNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public EntityNotFoundException(String message) {
		super(message);
	}

	public EntityNotFoundException(Throwable cause) {
		super(cause);
	}

	public EntityNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}	

}
