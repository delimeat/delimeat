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

public class TorrentCompressedValidator_ImplTest {
	
	private TorrentCompressedValidator_Impl validator;
   private Show show;
   private Config config;
   private Torrent torrent;
	
	@Before
	public void setUp(){
		validator = new TorrentCompressedValidator_Impl();
      show = new Show();
      config = new Config();
      torrent = new Torrent();
	}
	
   @Test
   public void rejectionTest(){
     Assert.assertEquals(FeedResultRejection.CONTAINS_COMPRESSED, validator.getRejection());
   }
  
	@Test
	public void singleFileValidTest() throws Exception{		
		TorrentInfo info = new TorrentInfo();
      info.setName("VALIDFILE_TYPE.WMV");
      info.setFiles(null);
		
      torrent.setInfo(info);		
		Assert.assertTrue(validator.validate(torrent, show, config));		
	}
	@Test
	public void singleFileInvalidTest() throws Exception{	
		TorrentInfo info = new TorrentInfo();
      info.setName("VALIDFILE_TYPE.ZIP");
		
      torrent.setInfo(info);	
		Assert.assertFalse(validator.validate(torrent, show, config));		

	}
	@Test
	public void multipleFileValidTest() throws Exception{
		TorrentInfo info = new TorrentInfo();
      TorrentFile file1 = new TorrentFile();
      file1.setName("VALIDFILE_TYPE.WMV");
      info.getFiles().add(file1);
      TorrentFile file2 = new TorrentFile();
      file2.setName("VALID_FILE_TYPE.FLV");
      info.getFiles().add(file2);     
		
      torrent.setInfo(info);		
		Assert.assertTrue(validator.validate(torrent, show, config));		
		
	}
	@Test
	public void multipleFileInvalidTest() throws Exception{
		TorrentInfo info = new TorrentInfo();
      TorrentFile file1 = new TorrentFile();
      file1.setName("VALIDFILE_TYPE.rar");
      info.getFiles().add(file1);
      TorrentFile file2 = new TorrentFile();
      file2.setName("VALID_FILE_TYPE.FLV");
      info.getFiles().add(file2);     
		
      torrent.setInfo(info);		
		Assert.assertFalse(validator.validate(torrent, show, config));		

	}
}