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
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.delimeat.torrent.bencode.BDictionary;
import io.delimeat.torrent.bencode.BList;
import io.delimeat.torrent.bencode.BString;
import io.delimeat.torrent.bencode.BencodeException;
import io.delimeat.torrent.entity.InfoHash;
import io.delimeat.torrent.entity.Torrent;
import io.delimeat.torrent.entity.TorrentFile;
import io.delimeat.torrent.entity.TorrentInfo;

public class TorrentUtilsTest {

	private String sep = System.getProperty("file.separator");

	@Test
	public void buildInfoHashFromMagnetTest() throws Exception {
		InfoHash infoHash = TorrentUtils.infoHashFromMagnet(new URI(
				"magnet:?xt=urn:btih:df706cf16f45e8c0fd226223509c7e97b4ffec13&tr=udp://tracker.coppersurfer.tk:6969/announce"));
		Assertions.assertEquals("df706cf16f45e8c0fd226223509c7e97b4ffec13", infoHash.getHex());
	}

	@Test
	public void buildInfoHashFromMagnetNoMatchTest() throws Exception {
		Assertions.assertNull(TorrentUtils.infoHashFromMagnet(new URI("magnet:?xt=urn:btih:")));
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

		TorrentFile file = TorrentUtils.parseTorrentFile(fileDict);
		Assertions.assertEquals(1234567890L, file.getLength());
		Assertions.assertEquals("part1" + sep + "part2" + sep + "part3" + sep + "fileName", file.getName());
	}

	@Test
	public void parseInfoDictionaryTest() throws IOException, BencodeException, NoSuchAlgorithmException {
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
		TorrentInfo info = TorrentUtils.parseInfoDictionary(infoDict);
		Assertions.assertEquals(987654321L, info.getLength());
		Assertions.assertEquals("NAME", info.getName());
		Assertions.assertEquals(2, info.getFiles().size());
		Assertions.assertEquals("1_part_1" + sep + "1_file_name", info.getFiles().get(0).getName());
		Assertions.assertEquals(1234, info.getFiles().get(0).getLength());
		Assertions.assertEquals("2_part_1" + sep + "2_file_name", info.getFiles().get(1).getName());
		Assertions.assertEquals(56789, info.getFiles().get(1).getLength());
		Assertions.assertEquals("ab835ef1b726e2aa4d1c6df6b91278d651b228a7", info.getInfoHash().getHex());
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

		List<String> trackers = TorrentUtils.parseAnnounceList(announceList);
		Assertions.assertEquals(4, trackers.size());
		Assertions.assertEquals("1_tracker_1", trackers.get(0));
		Assertions.assertEquals("1_tracker_2", trackers.get(1));
		Assertions.assertEquals("2_tracker_1", trackers.get(2));
		Assertions.assertEquals("2_tracker_2", trackers.get(3));
	}

	@Test
	public void parseRootDictionaryTest() throws IOException, BencodeException, NoSuchAlgorithmException {
		byte[] bytes = "d8:announce9:TRACKER_113:announce-listll11:1_tracker_111:1_tracker_2el11:2_tracker_111:2_tracker_2ee4:infod5:filesld6:lengthi1234e4:pathl8:1_part_111:1_file_nameeed6:lengthi56789e4:pathl8:2_part_111:2_file_nameeee6:lengthi987654321e4:name4:NAMEee"
				.getBytes();

		Torrent torrent = TorrentUtils.parseRootDictionary(bytes);
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
		Assertions.assertNotNull(torrent.getBytes());

	}
}
