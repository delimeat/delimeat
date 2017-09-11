package io.delimeat.torrent;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import io.delimeat.torrent.domain.InfoHash;
import io.delimeat.torrent.domain.ScrapeResult;
import io.delimeat.torrent.exception.TorrentException;
import io.delimeat.torrent.exception.UnhandledScrapeException;
import io.delimeat.torrent.udp.UdpUtils;
import io.delimeat.torrent.udp.domain.ConnectUdpRequest;
import io.delimeat.torrent.udp.domain.ConnectUdpResponse;
import io.delimeat.torrent.udp.domain.ConnectionId;
import io.delimeat.torrent.udp.domain.ErrorUdpResponse;
import io.delimeat.torrent.udp.domain.ScrapeUdpRequest;
import io.delimeat.torrent.udp.domain.ScrapeUdpResponse;
import io.delimeat.torrent.udp.domain.UdpAction;
import io.delimeat.torrent.udp.domain.UdpRequest;
import io.delimeat.torrent.udp.domain.UdpResponse;
import io.delimeat.torrent.udp.exception.UdpErrorResponseException;
import io.delimeat.torrent.udp.exception.UdpException;

public class UdpNIOScrapeRequestHandler_Impl extends AbstractScrapeRequestHandler implements ScrapeRequestHandler {

	private static final Random RANDOM_GEN = new Random();

	
	@Autowired
	@Qualifier("udpScrapeDatagramChannel")
	private DatagramChannel channel;

	private Map<InetSocketAddress,ConnectionId> connections = new ConcurrentHashMap<>(); 

	public UdpNIOScrapeRequestHandler_Impl(){
		super(Arrays.asList("UDP"));
	}
	
	/**
	 * @return the channel
	 */
	public DatagramChannel getChannel() {
		return channel;
	}

	/**
	 * @param channel the channel to set
	 */
	public void setChannel(DatagramChannel channel) {
		this.channel = channel;
	}

	@Override
	public ScrapeResult doScrape(URI uri, InfoHash infoHash) throws UnhandledScrapeException, TorrentException {
		String host = uri.getHost();
		int port = uri.getPort() != -1 ? uri.getPort() : 80; // if no port is provided use default of 80
		InetSocketAddress address = new InetSocketAddress(host,port);
		
		try{
			ConnectionId connectionId = getConnectionId(address);
			return getScrapeResult(connectionId, infoHash, address);
		}catch(Exception ex){
			throw new TorrentException(ex);
		}
	}
	
	public Integer generateTransactionId(){
		Integer transactionId = RANDOM_GEN.nextInt();
		return transactionId;
	}
	
	public ConnectionId getConnectionId(InetSocketAddress address) throws Exception{
		ConnectionId connectionId = connections.get(address);
		if(connectionId == null || connectionId.getExpiry().isBefore(Instant.now())){
			ConnectUdpRequest connReq = new ConnectUdpRequest(generateTransactionId());
			ConnectUdpResponse response = (ConnectUdpResponse)send(connReq, address);
			connectionId = new ConnectionId(response.getConnectionId(),address, Instant.now().plusSeconds(10*60));
			connections.put(address, connectionId);
			return connectionId;
		}else{
			return connectionId;
		}
	}
	
	public ScrapeResult getScrapeResult(ConnectionId connectionId,InfoHash infoHash, InetSocketAddress address) throws Exception{
		ScrapeUdpRequest request = new ScrapeUdpRequest(connectionId.getValue(), generateTransactionId(), infoHash);
		ScrapeUdpResponse response = (ScrapeUdpResponse)send(request, address);
		return new ScrapeResult(response.getSeeders(), response.getLeechers());
	}
	
	
	public UdpResponse send(UdpRequest request, InetSocketAddress toAddress) throws IOException, UdpException{
		channel.send(request.toByteBuffer(), toAddress);
		
		ByteBuffer readBuffer = ByteBuffer.allocate(1024);
		InetSocketAddress fromAddress = (InetSocketAddress)channel.receive(readBuffer);

		UdpResponse response = UdpUtils.buildResponse(readBuffer);
		if(response.getTransactionId() != request.getTransactionId()){
			//TODO mismatched transaction id error
		}else if (fromAddress != toAddress){
			//TODO mismatched address exception
		}else if(response.getAction() == UdpAction.ERROR){
			throw new UdpErrorResponseException((ErrorUdpResponse)response);
		}else if(response.getAction() != request.getAction()){
			throw new UdpException(String.format("Response action doesnt match request\n%s\n%s", request, response));
		}
		return response;
		
	}

}
