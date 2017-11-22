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
package io.delimeat.torrent.exception;

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TorrentTimeoutExceptionTest {

	@Test
	public void constructorURLTest() throws MalformedURLException {
		Assertions.assertEquals("Timeout for http://test.com",
				new TorrentTimeoutException(new URL("http://test.com")).getMessage());
	}

	@Test
	public void constructorInetAddressTest() throws MalformedURLException {
		Assertions.assertEquals("Timeout for 0.0.0.0:1234",
				new TorrentTimeoutException(InetSocketAddress.createUnresolved("0.0.0.0", 1234)).getMessage());
	}
}
