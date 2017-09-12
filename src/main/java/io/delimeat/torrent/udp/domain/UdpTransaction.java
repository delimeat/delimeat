package io.delimeat.torrent.udp.domain;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.delimeat.torrent.udp.exception.UdpTimeoutException;

public class UdpTransaction {

	public enum Status {PENDING, COMPLETE};
	
	private final InetSocketAddress toAddress;
	private final UdpRequest request;
	private UdpResponse response;
	private Exception exception;
	private final CountDownLatch latch = new CountDownLatch(1);
	
	public UdpTransaction(UdpRequest request, InetSocketAddress toAddress){
		this.request = request;
		this.toAddress = toAddress;
	}

	/**
	 * @return the response
	 */
	public UdpResponse getResponse(long timeout) throws Exception {
		
		if(response == null){
			try{
				latch.await(timeout, TimeUnit.MILLISECONDS);
			}catch(InterruptedException ex){
				throw new UdpTimeoutException();
			}
		}
		
		if(exception != null){
			throw exception;
		}
		
		return response;
	}

	/**
	 * @param response the response to set
	 */
	public void setResponse(UdpResponse response) {
		this.response = response;
		latch.countDown();
		
	}
	
	public void setException(Exception exception){
		this.exception = exception;
		latch.countDown();
	}

	/**
	 * @return the toAddress
	 */
	public InetSocketAddress getToAddress() {
		return toAddress;
	}

	/**
	 * @return the request
	 */
	public UdpRequest getRequest() {
		return request;
	}

	
	
}