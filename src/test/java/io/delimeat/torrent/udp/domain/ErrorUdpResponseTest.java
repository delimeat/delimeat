package io.delimeat.torrent.udp.domain;

import org.junit.Assert;
import org.junit.Test;

public class ErrorUdpResponseTest {


	@Test
	public void constructorTest(){
		ErrorUdpResponse response = new ErrorUdpResponse(Integer.MAX_VALUE, "MESSAGE");
		
		Assert.assertEquals(Integer.MAX_VALUE, response.getTransactionId());
		Assert.assertEquals("MESSAGE", response.getMessage());
	}
	
	@Test
	public void hashCodeTest(){
		ErrorUdpResponse response = new ErrorUdpResponse(Integer.MAX_VALUE, "MESSAGE");
		
		Assert.assertEquals(-1826949959, response.hashCode());
	}
	
	@Test
	public void toStringTest(){
		ErrorUdpResponse response = new ErrorUdpResponse(Integer.MAX_VALUE, "MESSAGE");

		Assert.assertEquals("ErrorUdpResponse [transactionId=2147483647, action=ERROR, message=MESSAGE]", response.toString());
	}
	

	@Test
	public void equalsSelfTest() {
		ErrorUdpResponse response = new ErrorUdpResponse(Integer.MAX_VALUE, "MESSAGE");

		Assert.assertTrue(response.equals(response));
	}

	@Test
	public void equalsNullTest() {
		ErrorUdpResponse response = new ErrorUdpResponse(Integer.MAX_VALUE, "MESSAGE");

		Assert.assertFalse(response.equals(null));
	}

	@Test
	public void equalsObjectTest() {
		ErrorUdpResponse response = new ErrorUdpResponse(Integer.MAX_VALUE, "MESSAGE");

		Assert.assertFalse(response.equals(new Object()));
	}

	@Test
	public void equalsTest() {
		ErrorUdpResponse response = new ErrorUdpResponse(Integer.MAX_VALUE, "MESSAGE");
		ErrorUdpResponse other = new ErrorUdpResponse(Integer.MAX_VALUE, "MESSAGE");

		Assert.assertTrue(response.equals(other));
	}

	@Test
	public void equalsTransactionIdTest() {
		ErrorUdpResponse response = new ErrorUdpResponse(Integer.MAX_VALUE, "MESSAGE");
		ErrorUdpResponse other = new ErrorUdpResponse(Integer.MIN_VALUE, "MESSAGE");

		Assert.assertFalse(response.equals(other));
	}
	
	@Test
	public void equalsMessageTest() {
		ErrorUdpResponse response = new ErrorUdpResponse(Integer.MAX_VALUE, "MESSAGE");
		ErrorUdpResponse other = new ErrorUdpResponse(Integer.MAX_VALUE, "OTHER");

		Assert.assertFalse(response.equals(other));
	}
	
	@Test
	public void equalsMessageNullTest() {
		ErrorUdpResponse response = new ErrorUdpResponse(Integer.MAX_VALUE, "MESSAGE");
		ErrorUdpResponse other = new ErrorUdpResponse(Integer.MAX_VALUE, null);

		Assert.assertFalse(response.equals(other));
	}
}
