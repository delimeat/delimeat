package io.delimeat.torrent.entity;

import java.nio.ByteBuffer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UdpConnectRequestTest {

	@Test
	public void constructorTest() {
		UdpConnectRequest request = new UdpConnectRequest(Integer.MIN_VALUE);

		Assertions.assertEquals(0x41727101980L, request.getConnectionId());
		Assertions.assertEquals(Integer.MIN_VALUE, request.getTransactionId());
		Assertions.assertEquals(UdpAction.CONNECT, request.getAction());
	}

	
	@Test
	public void toByteBufferTest(){
		UdpConnectRequest request = new UdpConnectRequest(Integer.MAX_VALUE);
		
		ByteBuffer result = request.toByteBuffer();
		
		Assertions.assertEquals(0x41727101980L, result.getLong());
		Assertions.assertEquals(0, result.getInt());
		Assertions.assertEquals(Integer.MAX_VALUE, result.getInt());
	}

	@Test
	public void hashCodeTest() {
		UdpConnectRequest request = new UdpConnectRequest(Integer.MIN_VALUE);

		Assertions.assertEquals(989042954, request.hashCode());
	}

	@Test
	public void equalsSelfTest() {
		UdpConnectRequest request = new UdpConnectRequest(Integer.MIN_VALUE);

		Assertions.assertTrue(request.equals(request));
	}

	@Test
	public void equalsNullTest() {
		UdpConnectRequest request = new UdpConnectRequest(Integer.MIN_VALUE);

		Assertions.assertFalse(request.equals(null));
	}

	@Test
	public void equalsObjectTest() {
		UdpConnectRequest request = new UdpConnectRequest(Integer.MIN_VALUE);

		Assertions.assertFalse(request.equals(new Object()));
	}

	@Test
	public void equalsTest() {
		UdpConnectRequest request = new UdpConnectRequest(Integer.MIN_VALUE);
		UdpConnectRequest other = new UdpConnectRequest(Integer.MIN_VALUE);

		Assertions.assertTrue(request.equals(other));
	}

	@Test
	public void equalsTransactionIdTest() {
		UdpConnectRequest request = new UdpConnectRequest(Integer.MIN_VALUE);
		UdpConnectRequest other = new UdpConnectRequest(Integer.MAX_VALUE);

		Assertions.assertFalse(request.equals(other));
	}

	@Test
	public void toStringTest() {
		UdpConnectRequest request = new UdpConnectRequest(Integer.MIN_VALUE);

		Assertions.assertEquals("ConnectUdpRequest [connectionId=4497486125440, action=CONNECT, transactionId=-2147483648]", request.toString());
	}
}
