package io.delimeat.core.feed.validation;

import io.delimeat.core.config.Config;
import io.delimeat.core.feed.FeedResultRejection;
import io.delimeat.core.show.Show;
import io.delimeat.core.torrent.Torrent;
import io.delimeat.core.torrent.TorrentFile;
import io.delimeat.core.torrent.TorrentInfo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SizeTorrentValidator_Impl_Test {

	private TorrentSizeValidator_Impl validator;
   private Show show;
   private Config config;
   private Torrent torrent;
	
	@Before
	public void create(){
		validator = new TorrentSizeValidator_Impl();
      show = new Show();
      config = new Config();
      torrent = new Torrent();
	}
  
   @Test
   public void rejectionTest(){
     Assert.assertEquals(FeedResultRejection.FILE_SIZE_INCORRECT, validator.getRejection());
   }
  
	@Test
	public void singleFileBelowMinSizeTest() throws Exception{
		TorrentInfo info = new TorrentInfo();
      info.setLength((long)(90*1024*1024));
		
      torrent.setInfo(info);
     
      show.setMinSize(91);
		show.setMaxSize(Integer.MAX_VALUE);
     
		Assert.assertFalse(validator.validate(torrent, show, config));		
	}
  
	@Test
	public void singleFileAboveMaxSizeTest() throws Exception{
		TorrentInfo info = new TorrentInfo();
      info.setLength((long)(90*1024*1024));
		
      torrent.setInfo(info);
     
      show.setMinSize(Integer.MIN_VALUE);
		show.setMaxSize(89);
     
		Assert.assertFalse(validator.validate(torrent, show, config));		
	}
  
	@Test
	public void singleFileCorrectSizeTest() throws Exception{
		TorrentInfo info = new TorrentInfo();
      info.setLength((long)(90*1024*1024));
		
      torrent.setInfo(info);
     
      show.setMinSize(89);
		show.setMaxSize(91);
     
		Assert.assertTrue(validator.validate(torrent, show, config));		
	}
	
	@Test
	public void multipleFileBelowMinSize() throws Exception{
		TorrentInfo info = new TorrentInfo();
      TorrentFile file1 = new TorrentFile();
     	file1.setLength((long)(44*1024*1024));
      info.getFiles().add(file1);
      TorrentFile file2 = new TorrentFile();
     	file2.setLength((long)(44*1024*1024));
      info.getFiles().add(file2);
     
      torrent.setInfo(info);
     
      show.setMinSize(89);
		show.setMaxSize(91);
     
		Assert.assertFalse(validator.validate(torrent, show, config));		
	}
  
	@Test
	public void multipleFileAboveMaxSize() throws Exception{
		TorrentInfo info = new TorrentInfo();
      TorrentFile file1 = new TorrentFile();
     	file1.setLength((long)(50*1024*1024));
      info.getFiles().add(file1);
      TorrentFile file2 = new TorrentFile();
     	file2.setLength((long)(50*1024*1024));
      info.getFiles().add(file2);
     
      torrent.setInfo(info);
     
      show.setMinSize(89);
		show.setMaxSize(91);
     
		Assert.assertFalse(validator.validate(torrent, show, config));		
	}
  
	@Test
	public void multipleFileCorrectSize() throws Exception{
		TorrentInfo info = new TorrentInfo();
      TorrentFile file1 = new TorrentFile();
     	file1.setLength((long)(45*1024*1024));
      info.getFiles().add(file1);
      TorrentFile file2 = new TorrentFile();
     	file2.setLength((long)(45*1024*1024));
      info.getFiles().add(file2);
     
      torrent.setInfo(info);
     
      show.setMinSize(89);
		show.setMaxSize(91);
     
		Assert.assertTrue(validator.validate(torrent, show, config));		
	}
}
