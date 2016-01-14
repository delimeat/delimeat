package io.delimeat.core.feed.validation;

import io.delimeat.core.config.Config;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class FileTypeTorrentValidator_Impl_Test {
	
	private TorrentFileTypeValidator_Impl validator;
	
	@Before
	public void setUp(){
		validator = new TorrentFileTypeValidator_Impl();
	}
	
	/*
	@Test
	public void singleFileValidTest() throws Exception{
		Config config = new Config();
		config.getIgnoredFileTypes().add("MP4");
		config.getIgnoredFileTypes().add("AVI");

		validator.configUpdated(mockedConfig);
		
		TorrentInfo mockedTorrentInfo=mock(TorrentInfo.class);
		when(mockedTorrentInfo.getName()).thenReturn("VALIDFILE_TYPE.WMV");
		when(mockedTorrentInfo.getFiles()).thenReturn(null);
		
		Torrent mockedTorrent=mock(Torrent.class);
		when(mockedTorrent.getInfo()).thenReturn(mockedTorrentInfo);
		
		FeedResult result = createFeedResult();
		when(result.getTorrent()).thenReturn(mockedTorrent);
		
		validator.validate(result, null);
		
		Assert.assertEquals(0, result.getTorrentRejections().size());	
	}
	
	@Test
	public void singleFileInvalidTest() throws Exception{
		List<String> ignoredFileTypes = new ArrayList<String>();
		ignoredFileTypes.add("MP4");
		ignoredFileTypes.add("AVI");
		Config mockedConfig=mock(Config.class);
		when(mockedConfig.getIgnoredFileTypes()).thenReturn(ignoredFileTypes);
		
		validator.configUpdated(mockedConfig);
		
		TorrentInfo mockedTorrentInfo=mock(TorrentInfo.class);
		when(mockedTorrentInfo.getName()).thenReturn("INVALIDFILE_TYPE.AVI");
		when(mockedTorrentInfo.getFiles()).thenReturn(null);
		
		Torrent mockedTorrent=mock(Torrent.class);
		when(mockedTorrent.getInfo()).thenReturn(mockedTorrentInfo);
		
		FeedResult result = createFeedResult();
		when(result.getTorrent()).thenReturn(mockedTorrent);
		validator.validate(result, null);
		
		Assert.assertEquals(1, result.getTorrentRejections().size());	
		Assert.assertEquals(TorrentRejection.CONTAINS_EXCLUDED_FILE_TYPES, result.getTorrentRejections().get(0));
	}
	
	@Test
	public void multipleFileValidTest() throws Exception{
		List<String> ignoredFileTypes = new ArrayList<String>();
		ignoredFileTypes.add("MP4");
		ignoredFileTypes.add("AVI");
		Config mockedConfig=mock(Config.class);
		when(mockedConfig.getIgnoredFileTypes()).thenReturn(ignoredFileTypes);
		
		validator.configUpdated(mockedConfig);
		
		TorrentFile mockedTorrentFile1=mock(TorrentFile.class);
		when(mockedTorrentFile1.getName()).thenReturn("VALID_FILE_TYPE.WMV");
		TorrentFile mockedTorrentFile2=mock(TorrentFile.class);
		when(mockedTorrentFile2.getName()).thenReturn("VALID_FILE_TYPE.FLV");		
		List<TorrentFile> torrentFiles = new ArrayList<TorrentFile>();
		torrentFiles.add(mockedTorrentFile1);
		torrentFiles.add(mockedTorrentFile2);
		
		TorrentInfo mockedTorrentInfo=mock(TorrentInfo.class);
		when(mockedTorrentInfo.getFiles()).thenReturn(torrentFiles);
		
		Torrent mockedTorrent=mock(Torrent.class);
		when(mockedTorrent.getInfo()).thenReturn(mockedTorrentInfo);
		
		FeedResult result = createFeedResult();
		when(result.getTorrent()).thenReturn(mockedTorrent);
		validator.validate(result, null);
		
		Assert.assertEquals(0, result.getTorrentRejections().size());		
	}
	@Test
	public void multipleFileInvalidTest() throws Exception{
		List<String> ignoredFileTypes = new ArrayList<String>();
		ignoredFileTypes.add("MP4");
		ignoredFileTypes.add("AVI");
		Config mockedConfig=mock(Config.class);
		when(mockedConfig.getIgnoredFileTypes()).thenReturn(ignoredFileTypes);
		
		validator.configUpdated(mockedConfig);
		
		TorrentFile mockedTorrentFile1=mock(TorrentFile.class);
		when(mockedTorrentFile1.getName()).thenReturn("VALID_FILE_TYPE.WMV");
		TorrentFile mockedTorrentFile2=mock(TorrentFile.class);
		when(mockedTorrentFile2.getName()).thenReturn("INVALID_FILE_TYPE.AVI");		
		List<TorrentFile> torrentFiles = new ArrayList<TorrentFile>();
		torrentFiles.add(mockedTorrentFile1);
		torrentFiles.add(mockedTorrentFile2);
		
		TorrentInfo mockedTorrentInfo=mock(TorrentInfo.class);
		when(mockedTorrentInfo.getFiles()).thenReturn(torrentFiles);
		
		Torrent mockedTorrent=mock(Torrent.class);
		when(mockedTorrent.getInfo()).thenReturn(mockedTorrentInfo);
		
		FeedResult result = createFeedResult();
		when(result.getTorrent()).thenReturn(mockedTorrent);
		
		validator.validate(result, null);
		
		Assert.assertEquals(1, result.getTorrentRejections().size());	
		Assert.assertEquals(TorrentRejection.CONTAINS_EXCLUDED_FILE_TYPES, result.getTorrentRejections().get(0));
	}
	*/
}
