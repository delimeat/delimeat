package io.delimeat.torrent.udp.domain;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import io.delimeat.torrent.domain.InfoHash;
import io.delimeat.util.DelimeatUtils;

public class ScrapeUdpRequestTest {

	@Test
	public void constructorTest() {

		byte[] infoBytes = DelimeatUtils.hexToBytes("df706cf16f45e8c0fd226223509c7e97b4ffec13");
		ScrapeUdpRequest request = new ScrapeUdpRequest(Long.MAX_VALUE, Integer.MIN_VALUE, new InfoHash(infoBytes));

		Assert.assertEquals(Long.MAX_VALUE, request.getConnectionId());
		Assert.assertEquals(Integer.MIN_VALUE, request.getTransactionId());
		Assert.assertEquals(new InfoHash(infoBytes), request.getInfoHash());

	}
	
	@Test
	public void toByteBufferTest(){
		ScrapeUdpRequest request = new ScrapeUdpRequest(Long.MAX_VALUE, Integer.MIN_VALUE, new InfoHash("INFO_HASH".getBytes()));
		
		ByteBuffer result = request.toByteBuffer();

		Assert.assertEquals(Long.MAX_VALUE, result.getLong());
		Assert.assertEquals(2, result.getInt());
		Assert.assertEquals(Integer.MIN_VALUE, result.getInt());
		byte[] bytes = new byte[result.remaining()];
		result.get(bytes);
		Assert.assertEquals("INFO_HASH", new String(bytes).trim());
	}

	@Test
	public void hashCodeTest() {
		byte[] infoBytes = DelimeatUtils.hexToBytes("df706cf16f45e8c0fd226223509c7e97b4ffec13");
		ScrapeUdpRequest request = new ScrapeUdpRequest(Long.MAX_VALUE, Integer.MIN_VALUE, new InfoHash(infoBytes));

		Assert.assertEquals(-1979621474, request.hashCode());
	}

	@Test
	public void equalsSelfTest() {
		byte[] infoBytes = DelimeatUtils.hexToBytes("df706cf16f45e8c0fd226223509c7e97b4ffec13");
		ScrapeUdpRequest request = new ScrapeUdpRequest(Long.MAX_VALUE, Integer.MIN_VALUE, new InfoHash(infoBytes));

		Assert.assertTrue(request.equals(request));
	}

	@Test
	public void equalsNullTest() {
		byte[] infoBytes = DelimeatUtils.hexToBytes("df706cf16f45e8c0fd226223509c7e97b4ffec13");
		ScrapeUdpRequest request = new ScrapeUdpRequest(Long.MAX_VALUE, Integer.MIN_VALUE, new InfoHash(infoBytes));

		Assert.assertFalse(request.equals(null));
	}

	@Test
	public void equalsObjectTest() {
		byte[] infoBytes = DelimeatUtils.hexToBytes("df706cf16f45e8c0fd226223509c7e97b4ffec13");
		ScrapeUdpRequest request = new ScrapeUdpRequest(Long.MAX_VALUE, Integer.MIN_VALUE, new InfoHash(infoBytes));

		Assert.assertFalse(request.equals(new Object()));
	}

	@Test
	public void equalsTest() {
		byte[] infoBytes = DelimeatUtils.hexToBytes("df706cf16f45e8c0fd226223509c7e97b4ffec13");
		ScrapeUdpRequest request = new ScrapeUdpRequest(Long.MAX_VALUE, Integer.MIN_VALUE, new InfoHash(infoBytes));
		ScrapeUdpRequest other = new ScrapeUdpRequest(Long.MAX_VALUE, Integer.MIN_VALUE, new InfoHash(infoBytes));

		Assert.assertTrue(request.equals(other));
	}

	@Test
	public void equalsConnectionIdTest() {
		byte[] infoBytes = DelimeatUtils.hexToBytes("df706cf16f45e8c0fd226223509c7e97b4ffec13");
		ScrapeUdpRequest request = new ScrapeUdpRequest(Long.MAX_VALUE, Integer.MIN_VALUE, new InfoHash(infoBytes));
		ScrapeUdpRequest other = new ScrapeUdpRequest(Long.MIN_VALUE, Integer.MIN_VALUE, new InfoHash(infoBytes));

		Assert.assertFalse(request.equals(other));
	}

	@Test
	public void equalsTransactionIdTest() {
		byte[] infoBytes = DelimeatUtils.hexToBytes("df706cf16f45e8c0fd226223509c7e97b4ffec13");
		ScrapeUdpRequest request = new ScrapeUdpRequest(Long.MAX_VALUE, Integer.MIN_VALUE, new InfoHash(infoBytes));
		ScrapeUdpRequest other = new ScrapeUdpRequest(Long.MAX_VALUE, Integer.MAX_VALUE, new InfoHash(infoBytes));

		Assert.assertFalse(request.equals(other));
	}

	@Test
	public void equalsInfoHashTest() {
		byte[] infoBytes = DelimeatUtils.hexToBytes("df706cf16f45e8c0fd226223509c7e97b4ffec13");
		ScrapeUdpRequest request = new ScrapeUdpRequest(Long.MAX_VALUE, Integer.MIN_VALUE, new InfoHash(infoBytes));
		ScrapeUdpRequest other = new ScrapeUdpRequest(Long.MAX_VALUE, Integer.MIN_VALUE,
				new InfoHash("INFO_HASH".getBytes()));

		Assert.assertFalse(request.equals(other));
	}

	@Test
	public void toStringTest() {
		byte[] infoBytes = DelimeatUtils.hexToBytes("df706cf16f45e8c0fd226223509c7e97b4ffec13");
		ScrapeUdpRequest request = new ScrapeUdpRequest(Long.MAX_VALUE, Integer.MIN_VALUE, new InfoHash(infoBytes));

		Assert.assertEquals(
				"ScrapeUdpRequest [connectionId=9223372036854775807, action=SCRAPE, transactionId=-2147483648, infoHash=InfoHash [getHex()=df706cf16f45e8c0fd226223509c7e97b4ffec13]]",
				request.toString());
	}
}
