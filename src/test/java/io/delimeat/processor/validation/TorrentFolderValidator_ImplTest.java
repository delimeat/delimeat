package io.delimeat.processor.validation;

import io.delimeat.config.domain.Config;
import io.delimeat.feed.domain.FeedResultRejection;
import io.delimeat.processor.validation.TorrentFolderValidator_Impl;
import io.delimeat.show.domain.Show;
import io.delimeat.torrent.domain.Torrent;
import io.delimeat.torrent.domain.TorrentFile;
import io.delimeat.torrent.domain.TorrentInfo;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TorrentFolderValidator_ImplTest {

	private TorrentFolderValidator_Impl validator;
	private Show show;
	private Config config;
	private Torrent torrent;

	@Before
	public void setUp() {
		validator = new TorrentFolderValidator_Impl();
		show = new Show();
		config = new Config();
		torrent = new Torrent();
	}

	@Test
	public void singleFileTest() throws Exception {
		TorrentInfo info = new TorrentInfo();
		torrent.setInfo(info);
		Assert.assertEquals(Optional.empty(), validator.validate(torrent, show, config));

	}

	@Test
	public void multipleFileInvalidTest() throws Exception {
		TorrentInfo info = new TorrentInfo();
		TorrentFile file1 = new TorrentFile();
		file1.setName("VALIDFILE_TYPE.WMV");
		info.getFiles().add(file1);
		TorrentFile file2 = new TorrentFile();
		file2.setName("VALID_FILE_TYPE.FLV");
		info.getFiles().add(file2);

		torrent.setInfo(info);

		Assert.assertEquals(Optional.of(FeedResultRejection.CONTAINS_FOLDERS), validator.validate(torrent, show, config));
	}
}
