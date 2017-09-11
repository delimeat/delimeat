package io.delimeat.torrent.udp.exception;

public class UdpTimeoutException extends UdpException {

	private static final long serialVersionUID = 1L;

	public UdpTimeoutException() {
		super("timeout");
	}

}
