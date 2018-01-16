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

public class UdpConnectionIdTest {

	@Test
	public void constructorTest() throws Exception {
		UdpConnectionId id = new UdpConnectionId(Long.MAX_VALUE, new InetSocketAddress("localhost", 8080),
				Instant.EPOCH);

		Assertions.assertEquals(Long.MAX_VALUE, id.getValue());
		Assertions.assertEquals(new InetSocketAddress("localhost", 8080), id.getFromAddress());
		Assertions.assertEquals(Instant.EPOCH, id.getExpiry());
	}

	@Test
	public void hashCodeTest() {
		UdpConnectionId id = new UdpConnectionId(Long.MAX_VALUE, new InetSocketAddress("localhost", 8080),
				Instant.EPOCH);

		Assertions.assertEquals(-519813394, id.hashCode());
	}

	@Test
	public void equalsSelfTest() {
		UdpConnectionId id = new UdpConnectionId(Long.MAX_VALUE, new InetSocketAddress("localhost", 8080),
				Instant.EPOCH);

		Assertions.assertTrue(id.equals(id));
	}

	@Test
	public void equalsNullTest() {
		UdpConnectionId id = new UdpConnectionId(Long.MAX_VALUE, new InetSocketAddress("localhost", 8080),
				Instant.EPOCH);

		Assertions.assertFalse(id.equals(null));
	}

	@Test
	public void equalsObjectTest() {
		UdpConnectionId id = new UdpConnectionId(Long.MAX_VALUE, new InetSocketAddress("localhost", 8080),
				Instant.EPOCH);

		Assertions.assertFalse(id.equals(new Object()));
	}

	@Test
	public void equalsTest() {
		UdpConnectionId id = new UdpConnectionId(Long.MAX_VALUE, new InetSocketAddress("localhost", 8080),
				Instant.EPOCH);
		UdpConnectionId other = new UdpConnectionId(Long.MAX_VALUE, new InetSocketAddress("localhost", 8080),
				Instant.EPOCH);

		Assertions.assertTrue(id.equals(other));
	}

	@Test
	public void equalsValueTest() {
		UdpConnectionId id = new UdpConnectionId(Long.MAX_VALUE, new InetSocketAddress("localhost", 8080),
				Instant.EPOCH);
		UdpConnectionId other = new UdpConnectionId(Long.MIN_VALUE, new InetSocketAddress("localhost", 8080),
				Instant.EPOCH);

		Assertions.assertFalse(id.equals(other));
	}

	@Test
	public void equalsExpiryTest() {
		UdpConnectionId id = new UdpConnectionId(Long.MAX_VALUE, new InetSocketAddress("localhost", 8080),
				Instant.EPOCH);
		UdpConnectionId other = new UdpConnectionId(Long.MAX_VALUE, new InetSocketAddress("localhost", 8080),
				Instant.MIN);

		Assertions.assertFalse(id.equals(other));
	}

	@Test
	public void equalsExpiryNullTest() {
		UdpConnectionId id = new UdpConnectionId(Long.MAX_VALUE, new InetSocketAddress("localhost", 8080), null);
		UdpConnectionId other = new UdpConnectionId(Long.MAX_VALUE, new InetSocketAddress("localhost", 8080),
				Instant.MIN);

		Assertions.assertFalse(id.equals(other));
	}

	@Test
	public void equalsFromAddressTest() {
		UdpConnectionId id = new UdpConnectionId(Long.MAX_VALUE, new InetSocketAddress("localhost", 8080),
				Instant.EPOCH);
		UdpConnectionId other = new UdpConnectionId(Long.MAX_VALUE, new InetSocketAddress("test", 8080), Instant.EPOCH);

		Assertions.assertFalse(id.equals(other));
	}

	@Test
	public void equalsFromAddressNullTest() {
		UdpConnectionId id = new UdpConnectionId(Long.MAX_VALUE, null, Instant.EPOCH);
		UdpConnectionId other = new UdpConnectionId(Long.MAX_VALUE, new InetSocketAddress("localhost", 8080),
				Instant.EPOCH);

		Assertions.assertFalse(id.equals(other));
	}

	@Test
	public void toStringTest() {
		UdpConnectionId id = new UdpConnectionId(Long.MAX_VALUE, new InetSocketAddress("localhost", 8080),
				Instant.EPOCH);

		Assertions.assertEquals(
				"ConnectionId [value=9223372036854775807, fromAddress=localhost/127.0.0.1:8080, expiry=1970-01-01T00:00:00Z]",
				id.toString());
	}
}
