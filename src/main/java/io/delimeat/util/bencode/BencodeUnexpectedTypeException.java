package io.delimeat.util.bencode;

public class BencodeUnexpectedTypeException extends BencodeException {

	private static final long serialVersionUID = 1L;

	public BencodeUnexpectedTypeException(String message) {
		super(message);
	}

	public BencodeUnexpectedTypeException(Throwable cause) {
		super(cause);
	}

	public BencodeUnexpectedTypeException(String message, Throwable cause) {
		super(message, cause);
	}

}
