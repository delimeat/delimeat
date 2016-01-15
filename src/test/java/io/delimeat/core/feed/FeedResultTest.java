package io.delimeat.core.feed;

import io.delimeat.core.torrent.Torrent;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class FeedResultTest {

	private FeedResult result;
	
	@Before
	public void setUp(){
		result = new FeedResult();
	}
	
	@Test
	public void torrentUrlTest(){
		Assert.assertEquals(null, result.getTorrentURL());
		result.setTorrentURL("TORRENT_LOCATION");
		Assert.assertEquals("TORRENT_LOCATION", result.getTorrentURL());
	}
	
	@Test
	public void titleTest(){
		Assert.assertEquals(null, result.getTitle());
		result.setTitle("FILE_NAME");
		Assert.assertEquals("FILE_NAME", result.getTitle());
	}
	
	@Test
	public void contentLengthTest(){
		Assert.assertEquals(0, result.getContentLength());
		result.setContentLength(Long.MAX_VALUE);
		Assert.assertEquals(Long.MAX_VALUE, result.getContentLength());
	}
	
	@Test
	public void seedersTest(){
		Assert.assertEquals(0,result.getSeeders());
		result.setSeeders(Long.MIN_VALUE);
		Assert.assertEquals(Long.MIN_VALUE, result.getSeeders());
	}
	
	@Test
	public void leechersTest(){
		Assert.assertEquals(0,result.getLeechers());
		result.setLeechers(Long.MAX_VALUE);
		Assert.assertEquals(Long.MAX_VALUE, result.getLeechers());		
	}
	
	@Test
	public void feedResultRejectionsTest(){
		Assert.assertNotNull(result.getFeedResultRejections());
		Assert.assertEquals(0, result.getFeedResultRejections().size());
		
		result.setFeedResultRejections(Arrays.asList(FeedResultRejection.CONTAINS_COMPRESSED,FeedResultRejection.INCORRECT_TITLE));
		
		Assert.assertEquals(2, 0, result.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.CONTAINS_COMPRESSED, result.getFeedResultRejections().get(0));
		Assert.assertEquals(FeedResultRejection.INCORRECT_TITLE, result.getFeedResultRejections().get(1));
	}
	
	@Test
	public void torrentTest(){
		Assert.assertNull(result.getTorrent());
		
		Torrent mockedTorrent = Mockito.mock(Torrent.class);
		result.setTorrent(mockedTorrent);
		
		Assert.assertEquals(mockedTorrent, result.getTorrent());
	}
}
