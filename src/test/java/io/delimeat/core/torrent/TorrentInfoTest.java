package io.delimeat.core.torrent;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
     	Assert.assertEquals("TorrentInfo{infoHash=null, name=null, length=0, files=[]}",torrentInfo.toString());
   }
}
