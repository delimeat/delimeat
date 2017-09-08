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

import io.delimeat.torrent.domain.InfoHash;

public class ScrapeUdpRequest extends UdpRequest {

	private final InfoHash infoHash;
	/**
	 * @param connectionId
	 * @param transactionId
	 * @param infoHash
	 */
	public ScrapeUdpRequest(long connectionId, int transactionId, InfoHash infoHash) {
		super(connectionId, UdpAction.SCRAPE, transactionId);
		this.infoHash = infoHash;
	}
	/**
	 * @return the infoHash
	 */
	public InfoHash getInfoHash() {
		return infoHash;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(connectionId,action,transactionId,infoHash);
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
		ScrapeUdpRequest other = (ScrapeUdpRequest) obj;
		if (connectionId != other.connectionId) 
			return false;
		if (action != other.action)
			return false;
		if (transactionId != other.transactionId)
			return false;
		if (infoHash == null) {
			if (other.infoHash != null)
				return false;
		} else if (!infoHash.equals(other.infoHash))
			return false;
		return true;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ScrapeUdpRequest [" + "connectionId=" + connectionId + ", " 
				+ (action != null ? "action=" + action + ", " : "") 
				+ "transactionId=" + transactionId 
				+ (infoHash != null ? "infoHash=" + infoHash + ", " : "") + "]";
	}
	
	

}
