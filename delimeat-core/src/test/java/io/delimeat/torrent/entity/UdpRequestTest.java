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
package io.delimeat.torrent.entity;

import java.net.InetSocketAddress;
import java.time.Instant;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.delimeat.util.DelimeatUtils;

public class UdpRequestTest {

	@Test
	public void connectRequestTest() {
		UdpRequest request = UdpRequest.connectRequest(Integer.MIN_VALUE);

		Assertions.assertEquals(0x41727101980L, request.getConnectionId());
		Assertions.assertEquals(Integer.MIN_VALUE, request.getTransactionId());
		Assertions.assertEquals(UdpAction.CONNECT, request.getAction());
	}
	
	@Test
	public void scrapeRequestTest() {

		byte[] infoBytes = DelimeatUtils.hexToBytes("df706cf16f45e8c0fd226223509c7e97b4ffec13");
		UdpConnectionId connId = new UdpConnectionId(Long.MAX_VALUE,InetSocketAddress.createUnresolved("localhost", 8080), Instant.EPOCH );
		UdpRequest request = UdpRequest.scrapeRequest(connId, Integer.MIN_VALUE, new InfoHash(infoBytes));

		Assertions.assertEquals(UdpScrapeRequest.class, request.getClass());
		Assertions.assertEquals(Long.MAX_VALUE, request.getConnectionId());
		Assertions.assertEquals(Integer.MIN_VALUE, request.getTransactionId());
		Assertions.assertEquals(UdpAction.SCRAPE, request.getAction());
		UdpScrapeRequest scrapeRequest = (UdpScrapeRequest)request;
		Assertions.assertEquals(new InfoHash(infoBytes), scrapeRequest.getInfoHash());
	}
}
