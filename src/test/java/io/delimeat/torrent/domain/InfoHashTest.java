/*
 * Copyright 2013-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.delimeat.torrent.domain;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.hash.Hashing;

public class InfoHashTest {

	@Test
	public void byteArrayConstructorTest() {
		byte[] sha1Bytes = Hashing.sha1().hashBytes("INFO_HASH".getBytes()).asBytes();
		InfoHash infoHash = new InfoHash(sha1Bytes);
		Assert.assertEquals("601492e054f9540eb0129c35deb385baa2faf0fe", infoHash.getHex());
	}

	@Test
	public void getBytesTest() throws UnsupportedEncodingException {
		byte[] sha1Bytes = Hashing.sha1().hashBytes("INFO_HASH".getBytes()).asBytes();
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
	public void hashCodeTest(){
		Assert.assertEquals(-358247363, new InfoHash("INFO_HASH".getBytes()).hashCode());
	}
	
	@Test
	public void toStringTest() {
		byte[] sha1Bytes = Hashing.sha1().hashBytes("INFO_HASH".getBytes()).asBytes();
		InfoHash infoHash = new InfoHash(sha1Bytes);
		Assert.assertEquals("InfoHash [getHex()=601492e054f9540eb0129c35deb385baa2faf0fe]", infoHash.toString());
	}
  	
}
