package io.delimeat.torrent.udp.domain;

import org.junit.Assert;
import org.junit.Test;

public class ScrapeUdpResponseTest {


	@Test
	public void constructorTest(){
		ScrapeUdpResponse response = new ScrapeUdpResponse(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
		
		Assert.assertEquals(Integer.MAX_VALUE, response.getTransactionId());
		Assert.assertEquals(Integer.MIN_VALUE, response.getSeeders());
		Assert.assertEquals(Integer.MAX_VALUE, response.getLeechers());

	}
	
	@Test
	public void hashCodeTest(){
		ScrapeUdpResponse response = new ScrapeUdpResponse(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
		
		Assert.assertEquals(-2147454819, response.hashCode());
	}
	
	@Test
	public void toStringTest(){
		ScrapeUdpResponse response = new ScrapeUdpResponse(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);

		Assert.assertEquals("ScrapeUdpResponse [transactionId=2147483647, action=SCRAPE, seeders=-2147483648, leechers=2147483647]", response.toString());
	}
	

	@Test
	public void equalsSelfTest() {
		ScrapeUdpResponse response = new ScrapeUdpResponse(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);

		Assert.assertTrue(response.equals(response));
	}

	@Test
	public void equalsNullTest() {
		ScrapeUdpResponse response = new ScrapeUdpResponse(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);

		Assert.assertFalse(response.equals(null));
	}

	@Test
	public void equalsObjectTest() {
		ScrapeUdpResponse response = new ScrapeUdpResponse(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);

		Assert.assertFalse(response.equals(new Object()));
	}

	@Test
	public void equalsTest() {
		ScrapeUdpResponse response = new ScrapeUdpResponse(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
		ScrapeUdpResponse other = new ScrapeUdpResponse(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);

		Assert.assertTrue(response.equals(other));
	}

	@Test
	public void equalsTransactionIdTest() {
		ScrapeUdpResponse response = new ScrapeUdpResponse(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
		ScrapeUdpResponse other = new ScrapeUdpResponse(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);

		Assert.assertFalse(response.equals(other));
	}
	
	@Test
	public void equalsSeedersTest() {
		ScrapeUdpResponse response = new ScrapeUdpResponse(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
		ScrapeUdpResponse other = new ScrapeUdpResponse(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);

		Assert.assertFalse(response.equals(other));
	}
	
	@Test
	public void equalsLeechersTest() {
		ScrapeUdpResponse response = new ScrapeUdpResponse(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
		ScrapeUdpResponse other = new ScrapeUdpResponse(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);

		Assert.assertFalse(response.equals(other));
	}
}
