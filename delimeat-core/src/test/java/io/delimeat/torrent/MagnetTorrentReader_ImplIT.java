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

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import java.net.URI;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.WireMockServer;

import io.delimeat.torrent.entity.Torrent;
import io.delimeat.torrent.exception.TorrentException;
import io.delimeat.torrent.exception.TorrentNotFoundException;

public class MagnetTorrentReader_ImplIT {

	private static WireMockServer server = new WireMockServer(8089);

	private String sep = System.getProperty("file.separator");
	private MagnetTorrentReader_Impl reader;

	@BeforeAll
	public static void setUpClass() {
		server.start();
	}
	
	@AfterAll
	public static void tearDownClass() {
		server.stop();
	}
	
	@BeforeEach
	public void setUp() {
		reader = new MagnetTorrentReader_Impl();
		
		reader.setDownloadUriTemplate("http://localhost:8089/%s");
	}


	@Test
	public void readTest() throws Exception {
		String bytesVal = "d8:announce9:TRACKER_113:announce-listll11:1_tracker_111:1_tracker_2el11:2_tracker_111:2_tracker_2ee4:infod5:filesld6:lengthi1234e4:pathl8:1_part_111:1_file_nameeed6:lengthi56789e4:pathl8:2_part_111:2_file_nameeee6:lengthi987654321e4:name4:NAMEee";

		server.stubFor(get(urlEqualTo("/DF706CF16F45E8C0FD226223509C7E97B4FFEC13"))
				.withHeader("Accept", matching("application/x-bittorrent"))
				//.withHeader("Referer", matching("http://localhost:8089/DF706CF16F45E8C0FD226223509C7E97B4FFEC1"))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/x-bittorrent")
						.withBody(bytesVal)));
				
		Torrent torrent = reader.read(new URI("magnet:?xt=urn:btih:df706cf16f45e8c0fd226223509c7e97b4ffec13"));

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
	public void readNotFoundExceptionTest() throws Exception {
		server.stubFor(get(urlEqualTo("/DF706CF16F45E8C0FD226223509C7E97B4FFEC13"))
				.withHeader("Accept", matching("application/x-bittorrent"))
				//.withHeader("Referer", matching("http://localhost:8089/DF706CF16F45E8C0FD226223509C7E97B4FFEC1"))
				.willReturn(aResponse()
						.withStatus(404)
						.withHeader("Content-Type", "application/x-bittorrent")));

		TorrentNotFoundException ex = Assertions.assertThrows(TorrentNotFoundException.class, () -> {
			reader.read(new URI("magnet:?xt=urn:btih:df706cf16f45e8c0fd226223509c7e97b4ffec13"));
		});

		Assertions.assertEquals("HTTP response code 404 with message \"HTTP 404 Not Found\" for url http://localhost:8089/DF706CF16F45E8C0FD226223509C7E97B4FFEC13",
				ex.getMessage());
	}

	@Test
	public void readResponseExceptionTest() throws Exception {
		server.stubFor(get(urlEqualTo("/DF706CF16F45E8C0FD226223509C7E97B4FFEC13"))
				.withHeader("Accept", matching("application/x-bittorrent"))
				//.withHeader("Referer", matching("http://localhost:8089/DF706CF16F45E8C0FD226223509C7E97B4FFEC1"))
				.willReturn(aResponse()
						.withStatus(500)
						.withHeader("Content-Type", "application/x-bittorrent")));

		TorrentException ex = Assertions.assertThrows(TorrentException.class, () -> {
			reader.read(new URI("magnet:?xt=urn:btih:df706cf16f45e8c0fd226223509c7e97b4ffec13"));
		});

		Assertions.assertEquals("javax.ws.rs.InternalServerErrorException: HTTP 500 Server Error", ex.getMessage());
	}
}
