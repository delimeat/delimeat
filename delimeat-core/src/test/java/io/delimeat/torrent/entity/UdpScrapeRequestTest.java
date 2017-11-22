package io.delimeat.torrent.entity;

import java.nio.ByteBuffer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.delimeat.util.DelimeatUtils;

public class UdpScrapeRequestTest {

	@Test
	public void constructorTest() {

		byte[] infoBytes = DelimeatUtils.hexToBytes("df706cf16f45e8c0fd226223509c7e97b4ffec13");
		UdpScrapeRequest request = new UdpScrapeRequest(Long.MAX_VALUE, Integer.MIN_VALUE, new InfoHash(infoBytes));

		Assertions.assertEquals(Long.MAX_VALUE, request.getConnectionId());
		Assertions.assertEquals(Integer.MIN_VALUE, request.getTransactionId());
		Assertions.assertEquals(new InfoHash(infoBytes), request.getInfoHash());
		Assertions.assertEquals(UdpAction.SCRAPE, request.getAction());

	}

	@Test
	public void toByteBufferTest() {
		UdpScrapeRequest request = new UdpScrapeRequest(Long.MAX_VALUE, Integer.MIN_VALUE,
				new InfoHash("INFO_HASH".getBytes()));

		ByteBuffer result = request.toByteBuffer();

		Assertions.assertEquals(Long.MAX_VALUE, result.getLong());
		Assertions.assertEquals(2, result.getInt());
		Assertions.assertEquals(Integer.MIN_VALUE, result.getInt());
		byte[] bytes = new byte[result.remaining()];
		result.get(bytes);
		Assertions.assertEquals("INFO_HASH", new String(bytes).trim());
	}

	@Test
	public void hashCodeTest() {
		byte[] infoBytes = DelimeatUtils.hexToBytes("df706cf16f45e8c0fd226223509c7e97b4ffec13");
		UdpScrapeRequest request = new UdpScrapeRequest(Long.MAX_VALUE, Integer.MIN_VALUE, new InfoHash(infoBytes));

		Assertions.assertEquals(-1979621474, request.hashCode());
	}

	@Test
	public void equalsSelfTest() {
		byte[] infoBytes = DelimeatUtils.hexToBytes("df706cf16f45e8c0fd226223509c7e97b4ffec13");
		UdpScrapeRequest request = new UdpScrapeRequest(Long.MAX_VALUE, Integer.MIN_VALUE, new InfoHash(infoBytes));

		Assertions.assertTrue(request.equals(request));
	}

	@Test
	public void equalsNullTest() {
		byte[] infoBytes = DelimeatUtils.hexToBytes("df706cf16f45e8c0fd226223509c7e97b4ffec13");
		UdpScrapeRequest request = new UdpScrapeRequest(Long.MAX_VALUE, Integer.MIN_VALUE, new InfoHash(infoBytes));

		Assertions.assertFalse(request.equals(null));
	}

	@Test
	public void equalsObjectTest() {
		byte[] infoBytes = DelimeatUtils.hexToBytes("df706cf16f45e8c0fd226223509c7e97b4ffec13");
		UdpScrapeRequest request = new UdpScrapeRequest(Long.MAX_VALUE, Integer.MIN_VALUE, new InfoHash(infoBytes));

		Assertions.assertFalse(request.equals(new Object()));
	}

	@Test
	public void equalsTest() {
		byte[] infoBytes = DelimeatUtils.hexToBytes("df706cf16f45e8c0fd226223509c7e97b4ffec13");
		UdpScrapeRequest request = new UdpScrapeRequest(Long.MAX_VALUE, Integer.MIN_VALUE, new InfoHash(infoBytes));
		UdpScrapeRequest other = new UdpScrapeRequest(Long.MAX_VALUE, Integer.MIN_VALUE, new InfoHash(infoBytes));

		Assertions.assertTrue(request.equals(other));
	}

	@Test
	public void equalsConnectionIdTest() {
		byte[] infoBytes = DelimeatUtils.hexToBytes("df706cf16f45e8c0fd226223509c7e97b4ffec13");
		UdpScrapeRequest request = new UdpScrapeRequest(Long.MAX_VALUE, Integer.MIN_VALUE, new InfoHash(infoBytes));
		UdpScrapeRequest other = new UdpScrapeRequest(Long.MIN_VALUE, Integer.MIN_VALUE, new InfoHash(infoBytes));

		Assertions.assertFalse(request.equals(other));
	}

	@Test
	public void equalsTransactionIdTest() {
		byte[] infoBytes = DelimeatUtils.hexToBytes("df706cf16f45e8c0fd226223509c7e97b4ffec13");
		UdpScrapeRequest request = new UdpScrapeRequest(Long.MAX_VALUE, Integer.MIN_VALUE, new InfoHash(infoBytes));
		UdpScrapeRequest other = new UdpScrapeRequest(Long.MAX_VALUE, Integer.MAX_VALUE, new InfoHash(infoBytes));

		Assertions.assertFalse(request.equals(other));
	}

	@Test
	public void equalsInfoHashTest() {
		byte[] infoBytes = DelimeatUtils.hexToBytes("df706cf16f45e8c0fd226223509c7e97b4ffec13");
		UdpScrapeRequest request = new UdpScrapeRequest(Long.MAX_VALUE, Integer.MIN_VALUE, new InfoHash(infoBytes));
		UdpScrapeRequest other = new UdpScrapeRequest(Long.MAX_VALUE, Integer.MIN_VALUE,
				new InfoHash("INFO_HASH".getBytes()));

		Assertions.assertFalse(request.equals(other));
	}

	@Test
	public void equalsInfoHashNullTest() {
		byte[] infoBytes = DelimeatUtils.hexToBytes("df706cf16f45e8c0fd226223509c7e97b4ffec13");
		UdpScrapeRequest request = new UdpScrapeRequest(Long.MAX_VALUE, Integer.MIN_VALUE, null);
		UdpScrapeRequest other = new UdpScrapeRequest(Long.MAX_VALUE, Integer.MIN_VALUE, new InfoHash(infoBytes));

		Assertions.assertFalse(request.equals(other));
	}

	@Test
	public void toStringTest() {
		byte[] infoBytes = DelimeatUtils.hexToBytes("df706cf16f45e8c0fd226223509c7e97b4ffec13");
		UdpScrapeRequest request = new UdpScrapeRequest(Long.MAX_VALUE, Integer.MIN_VALUE, new InfoHash(infoBytes));

		Assertions.assertEquals(
				"ScrapeUdpRequest [connectionId=9223372036854775807, action=SCRAPE, transactionId=-2147483648, infoHash=InfoHash [getHex()=df706cf16f45e8c0fd226223509c7e97b4ffec13]]",
				request.toString());
	}
}
