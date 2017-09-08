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
package io.delimeat.torrent.udp.domain;

import java.util.Objects;

public class ConnectUdpResponse extends UdpResponse {

	private final long connectionId;
	
	/**
	 * @param action
	 * @param transactionId
	 */
	public ConnectUdpResponse(int transactionId, long connectionId) {
		super(UdpAction.CONNECT, transactionId);
		this.connectionId = connectionId;
	}
	
	/**
	 * @return the connectionId
	 */
	public long getConnectionId() {
		return connectionId;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(action,transactionId,connectionId);
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
		ConnectUdpResponse other = (ConnectUdpResponse) obj;
		if (action != other.action)
			return false;
		if (transactionId != other.transactionId)
			return false;
		if (connectionId != other.connectionId) 
			return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ConnectUdpResponse [" + (action != null ? "action=" + action + ", " : "") 
				+ "transactionId=" + transactionId + ", connectionId=" + connectionId + "]";
	}
	
	

}
