package io.delimeat.core.torrent;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class UDPScrapeRequestHandler_ImpTest {

	private UDPScrapeRequestHandler_Impl scraper;

	@Before
	public void setUp() {
		scraper = new UDPScrapeRequestHandler_Impl();
	}

	@Test
	public void socketTest() {
		Assert.assertNull(scraper.getSocket());
		DatagramSocket mockedSocket = Mockito.mock(DatagramSocket.class);
		scraper.setSocket(mockedSocket);
		Assert.assertEquals(mockedSocket, scraper.getSocket());
	}

	@Test
	public void createConnectRequestTest() {
		byte[] req = scraper.createConnectRequest(100);
		Assert.assertEquals(16, req.length);
		ByteBuffer buf = ByteBuffer.wrap(req);
		Assert.assertEquals(0x41727101980L, buf.getLong());
		Assert.assertEquals(0, buf.getInt());
		Assert.assertEquals(100, buf.getInt());
	}

	@Test
	public void createScrapeRequestTest() {
      InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		byte[] req = scraper.createScrapeRequest(Long.MAX_VALUE,
				Integer.MIN_VALUE, infoHash);
		Assert.assertEquals(36, req.length);
		ByteBuffer buf = ByteBuffer.wrap(req);
		Assert.assertEquals(Long.MAX_VALUE, buf.getLong());
		Assert.assertEquals(2, buf.getInt());
		Assert.assertEquals(Integer.MIN_VALUE, buf.getInt());
		byte[] infoHashBytes = new byte[20];
		buf.get(infoHashBytes);
     	System.out.println(infoHashBytes.length);
     	System.out.println(infoHash.getBytes().length);  
		Assert.assertTrue(Arrays.equals(infoHash.getBytes(),infoHashBytes));
	}

	@Test(expected = TorrentException.class)
	public void handleConnectResponseWrongTransIdTest() throws Exception {
		ByteBuffer buf = ByteBuffer.allocate(8);
		buf.putInt(0).putInt(Integer.MIN_VALUE);
		scraper.handleConnectionResponse(buf.array(), Integer.MAX_VALUE);
	}

	@Test(expected = TorrentException.class)
	public void handleConnectResponseErrorTest() throws Exception {
		ByteBuffer buf = ByteBuffer.allocate(8);
		buf.putInt(3).putInt(Integer.MIN_VALUE);
		scraper.handleConnectionResponse(buf.array(), Integer.MIN_VALUE);
	}

	@Test(expected = TorrentException.class)
	public void handleConnectResponseWrongActionTest() throws Exception {
		ByteBuffer buf = ByteBuffer.allocate(8);
		buf.putInt(2).putInt(Integer.MIN_VALUE);
		scraper.handleConnectionResponse(buf.array(), Integer.MIN_VALUE);
	}

	@Test(expected = TorrentException.class)
	public void handleConnectResponseOnlyActionTest() throws Exception {
		ByteBuffer buf = ByteBuffer.allocate(4);
		buf.putInt(0);
		scraper.handleConnectionResponse(buf.array(), Integer.MIN_VALUE);
	}

	@Test(expected = TorrentException.class)
	public void handleConnectResponseOnlyActionAndTransIdTest()
			throws Exception {
		ByteBuffer buf = ByteBuffer.allocate(8);
		buf.putInt(0).putInt(Integer.MIN_VALUE);
		scraper.handleConnectionResponse(buf.array(), Integer.MIN_VALUE);
	}

	@Test
	public void handleConnectResponseValidTest() throws Exception {
		ByteBuffer buf = ByteBuffer.allocate(16);
		buf.putInt(0).putInt(Integer.MIN_VALUE).putLong(Long.MAX_VALUE);
		Assert.assertEquals(Long.MAX_VALUE, scraper.handleConnectionResponse(
				buf.array(), Integer.MIN_VALUE));
	}

	@Test(expected = TorrentException.class)
	public void handleScrapeResponseWrongTransIdTest() throws Exception {
		ByteBuffer buf = ByteBuffer.allocate(8);
		buf.putInt(2).putInt(Integer.MIN_VALUE);
		scraper.handleScrapeResponse(buf.array(), Integer.MAX_VALUE);
	}

	@Test(expected = TorrentException.class)
	public void handleScrapeResponseErrorTest() throws Exception {
		ByteBuffer buf = ByteBuffer.allocate(8);
		buf.putInt(3).putInt(Integer.MIN_VALUE);
		scraper.handleScrapeResponse(buf.array(), Integer.MIN_VALUE);
	}

	@Test(expected = TorrentException.class)
	public void handleScrapeResponseWrongActionTest() throws Exception {
		ByteBuffer buf = ByteBuffer.allocate(8);
		buf.putInt(0).putInt(Integer.MIN_VALUE);
		scraper.handleScrapeResponse(buf.array(), Integer.MIN_VALUE);
	}

	@Test(expected = TorrentException.class)
	public void handleScrapeResponseOnlyActionTest() throws Exception {
		ByteBuffer buf = ByteBuffer.allocate(4);
		buf.putInt(2);
		scraper.handleScrapeResponse(buf.array(), Integer.MIN_VALUE);
	}

	@Test(expected = TorrentException.class)
	public void handleScrapeResponseOnlyActionAndTransIdTest() throws Exception {
		ByteBuffer buf = ByteBuffer.allocate(8);
		buf.putInt(2).putInt(Integer.MIN_VALUE);
		scraper.handleScrapeResponse(buf.array(), Integer.MIN_VALUE);
	}

	@Test
	public void handleScrapeResponseValidTest() throws Exception {
		ByteBuffer buf = ByteBuffer.allocate(20);
		buf.putInt(2).putInt(Integer.MIN_VALUE).putInt(100).putInt(20)
				.putInt(200);
		ScrapeResult result = scraper.handleScrapeResponse(buf.array(),
				Integer.MIN_VALUE);
		Assert.assertEquals(100, result.getSeeders());
		Assert.assertEquals(200, result.getLeechers());
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void sendRequestValidTest() throws Exception {
		DatagramSocket mockedSocket = Mockito.mock(DatagramSocket.class);
		Mockito.doThrow(new SocketTimeoutException())
				.doThrow(new SocketTimeoutException())
				.doAnswer(new Answer() {
					public Object answer(InvocationOnMock invocation) {
						Object[] args = invocation.getArguments();
						if (args[0] instanceof DatagramPacket) {
							DatagramPacket recievePacket = (DatagramPacket) args[0];
							recievePacket.setData("RECIVEDPACKET".getBytes());
						}
						return null;
					}
				}).when(mockedSocket)
				.receive(Mockito.any(DatagramPacket.class));

		Answer sendAnswer = new Answer() {
			public Object answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				if (args[0] instanceof DatagramPacket) {
					DatagramPacket sendPacket = (DatagramPacket) args[0];
					Assert.assertEquals("SENDDATA",
							new String(sendPacket.getData()));
				}
				return null;
			}
		};
		Mockito.doAnswer(sendAnswer).when(mockedSocket)
				.send(Mockito.any(DatagramPacket.class));

		Mockito.doAnswer(new Answer() {
			int call = 1;

			public Object answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				int newval = (Integer) args[0];
				Assert.assertEquals(call * 1000, newval);
				call++;
				return null;
			}
		}).when(mockedSocket).setSoTimeout(Mockito.any(int.class));

		scraper.setSocket(mockedSocket);
		byte[] result = scraper.sendRequest("SENDDATA".getBytes(),
				new InetSocketAddress("localhost", 80));
		Assert.assertEquals("RECIVEDPACKET", new String(result));
	}

	@SuppressWarnings("rawtypes")
	@Test(expected = SocketTimeoutException.class)
	public void sendRequestTimeoutTest() throws Exception {
		DatagramSocket mockedSocket = Mockito.mock(DatagramSocket.class);

		Mockito.doThrow(new SocketTimeoutException())
				.doThrow(new SocketTimeoutException())
				.doThrow(new SocketTimeoutException()).when(mockedSocket)
				.receive(Mockito.any(DatagramPacket.class));

		Mockito.doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				if (args[0] instanceof DatagramPacket) {
					DatagramPacket sendPacket = (DatagramPacket) args[0];
					Assert.assertEquals("SENDDATA",
							new String(sendPacket.getData()));
				}
				return null;
			}
		}).when(mockedSocket).send(Mockito.any(DatagramPacket.class));

		Mockito.doAnswer(new Answer() {
			int call = 1;

			public Object answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				int newval = (Integer) args[0];
				Assert.assertEquals(call * 1000, newval);
				call++;
				return null;
			}
		}).when(mockedSocket).setSoTimeout(Mockito.any(int.class));

		scraper.setSocket(mockedSocket);
		scraper.sendRequest("SENDDATA".getBytes(), new InetSocketAddress(
				"localhost", 8080));
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void scrapeTest() throws Exception {
		DatagramSocket mockedSocket = Mockito.mock(DatagramSocket.class);

		final ByteBuffer connSendBuf = ByteBuffer.allocate(16);
		final ByteBuffer scapeSendBuf = ByteBuffer.allocate(36);
		Mockito.doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				if (args[0] instanceof DatagramPacket) {
					DatagramPacket sendPacket = (DatagramPacket) args[0];
					connSendBuf.put(sendPacket.getData());
				}
				return null;
			}
		}).doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				if (args[0] instanceof DatagramPacket) {
					DatagramPacket sendPacket = (DatagramPacket) args[0];
					scapeSendBuf.put(sendPacket.getData());
				}
				return null;
			}
		}).when(mockedSocket).send(Mockito.any(DatagramPacket.class));

		Mockito.doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				if (args[0] instanceof DatagramPacket) {
					DatagramPacket sendPacket = (DatagramPacket) args[0];
					ByteBuffer connRespBuf = ByteBuffer.allocate(16);
					connRespBuf.putInt(0).putInt(connSendBuf.getInt(12))
							.putLong(Long.MAX_VALUE);
					sendPacket.setData(connRespBuf.array());
				}
				return null;
			}
		}).doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				if (args[0] instanceof DatagramPacket) {
					DatagramPacket sendPacket = (DatagramPacket) args[0];
					ByteBuffer connRespBuf = ByteBuffer.allocate(20);
					connRespBuf.putInt(2).putInt(scapeSendBuf.getInt(12))
							.putInt(200).putInt(0).putInt(100);
					sendPacket.setData(connRespBuf.array());
				}
				return null;
			}
		}).when(mockedSocket).receive(Mockito.any(DatagramPacket.class));

		scraper.setSocket(mockedSocket);
      InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());

		ScrapeResult result = scraper.scrape(new URI("udp://test.com:8080"),infoHash);
		Assert.assertEquals(200, result.getSeeders());
		Assert.assertEquals(100, result.getLeechers());
	}

}
