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

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import org.springframework.stereotype.Component;

import io.delimeat.torrent.udp.domain.ConnectUdpRequest;
import io.delimeat.torrent.udp.domain.ConnectUdpResponse;
import io.delimeat.torrent.udp.domain.ErrorUdpResponse;
import io.delimeat.torrent.udp.domain.ScrapeUdpRequest;
import io.delimeat.torrent.udp.domain.ScrapeUdpResponse;
import io.delimeat.torrent.udp.domain.UdpResponse;
import io.delimeat.torrent.udp.exception.UdpInvalidFormatException;
import io.delimeat.torrent.udp.exception.UdpUnsupportedActionException;

@Component
public class UdpMessageFactory_Impl implements UdpMessageFactory {

	private static final int CONNECT_ACTION = 0;
	private static final int SCRAPE_ACTION = 2;
	private static final int ERROR_ACTION = 3;
	
	/* (non-Javadoc)
	 * @see io.delimeat.torrent.udp.UdpMessageFactory#connectRequest(io.delimeat.torrent.udp.domain.ConnectUdpRequest)
	 */
	@Override
	public byte[] connectRequest(ConnectUdpRequest request) {
		ByteBuffer buf = ByteBuffer.allocate(16);
		buf.putLong(request.getConnectionId());
		buf.putInt(request.getAction().value());
		buf.putInt(request.getTransactionId());
		return buf.array();
	}

	/* (non-Javadoc)
	 * @see io.delimeat.torrent.udp.UdpMessageFactory#scrapeRequest(io.delimeat.torrent.udp.domain.ScrapeUdpRequest)
	 */
	@Override
	public byte[] scrapeRequest(ScrapeUdpRequest request) {
		ByteBuffer buf = ByteBuffer.allocate(36);
		buf.putLong(request.getConnectionId());
		buf.putInt(request.getAction().value());
		buf.putInt(request.getTransactionId());
		buf.put(request.getInfoHash().getBytes());
		return buf.array();
	}

	/* (non-Javadoc)
	 * @see io.delimeat.torrent.udp.UdpMessageFactory#buildResponse(byte[])
	 */
	@Override
	public UdpResponse buildResponse(byte[] bytes) throws UdpInvalidFormatException, UdpUnsupportedActionException{
		if(bytes.length < 8){
			throw new UdpInvalidFormatException(String.format("Incorrect message length received %s expected more than 8 bytes",bytes.length));
		}
		ByteBuffer buf = ByteBuffer.wrap(bytes);
		int action = buf.getInt();
		int transactionId = buf.getInt();
		
		switch(action){
		case CONNECT_ACTION:
			return buildConnect(transactionId,buf);
		case SCRAPE_ACTION:
			return buildScrape(transactionId,buf);
		case ERROR_ACTION:
			return buildError(transactionId,buf);
		default:
			throw new UdpUnsupportedActionException(String.format("Action {} received, this is unsupported", action));
		}
	}
	
	/**
	 * @param transactionId
	 * @param buf
	 * @return
	 * @throws UdpInvalidFormatException
	 */
	public ConnectUdpResponse buildConnect(int transactionId,  ByteBuffer buf) throws UdpInvalidFormatException{
		try{
			return new ConnectUdpResponse(transactionId, buf.getLong());
		}catch(BufferUnderflowException ex){
			throw new UdpInvalidFormatException(String.format("Unable to build connect response\nbytes: %s", new String(buf.array())));
		}
	}
	
	/**
	 * @param transactionId
	 * @param buf
	 * @return
	 * @throws UdpInvalidFormatException
	 */
	public ScrapeUdpResponse buildScrape(int transactionId, ByteBuffer buf) throws UdpInvalidFormatException{
		try{
			return new ScrapeUdpResponse(transactionId, buf.getInt(), buf.getInt());
		}catch(BufferUnderflowException ex){
			throw new UdpInvalidFormatException(String.format("Unable to build connect response\nbytes: %s", new String(buf.array())));
		}
	}
	
	/**
	 * @param transactionId
	 * @param buf
	 * @return
	 * @throws UdpInvalidFormatException
	 */
	public ErrorUdpResponse buildError(int transactionId, ByteBuffer buf) throws UdpInvalidFormatException{
		if(buf.hasRemaining() == false)
			throw new UdpInvalidFormatException(String.format("Unable to build error response\nbytes: %s", new String(buf.array())));

		byte[] messageBytes = new byte[buf.remaining()] ;
		buf.get(messageBytes);
		return new ErrorUdpResponse(transactionId, new String(messageBytes).trim());
	}

}
