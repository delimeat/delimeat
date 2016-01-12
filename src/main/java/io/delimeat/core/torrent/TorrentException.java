package io.delimeat.core.torrent;

public class TorrentException extends Exception {

	static final long serialVersionUID = -1;

	public TorrentException(String message) {
		super(message);
	}

	public TorrentException(Throwable cause) {
		super(cause);
	}

	public TorrentException(String message, Throwable cause) {
		super(message, cause);
	}
}
