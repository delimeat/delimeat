package io.delimeat.torrent.exception;

import io.delimeat.torrent.domain.UdpTransaction;

public class UdpTimeoutException extends UdpTorrentException {

	private static final long serialVersionUID = 1L;

	public UdpTimeoutException(UdpTransaction transaction) {
		super(String.format("Transaction timeout\n{}", transaction));
	}

}
