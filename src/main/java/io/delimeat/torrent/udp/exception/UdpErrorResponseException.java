package io.delimeat.torrent.udp.exception;

import io.delimeat.torrent.udp.domain.ErrorUdpResponse;

public class UdpErrorResponseException extends UdpException {
	
	private static final long serialVersionUID = 1L;

	public UdpErrorResponseException(ErrorUdpResponse response) {
		super(response.getMessage());
	}

}
