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
