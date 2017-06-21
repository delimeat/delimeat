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
package io.delimeat.torrent.domain;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.torrent.domain.InfoHash;
import io.delimeat.torrent.domain.TorrentFile;
import io.delimeat.torrent.domain.TorrentInfo;

public class TorrentInfoTest {

	private TorrentInfo torrentInfo;

	@Before
	public void setUp() {
		torrentInfo = new TorrentInfo();
	}

	@Test
	public void lengthTest() {
		Assert.assertEquals(0, torrentInfo.getLength());
		torrentInfo.setLength(Long.MAX_VALUE);
		Assert.assertEquals(Long.MAX_VALUE, torrentInfo.getLength());
	}

	@Test
	public void nameTest() {
		Assert.assertNull(torrentInfo.getName());
		torrentInfo.setName("NAME");
		Assert.assertEquals("NAME", torrentInfo.getName());
	}

	@Test
	public void filesTest() {
		Assert.assertEquals(0, torrentInfo.getFiles().size());

		TorrentFile file1 = new TorrentFile();
		TorrentFile file2 = new TorrentFile();
		torrentInfo.setFiles(Arrays.asList(file1,file2));
		
		Assert.assertEquals(2, torrentInfo.getFiles().size());
		Assert.assertEquals(file1, torrentInfo.getFiles().get(0));
		Assert.assertEquals(file2, torrentInfo.getFiles().get(1));
	}

	@Test
	public void infoHashTest() {
		Assert.assertNull(torrentInfo.getInfoHash());
     	InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		torrentInfo.setInfoHash(infoHash);
		Assert.assertEquals(infoHash, torrentInfo.getInfoHash());
	}
  
  	@Test
  	public void toStringTest(){
     	Assert.assertEquals("TorrentInfo(files=[], infoHash=null, length=0, name=null)",torrentInfo.toString());
   }
}
