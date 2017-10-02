package io.delimeat.torrent.domain;

import org.junit.Assert;
import org.junit.Test;

import io.delimeat.torrent.domain.UdpAction;
import io.delimeat.torrent.domain.UdpErrorResponse;

public class UdpErrorResponseTest {


	@Test
	public void constructorTest(){
		UdpErrorResponse response = new UdpErrorResponse(Integer.MAX_VALUE, "MESSAGE");
		
		Assert.assertEquals(Integer.MAX_VALUE, response.getTransactionId());
		Assert.assertEquals("MESSAGE", response.getMessage());
		Assert.assertEquals(UdpAction.ERROR, response.getAction());

	}
	
	@Test
	public void hashCodeTest(){
		UdpErrorResponse response = new UdpErrorResponse(Integer.MAX_VALUE, "MESSAGE");
		
		Assert.assertEquals(-1826949959, response.hashCode());
	}
	
	@Test
	public void toStringTest(){
		UdpErrorResponse response = new UdpErrorResponse(Integer.MAX_VALUE, "MESSAGE");

		Assert.assertEquals("ErrorUdpResponse [transactionId=2147483647, action=ERROR, message=MESSAGE]", response.toString());
	}
	

	@Test
	public void equalsSelfTest() {
		UdpErrorResponse response = new UdpErrorResponse(Integer.MAX_VALUE, "MESSAGE");

		Assert.assertTrue(response.equals(response));
	}

	@Test
	public void equalsNullTest() {
		UdpErrorResponse response = new UdpErrorResponse(Integer.MAX_VALUE, "MESSAGE");

		Assert.assertFalse(response.equals(null));
	}

	@Test
	public void equalsObjectTest() {
		UdpErrorResponse response = new UdpErrorResponse(Integer.MAX_VALUE, "MESSAGE");

		Assert.assertFalse(response.equals(new Object()));
	}

	@Test
	public void equalsTest() {
		UdpErrorResponse response = new UdpErrorResponse(Integer.MAX_VALUE, "MESSAGE");
		UdpErrorResponse other = new UdpErrorResponse(Integer.MAX_VALUE, "MESSAGE");

		Assert.assertTrue(response.equals(other));
	}

	@Test
	public void equalsTransactionIdTest() {
		UdpErrorResponse response = new UdpErrorResponse(Integer.MAX_VALUE, "MESSAGE");
		UdpErrorResponse other = new UdpErrorResponse(Integer.MIN_VALUE, "MESSAGE");

		Assert.assertFalse(response.equals(other));
	}
	
	@Test
	public void equalsMessageTest() {
		UdpErrorResponse response = new UdpErrorResponse(Integer.MAX_VALUE, "MESSAGE");
		UdpErrorResponse other = new UdpErrorResponse(Integer.MAX_VALUE, "OTHER");

		Assert.assertFalse(response.equals(other));
	}
	
	@Test
	public void equalsMessageNullTest() {
		UdpErrorResponse response = new UdpErrorResponse(Integer.MAX_VALUE, null );
		UdpErrorResponse other = new UdpErrorResponse(Integer.MAX_VALUE, "MESSAGE");

		Assert.assertFalse(response.equals(other));
	}
}
