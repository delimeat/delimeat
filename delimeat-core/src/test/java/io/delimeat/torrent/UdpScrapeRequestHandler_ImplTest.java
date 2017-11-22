package io.delimeat.torrent;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.net.URI;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.delimeat.torrent.entity.InfoHash;
import io.delimeat.torrent.entity.ScrapeResult;
import io.delimeat.torrent.entity.UdpConnectResponse;
import io.delimeat.torrent.entity.UdpConnectionId;
import io.delimeat.torrent.entity.UdpErrorResponse;
import io.delimeat.torrent.entity.UdpScrapeResponse;
import io.delimeat.util.DelimeatUtils;

public class UdpScrapeRequestHandler_ImplTest {

	private UdpScrapeRequestHandler_Impl handler;

	@BeforeEach
	public void setUp() {
		handler = new UdpScrapeRequestHandler_Impl();
	}

	@Test
	public void supportedProtocolTest() {
		Assertions.assertEquals(Arrays.asList("UDP"), handler.getSupportedProtocols());
	}

	@Test
	public void addressTest() {
		Assertions.assertNull(handler.getAddress());

		InetSocketAddress address = new InetSocketAddress("localhost", 4040);
		handler.setAddress(address);

		Assertions.assertEquals(address, handler.getAddress());
	}

	@Test
	public void initializeAndShutdownTest() throws Exception {
		InetSocketAddress address = new InetSocketAddress("localhost", 4040);
		handler.setAddress(address);

		handler.initialize();

		Assertions.assertTrue(handler.isActive());
		Assertions.assertTrue(handler.getSelector().isOpen());

		Assertions.assertFalse(handler.getExecutor().isShutdown());
		Assertions.assertFalse(handler.getExecutor().isTerminated());

		Assertions.assertTrue(handler.getChannel().isOpen());
		Assertions.assertFalse(handler.getChannel().isBlocking());
		// this is causing an error when building on travis-ci disabling for now
		// Assertions.assertEquals("2048",
		// handler.getChannel().getOption(StandardSocketOptions.SO_SNDBUF).toString());
		// Assertions.assertEquals("2048",
		// handler.getChannel().getOption(StandardSocketOptions.SO_RCVBUF).toString());
		Assertions.assertTrue(handler.getChannel().getOption(StandardSocketOptions.SO_REUSEADDR).booleanValue());

		handler.shutdown();
		Assertions.assertFalse(handler.isActive());
		Assertions.assertFalse(handler.getSelector().isOpen());
		Assertions.assertFalse(handler.getChannel().isOpen());
		Assertions.assertTrue(handler.getExecutor().isShutdown());
		Assertions.assertTrue(handler.getExecutor().isTerminated());
	}

	@Test
	public void shutdownDueToInactivityTest() throws Exception {
		InetSocketAddress address = new InetSocketAddress("localhost", 4040);
		handler.setAddress(address);

		handler.initialize();
		Assertions.assertTrue(handler.isActive());

		handler.shutdownDueToInactivity();
		Assertions.assertFalse(handler.isActive());
		Assertions.assertFalse(handler.getSelector().isOpen());
		Assertions.assertFalse(handler.getChannel().isOpen());
		Assertions.assertTrue(handler.getExecutor().isShutdown());
		Assertions.assertTrue(handler.getExecutor().isTerminated());
	}

	@Test
	public void purgeInvalidConnectionIdsTest() {
		UdpConnectionId connIdRemove = new UdpConnectionId(Long.MAX_VALUE, new InetSocketAddress("localhost", 9004),
				Instant.EPOCH);
		UdpConnectionId connIdKeep = new UdpConnectionId(Long.MIN_VALUE, new InetSocketAddress("0.0.0.0", 9004),
				Instant.MAX);

		handler.getConnections().put(connIdRemove.getFromAddress(), connIdRemove);
		handler.getConnections().put(connIdKeep.getFromAddress(), connIdKeep);

		handler.purgeInvalidConnectionIds();

		Assertions.assertEquals(1, handler.getConnections().size());
		Assertions.assertEquals(connIdKeep, handler.getConnections().get(connIdKeep.getFromAddress()));
	}

