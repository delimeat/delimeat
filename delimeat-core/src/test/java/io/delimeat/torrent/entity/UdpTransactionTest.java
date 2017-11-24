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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.delimeat.torrent.exception.UdpTimeoutException;

public class UdpTransactionTest {

	@Test
	public void constructorTest() {
		UdpRequest request = Mockito.mock(UdpRequest.class);
		InetSocketAddress address = InetSocketAddress.createUnresolved("localhost", 9090);
		UdpTransaction transaction = new UdpTransaction(request, address);

		Assertions.assertEquals(request, transaction.getRequest());
		Assertions.assertEquals(address, transaction.getToAddress());
	}

	@Test
	public void toStringTest() {
		UdpScrapeRequest request = new UdpScrapeRequest(1, 2, new InfoHash("INFO_HASH".getBytes()));
		InetSocketAddress address = InetSocketAddress.createUnresolved("localhost", 9090);
		UdpTransaction transaction = new UdpTransaction(request, address);

		Assertions.assertEquals(
				"UdpTransaction [toAddress=localhost:9090, request=ScrapeUdpRequest [connectionId=1, action=SCRAPE, transactionId=2, infoHash=InfoHash [getHex()=494e464f5f48415348]], ]",
				transaction.toString());
	}

	@Test
	public void awaitResponseTimeoutTest() throws Exception {
		UdpRequest request = Mockito.mock(UdpRequest.class);
		InetSocketAddress address = Mockito.mock(InetSocketAddress.class);
		UdpTransaction transaction = new UdpTransaction(request, address);

		UdpTimeoutException ex = Assertions.assertThrows(UdpTimeoutException.class, () -> {
			transaction.awaitResponse(1000);
		});

		Assertions.assertEquals("Transaction timeout\n{}", ex.getMessage());
	}

	@Test
	public void awaitResponseExceptionTest() {
		UdpRequest request = Mockito.mock(UdpRequest.class);
		InetSocketAddress address = Mockito.mock(InetSocketAddress.class);
		UdpTransaction transaction = new UdpTransaction(request, address);

		Exception exception = new Exception("EXCEPTION");
		transaction.setException(exception);

		Exception ex = Assertions.assertThrows(Exception.class, () -> {
			transaction.awaitResponse(1000);
		});

		Assertions.assertEquals(exception, ex);
	}

	@Test
	public void getResponseTest() throws Exception {
		UdpRequest request = Mockito.mock(UdpRequest.class);
		InetSocketAddress address = Mockito.mock(InetSocketAddress.class);
		UdpTransaction transaction = new UdpTransaction(request, address);

		UdpResponse response = Mockito.mock(UdpResponse.class);
		transaction.setResponse(response);

		Assertions.assertEquals(response, transaction.getResponse());
	}
}
