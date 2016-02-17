package io.delimeat.core.torrent;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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

		TorrentFile mockedFile1 = Mockito.mock(TorrentFile.class);
		TorrentFile mockedFile2 = Mockito.mock(TorrentFile.class);
		torrentInfo.setFiles(Arrays.asList(mockedFile1,mockedFile2));
		
		Assert.assertEquals(2, torrentInfo.getFiles().size());
		Assert.assertEquals(mockedFile1, torrentInfo.getFiles().get(0));
		Assert.assertEquals(mockedFile2, torrentInfo.getFiles().get(1));
	}

	@Test
	public void infoHashTest() {
		Assert.assertNull(torrentInfo.getInfoHash());
     	InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		torrentInfo.setInfoHash(infoHash);
		Assert.assertEquals(infoHash, torrentInfo.getInfoHash());
	}
}
