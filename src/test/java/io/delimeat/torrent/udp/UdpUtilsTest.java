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
package io.delimeat.torrent.udp;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import io.delimeat.torrent.udp.domain.ConnectUdpResponse;
import io.delimeat.torrent.udp.domain.ErrorUdpResponse;
import io.delimeat.torrent.udp.domain.ScrapeUdpResponse;
import io.delimeat.torrent.udp.exception.UdpInvalidFormatException;

public class UdpUtilsTest {
	
	@Test
	public void buildErrorResponseTest() throws Exception{
		ByteBuffer buf = ByteBuffer.allocate(1024)
				.putInt(Integer.MAX_VALUE)
				.put("MESSAGE".getBytes());
		buf.clear();

		ErrorUdpResponse result = UdpUtils.buildErrorResponse(buf);
		
		
		Assert.assertEquals(Integer.MAX_VALUE, result.getTransactionId());
		Assert.assertEquals("MESSAGE", result.getMessage());
	}
	
	@Test(expected=UdpInvalidFormatException.class)
	public void buildErrorResponseExceptionTest() throws Exception{
		ByteBuffer buf = ByteBuffer.allocate(0);
		UdpUtils.buildErrorResponse( buf);
	}
	
	@Test
	public void buildConnectResponseTest() throws Exception{
		ByteBuffer buf = ByteBuffer.allocate(12)
				.putInt(Integer.MAX_VALUE)
				.putLong(0x41727101980L);
		buf.clear();
		
		ConnectUdpResponse result = UdpUtils.buildConnectResponse(buf);
		
		Assert.assertEquals(Integer.MAX_VALUE, result.getTransactionId());
		Assert.assertEquals(0x41727101980L, result.getConnectionId());
	}
	
	@Test(expected=UdpInvalidFormatException.class)
	public void buildConnectResponseExceptionTest() throws Exception{
		ByteBuffer buf = ByteBuffer.wrap("MESSAGE".getBytes());
		UdpUtils.buildConnectResponse(buf);
	}
	
	@Test
	public void buildScrapeResponseTest() throws Exception{
		ByteBuffer buf = ByteBuffer.allocate(12)
				.putInt(Integer.MAX_VALUE)
				.putInt(Integer.MAX_VALUE)
				.putInt(Integer.MIN_VALUE);
		
		ByteBuffer buff2 = ByteBuffer.wrap(buf.array());
		ScrapeUdpResponse result = UdpUtils.buildScrapeResponse(buff2);
		
		Assert.assertEquals(Integer.MAX_VALUE, result.getTransactionId());
		Assert.assertEquals(Integer.MAX_VALUE, result.getSeeders());
		Assert.assertEquals(Integer.MIN_VALUE, result.getLeechers());
	}
	
	@Test(expected=UdpInvalidFormatException.class)
	public void buildScrapeResponseExceptionTest() throws Exception{
		ByteBuffer buf = ByteBuffer.wrap("MESSAGE".getBytes());
		UdpUtils.buildScrapeResponse(buf);
	}

}
