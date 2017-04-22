package io.delimeat.torrent.exception;

public class UnhandledScrapeException extends TorrentException {

	static final long serialVersionUID = -1;

	public UnhandledScrapeException(String message) {
		super(message);
	}

	public UnhandledScrapeException(Throwable cause) {
		super(cause);
	}

	public UnhandledScrapeException(String message, Throwable cause) {
		super(message, cause);
	}
}
