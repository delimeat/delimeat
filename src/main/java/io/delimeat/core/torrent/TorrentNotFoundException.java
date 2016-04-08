package io.delimeat.core.torrent;

public class TorrentNotFoundException extends TorrentException {

	static final long serialVersionUID = 1L;

	public TorrentNotFoundException(String message) {
		super(message);
	}

	public TorrentNotFoundException(Throwable cause) {
		super(cause);
	}

	public TorrentNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
