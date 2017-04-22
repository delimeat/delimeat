package io.delimeat.torrent;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.torrent.domain.TorrentFile;

public class TorrentFileTest {

	private TorrentFile torrentFile;

	@Before
	public void setUp() {
		torrentFile = new TorrentFile();
	}

	@Test
	public void lengthTest() {
		Assert.assertEquals(0, torrentFile.getLength());
		torrentFile.setLength(Long.MAX_VALUE);
		Assert.assertEquals(Long.MAX_VALUE, torrentFile.getLength());
	}

	@Test
	public void nameTest() {
		Assert.assertNull(torrentFile.getName());
		torrentFile.setName("NAME");
		Assert.assertEquals("NAME", torrentFile.getName());
	}

	@Test
	public void toStringTest() {
		Assert.assertEquals("TorrentFile{length=0}", torrentFile.toString());
	}
}
