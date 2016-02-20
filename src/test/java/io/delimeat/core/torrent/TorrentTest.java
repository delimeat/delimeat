package io.delimeat.core.torrent;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TorrentTest {

	private Torrent torrent;

	@Before
	public void setUp() {
		torrent = new Torrent();
	}

	@Test
	public void trackerTest() {
		Assert.assertNull(torrent.getTracker());
		torrent.setTracker("TRACKER");
		Assert.assertEquals("TRACKER", torrent.getTracker());
	}

	@Test
	public void trackersTest() {
		Assert.assertEquals(0, torrent.getTrackers().size());

		torrent.setTrackers(Arrays.asList("TRACKER_1","TRACKER_2"));
		
		Assert.assertEquals(2, torrent.getTrackers().size());
		Assert.assertEquals("TRACKER_1", torrent.getTrackers().get(0));
		Assert.assertEquals("TRACKER_2", torrent.getTrackers().get(1));
	}

	@Test
	public void infoTest() {
		Assert.assertNull(torrent.getInfo());
		
		TorrentInfo info = new TorrentInfo();
		torrent.setInfo(info);
		
		Assert.assertEquals(info, torrent.getInfo());
	}

	@Test
	public void bytesTest() {
		Assert.assertNull(torrent.getBytes());
		torrent.setBytes("BYTES".getBytes());
		Assert.assertEquals("BYTES", new String(torrent.getBytes()));
	}
  
  	@Test
  	public void toStringTest(){
		torrent.setTracker("TRACKER");
		torrent.setTrackers(Arrays.asList("TRACKER_1","TRACKER_2"));
		TorrentInfo info = new TorrentInfo();
		torrent.setInfo(info);
		torrent.setBytes("BYTES".getBytes());
     	System.out.println(torrent);
     	Assert.assertEquals("Torrent{tracker=TRACKER, trackers=[TRACKER_1, TRACKER_2], info=TorrentInfo{infoHash=null, name=null, length=0, files=[]}}", torrent.toString());
   }
}
