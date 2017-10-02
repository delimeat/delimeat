package io.delimeat.torrent.domain;

import java.net.InetSocketAddress;
import java.time.Instant;
import java.util.Objects;

public class UdpConnectionId {
	
	private final long value;
	private final InetSocketAddress fromAddress;
	private final Instant expiry;
	
	/**
	 * @param value
	 * @param expiry
	 */
	public UdpConnectionId(long value, InetSocketAddress fromAddress, Instant expiry){
		this.value = value;
		this.fromAddress = fromAddress;
		this.expiry = expiry;
	}

	/**
	 * @return the value
	 */
	public long getValue() {
		return value;
	}

	/**
	 * @return the fromAddress
	 */
	public InetSocketAddress getFromAddress() {
		return fromAddress;
	}

	/**
	 * @return the expiry
	 */
	public Instant getExpiry() {
		return expiry;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(expiry, fromAddress, value);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UdpConnectionId other = (UdpConnectionId) obj;
		if (expiry == null) {
			if (other.expiry != null)
				return false;
		} else if (!expiry.equals(other.expiry))
			return false;
		if (fromAddress == null) {
			if (other.fromAddress != null)
				return false;
		} else if (!fromAddress.equals(other.fromAddress))
			return false;
		if (value != other.value)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ConnectionId [value=" + value + ", fromAddress=" + fromAddress + ", expiry=" + expiry + "]";
	}
	
}
