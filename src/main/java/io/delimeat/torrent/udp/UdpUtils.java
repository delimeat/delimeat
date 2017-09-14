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

import io.delimeat.torrent.udp.domain.ConnectUdpResponse;
import io.delimeat.torrent.udp.domain.ErrorUdpResponse;
import io.delimeat.torrent.udp.domain.ScrapeUdpResponse;
import io.delimeat.torrent.udp.domain.UdpResponse;
import io.delimeat.torrent.udp.exception.UdpException;
import io.delimeat.torrent.udp.exception.UdpInvalidFormatException;

public class UdpUtils {

	public final static int CONNECT_ACTION = 0;
	public final static int SCRAPE_ACTION = 2;
	public final static int ERROR_ACTION = 3;
	
	
	public static UdpResponse buildResponse(ByteBuffer buffer) throws UdpException{
		int action = buffer.getInt();			
		switch(action){
		case UdpUtils.CONNECT_ACTION:
			return UdpUtils.unmarshallConnectResponse(buffer);		
		case UdpUtils.SCRAPE_ACTION:
			return UdpUtils.unmarshallScrapeResponse(buffer);
		case UdpUtils.ERROR_ACTION:
			return UdpUtils.unmarshallErrorResponse(buffer);
		default:
			throw new UdpException(String.format("Received unsupported udp action %s", action));
		}
	}
	public static ConnectUdpResponse unmarshallConnectResponse(ByteBuffer buffer) throws UdpInvalidFormatException{
		try{
			return new ConnectUdpResponse(buffer.getInt(), buffer.getLong());
		}catch(BufferUnderflowException ex){
			throw new UdpInvalidFormatException(String.format("Unable to build connect response\nbytes: %s", new String(buffer.array())));
		}
	}
	
	public static ScrapeUdpResponse unmarshallScrapeResponse(ByteBuffer buffer) throws UdpInvalidFormatException{
		try{
			return new ScrapeUdpResponse(buffer.getInt(), buffer.getInt(), buffer.getInt());
		}catch(BufferUnderflowException ex){
			throw new UdpInvalidFormatException(String.format("Unable to build connect response\nbytes: %s", new String(buffer.array())));
		}
	}
	
	public static ErrorUdpResponse unmarshallErrorResponse(ByteBuffer buffer) throws UdpInvalidFormatException{
		try{
			int transactionId = buffer.getInt();
			byte[] msgBytes = new byte[buffer.remaining()];
			buffer.get(msgBytes);
			String message = new String(msgBytes).trim();
			return new ErrorUdpResponse(transactionId, message);
		}catch(BufferUnderflowException ex){
			throw new UdpInvalidFormatException(String.format("Unable to build connect response\nbytes: %s", new String(buffer.array())));
		}
	}
}
