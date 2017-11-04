package io.delimeat.torrent.entity;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

public class UdpConnectRequestTest {

	@Test
	public void constructorTest() {
		UdpConnectRequest request = new UdpConnectRequest(Integer.MIN_VALUE);

		Assert.assertEquals(0x41727101980L, request.getConnectionId());
		Assert.assertEquals(Integer.MIN_VALUE, request.getTransactionId());
		Assert.assertEquals(UdpAction.CONNECT, request.getAction());
	}

	
	@Test
	public void toByteBufferTest(){
		UdpConnectRequest request = new UdpConnectRequest(Integer.MAX_VALUE);
		
		ByteBuffer result = request.toByteBuffer();
		
		Assert.assertEquals(0x41727101980L, result.getLong());
		Assert.assertEquals(0, result.getInt());
		Assert.assertEquals(Integer.MAX_VALUE, result.getInt());
	}

	@Test
	public void hashCodeTest() {
		UdpConnectRequest request = new UdpConnectRequest(Integer.MIN_VALUE);

		Assert.assertEquals(989042954, request.hashCode());
	}

	@Test
	public void equalsSelfTest() {
		UdpConnectRequest request = new UdpConnectRequest(Integer.MIN_VALUE);

		Assert.assertTrue(request.equals(request));
	}

	@Test
	public void equalsNullTest() {
		UdpConnectRequest request = new UdpConnectRequest(Integer.MIN_VALUE);

		Assert.assertFalse(request.equals(null));
	}

	@Test
	public void equalsObjectTest() {
		UdpConnectRequest request = new UdpConnectRequest(Integer.MIN_VALUE);

		Assert.assertFalse(request.equals(new Object()));
	}

	@Test
	public void equalsTest() {
		UdpConnectRequest request = new UdpConnectRequest(Integer.MIN_VALUE);
		UdpConnectRequest other = new UdpConnectRequest(Integer.MIN_VALUE);

		Assert.assertTrue(request.equals(other));
	}

	@Test
	public void equalsTransactionIdTest() {
		UdpConnectRequest request = new UdpConnectRequest(Integer.MIN_VALUE);
		UdpConnectRequest other = new UdpConnectRequest(Integer.MAX_VALUE);

		Assert.assertFalse(request.equals(other));
	}

	@Test
	public void toStringTest() {
		UdpConnectRequest request = new UdpConnectRequest(Integer.MIN_VALUE);

		Assert.assertEquals("ConnectUdpRequest [connectionId=4497486125440, action=CONNECT, transactionId=-2147483648]", request.toString());
	}
}
