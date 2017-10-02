package io.delimeat.torrent.domain;

import org.junit.Assert;
import org.junit.Test;

import io.delimeat.torrent.domain.UdpAction;
import io.delimeat.torrent.domain.UdpScrapeResponse;

public class UdpScrapeResponseTest {


	@Test
	public void constructorTest(){
		UdpScrapeResponse response = new UdpScrapeResponse(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
		
		Assert.assertEquals(Integer.MAX_VALUE, response.getTransactionId());
		Assert.assertEquals(Integer.MIN_VALUE, response.getSeeders());
		Assert.assertEquals(Integer.MAX_VALUE, response.getLeechers());
		Assert.assertEquals(UdpAction.SCRAPE, response.getAction());

	}
	
	@Test
	public void hashCodeTest(){
		UdpScrapeResponse response = new UdpScrapeResponse(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
		
		Assert.assertEquals(-2147454819, response.hashCode());
	}
	
	@Test
	public void toStringTest(){
		UdpScrapeResponse response = new UdpScrapeResponse(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);

		Assert.assertEquals("ScrapeUdpResponse [transactionId=2147483647, action=SCRAPE, seeders=-2147483648, leechers=2147483647]", response.toString());
	}
	

	@Test
	public void equalsSelfTest() {
		UdpScrapeResponse response = new UdpScrapeResponse(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);

		Assert.assertTrue(response.equals(response));
	}

	@Test
	public void equalsNullTest() {
		UdpScrapeResponse response = new UdpScrapeResponse(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);

		Assert.assertFalse(response.equals(null));
	}

	@Test
	public void equalsObjectTest() {
		UdpScrapeResponse response = new UdpScrapeResponse(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);

		Assert.assertFalse(response.equals(new Object()));
	}

	@Test
	public void equalsTest() {
		UdpScrapeResponse response = new UdpScrapeResponse(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
		UdpScrapeResponse other = new UdpScrapeResponse(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);

		Assert.assertTrue(response.equals(other));
	}

	@Test
	public void equalsTransactionIdTest() {
		UdpScrapeResponse response = new UdpScrapeResponse(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
		UdpScrapeResponse other = new UdpScrapeResponse(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);

		Assert.assertFalse(response.equals(other));
	}
	
	@Test
	public void equalsSeedersTest() {
		UdpScrapeResponse response = new UdpScrapeResponse(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
		UdpScrapeResponse other = new UdpScrapeResponse(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);

		Assert.assertFalse(response.equals(other));
	}
	
	@Test
	public void equalsLeechersTest() {
		UdpScrapeResponse response = new UdpScrapeResponse(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
		UdpScrapeResponse other = new UdpScrapeResponse(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);

		Assert.assertFalse(response.equals(other));
	}
}
