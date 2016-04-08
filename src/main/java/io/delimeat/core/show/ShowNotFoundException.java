package io.delimeat.core.show;

public class ShowNotFoundException extends ShowException {

	static final long serialVersionUID = 1L;

	public ShowNotFoundException(String message) {
		super(message);
	}

	public ShowNotFoundException(Throwable cause) {
		super(cause);
	}

	public ShowNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
