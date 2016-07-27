package io.delimeat.core.feed;

import org.junit.Assert;
import org.junit.Test;

public class FeedSourceTest {

	@Test
	public void katTest() {
		Assert.assertEquals(0, FeedSource.KAT.getValue());
	}

	@Test
	public void torrentProjectTest() {
		Assert.assertEquals(1, FeedSource.TORRENTPROJECT.getValue());
	}

	@Test
	public void limeTorrentsTest() {
		Assert.assertEquals(2, FeedSource.LIMETORRENTS.getValue());
	}

	@Test
	public void extraTorrentTest() {
		Assert.assertEquals(3, FeedSource.EXTRATORRENT.getValue());
	}

	@Test
	public void bitSnoopTest() {
		Assert.assertEquals(4, FeedSource.BITSNOOP.getValue());
	}

	@Test
	public void torrentDownloadsTest() {
		Assert.assertEquals(5, FeedSource.TORRENTDOWNLOADS.getValue());
	}
}
