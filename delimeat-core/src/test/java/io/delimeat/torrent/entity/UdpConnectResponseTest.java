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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UdpConnectResponseTest {

	@Test
	public void constructorTest() {
		UdpConnectResponse response = new UdpConnectResponse(Integer.MAX_VALUE, Long.MAX_VALUE);

		Assertions.assertEquals(Integer.MAX_VALUE, response.getTransactionId());
		Assertions.assertEquals(Long.MAX_VALUE, response.getConnectionId());
		Assertions.assertEquals(UdpAction.CONNECT, response.getAction());
	}

	@Test
	public void hashCodeTest() {
		UdpConnectResponse response = new UdpConnectResponse(Integer.MAX_VALUE, Long.MAX_VALUE);

		Assertions.assertEquals(960, response.hashCode());
	}

	@Test
	public void toStringTest() {
		UdpConnectResponse response = new UdpConnectResponse(Integer.MAX_VALUE, Long.MAX_VALUE);

		Assertions.assertEquals(
				"ConnectUdpResponse [transactionId=2147483647, action=CONNECT, connectionId=9223372036854775807]",
				response.toString());
	}

	@Test
	public void equalsSelfTest() {
		UdpConnectResponse response = new UdpConnectResponse(Integer.MAX_VALUE, Long.MAX_VALUE);

		Assertions.assertTrue(response.equals(response));
	}

	@Test
	public void equalsNullTest() {
		UdpConnectResponse response = new UdpConnectResponse(Integer.MAX_VALUE, Long.MAX_VALUE);

		Assertions.assertFalse(response.equals(null));
	}

	@Test
	public void equalsObjectTest() {
		UdpConnectResponse response = new UdpConnectResponse(Integer.MAX_VALUE, Long.MAX_VALUE);

		Assertions.assertFalse(response.equals(new Object()));
	}

	@Test
	public void equalsTest() {
		UdpConnectResponse response = new UdpConnectResponse(Integer.MAX_VALUE, Long.MAX_VALUE);
		UdpConnectResponse other = new UdpConnectResponse(Integer.MAX_VALUE, Long.MAX_VALUE);

		Assertions.assertTrue(response.equals(other));
	}

	@Test
	public void equalsTransactionIdTest() {
		UdpConnectResponse response = new UdpConnectResponse(Integer.MAX_VALUE, Long.MAX_VALUE);
		UdpConnectResponse other = new UdpConnectResponse(Integer.MIN_VALUE, Long.MAX_VALUE);

		Assertions.assertFalse(response.equals(other));
	}

	@Test
	public void equalsConnectionIdTest() {
		UdpConnectResponse response = new UdpConnectResponse(Integer.MAX_VALUE, Long.MAX_VALUE);
		UdpConnectResponse other = new UdpConnectResponse(Integer.MAX_VALUE, Long.MIN_VALUE);

		Assertions.assertFalse(response.equals(other));
	}

}
