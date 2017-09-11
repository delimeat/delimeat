package io.delimeat.torrent.udp.domain;

import org.junit.Assert;
import org.junit.Test;


public class ConnectUdpResponseTest {

	@Test
	public void constructorTest(){
		ConnectUdpResponse response = new ConnectUdpResponse(Integer.MAX_VALUE, Long.MAX_VALUE);
		
		Assert.assertEquals(Integer.MAX_VALUE, response.getTransactionId());
		Assert.assertEquals(Long.MAX_VALUE, response.getConnectionId());
	}
	
	@Test
	public void hashCodeTest(){
		ConnectUdpResponse response = new ConnectUdpResponse(Integer.MAX_VALUE, Long.MAX_VALUE);
		
		Assert.assertEquals(960, response.hashCode());
	}
	
	@Test
	public void toStringTest(){
		ConnectUdpResponse response = new ConnectUdpResponse(Integer.MAX_VALUE, Long.MAX_VALUE);

		Assert.assertEquals("ConnectUdpResponse [transactionId=2147483647, action=CONNECT, connectionId=9223372036854775807]", response.toString());
	}
	

	@Test
	public void equalsSelfTest() {
		ConnectUdpResponse response = new ConnectUdpResponse(Integer.MAX_VALUE, Long.MAX_VALUE);

		Assert.assertTrue(response.equals(response));
	}

	@Test
	public void equalsNullTest() {
		ConnectUdpResponse response = new ConnectUdpResponse(Integer.MAX_VALUE, Long.MAX_VALUE);

		Assert.assertFalse(response.equals(null));
	}

	@Test
	public void equalsObjectTest() {
		ConnectUdpResponse response = new ConnectUdpResponse(Integer.MAX_VALUE, Long.MAX_VALUE);

		Assert.assertFalse(response.equals(new Object()));
	}

	@Test
	public void equalsTest() {
		ConnectUdpResponse response = new ConnectUdpResponse(Integer.MAX_VALUE, Long.MAX_VALUE);
		ConnectUdpResponse other = new ConnectUdpResponse(Integer.MAX_VALUE, Long.MAX_VALUE);

		Assert.assertTrue(response.equals(other));
	}

	@Test
	public void equalsTransactionIdTest() {
		ConnectUdpResponse response = new ConnectUdpResponse(Integer.MAX_VALUE, Long.MAX_VALUE);
		ConnectUdpResponse other = new ConnectUdpResponse(Integer.MIN_VALUE, Long.MAX_VALUE);

		Assert.assertFalse(response.equals(other));
	}
	
	@Test
	public void equalsConnectionIdTest() {
		ConnectUdpResponse response = new ConnectUdpResponse(Integer.MAX_VALUE, Long.MAX_VALUE);
		ConnectUdpResponse other = new ConnectUdpResponse(Integer.MAX_VALUE, Long.MIN_VALUE);

		Assert.assertFalse(response.equals(other));
	}

}
