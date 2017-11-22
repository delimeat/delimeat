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
