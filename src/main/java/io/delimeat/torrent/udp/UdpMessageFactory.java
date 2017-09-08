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
package io.delimeat.torrent.udp;

import io.delimeat.torrent.udp.domain.ConnectUdpRequest;
import io.delimeat.torrent.udp.domain.ScrapeUdpRequest;
import io.delimeat.torrent.udp.domain.UdpResponse;
import io.delimeat.torrent.udp.exception.UdpInvalidFormatException;
import io.delimeat.torrent.udp.exception.UdpUnsupportedActionException;

public interface UdpMessageFactory {

	/**
	 * Connect request bytes
	 * @param request
	 * @return
	 */
	public byte[] connectRequest(ConnectUdpRequest request);
	
	/**
	 * Scrape request bytes
	 * @param request
	 * @return
	 */
	public byte[] scrapeRequest(ScrapeUdpRequest request);
	
	/**
	 * Build a response from bytes
	 * @param bytes
	 * @return
	 * @throws UdpInvalidFormatException
	 * @throws UdpUnsupportedActionException
	 */
	public UdpResponse buildResponse(byte[] bytes) throws UdpInvalidFormatException, UdpUnsupportedActionException;
	
}
