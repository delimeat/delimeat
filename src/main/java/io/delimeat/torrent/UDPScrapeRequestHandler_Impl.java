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
package io.delimeat.torrent;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import io.delimeat.torrent.domain.InfoHash;
import io.delimeat.torrent.domain.ScrapeResult;
import io.delimeat.torrent.exception.TorrentException;
import io.delimeat.torrent.exception.TorrentTimeoutException;
import io.delimeat.torrent.exception.UnhandledScrapeException;

@Component
public class UDPScrapeRequestHandler_Impl implements ScrapeRequestHandler {

	private static final Random RANDOM_GEN = new Random();
	private static final int ACTION_CONNECT = 0;
	private static final int ACTION_SCRAPE = 2;
	private static final int ACTION_ERROR = 3;
	private static final long CONNECT_CONNECTION_ID = 0x41727101980L;
	private static final int MAX_SEND_RETRYS = 3; // 3 try's is lazy, because i can't be bothered getting an new connection id
	
	private final List<String> protocols = Arrays.asList("UDP");
	
	@Autowired
	@Qualifier("updScrapeDatagramSocket")
	private DatagramSocket socket;
	
	/**
	 * Set datagram socket
	 * @param socket
	 */
	public void setSocket(DatagramSocket socket){
		this.socket = socket;
	}
	
	/**
	 * get datagram socket
	 * @return
	 */
	public DatagramSocket getSocket(){
		return socket;
	}

	/* (non-Javadoc)
	 * @see io.delimeat.torrent.ScrapeRequestHandler#getSupportedProtocols()
	 */
	@Override
	public List<String> getSupportedProtocols() {
		return protocols;
	}
	
	/* (non-Javadoc)
	 * @see io.delimeat.torrent.ScrapeRequestHandler#scrape(java.net.URI, io.delimeat.torrent.domain.InfoHash)
	 */
	@Override
	public ScrapeResult scrape(URI uri, InfoHash infoHash) throws UnhandledScrapeException,TorrentTimeoutException, TorrentException, IOException {
		if(protocols.contains(uri.getScheme().toUpperCase()) == false ){
			throw new TorrentException(String.format("Unsupported protocol %s", uri.getScheme()));
		}
		
		String host = uri.getHost();
		int port = uri.getPort() != -1 ? uri.getPort() : 80; // if no port is provided use default of 80
		InetSocketAddress address = new InetSocketAddress(host,port);
		int connTransId = RANDOM_GEN.nextInt();
		byte[] connRequest = createConnectRequest(connTransId);
		byte[] connResponse = sendRequest(connRequest,address);
		long scrapeConnId = handleConnectionResponse(connResponse, connTransId);
		int scrapeTransId = RANDOM_GEN.nextInt();
		byte[] scrapeRequest = createScrapeRequest(scrapeConnId,scrapeTransId, infoHash);
		byte[] scrapeResponse = sendRequest(scrapeRequest,address);
		ScrapeResult result = handleScrapeResponse(scrapeResponse, scrapeTransId);
		return result;

	}
	
	public byte[] createConnectRequest(int transId){
		ByteBuffer buf = ByteBuffer.allocate(16);
		buf.putLong(CONNECT_CONNECTION_ID);
		buf.putInt(ACTION_CONNECT);
		buf.putInt(transId);
		return buf.array();
	}
	
	public byte[] createScrapeRequest(long connId, int transId, InfoHash infoHash ){
		ByteBuffer buf = ByteBuffer.allocate(16+infoHash.getBytes().length);
		buf.putLong(connId);
		buf.putInt(ACTION_SCRAPE);
		buf.putInt(transId);
		buf.put(infoHash.getBytes());
		return buf.array();
	}
	
	public long handleConnectionResponse(byte[] response, int sentTransId) throws TorrentException{
		ByteBuffer buf = ByteBuffer.wrap(response);
		try{
			int action = buf.getInt();
			int receivedTransId = buf.getInt();
			if(receivedTransId!=sentTransId){
				throw new TorrentException("incorrect transaction id, sent: " + sentTransId + " recived: " + receivedTransId);
			}
			else if(action==ACTION_ERROR){
				throw new TorrentException("received an error response from tracker");
			}
			else if(action!=ACTION_CONNECT){
				throw new TorrentException("received an incorrect action from tracker, expected: "+ ACTION_CONNECT +" recieved: " + action);
			}
			else {
				long connId = buf.getLong();
				return connId;
			}
		}catch(BufferUnderflowException e){
			throw new TorrentException("incorrect response length",e);
		}
	}
	
	public ScrapeResult handleScrapeResponse(byte[] response, int sentTransId) throws TorrentException{
		ByteBuffer buf = ByteBuffer.wrap(response);
		try{
			int action = buf.getInt();
			int receivedTransId = buf.getInt();
			if(receivedTransId!=sentTransId){
				throw new TorrentException("incorrect transaction id, sent: " + sentTransId + " recived: " + receivedTransId);
			}
			else if(action==ACTION_ERROR){
				throw new TorrentException("Received an error response from tracker");
			}
			else if(action!=ACTION_SCRAPE){
				throw new TorrentException("received an incorrect action from tracker, expected: "+ ACTION_SCRAPE +" recieved: " + action);

			} else{
				int seeders = buf.getInt();
				buf.getInt(); // number of completed downloads, don't store it
				int leechers = buf.getInt();
				return new ScrapeResult(seeders, leechers);
			}
		}catch(BufferUnderflowException e){
			throw new TorrentException("incorrect response length",e);
		}
	}
	
	public synchronized byte[] sendRequest(byte[] sendData, InetSocketAddress address) throws IOException, TorrentTimeoutException{
		DatagramSocket clientSocket = getSocket();
		byte[] receiveData = new byte[1024];
		byte[] response = null;
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address);
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		int sendTry = 0;
		while(response==null && sendTry < MAX_SEND_RETRYS){
			sendTry++;
			clientSocket.setSoTimeout(sendTry*1000);
			clientSocket.send(sendPacket);
			try{
				clientSocket.receive(receivePacket);
			}catch(SocketTimeoutException ex){
				if(sendTry==MAX_SEND_RETRYS){
					throw new TorrentTimeoutException(address);
				}else{
					continue;
				}
			}
			response = receivePacket.getData();
		}
		return response;
	}

}
