package io.delimeat.util.bencode;

public class BencodeUnexpectedCharacterException extends BencodeException {
	
	private static final long serialVersionUID = 1L;

	public BencodeUnexpectedCharacterException(String message) {
		super(message);
	}

	public BencodeUnexpectedCharacterException(Throwable cause) {
		super(cause);
	}

	public BencodeUnexpectedCharacterException(String message,
			Throwable cause) {
		super(message, cause);
	}

}
