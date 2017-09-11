package io.delimeat.torrent.udp.domain;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

public class ConnectUdpRequestTest {

	@Test
	public void constructorTest() {
		ConnectUdpRequest request = new ConnectUdpRequest(Integer.MIN_VALUE);

		Assert.assertEquals(0x41727101980L, request.getConnectionId());
		Assert.assertEquals(Integer.MIN_VALUE, request.getTransactionId());
	}
	
	@Test
	public void toByteBufferTest(){
		ConnectUdpRequest request = new ConnectUdpRequest(Integer.MAX_VALUE);
		
		ByteBuffer result = request.toByteBuffer();
		
		Assert.assertEquals(0x41727101980L, result.getLong());
		Assert.assertEquals(0, result.getInt());
		Assert.assertEquals(Integer.MAX_VALUE, result.getInt());
	}

	@Test
	public void hashCodeTest() {
		ConnectUdpRequest request = new ConnectUdpRequest(Integer.MIN_VALUE);

		Assert.assertEquals(989042954, request.hashCode());
	}

	@Test
	public void equalsSelfTest() {
		ConnectUdpRequest request = new ConnectUdpRequest(Integer.MIN_VALUE);

		Assert.assertTrue(request.equals(request));
	}

	@Test
	public void equalsNullTest() {
		ConnectUdpRequest request = new ConnectUdpRequest(Integer.MIN_VALUE);

		Assert.assertFalse(request.equals(null));
	}

	@Test
	public void equalsObjectTest() {
		ConnectUdpRequest request = new ConnectUdpRequest(Integer.MIN_VALUE);

		Assert.assertFalse(request.equals(new Object()));
	}

	@Test
	public void equalsTest() {
		ConnectUdpRequest request = new ConnectUdpRequest(Integer.MIN_VALUE);
		ConnectUdpRequest other = new ConnectUdpRequest(Integer.MIN_VALUE);

		Assert.assertTrue(request.equals(other));
	}

	@Test
	public void equalsTransactionIdTest() {
		ConnectUdpRequest request = new ConnectUdpRequest(Integer.MIN_VALUE);
		ConnectUdpRequest other = new ConnectUdpRequest(Integer.MAX_VALUE);

		Assert.assertFalse(request.equals(other));
	}

	@Test
	public void toStringTest() {
		ConnectUdpRequest request = new ConnectUdpRequest(Integer.MIN_VALUE);

		Assert.assertEquals("ConnectUdpRequest [connectionId=4497486125440, action=CONNECT, transactionId=-2147483648]", request.toString());
	}
}
