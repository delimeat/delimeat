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

import org.bouncycastle.util.Arrays;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.torrent.domain.InfoHash;
import io.delimeat.torrent.udp.domain.ConnectUdpRequest;
import io.delimeat.torrent.udp.domain.ConnectUdpResponse;
import io.delimeat.torrent.udp.domain.ErrorUdpResponse;
import io.delimeat.torrent.udp.domain.ScrapeUdpRequest;
import io.delimeat.torrent.udp.domain.ScrapeUdpResponse;
import io.delimeat.torrent.udp.domain.UdpAction;
import io.delimeat.torrent.udp.domain.UdpResponse;
import io.delimeat.torrent.udp.exception.UdpInvalidFormatException;
import io.delimeat.torrent.udp.exception.UdpUnsupportedActionException;
import io.delimeat.util.DelimeatUtils;

public class UdpMessageFactory_ImplTest {
	
	private UdpMessageFactory_Impl factory;
	
	@Before
	public void setUp(){
		factory = new UdpMessageFactory_Impl();
	}
	
	@Test
	public void buildErrorTest() throws Exception{
		ByteBuffer buf = ByteBuffer.wrap("MESSAGE".getBytes());
		ErrorUdpResponse result = factory.buildError(Integer.MAX_VALUE, buf);
		
		Assert.assertEquals(Integer.MAX_VALUE, result.getTransactionId());
		Assert.assertEquals("MESSAGE", result.getMessage());
	}
	
	@Test(expected=UdpInvalidFormatException.class)
	public void buildErrorExceptionTest() throws Exception{
		byte[] messageBytes = new byte[0];
		ByteBuffer buf = ByteBuffer.wrap(messageBytes);
		factory.buildError(Integer.MAX_VALUE, buf);
	}
	
	@Test
	public void buildConnectTest() throws Exception{
		ByteBuffer buf = ByteBuffer.allocate(8);
		buf.putLong(0x41727101980L);
		
		ByteBuffer buff2 = ByteBuffer.wrap(buf.array());
		ConnectUdpResponse result = factory.buildConnect(Integer.MAX_VALUE, buff2);
		
		Assert.assertEquals(Integer.MAX_VALUE, result.getTransactionId());
		Assert.assertEquals(0x41727101980L, result.getConnectionId());
	}
	
	@Test(expected=UdpInvalidFormatException.class)
	public void buildConnectExceptionTest() throws Exception{
		ByteBuffer buf = ByteBuffer.wrap("MESSAGE".getBytes());
		factory.buildConnect(Integer.MAX_VALUE, buf);
	}
	
	@Test
	public void buildScrapeTest() throws Exception{
		ByteBuffer buf = ByteBuffer.allocate(8);
		buf.putInt(Integer.MAX_VALUE);
		buf.putInt(Integer.MIN_VALUE);
		
		ByteBuffer buff2 = ByteBuffer.wrap(buf.array());
		ScrapeUdpResponse result = factory.buildScrape(Integer.MAX_VALUE, buff2);
		
		Assert.assertEquals(Integer.MAX_VALUE, result.getTransactionId());
		Assert.assertEquals(Integer.MAX_VALUE, result.getSeeders());
		Assert.assertEquals(Integer.MIN_VALUE, result.getLeechers());
	}
	
	@Test(expected=UdpInvalidFormatException.class)
	public void buildScrapeExceptionTest() throws Exception{
		ByteBuffer buf = ByteBuffer.wrap("MESSAGE".getBytes());
		factory.buildScrape(Integer.MAX_VALUE, buf);
	}
	
	@Test
	public void connectRequestTest(){
		ConnectUdpRequest request = new ConnectUdpRequest(Integer.MIN_VALUE);
		byte[] result = factory.connectRequest(request);
		
		ByteBuffer buf = ByteBuffer.wrap(result);
		Assert.assertEquals(0x41727101980L, buf.getLong());
		Assert.assertEquals(0, buf.getInt());
		Assert.assertEquals(Integer.MIN_VALUE, buf.getInt());
	}
	
	@Test
	public void scrapeRequestTest(){
		byte[] infoBytes = DelimeatUtils.hexToBytes("df706cf16f45e8c0fd226223509c7e97b4ffec13");
		InfoHash infoHash = new InfoHash(infoBytes);
		ScrapeUdpRequest request = new ScrapeUdpRequest(Long.MAX_VALUE, Integer.MIN_VALUE, infoHash);
		byte[] result = factory.scrapeRequest(request);
		
		ByteBuffer buf = ByteBuffer.wrap(result);
		Assert.assertEquals(Long.MAX_VALUE, buf.getLong());
		Assert.assertEquals(2, buf.getInt());
		Assert.assertEquals(Integer.MIN_VALUE, buf.getInt());
		byte[] resultInfoBytes = new byte[20]; 
		buf.get(resultInfoBytes);
		Assert.assertTrue(Arrays.areEqual(infoBytes, resultInfoBytes));
	}
	
	@Test(expected=UdpInvalidFormatException.class)
	public void buildResponseTooShortTest() throws Exception{
		byte[] bytes = new byte[]{1,3,4,5};
		factory.buildResponse(bytes);
	}
	
	@Test(expected=UdpUnsupportedActionException.class)
	public void buildResponseUnsupportedActionTest() throws Exception{
		ByteBuffer buf = ByteBuffer.allocate(8);
		buf.putInt(Integer.MAX_VALUE);
		buf.putInt(Integer.MIN_VALUE);
		factory.buildResponse(buf.array());
	}
	
	@Test
	public void buildResponseConnectTest() throws Exception{
		ByteBuffer buf = ByteBuffer.allocate(16);
		buf.putInt(0);
		buf.putInt(Integer.MIN_VALUE);
		buf.putLong(Long.MAX_VALUE);
		UdpResponse result = factory.buildResponse(buf.array());
		
		Assert.assertEquals(UdpAction.CONNECT, result.getAction());
		Assert.assertEquals(Integer.MIN_VALUE, result.getTransactionId());
		
		ConnectUdpResponse response = (ConnectUdpResponse)result;
		Assert.assertEquals(Long.MAX_VALUE, response.getConnectionId());
	}
	
	@Test
	public void buildResponseScrapeTest() throws Exception{
		ByteBuffer buf = ByteBuffer.allocate(16);
		buf.putInt(2);
		buf.putInt(Integer.MIN_VALUE);
		buf.putInt(Integer.MAX_VALUE);
		buf.putInt(Integer.MIN_VALUE);
		UdpResponse result = factory.buildResponse(buf.array());
		
		Assert.assertEquals(UdpAction.SCRAPE, result.getAction());
		Assert.assertEquals(Integer.MIN_VALUE, result.getTransactionId());
		
		ScrapeUdpResponse response = (ScrapeUdpResponse)result;
		Assert.assertEquals(Integer.MAX_VALUE, response.getSeeders());
		Assert.assertEquals(Integer.MIN_VALUE, response.getLeechers());
	}
	
	@Test
	public void buildResponseErrorTest() throws Exception{
		ByteBuffer buf = ByteBuffer.allocate(16);
		buf.putInt(3);
		buf.putInt(Integer.MIN_VALUE);
		buf.put("MESSAGE".getBytes());
		
		UdpResponse result = factory.buildResponse(buf.array());
		
		Assert.assertEquals(UdpAction.ERROR, result.getAction());
		Assert.assertEquals(Integer.MIN_VALUE, result.getTransactionId());
		
		ErrorUdpResponse response = (ErrorUdpResponse)result;
		Assert.assertEquals("MESSAGE", response.getMessage());
	}
}
