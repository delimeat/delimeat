package io.delimeat.torrent.exception;

import io.delimeat.torrent.domain.UdpErrorResponse;

public class UdpErrorResponseException extends UdpTorrentException {
	
	private static final long serialVersionUID = 1L;

	public UdpErrorResponseException(UdpErrorResponse response) {
		super(response.getMessage());
	}

}
