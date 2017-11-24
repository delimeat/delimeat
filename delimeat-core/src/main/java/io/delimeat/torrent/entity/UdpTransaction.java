package io.delimeat.torrent.entity;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.delimeat.torrent.exception.UdpTimeoutException;

public class UdpTransaction {
	
	private final InetSocketAddress toAddress;
	private final UdpRequest request;
	private UdpResponse response;
	private Exception exception;
	private final CountDownLatch latch = new CountDownLatch(1);
	
	public UdpTransaction(UdpRequest request, InetSocketAddress toAddress){
		this.request = request;
		this.toAddress = toAddress;
	}
	
	public void awaitResponse(long timeout) throws UdpTimeoutException, Exception{
		if(response == null && exception == null){
			try{
				if(latch.await(timeout, TimeUnit.MILLISECONDS) == false){
					throw new UdpTimeoutException(this);
				}
			}catch(InterruptedException ex){
				if(exception == null){
					exception = ex;
				}
			}
		}
		
		if(exception != null){
			throw exception;
		}
	}

	/**
	 * @return the response
	 */
	public UdpResponse getResponse() {
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UdpTransaction [" + (toAddress != null ? "toAddress=" + toAddress + ", " : "")
				+ (request != null ? "request=" + request + ", " : "")
				+ (response != null ? "response=" + response + ", " : "")
				+ (exception != null ? "exception=" + exception : "") + "]";
	}

	

	
	
}
