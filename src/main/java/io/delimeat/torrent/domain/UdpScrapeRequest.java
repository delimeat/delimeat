/*
 * Copyright 2013-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.delimeat.torrent.domain;

import java.nio.ByteBuffer;
import java.util.Objects;

public class UdpScrapeRequest implements UdpRequest {

	private final long connectionId;
	private final int transactionId;
	private final InfoHash infoHash;
	private final UdpAction action = UdpAction.SCRAPE;

	public UdpScrapeRequest(long connectionId, int transactionId, InfoHash infoHash) {
		this.connectionId = connectionId;
		this.transactionId = transactionId;
		this.infoHash = infoHash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.delimeat.torrent.udp.domain.UdpRequest#getConnectionId()
	 */
	@Override
	public long getConnectionId() {
		return connectionId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.delimeat.torrent.udp.domain.UdpRequest#getAction()
	 */
	@Override
	public UdpAction getAction() {
		return action;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.delimeat.torrent.udp.domain.UdpRequest#getTransactionId()
	 */
	@Override
	public int getTransactionId() {
		return transactionId;
	}

	/**
	 * @return the infoHash
	 */
	public InfoHash getInfoHash() {
		return infoHash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.delimeat.torrent.udp.domain.UdpRequest#toByteBuffer()
	 */
	@Override
	public ByteBuffer toByteBuffer() {
		ByteBuffer buffer = ByteBuffer.allocate(36)
				.putLong(connectionId)
				.putInt(action.value())
				.putInt(transactionId)
				.put(infoHash.getBytes());
		buffer.clear();
		return buffer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(connectionId, infoHash, transactionId);
	}

	/*
	 * (non-Javadoc)
	 * 
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
		UdpScrapeRequest other = (UdpScrapeRequest) obj;
		if (connectionId != other.connectionId)
			return false;
		if (infoHash == null) {
			if (other.infoHash != null)
				return false;
		} else if (!infoHash.equals(other.infoHash))
			return false;
		if (transactionId != other.transactionId)
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ScrapeUdpRequest [connectionId=" + connectionId + ", action=" + action + ", transactionId="
				+ transactionId + ", infoHash=" + infoHash + "]";
	}

}
