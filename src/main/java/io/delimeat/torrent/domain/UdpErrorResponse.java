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

import java.util.Objects;

public class UdpErrorResponse implements UdpResponse {

	private final int transactionId;
	private final String message;
	private final UdpAction action = UdpAction.ERROR;
	
	public UdpErrorResponse(int transactionId, String message) {
		this.transactionId = transactionId;
		this.message = message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.delimeat.torrent.udp.domain.AbstractUdpResponse#getTransactionId()
	 */
	@Override
	public int getTransactionId() {
		return transactionId;
	}
	
	/* (non-Javadoc)
	 * @see io.delimeat.torrent.udp.domain.UdpResponse#getAction()
	 */
	@Override
	public UdpAction getAction() {
		return action;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(message, transactionId);
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
		UdpErrorResponse other = (UdpErrorResponse) obj;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
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
		return "ErrorUdpResponse [transactionId=" + transactionId + ", action=" + action + ", message=" + message + "]";
	}

}
