package io.delimeat.core.torrent;

import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import io.delimeat.common.util.DelimeatUtils;

public class InfoHashTest {

	@Test
	public void byteArrayConstructorTest() {
		byte[] sha1Bytes = DelimeatUtils.getSHA1("INFO_HASH".getBytes());
		InfoHash infoHash = new InfoHash(sha1Bytes);
		Assert.assertEquals("601492e054f9540eb0129c35deb385baa2faf0fe", infoHash.getHex());
	}

	@Test
	public void getBytesTest() throws UnsupportedEncodingException {
		byte[] sha1Bytes = DelimeatUtils.getSHA1("INFO_HASH".getBytes());
		InfoHash infoHash = new InfoHash(sha1Bytes);

		String encodedInfoHash = URLEncoder.encode( new String(infoHash.getBytes(),"ISO-8859-1"), "ISO-8859-1");

		Assert.assertEquals("%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE", encodedInfoHash);
	}

	@Test
	public void nullEqualsTest() {
		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		Assert.assertFalse(infoHash.equals(null));
	}

	@Test
	public void notInfoHashEqualsTest() {
		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		Assert.assertFalse(infoHash.equals(new Object()));
	}

	@Test
	public void selfEqualsTest() {
		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		Assert.assertTrue(infoHash.equals(infoHash));
	}

	@Test
	public void matchingEqualsTest() {
		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		InfoHash infoHash1 = new InfoHash("INFO_HASH".getBytes());
		Assert.assertTrue(infoHash.equals(infoHash1));
	}

	@Test
	public void notMatchingEqualsTest() {
		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		InfoHash infoHash1 = new InfoHash("OTHER_VALUE".getBytes());
		Assert.assertFalse(infoHash.equals(infoHash1));
	}

	@Test
	public void toStringTest() {
		byte[] sha1Bytes = DelimeatUtils.getSHA1("INFO_HASH".getBytes());
		InfoHash infoHash = new InfoHash(sha1Bytes);
		Assert.assertEquals("InfoHash{value=601492e054f9540eb0129c35deb385baa2faf0fe}", infoHash.toString());
	}
  	
}
