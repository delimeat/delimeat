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
import java.net.URI;
import java.util.Arrays;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.delimeat.torrent.entity.Torrent;
import io.delimeat.torrent.exception.TorrentException;
import io.delimeat.torrent.exception.TorrentNotFoundException;
import io.delimeat.torrent.exception.TorrentResponseBodyException;
import io.delimeat.torrent.exception.TorrentResponseException;
import io.delimeat.torrent.exception.TorrentTimeoutException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okhttp3.mockwebserver.SocketPolicy;
import okio.Buffer;

public class HttpTorrentReader_ImplTest {

	private static final int PORT = 8089;
	private static MockWebServer mockedServer = new MockWebServer();

	private String sep = System.getProperty("file.separator");
	private HttpTorrentReader_Impl reader;

	@BeforeAll
	public static void beforeClass() throws IOException {
		mockedServer.start(PORT);
	}

	@AfterAll
	public static void tearDown() throws IOException {
		mockedServer.shutdown();
	}

	@BeforeEach
	public void setUp() {
		reader = new HttpTorrentReader_Impl();
	}

	@Test
	public void supportedProtocolTest() {
		Assertions.assertEquals(Arrays.asList("HTTP", "HTTPS"), reader.getSupportedProtocols());
	}

	@Test
	public void readTest() throws Exception {
		String bytesVal = "d8:announce9:TRACKER_113:announce-listll11:1_tracker_111:1_tracker_2el11:2_tracker_111:2_tracker_2ee4:infod5:filesld6:lengthi1234e4:pathl8:1_part_111:1_file_nameeed6:lengthi56789e4:pathl8:2_part_111:2_file_nameeee6:lengthi987654321e4:name4:NAMEee";

		Buffer buffer = new Buffer();
		buffer.write(bytesVal.getBytes());

		MockResponse mockResponse = new MockResponse().setResponseCode(200)
				.addHeader("Content-Type", "application/x-bittorrent").setBody(buffer);

		mockedServer.enqueue(mockResponse);

		Torrent torrent = reader.read(new URI("http://localhost:8089"));

		RecordedRequest request = mockedServer.takeRequest();
		Assertions.assertEquals("/", request.getPath());
		Assertions.assertEquals("application/x-bittorrent", request.getHeader("Accept"));
		Assertions.assertEquals("http://localhost:8089", request.getHeader("Referer"));

		Assertions.assertEquals("TRACKER_1", torrent.getTracker());
		Assertions.assertEquals(4, torrent.getTrackers().size());
		Assertions.assertEquals("1_tracker_1", torrent.getTrackers().get(0));
		Assertions.assertEquals("1_tracker_2", torrent.getTrackers().get(1));
		Assertions.assertEquals("2_tracker_1", torrent.getTrackers().get(2));
		Assertions.assertEquals("2_tracker_2", torrent.getTrackers().get(3));
		Assertions.assertEquals(987654321L, torrent.getInfo().getLength());
		Assertions.assertEquals("NAME", torrent.getInfo().getName());
		Assertions.assertEquals(2, torrent.getInfo().getFiles().size());
		Assertions.assertEquals("1_part_1" + sep + "1_file_name", torrent.getInfo().getFiles().get(0).getName());
		Assertions.assertEquals(1234, torrent.getInfo().getFiles().get(0).getLength());
		Assertions.assertEquals("2_part_1" + sep + "2_file_name", torrent.getInfo().getFiles().get(1).getName());
		Assertions.assertEquals(56789, torrent.getInfo().getFiles().get(1).getLength());
		Assertions.assertEquals("ab835ef1b726e2aa4d1c6df6b91278d651b228a7", torrent.getInfo().getInfoHash().getHex());
		Assertions.assertEquals(bytesVal, new String(torrent.getBytes()));
	}

	@Test
	public void readTimeoutExceptionTest() throws Exception {

		MockResponse mockResponse = new MockResponse().setSocketPolicy(SocketPolicy.NO_RESPONSE);

		mockedServer.enqueue(mockResponse);

		Exception ex = Assertions.assertThrows(TorrentTimeoutException.class, () -> {
			reader.read(new URI("http://localhost:8089"));
		});

		RecordedRequest request = mockedServer.takeRequest();
		Assertions.assertEquals("/", request.getPath());
		Assertions.assertEquals("application/x-bittorrent", request.getHeader("Accept"));
		Assertions.assertEquals("http://localhost:8089", request.getHeader("Referer"));

		Assertions.assertEquals("Timeout for http://localhost:8089", ex.getMessage());

	}

	@Test
	public void readNotFoundExceptionTest() throws Exception {
		MockResponse mockResponse = new MockResponse().setResponseCode(404).addHeader("Content-Type",
				"application/x-bittorrent");

		mockedServer.enqueue(mockResponse);

		Exception ex = Assertions.assertThrows(TorrentNotFoundException.class, () -> {
			reader.read(new URI("http://localhost:8089"));
		});

		RecordedRequest request = mockedServer.takeRequest();
		Assertions.assertEquals("/", request.getPath());
		Assertions.assertEquals("application/x-bittorrent", request.getHeader("Accept"));
		Assertions.assertEquals("http://localhost:8089", request.getHeader("Referer"));

		Assertions.assertEquals("HTTP response code 404 with message \"Client Error\" for url http://localhost:8089",
				ex.getMessage());
	}

	@Test
	public void readResponseExceptionTest() throws Exception {
		MockResponse mockResponse = new MockResponse().setResponseCode(500).addHeader("Content-Type",
				"application/x-bittorrent");

		mockedServer.enqueue(mockResponse);

		Exception ex = Assertions.assertThrows(TorrentResponseException.class, () -> {
			reader.read(new URI("http://localhost:8089"));
		});

		RecordedRequest request = mockedServer.takeRequest();
		Assertions.assertEquals("/", request.getPath());
		Assertions.assertEquals("application/x-bittorrent", request.getHeader("Accept"));
		Assertions.assertEquals("http://localhost:8089", request.getHeader("Referer"));

		Assertions.assertEquals("HTTP response code 500 with message \"Server Error\" for url http://localhost:8089",
				ex.getMessage());
	}

	@Test
	public void readResponseBodyExceptionTest() throws Exception {
		MockResponse mockResponse = new MockResponse().setResponseCode(200)
				.addHeader("Content-Type", "application/x-bittorrent").setBody("X");

		mockedServer.enqueue(mockResponse);

		Exception ex = Assertions.assertThrows(TorrentResponseBodyException.class, () -> {
			reader.read(new URI("http://localhost:8089"));
		});

		RecordedRequest request = mockedServer.takeRequest();
		Assertions.assertEquals("/", request.getPath());
		Assertions.assertEquals("application/x-bittorrent", request.getHeader("Accept"));
		Assertions.assertEquals("http://localhost:8089", request.getHeader("Referer"));

		Assertions.assertEquals("Unable to parse response for url http://localhost:8089 \nX", ex.getMessage());
	}

	@Test
	public void readUnsupportedProtocalTest() throws Exception {
		Exception ex = Assertions.assertThrows(TorrentException.class, () -> {
			reader.read(new URI("udp://read.com:8080"));
		});

		Assertions.assertEquals("Unsupported protocol udp", ex.getMessage());
	}

}
