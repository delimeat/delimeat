package io.delimeat.core.feed.validation;

import io.delimeat.core.feed.FeedException;

public class TorrentValidatorException extends FeedException {
	
	private static final long serialVersionUID = 1L;
	
	public TorrentValidatorException(final Throwable throwable){
		super(throwable);
	}
	public TorrentValidatorException(final String message, final Throwable throwable){
		super(message,throwable);
	}
	public TorrentValidatorException(final String message){
		super(message);
	}
}
