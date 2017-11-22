package io.delimeat.torrent.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UdpScrapeResponseTest {

	@Test
	public void constructorTest() {
		UdpScrapeResponse response = new UdpScrapeResponse(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);

		Assertions.assertEquals(Integer.MAX_VALUE, response.getTransactionId());
		Assertions.assertEquals(Integer.MIN_VALUE, response.getSeeders());
		Assertions.assertEquals(Integer.MAX_VALUE, response.getLeechers());
		Assertions.assertEquals(UdpAction.SCRAPE, response.getAction());

	}

	@Test
	public void hashCodeTest() {
		UdpScrapeResponse response = new UdpScrapeResponse(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);

		Assertions.assertEquals(-2147454819, response.hashCode());
	}

	@Test
	public void toStringTest() {
		UdpScrapeResponse response = new UdpScrapeResponse(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);

		Assertions.assertEquals(
				"ScrapeUdpResponse [transactionId=2147483647, action=SCRAPE, seeders=-2147483648, leechers=2147483647]",
				response.toString());
	}

	@Test
	public void equalsSelfTest() {
		UdpScrapeResponse response = new UdpScrapeResponse(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);

		Assertions.assertTrue(response.equals(response));
	}

	@Test
	public void equalsNullTest() {
		UdpScrapeResponse response = new UdpScrapeResponse(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);

		Assertions.assertFalse(response.equals(null));
	}

	@Test
	public void equalsObjectTest() {
		UdpScrapeResponse response = new UdpScrapeResponse(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);

		Assertions.assertFalse(response.equals(new Object()));
	}

	@Test
	public void equalsTest() {
		UdpScrapeResponse response = new UdpScrapeResponse(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
		UdpScrapeResponse other = new UdpScrapeResponse(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);

		Assertions.assertTrue(response.equals(other));
	}

	@Test
	public void equalsTransactionIdTest() {
		UdpScrapeResponse response = new UdpScrapeResponse(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
		UdpScrapeResponse other = new UdpScrapeResponse(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);

		Assertions.assertFalse(response.equals(other));
	}

	@Test
	public void equalsSeedersTest() {
		UdpScrapeResponse response = new UdpScrapeResponse(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
		UdpScrapeResponse other = new UdpScrapeResponse(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);

		Assertions.assertFalse(response.equals(other));
	}

	@Test
	public void equalsLeechersTest() {
		UdpScrapeResponse response = new UdpScrapeResponse(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
		UdpScrapeResponse other = new UdpScrapeResponse(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);

		Assertions.assertFalse(response.equals(other));
	}
}
