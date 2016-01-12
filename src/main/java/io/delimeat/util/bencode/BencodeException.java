package io.delimeat.util.bencode;

public class BencodeException extends Exception {

	static final long serialVersionUID = -1;

	public BencodeException(String message) {
		super(message);
	}

	public BencodeException(Throwable cause) {
		super(cause);
	}

	public BencodeException(String message, Throwable cause) {
		super(message, cause);
	}
}