	@Test
	public void buildErrorResponseTest() throws Exception {
		ByteBuffer buf = ByteBuffer.allocate(1024).putInt(Integer.MAX_VALUE).put("MESSAGE".getBytes());
		buf.clear();

		UdpErrorResponse result = handler.unmarshallErrorResponse(buf);

		Assertions.assertEquals(Integer.MAX_VALUE, result.getTransactionId());
		Assertions.assertEquals("MESSAGE", result.getMessage());
	}

	@Test
	public void buildErrorResponseExceptionTest() throws Exception {
		ByteBuffer buf = ByteBuffer.allocate(0);
		Exception ex = Assertions.assertThrows(BufferUnderflowException.class, () -> {
			handler.unmarshallErrorResponse(buf);
		});

		Assertions.assertNull(ex.getMessage());
	}

	@Test
	public void buildConnectResponseTest() throws Exception {
		ByteBuffer buf = ByteBuffer.allocate(12).putInt(Integer.MAX_VALUE).putLong(0x41727101980L);
		buf.clear();

		UdpConnectResponse result = handler.unmarshallConnectResponse(buf);

		Assertions.assertEquals(Integer.MAX_VALUE, result.getTransactionId());
		Assertions.assertEquals(0x41727101980L, result.getConnectionId());
	}

	@Test
	public void buildConnectResponseExceptionTest() throws Exception {
		ByteBuffer buf = ByteBuffer.wrap("MESSAGE".getBytes());
		Exception ex = Assertions.assertThrows(BufferUnderflowException.class, () -> {
			handler.unmarshallConnectResponse(buf);
		});

		Assertions.assertNull(ex.getMessage());
	}

	@Test
	public void buildScrapeResponseTest() throws Exception {
		ByteBuffer buf = ByteBuffer.allocate(12).putInt(Integer.MAX_VALUE).putInt(Integer.MAX_VALUE)
				.putInt(Integer.MIN_VALUE);

		ByteBuffer buff2 = ByteBuffer.wrap(buf.array());
		UdpScrapeResponse result = handler.unmarshallScrapeResponse(buff2);

		Assertions.assertEquals(Integer.MAX_VALUE, result.getTransactionId());
		Assertions.assertEquals(Integer.MAX_VALUE, result.getSeeders());
		Assertions.assertEquals(Integer.MIN_VALUE, result.getLeechers());
	}

	@Test
	public void buildScrapeResponseExceptionTest() throws Exception {
		ByteBuffer buf = ByteBuffer.wrap("MESSAGE".getBytes());
		Exception ex = Assertions.assertThrows(BufferUnderflowException.class, () -> {
			handler.unmarshallConnectResponse(buf);
		});

		Assertions.assertNull(ex.getMessage());
	}

	// TODO remove once test coverage increased
	@Disabled
	@Test
	public void test() throws Exception {
		InetSocketAddress address = new InetSocketAddress("0.0.0.0", 9004);

		UdpScrapeRequestHandler_Impl server = new UdpScrapeRequestHandler_Impl();
		server.setAddress(address);

		InfoHash infoHash = new InfoHash(DelimeatUtils.hexToBytes("d73ae09044abf44faf3ade9e43cbc902b319c15b"));

		List<URI> uris = Arrays.asList(new URI("udp://tracker.coppersurfer.tk:6969"),
				new URI("udp://tracker.leechers-paradise.org:6969"), new URI("udp://eddie4.nl:6969"),
				new URI("udp://tracker.pirateparty.gr:6969"), new URI("udp://tracker.zer0day.to:1337"),
				new URI("udp://tracker.coppersurfer.tk:6969"));
		ScrapeResult result = null;
		for (URI uri : uris) {
			try {
				result = server.scrape(uri, infoHash);
				System.out.println(result);
			} catch (Exception ex) {
				System.out.println("Exception");
			}
		}

		Thread.sleep(10000);
		server.shutdown();
		System.out.println("FINISHED");
	}

	// TODO remove once test coverage increased
	@Disabled
	@Test
	public void test2() throws Exception {
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

		scheduler.execute(this::doThread);

		scheduler.scheduleWithFixedDelay(this::doSchedule, 200, 3000, TimeUnit.MILLISECONDS);

		Thread.sleep(11000);
	}

	// TODO remove once test coverage increased
	public void doThread() {
		int count = 0;
		while (count < 10) {
			System.out.println("Sleeping");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			count++;
			System.out.println("Woke up");
		}
	}

	// TODO remove once test coverage increased
	public void doSchedule() {
		System.out.println("Scheduled run");
	}
}
