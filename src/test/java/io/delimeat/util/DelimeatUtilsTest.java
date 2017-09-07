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
package io.delimeat.util;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import okhttp3.OkHttpClient;

public class DelimeatUtilsTest {

	@Test
	public void urlEscapeTest() {
		Assert.assertEquals("ENCODE+THIS", DelimeatUtils.urlEscape("ENCODE THIS", "UTF-8"));
	}

	@Test(expected = RuntimeException.class)
	public void urlEscapeUnsupportedEncodingTest() {
		DelimeatUtils.urlEscape("ENCODE THIS", "JIBBERISH");
	}

	@Test
	public void toHexTest() {
		Assert.assertEquals("6279746573", DelimeatUtils.toHex("bytes".getBytes()));
	}
	
	@Test
	public void hexToBytesTest() {
		Assert.assertEquals("bytes", new String(DelimeatUtils.hexToBytes("6279746573")));
	}

	@Test
	public void sha1HashTest() {
		byte[] expectedBytes = new byte[] { 50, 82, 79, -89, 112, -96, 70, -88, 123, 28, 107, -61, 15, 99, -84, 52, 18,
				-84, -125, 37 };
		Assert.assertTrue(Arrays.equals(expectedBytes, DelimeatUtils.hashBytes("byes".getBytes(),"SHA-1")));
	}
	
	@Test(expected=RuntimeException.class)
	public void hashBytesInvalidAlgorithmTest(){
		DelimeatUtils.hashBytes("byes".getBytes(),"JIBBERISH");
	}
	
	@Test
	public void httpClientTest(){
		OkHttpClient client = DelimeatUtils.httpClient();
		Assert.assertEquals(2000, client.connectTimeoutMillis());
		Assert.assertEquals(2000, client.readTimeoutMillis());
		Assert.assertEquals(2000, client.writeTimeoutMillis());
	}
}
