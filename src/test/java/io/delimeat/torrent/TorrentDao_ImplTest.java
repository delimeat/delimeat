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
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

import java.io.IOException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import io.delimeat.torrent.bencode.BDictionary;
import io.delimeat.torrent.bencode.BList;
import io.delimeat.torrent.bencode.BString;
import io.delimeat.torrent.bencode.BencodeException;
import io.delimeat.torrent.domain.Torrent;
import io.delimeat.torrent.domain.TorrentFile;
import io.delimeat.torrent.domain.TorrentInfo;
import io.delimeat.torrent.exception.TorrentException;
import io.delimeat.torrent.exception.TorrentNotFoundException;
import io.delimeat.torrent.exception.TorrentResponseBodyException;
import io.delimeat.torrent.exception.TorrentResponseException;
import io.delimeat.torrent.exception.TorrentTimeoutException;

public class TorrentDao_ImplTest {

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8089);
	
	private String sep = System.getProperty("file.separator");
	private TorrentDao_Impl dao;

	@Before
	public void setUp() {
		dao = new TorrentDao_Impl();
	}

	@Test
	public void parseTorrentFileTest() {
		BDictionary fileDict = new BDictionary();
		fileDict.put("length", 1234567890L);
		BList pathList = new BList();
		pathList.add("part1");
		pathList.add("part2");
		pathList.add("part3");
		pathList.add("fileName");
		fileDict.put("path", pathList);

		TorrentFile file = dao.parseTorrentFile(fileDict);
		Assert.assertEquals(1234567890L, file.getLength());
		Assert.assertEquals("part1" + sep + "part2" + sep + "part3" + sep
				+ "fileName", file.getName());
	}

	@Test
	public void parseInfoDictionaryTest() throws IOException, BencodeException,
			NoSuchAlgorithmException {
		BDictionary infoDict = new BDictionary();
		infoDict.put("name", "NAME");
		infoDict.put("length", 987654321L);
		BList filesList = new BList();
		BDictionary fileDict1 = new BDictionary();
		BList pathList1 = new BList();
		pathList1.add("1_part_1");
		pathList1.add("1_file_name");
		fileDict1.put("length", 1234);
		fileDict1.put("path", pathList1);
		filesList.add(fileDict1);
		BDictionary fileDict2 = new BDictionary();
		BList pathList2 = new BList();
		pathList2.add("2_part_1");
		pathList2.add("2_file_name");
		fileDict2.put("length", 56789);
		fileDict2.put("path", pathList2);
		filesList.add(fileDict2);
		infoDict.put("files", filesList);
		TorrentInfo info = dao.parseInfoDictionary(infoDict);
		Assert.assertEquals(987654321L, info.getLength());
		Assert.assertEquals("NAME", info.getName());
		Assert.assertEquals(2, info.getFiles().size());
		Assert.assertEquals("1_part_1" + sep + "1_file_name", info.getFiles()
				.get(0).getName());
		Assert.assertEquals(1234, info.getFiles().get(0).getLength());
		Assert.assertEquals("2_part_1" + sep + "2_file_name", info.getFiles()
				.get(1).getName());
		Assert.assertEquals(56789, info.getFiles().get(1).getLength());
		Assert.assertEquals("ab835ef1b726e2aa4d1c6df6b91278d651b228a7", info.getInfoHash().getHex());
	}

	@Test
	public void parseAnnounceListTest() {
		BList announceList = new BList();
		BList tier1 = new BList();
		tier1.add(new BString("1_tracker_1"));
		tier1.add(new BString("1_tracker_2"));
		announceList.add(tier1);
		BList tier2 = new BList();
		tier2.add(new BString("2_tracker_1"));
		tier2.add(new BString("2_tracker_2"));
		announceList.add(tier2);

		List<String> trackers = dao.parseAnnounceList(announceList);
		Assert.assertEquals(4, trackers.size());
		Assert.assertEquals("1_tracker_1", trackers.get(0));
		Assert.assertEquals("1_tracker_2", trackers.get(1));
		Assert.assertEquals("2_tracker_1", trackers.get(2));
		Assert.assertEquals("2_tracker_2", trackers.get(3));
	}

	@Test
	public void parseRootDictionaryTest() throws IOException, BencodeException,
			NoSuchAlgorithmException {
		BDictionary root = new BDictionary();
		root.put("announce", "TRACKER_1");

		BList announceList = new BList();
		BList tier1 = new BList();
		tier1.add(new BString("1_tracker_1"));
		tier1.add(new BString("1_tracker_2"));
		announceList.add(tier1);
		BList tier2 = new BList();
		tier2.add(new BString("2_tracker_1"));
		tier2.add(new BString("2_tracker_2"));
		announceList.add(tier2);
		root.put("announce-list", announceList);

		BDictionary infoDict = new BDictionary();
		infoDict.put("name", "NAME");
		infoDict.put("length", 987654321L);
		BList filesList = new BList();
		BDictionary fileDict1 = new BDictionary();
		BList pathList1 = new BList();
		pathList1.add("1_part_1");
		pathList1.add("1_file_name");
		fileDict1.put("length", 1234);
		fileDict1.put("path", pathList1);
		filesList.add(fileDict1);
		BDictionary fileDict2 = new BDictionary();
		BList pathList2 = new BList();
		pathList2.add("2_part_1");
		pathList2.add("2_file_name");
		fileDict2.put("length", 56789);
		fileDict2.put("path", pathList2);
		filesList.add(fileDict2);
		infoDict.put("files", filesList);
		root.put("info", infoDict);

		Torrent torrent = dao.parseRootDictionary(root);
		Assert.assertEquals("TRACKER_1", torrent.getTracker());
		Assert.assertEquals(4, torrent.getTrackers().size());
		Assert.assertEquals("1_tracker_1", torrent.getTrackers().get(0));
		Assert.assertEquals("1_tracker_2", torrent.getTrackers().get(1));
		Assert.assertEquals("2_tracker_1", torrent.getTrackers().get(2));
		Assert.assertEquals("2_tracker_2", torrent.getTrackers().get(3));
		Assert.assertEquals(987654321L, torrent.getInfo().getLength());
		Assert.assertEquals("NAME", torrent.getInfo().getName());
		Assert.assertEquals(2, torrent.getInfo().getFiles().size());
		Assert.assertEquals("1_part_1" + sep + "1_file_name", torrent.getInfo()
				.getFiles().get(0).getName());
		Assert.assertEquals(1234, torrent.getInfo().getFiles().get(0)
				.getLength());
		Assert.assertEquals("2_part_1" + sep + "2_file_name", torrent.getInfo()
				.getFiles().get(1).getName());
		Assert.assertEquals(56789, torrent.getInfo().getFiles().get(1)
				.getLength());
		Assert.assertEquals("ab835ef1b726e2aa4d1c6df6b91278d651b228a7", torrent.getInfo().getInfoHash().getHex());
		Assert.assertNull(torrent.getBytes());
	}

	@Test
	public void readTest() throws Exception {
		String bytesVal = "d8:announce9:TRACKER_113:announce-listll11:1_tracker_111:1_tracker_2el11:2_tracker_111:2_tracker_2ee4:infod5:filesld6:lengthi1234e4:pathl8:1_part_111:1_file_nameeed6:lengthi56789e4:pathl8:2_part_111:2_file_nameeee6:lengthi987654321e4:name4:NAMEee";
		
		stubFor(get(urlPathEqualTo("/"))
				.withHeader("Accept", equalTo("application/x-bittorrent"))
				.withHeader("Referer", equalTo("http://localhost:8089"))			
				.willReturn(aResponse()
							.withStatus(200)
							.withHeader("Content-Type", "application/x-bittorrent")
							.withBody(bytesVal)
							));
	
		Torrent torrent = dao.read(new URI("http://localhost:8089"));
		Assert.assertEquals("TRACKER_1", torrent.getTracker());
		Assert.assertEquals(4, torrent.getTrackers().size());
		Assert.assertEquals("1_tracker_1", torrent.getTrackers().get(0));
		Assert.assertEquals("1_tracker_2", torrent.getTrackers().get(1));
		Assert.assertEquals("2_tracker_1", torrent.getTrackers().get(2));
		Assert.assertEquals("2_tracker_2", torrent.getTrackers().get(3));
		Assert.assertEquals(987654321L, torrent.getInfo().getLength());
		Assert.assertEquals("NAME", torrent.getInfo().getName());
		Assert.assertEquals(2, torrent.getInfo().getFiles().size());
		Assert.assertEquals("1_part_1" + sep + "1_file_name", torrent.getInfo().getFiles().get(0).getName());
		Assert.assertEquals(1234, torrent.getInfo().getFiles().get(0).getLength());
		Assert.assertEquals("2_part_1" + sep + "2_file_name", torrent.getInfo().getFiles().get(1).getName());
		Assert.assertEquals(56789, torrent.getInfo().getFiles().get(1).getLength());
		Assert.assertEquals("ab835ef1b726e2aa4d1c6df6b91278d651b228a7", torrent.getInfo().getInfoHash().getHex());
		Assert.assertEquals(bytesVal, new String(torrent.getBytes()));
	}
	
	@Test(expected=TorrentTimeoutException.class)
	public void readTimeoutExceptionTest() throws Exception {	
		stubFor(get(urlPathEqualTo("/"))
				.withHeader("Accept", equalTo("application/x-bittorrent"))
				.withHeader("Referer", equalTo("http://localhost:8089"))			
				.willReturn(aResponse()
							.withStatus(200)
							.withHeader("Content-Type", "application/x-bittorrent")
							.withFixedDelay(2000)
							));
	
		dao.read(new URI("http://localhost:8089"));
		Assert.fail();
	}
	
  	@Test(expected=TorrentNotFoundException.class)
	public void readNotFoundExceptionTest() throws Exception {
		stubFor(get(urlPathEqualTo("/"))
				.withHeader("Accept", equalTo("application/x-bittorrent"))
				.withHeader("Referer", equalTo("http://localhost:8089"))			
				.willReturn(aResponse()
							.withStatus(404)
							.withHeader("Content-Type", "application/x-bittorrent")
							));
	
		dao.read(new URI("http://localhost:8089"));
		Assert.fail();
	}
  	
  	@Test(expected=TorrentResponseException.class)
	public void readResponseExceptionTest() throws Exception {
		stubFor(get(urlPathEqualTo("/"))
				.withHeader("Accept", equalTo("application/x-bittorrent"))
				.withHeader("Referer", equalTo("http://localhost:8089"))			
				.willReturn(aResponse()
							.withStatus(500)
							.withHeader("Content-Type", "application/x-bittorrent")
							));
	
		dao.read(new URI("http://localhost:8089"));
		Assert.fail();
	}
  
	@Test(expected=TorrentResponseBodyException.class)
	public void readResponseBodyTest() throws Exception {
		stubFor(get(urlPathEqualTo("/"))
				.withHeader("Accept", equalTo("application/x-bittorrent"))
				.withHeader("Referer", equalTo("http://localhost:8089"))			
				.willReturn(aResponse()
							.withStatus(200)
							.withHeader("Content-Type", "application/x-bittorrent")
							.withBody("X")
							));
	
		dao.read(new URI("http://localhost:8089"));
		Assert.fail();
	}
  
	//TODO re-enable after content type validation is enabled
	@Ignore
	@Test(expected=TorrentException.class)
	public void readInvalidContentTypeTest() throws Exception {
		String bytesVal = "d8:announce9:TRACKER_113:announce-listll11:1_tracker_111:1_tracker_2el11:2_tracker_111:2_tracker_2ee4:infod5:filesld6:lengthi1234e4:pathl8:1_part_111:1_file_nameeed6:lengthi56789e4:pathl8:2_part_111:2_file_nameeee6:lengthi987654321e4:name4:NAMEee";
		
		stubFor(get(urlPathEqualTo("/"))
				.withHeader("Accept", equalTo("application/x-bittorrent"))
				.withHeader("Referer", equalTo("http://localhost:8089"))			
				.willReturn(aResponse()
							.withStatus(200)
							.withHeader("Content-Type", "text/html")
							.withBody(bytesVal)
							));
	
		dao.read(new URI("http://localhost:8089"));
	}
  
  	@Test(expected=TorrentException.class)
  	public void readUnsupportedProtocalTest() throws Exception{
   	dao.read(new URI("udp://read.com:8080"));  	
   }

}
