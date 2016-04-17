package io.delimeat.core.torrent;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
		System.out.println(torrentFile);
		Assert.assertEquals("TorrentFile{name=null, length=0}", torrentFile.toString());
	}
}
