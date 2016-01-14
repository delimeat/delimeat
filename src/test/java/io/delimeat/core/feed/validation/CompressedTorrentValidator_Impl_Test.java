package io.delimeat.core.feed.validation;

import io.delimeat.core.feed.FeedResult;
import io.delimeat.core.feed.TorrentRejection;
import io.delimeat.core.torrent.Torrent;
import io.delimeat.core.torrent.TorrentFile;
import io.delimeat.core.torrent.TorrentInfo;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class CompressedTorrentValidator_Impl_Test {
	
	private static final String REGEX = "(zip|rar|tar)$";
	
	private TorrentCompressedValidator_Impl validator;
	private FeedResult result;
	
	@Before
	public void setUp(){
		validator = new TorrentCompressedValidator_Impl();
		result = new FeedResult();
	}
	
	@Test
	public void regexTest(){
		Assert.assertNull(validator.getRegex());
		validator.setRegex(REGEX);
		Assert.assertEquals(REGEX, validator.getRegex());
	}
	
	@Test
	public void singleFileValidTest() throws Exception{		
		TorrentInfo mockedTorrentInfo= Mockito.mock(TorrentInfo.class);
		Mockito.when(mockedTorrentInfo.getName()).thenReturn("VALIDFILE_TYPE.WMV");
		Mockito.when(mockedTorrentInfo.getFiles()).thenReturn(null);
		
		Torrent mockedTorrent= Mockito.mock(Torrent.class);
		Mockito.when(mockedTorrent.getInfo()).thenReturn(mockedTorrentInfo);
		
		result.setTorrent(mockedTorrent);

		validator.setRegex(REGEX);
		validator.validate(result, null);
		
		Assert.assertEquals(0, result.getTorrentRejections().size());	
	}
	@Test
	public void singleFileInvalidTest() throws Exception{		
		TorrentInfo mockedTorrentInfo=Mockito.mock(TorrentInfo.class);
		Mockito.when(mockedTorrentInfo.getName()).thenReturn("VALIDFILE_TYPE.ZIP");
		Mockito.when(mockedTorrentInfo.getFiles()).thenReturn(null);
		
		Torrent mockedTorrent= Mockito.mock(Torrent.class);
		Mockito.when(mockedTorrent.getInfo()).thenReturn(mockedTorrentInfo);
		
		result.setTorrent(mockedTorrent);
		validator.setRegex(REGEX);
		validator.validate(result, null);
		
		Assert.assertEquals(1, result.getTorrentRejections() .size());	
		Assert.assertEquals(TorrentRejection.CONTAINS_COMPRESSED, result.getTorrentRejections().get(0));
	}
	@Test
	public void multipleFileValidTest() throws Exception{

		TorrentFile mockedTorrentFile1=Mockito.mock(TorrentFile.class);
		Mockito.when(mockedTorrentFile1.getName()).thenReturn("VALID_FILE_TYPE.WMV");
		TorrentFile mockedTorrentFile2=Mockito.mock(TorrentFile.class);
		Mockito.when(mockedTorrentFile2.getName()).thenReturn("VALID_FILE_TYPE.FLV");		
		List<TorrentFile> torrentFiles = new ArrayList<TorrentFile>();
		torrentFiles.add(mockedTorrentFile1);
		torrentFiles.add(mockedTorrentFile2);
		
		TorrentInfo mockedTorrentInfo=Mockito.mock(TorrentInfo.class);
		Mockito.when(mockedTorrentInfo.getFiles()).thenReturn(torrentFiles);
		
		Torrent mockedTorrent=Mockito.mock(Torrent.class);
		Mockito.when(mockedTorrent.getInfo()).thenReturn(mockedTorrentInfo);
		
		result.setTorrent(mockedTorrent);
		validator.setRegex(REGEX);
		validator.validate(result, null);
		
		Assert.assertEquals(0, result.getTorrentRejections().size());		
	}
	@Test
	public void multipleFileInvalidTest() throws Exception{

		TorrentFile mockedTorrentFile1=Mockito.mock(TorrentFile.class);
		Mockito.when(mockedTorrentFile1.getName()).thenReturn("INVALID_FILE_TYPE.rar");
		TorrentFile mockedTorrentFile2=Mockito.mock(TorrentFile.class);
		Mockito.when(mockedTorrentFile2.getName()).thenReturn("VALID_FILE_TYPE.AVI");		
		List<TorrentFile> torrentFiles = new ArrayList<TorrentFile>();
		torrentFiles.add(mockedTorrentFile1);
		torrentFiles.add(mockedTorrentFile2);
		
		TorrentInfo mockedTorrentInfo=Mockito.mock(TorrentInfo.class);
		Mockito.when(mockedTorrentInfo.getFiles()).thenReturn(torrentFiles);
		
		Torrent mockedTorrent=Mockito.mock(Torrent.class);
		Mockito.when(mockedTorrent.getInfo()).thenReturn(mockedTorrentInfo);

		result.setTorrent(mockedTorrent);
		validator.setRegex(REGEX);
		validator.validate(result, null);
		
		Assert.assertEquals(1, result.getTorrentRejections().size());	
		Assert.assertEquals(TorrentRejection.CONTAINS_COMPRESSED, result.getTorrentRejections().get(0));
	}
}
