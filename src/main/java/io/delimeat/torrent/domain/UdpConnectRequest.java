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

public class UdpConnectRequest implements UdpRequest {

	private final long connectionId = 0x41727101980L;
	private final int transactionId;
	private final UdpAction action = UdpAction.CONNECT;

	public UdpConnectRequest(int transactionId) {
		this.transactionId = transactionId;
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
	
	/* 
	 * (non-Javadoc)
	 * 
	 * @see io.delimeat.torrent.udp.domain.UdpRequest#toByteBuffer()
	 */
	@Override
	public ByteBuffer toByteBuffer() {
		ByteBuffer buffer = ByteBuffer.allocate(16)
				.putLong(connectionId)
				.putInt(action.value())
				.putInt(transactionId);
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
		return Objects.hash(connectionId, transactionId);
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
		UdpConnectRequest other = (UdpConnectRequest) obj;
		if (transactionId != other.transactionId)
			return false;
		return true;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ConnectUdpRequest [connectionId=" + connectionId + ", action=" + action + ", transactionId="
				+ transactionId + "]";
	}

}
