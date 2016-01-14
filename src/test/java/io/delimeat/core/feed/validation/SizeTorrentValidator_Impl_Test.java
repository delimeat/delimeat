package io.delimeat.core.feed.validation;

import io.delimeat.core.feed.FeedResult;
import io.delimeat.core.feed.TorrentRejection;
import io.delimeat.core.show.Show;
import io.delimeat.core.torrent.Torrent;
import io.delimeat.core.torrent.TorrentFile;
import io.delimeat.core.torrent.TorrentInfo;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SizeTorrentValidator_Impl_Test {

	private TorrentSizeValidator_Impl validator;
	private FeedResult result;
	
	@Before
	public void create(){
		validator = new TorrentSizeValidator_Impl();
		result = new FeedResult();
	}
	
	@Test
	public void singleFileBelowMinSizeTest() throws Exception{
		Show mockedShow = Mockito.mock(Show.class);

		TorrentInfo mockedInfo = Mockito.mock(TorrentInfo.class);
		Mockito.when(mockedInfo.getLength()).thenReturn((long)(90*1024*1024));
		Mockito.when(mockedInfo.getFiles()).thenReturn(null);
		
		Torrent mockedTorrent = Mockito.mock(Torrent.class);
		Mockito.when(mockedTorrent.getInfo()).thenReturn(mockedInfo);

		result.setTorrent(mockedTorrent);
		
		validator.validate(result, mockedShow);
		
		Assert.assertEquals(1, result.getTorrentRejections().size());
		Assert.assertEquals(TorrentRejection.FILE_SIZE_INCORRECT, result.getTorrentRejections().get(0));
	}
	@Test
	public void singleFileAboveMaxSizeTest() throws Exception{
		Show mockedShow = Mockito.mock(Show.class);
		
		TorrentInfo mockedInfo = Mockito.mock(TorrentInfo.class);
		Mockito.when(mockedInfo.getLength()).thenReturn((long)(160*1024*1024));
		Mockito.when(mockedInfo.getFiles()).thenReturn(null);
		
		Torrent mockedTorrent = Mockito.mock(Torrent.class);
		Mockito.when(mockedTorrent.getInfo()).thenReturn(mockedInfo);

		result.setTorrent(mockedTorrent);
		
		validator.validate(result, mockedShow);
		
		Assert.assertEquals(1, result.getTorrentRejections().size());
		Assert.assertEquals(TorrentRejection.FILE_SIZE_INCORRECT, result.getTorrentRejections().get(0));
	}
	@Test
	public void singleFileCorrectSizeTest() throws Exception{
		Show mockedShow = Mockito.mock(Show.class);
		
		TorrentInfo mockedInfo = Mockito.mock(TorrentInfo.class);
		Mockito.when(mockedInfo.getLength()).thenReturn((long)(125*1024*1024));
		Mockito.when(mockedInfo.getFiles()).thenReturn(null);
		
		Torrent mockedTorrent = Mockito.mock(Torrent.class);
		Mockito.when(mockedTorrent.getInfo()).thenReturn(mockedInfo);

		result.setTorrent(mockedTorrent);
		
		validator.validate(result, mockedShow);
		
		Assert.assertEquals(0, result.getTorrentRejections().size());
	}
	
	@Test
	public void multipleFileBelowMinSize() throws Exception{
		Show mockedShow = Mockito.mock(Show.class);
		
		
		TorrentFile mockedTorrentFile1=Mockito.mock(TorrentFile.class);
		Mockito.when(mockedTorrentFile1.getLength()).thenReturn((long)(10*1024*1024));
		TorrentFile mockedTorrentFile2=Mockito.mock(TorrentFile.class);
		Mockito.when(mockedTorrentFile2.getLength()).thenReturn((long)(10*1024*1024));		
		List<TorrentFile> torrentFiles = new ArrayList<TorrentFile>();
		torrentFiles.add(mockedTorrentFile1);
		torrentFiles.add(mockedTorrentFile2);
		
		TorrentInfo mockedInfo=Mockito.mock(TorrentInfo.class);
		Mockito.when(mockedInfo.getFiles()).thenReturn(torrentFiles);
		
		Torrent mockedTorrent = Mockito.mock(Torrent.class);
		Mockito.when(mockedTorrent.getInfo()).thenReturn(mockedInfo);

		result.setTorrent(mockedTorrent);
		
		validator.validate(result, mockedShow);
		
		Assert.assertEquals(1, result.getTorrentRejections().size());
		Assert.assertEquals(TorrentRejection.FILE_SIZE_INCORRECT, result.getTorrentRejections().get(0));
	}
	@Test
	public void multipleFileAboveMaxSize() throws Exception{
		Show mockedShow = Mockito.mock(Show.class);
		
		TorrentFile mockedTorrentFile1=Mockito.mock(TorrentFile.class);
		Mockito.when(mockedTorrentFile1.getLength()).thenReturn((long)(100*1024*1024));
		TorrentFile mockedTorrentFile2=Mockito.mock(TorrentFile.class);
		Mockito.when(mockedTorrentFile2.getLength()).thenReturn((long)(200*1024*1024));		
		List<TorrentFile> torrentFiles = new ArrayList<TorrentFile>();
		torrentFiles.add(mockedTorrentFile1);
		torrentFiles.add(mockedTorrentFile2);
		
		TorrentInfo mockedInfo=Mockito.mock(TorrentInfo.class);
		Mockito.when(mockedInfo.getFiles()).thenReturn(torrentFiles);
		
		Torrent mockedTorrent = Mockito.mock(Torrent.class);
		Mockito.when(mockedTorrent.getInfo()).thenReturn(mockedInfo);

		result.setTorrent(mockedTorrent);
		
		validator.validate(result, mockedShow);
		
		Assert.assertEquals(1, result.getTorrentRejections().size());
		Assert.assertEquals(TorrentRejection.FILE_SIZE_INCORRECT, result.getTorrentRejections().get(0));
	}
	@Test
	public void multipleFileCorrectSize() throws Exception{
		Show mockedShow = Mockito.mock(Show.class);		
		
		TorrentFile mockedTorrentFile1=Mockito.mock(TorrentFile.class);
		Mockito.when(mockedTorrentFile1.getLength()).thenReturn((long)(75*1024*1024));
		TorrentFile mockedTorrentFile2=Mockito.mock(TorrentFile.class);
		Mockito.when(mockedTorrentFile2.getLength()).thenReturn((long)(25*1024*1024));		
		List<TorrentFile> torrentFiles = new ArrayList<TorrentFile>();
		torrentFiles.add(mockedTorrentFile1);
		torrentFiles.add(mockedTorrentFile2);
		
		TorrentInfo mockedInfo=Mockito.mock(TorrentInfo.class);
		Mockito.when(mockedInfo.getFiles()).thenReturn(torrentFiles);
		
		Torrent mockedTorrent = Mockito.mock(Torrent.class);
		Mockito.when(mockedTorrent.getInfo()).thenReturn(mockedInfo);

		result.setTorrent(mockedTorrent);
		
		validator.validate(result, mockedShow);
		
		Assert.assertEquals(0, result.getTorrentRejections().size());
	}
}
