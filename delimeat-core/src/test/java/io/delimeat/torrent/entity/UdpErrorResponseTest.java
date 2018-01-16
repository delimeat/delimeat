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

public class UdpErrorResponseTest {

	@Test
	public void constructorTest() {
		UdpErrorResponse response = new UdpErrorResponse(Integer.MAX_VALUE, "MESSAGE");

		Assertions.assertEquals(Integer.MAX_VALUE, response.getTransactionId());
		Assertions.assertEquals("MESSAGE", response.getMessage());
		Assertions.assertEquals(UdpAction.ERROR, response.getAction());

	}

	@Test
	public void hashCodeTest() {
		UdpErrorResponse response = new UdpErrorResponse(Integer.MAX_VALUE, "MESSAGE");

		Assertions.assertEquals(-1826949959, response.hashCode());
	}

	@Test
	public void toStringTest() {
		UdpErrorResponse response = new UdpErrorResponse(Integer.MAX_VALUE, "MESSAGE");

		Assertions.assertEquals("ErrorUdpResponse [transactionId=2147483647, action=ERROR, message=MESSAGE]",
				response.toString());
	}

	@Test
	public void equalsSelfTest() {
		UdpErrorResponse response = new UdpErrorResponse(Integer.MAX_VALUE, "MESSAGE");

		Assertions.assertTrue(response.equals(response));
	}

	@Test
	public void equalsNullTest() {
		UdpErrorResponse response = new UdpErrorResponse(Integer.MAX_VALUE, "MESSAGE");

		Assertions.assertFalse(response.equals(null));
	}

	@Test
	public void equalsObjectTest() {
		UdpErrorResponse response = new UdpErrorResponse(Integer.MAX_VALUE, "MESSAGE");

		Assertions.assertFalse(response.equals(new Object()));
	}

	@Test
	public void equalsTest() {
		UdpErrorResponse response = new UdpErrorResponse(Integer.MAX_VALUE, "MESSAGE");
		UdpErrorResponse other = new UdpErrorResponse(Integer.MAX_VALUE, "MESSAGE");

		Assertions.assertTrue(response.equals(other));
	}

	@Test
	public void equalsTransactionIdTest() {
		UdpErrorResponse response = new UdpErrorResponse(Integer.MAX_VALUE, "MESSAGE");
		UdpErrorResponse other = new UdpErrorResponse(Integer.MIN_VALUE, "MESSAGE");

		Assertions.assertFalse(response.equals(other));
	}

	@Test
	public void equalsMessageTest() {
		UdpErrorResponse response = new UdpErrorResponse(Integer.MAX_VALUE, "MESSAGE");
		UdpErrorResponse other = new UdpErrorResponse(Integer.MAX_VALUE, "OTHER");

		Assertions.assertFalse(response.equals(other));
	}

	@Test
	public void equalsMessageNullTest() {
		UdpErrorResponse response = new UdpErrorResponse(Integer.MAX_VALUE, null);
		UdpErrorResponse other = new UdpErrorResponse(Integer.MAX_VALUE, "MESSAGE");

		Assertions.assertFalse(response.equals(other));
	}
}
