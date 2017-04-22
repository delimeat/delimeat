package io.delimeat.common.util.exception;

public class EntityConcurrencyException extends EntityException {

	private static final long serialVersionUID = 1L;
	
	public EntityConcurrencyException(String message) {
		super(message);
	}

	public EntityConcurrencyException(Throwable cause) {
		super(cause);
	}

	public EntityConcurrencyException(String message, Throwable cause) {
		super(message, cause);
	}	

}
