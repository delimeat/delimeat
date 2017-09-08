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

public class ConnectUdpRequest extends UdpRequest {
	
	/**
	 * @param transactionId
	 */
	public ConnectUdpRequest(int transactionId){
		super(0x41727101980L,UdpAction.CONNECT,transactionId);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(connectionId,action,transactionId);
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
		ConnectUdpRequest other = (ConnectUdpRequest) obj;
		if (connectionId != other.connectionId) 
				return false;
		if (action != other.action)
			return false;
		if (transactionId != other.transactionId)
			return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ConnectUdpRequest [connectionId=" + connectionId + ", "
				+ (action != null ? "action=" + action + ", " : "") + "transactionId=" + transactionId + "]";
	}	
	
}
