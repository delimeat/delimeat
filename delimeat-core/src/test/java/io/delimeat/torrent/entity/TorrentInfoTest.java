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
package io.delimeat.torrent.entity;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TorrentInfoTest {

	private TorrentInfo torrentInfo;

	@BeforeEach
	public void setUp() {
		torrentInfo = new TorrentInfo();
	}

	@Test
	public void lengthTest() {
		Assertions.assertEquals(0, torrentInfo.getLength());
		torrentInfo.setLength(Long.MAX_VALUE);
		Assertions.assertEquals(Long.MAX_VALUE, torrentInfo.getLength());
	}

	@Test
	public void nameTest() {
		Assertions.assertNull(torrentInfo.getName());
		torrentInfo.setName("NAME");
		Assertions.assertEquals("NAME", torrentInfo.getName());
	}

	@Test
	public void filesTest() {
		Assertions.assertEquals(0, torrentInfo.getFiles().size());

		TorrentFile file1 = new TorrentFile();
		TorrentFile file2 = new TorrentFile();
		torrentInfo.setFiles(Arrays.asList(file1, file2));

		Assertions.assertEquals(2, torrentInfo.getFiles().size());
		Assertions.assertEquals(file1, torrentInfo.getFiles().get(0));
		Assertions.assertEquals(file2, torrentInfo.getFiles().get(1));
	}

	@Test
	public void infoHashTest() {
		Assertions.assertNull(torrentInfo.getInfoHash());
		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		torrentInfo.setInfoHash(infoHash);
		Assertions.assertEquals(infoHash, torrentInfo.getInfoHash());
	}

	@Test
	public void toStringTest() {
		Assertions.assertEquals("TorrentInfo [files=[], length=0, ]", torrentInfo.toString());
	}

	@Test
	public void equalsTest() {
		torrentInfo.setInfoHash(new InfoHash("INFO".getBytes()));
		torrentInfo.setLength(Long.MAX_VALUE);
		torrentInfo.setName("NAME");
		torrentInfo.setFiles(Arrays.asList(new TorrentFile()));
		TorrentInfo other = new TorrentInfo();
		other.setInfoHash(new InfoHash("INFO".getBytes()));
		other.setLength(Long.MAX_VALUE);
		other.setName("NAME");
		other.setFiles(Arrays.asList(new TorrentFile()));

		Assertions.assertTrue(torrentInfo.equals(other));
	}

	@Test
	public void equalsSelfTest() {
		Assertions.assertTrue(torrentInfo.equals(torrentInfo));
	}

	@Test
	public void equalsNullTest() {
		Assertions.assertFalse(torrentInfo.equals(null));
	}

	@Test
	public void equalsOtherClassTest() {
		Assertions.assertFalse(torrentInfo.equals(new Object()));
	}

	@Test
	public void equalsInfoHashNullTest() {
		torrentInfo.setInfoHash(null);
		torrentInfo.setLength(Long.MAX_VALUE);
		torrentInfo.setName("NAME");
		torrentInfo.setFiles(Arrays.asList(new TorrentFile()));
		TorrentInfo other = new TorrentInfo();
		other.setInfoHash(new InfoHash("INFO".getBytes()));
		other.setLength(Long.MAX_VALUE);
		other.setName("NAME");
		other.setFiles(Arrays.asList(new TorrentFile()));

		Assertions.assertFalse(torrentInfo.equals(other));
	}

	@Test
	public void equalsFilesNullTest() {
		torrentInfo.setInfoHash(new InfoHash("INFO".getBytes()));
		torrentInfo.setLength(Long.MAX_VALUE);
		torrentInfo.setName("NAME");
		torrentInfo.setFiles(null);
		TorrentInfo other = new TorrentInfo();
		other.setInfoHash(new InfoHash("INFO".getBytes()));
		other.setLength(Long.MAX_VALUE);
		other.setName("NAME");
		other.setFiles(Arrays.asList(new TorrentFile()));

		Assertions.assertFalse(torrentInfo.equals(other));
	}

	@Test
	public void equalsNameNullTest() {
		torrentInfo.setInfoHash(new InfoHash("INFO".getBytes()));
		torrentInfo.setLength(Long.MAX_VALUE);
		torrentInfo.setName(null);
		torrentInfo.setFiles(Arrays.asList(new TorrentFile()));
		TorrentInfo other = new TorrentInfo();
		other.setInfoHash(new InfoHash("INFO".getBytes()));
		other.setLength(Long.MAX_VALUE);
		other.setName("NAME");
		other.setFiles(Arrays.asList(new TorrentFile()));

		Assertions.assertFalse(torrentInfo.equals(other));
	}

	@Test
	public void equalsInfoHashTest() {
		torrentInfo.setInfoHash(new InfoHash("INFO".getBytes()));
		torrentInfo.setLength(Long.MAX_VALUE);
		torrentInfo.setName("NAME");
		torrentInfo.setFiles(Arrays.asList(new TorrentFile()));
		TorrentInfo other = new TorrentInfo();
		other.setInfoHash(new InfoHash("INFO_OTHER".getBytes()));
		other.setLength(Long.MAX_VALUE);
		other.setName("NAME");
		other.setFiles(Arrays.asList(new TorrentFile()));

		Assertions.assertFalse(torrentInfo.equals(other));
	}

	@Test
	public void equalsLengthTest() {
		torrentInfo.setInfoHash(new InfoHash("INFO".getBytes()));
		torrentInfo.setLength(Long.MAX_VALUE);
		torrentInfo.setName("NAME");
		torrentInfo.setFiles(Arrays.asList(new TorrentFile()));
		TorrentInfo other = new TorrentInfo();
		other.setInfoHash(new InfoHash("INFO".getBytes()));
		other.setLength(Long.MIN_VALUE);
		other.setName("NAME");
		other.setFiles(Arrays.asList(new TorrentFile()));

		Assertions.assertFalse(torrentInfo.equals(other));
	}

	@Test
	public void equalsNameTest() {
		torrentInfo.setInfoHash(new InfoHash("INFO".getBytes()));
		torrentInfo.setLength(Long.MAX_VALUE);
		torrentInfo.setName("NAME_OTHER");
		torrentInfo.setFiles(Arrays.asList(new TorrentFile()));
		TorrentInfo other = new TorrentInfo();
		other.setInfoHash(new InfoHash("INFO".getBytes()));
		other.setLength(Long.MAX_VALUE);
		other.setName("NAME");
		other.setFiles(Arrays.asList(new TorrentFile()));

		Assertions.assertFalse(torrentInfo.equals(other));
	}

	@Test
	public void equalsFilesTest() {
		torrentInfo.setInfoHash(new InfoHash("INFO".getBytes()));
		torrentInfo.setLength(Long.MAX_VALUE);
		torrentInfo.setName("NAME");
		torrentInfo.setFiles(Arrays.asList(new TorrentFile()));
		TorrentInfo other = new TorrentInfo();
		other.setInfoHash(new InfoHash("INFO".getBytes()));
		other.setLength(Long.MAX_VALUE);
		other.setName("NAME");
		other.setFiles(Arrays.asList(new TorrentFile(), new TorrentFile()));

		Assertions.assertFalse(torrentInfo.equals(other));
	}
}
