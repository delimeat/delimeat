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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.delimeat.util.okhttp.LoggingInterceptor;
import okhttp3.OkHttpClient;

public class DelimeatUtilsTest {

	@Test
	public void urlEscapeTest() {
		Assertions.assertEquals("ENCODE+THIS", DelimeatUtils.urlEscape("ENCODE THIS", "UTF-8"));
	}

	@Test
	public void urlEscapeUnsupportedEncodingTest() {
		RuntimeException ex = Assertions.assertThrows(RuntimeException.class, () ->{
			DelimeatUtils.urlEscape("ENCODE THIS", "JIBBERISH");
		});
		
		Assertions.assertEquals("java.io.UnsupportedEncodingException: JIBBERISH", ex.getMessage());
		
	}

	@Test
	public void toHexTest() {
		Assertions.assertEquals("6279746573", DelimeatUtils.toHex("bytes".getBytes()));
	}
	
	@Test
	public void hexToBytesTest() {
		Assertions.assertEquals("bytes", new String(DelimeatUtils.hexToBytes("6279746573")));
	}

	@Test
	public void sha1HashTest() {
		byte[] expectedBytes = new byte[] { 50, 82, 79, -89, 112, -96, 70, -88, 123, 28, 107, -61, 15, 99, -84, 52, 18,
				-84, -125, 37 };
		Assertions.assertTrue(Arrays.equals(expectedBytes, DelimeatUtils.hashBytes("byes".getBytes(),"SHA-1")));
	}
	
	@Test
	public void hashBytesInvalidAlgorithmTest(){
		RuntimeException ex = Assertions.assertThrows(RuntimeException.class, () -> {
			DelimeatUtils.hashBytes("bytes".getBytes(),"JIBBERISH");
		});
		
		Assertions.assertEquals("java.security.NoSuchAlgorithmException: JIBBERISH MessageDigest not available", ex.getMessage());
	}
	
	@Test
	public void httpClientTest(){
		OkHttpClient client = DelimeatUtils.httpClient();
		Assertions.assertEquals(2000, client.connectTimeoutMillis());
		Assertions.assertEquals(2000, client.readTimeoutMillis());
		Assertions.assertEquals(2000, client.writeTimeoutMillis());
		Assertions.assertEquals(1, client.interceptors().size());
		Assertions.assertEquals(LoggingInterceptor.class, client.interceptors().get(0).getClass());
	}
	
	@Test
	public void cleanTitleTest() {
		String result = DelimeatUtils.cleanTitle("Marvel's DC's This is 2015 a V'ery-Nice $#10293734521,.<>~?! title & (2016)");

		Assertions.assertEquals("This is 2015 a Very-Nice 10293734521 title and", result);
	}
	
	@Test
	public void cleanTitleNullTest() {
		Assertions.assertNull(DelimeatUtils.cleanTitle(null));
	}
	
	@Test
	public void cleanTitleEmptyTest() {
		Assertions.assertEquals("", DelimeatUtils.cleanTitle(""));
	}

}
