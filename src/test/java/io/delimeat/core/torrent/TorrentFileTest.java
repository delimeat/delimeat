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
		torrentFile.setLength(Long.MAX_VALUE);
		torrentFile.setName("NAME");
		Assert.assertEquals("TorrentFile{name=NAME, length=9223372036854775807}", torrentFile.toString());
	}
}
