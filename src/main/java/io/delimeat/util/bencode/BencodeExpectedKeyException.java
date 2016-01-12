package io.delimeat.util.bencode;

public class BencodeExpectedKeyException extends BencodeException {

	static final long serialVersionUID = -1;

	public BencodeExpectedKeyException(String message) {
		super(message);
	}

	public BencodeExpectedKeyException(Throwable cause) {
		super(cause);
	}

	public BencodeExpectedKeyException(String message, Throwable cause) {
		super(message, cause);
	}

}
