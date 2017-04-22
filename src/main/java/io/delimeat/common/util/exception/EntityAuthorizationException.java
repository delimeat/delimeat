package io.delimeat.common.util.exception;

public class EntityAuthorizationException extends EntityException {

	private static final long serialVersionUID = 1L;
	
	public EntityAuthorizationException(String message) {
		super(message);
	}

	public EntityAuthorizationException(Throwable cause) {
		super(cause);
	}

	public EntityAuthorizationException(String message, Throwable cause) {
		super(message, cause);
	}	

}
