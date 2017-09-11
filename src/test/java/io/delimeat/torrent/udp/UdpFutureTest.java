package io.delimeat.torrent.udp;

import java.net.InetSocketAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import io.delimeat.torrent.udp.domain.ConnectUdpRequest;
import io.delimeat.torrent.udp.domain.ConnectUdpResponse;
import io.delimeat.torrent.udp.domain.ErrorUdpResponse;
import io.delimeat.torrent.udp.domain.UdpRequest;
import io.delimeat.torrent.udp.domain.UdpResponse;
import io.delimeat.torrent.udp.domain.UdpTransaction;

public class UdpFutureTest {

	public class ConnectCallable implements Callable<ConnectUdpResponse>{

		@Override
		public ConnectUdpResponse call() throws Exception {
			return new ConnectUdpResponse(Integer.MIN_VALUE, Long.MAX_VALUE);
		}
		
	}
	
	@Test
	public void test() throws Exception {
		FutureTask<ConnectUdpResponse> task = new FutureTask<>(new ConnectCallable());

		ExecutorService executor = Executors.newFixedThreadPool(2);
		executor.execute(task);
		
		ConnectUdpResponse response = task.get(2000,TimeUnit.MILLISECONDS);
		
		Assert.assertEquals(Long.MAX_VALUE, response.getConnectionId());
	}
	
	@Test
	public void test2(){
		ConnectUdpRequest request = new ConnectUdpRequest(Integer.MAX_VALUE);
		
		UdpTransaction transaction = new UdpTransaction(request, new InetSocketAddress("localhost",80));
		
		ErrorUdpResponse response = new ErrorUdpResponse(Integer.MAX_VALUE, "ERROR RESPONSE");
		
		transaction.setResponse(response);
	}
}
