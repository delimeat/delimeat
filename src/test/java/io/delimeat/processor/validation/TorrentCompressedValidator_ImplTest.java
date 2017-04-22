package io.delimeat.processor.validation;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.config.domain.Config;
import io.delimeat.feed.domain.FeedResultRejection;
import io.delimeat.processor.validation.TorrentCompressedValidator_Impl;
import io.delimeat.show.domain.Show;
import io.delimeat.torrent.domain.Torrent;
import io.delimeat.torrent.domain.TorrentFile;
import io.delimeat.torrent.domain.TorrentInfo;

public class TorrentCompressedValidator_ImplTest {

	private TorrentCompressedValidator_Impl validator;
	private Show show;
	private Config config;
	private Torrent torrent;

	@Before
	public void setUp() {
		validator = new TorrentCompressedValidator_Impl();
		show = new Show();
		config = new Config();
		torrent = new Torrent();
	}

	@Test
	public void singleFileValidTest() throws Exception {
		TorrentInfo info = new TorrentInfo();
		info.setName("VALIDFILE_TYPE.WMV");
		info.setFiles(null);

		torrent.setInfo(info);
		Assert.assertEquals(Optional.empty(), validator.validate(torrent, show, config));
	}

	@Test
	public void singleFileInvalidTest() throws Exception {
		TorrentInfo info = new TorrentInfo();
		info.setName("VALIDFILE_TYPE.ZIP");

		torrent.setInfo(info);
		Assert.assertEquals(Optional.of(FeedResultRejection.CONTAINS_COMPRESSED), validator.validate(torrent, show, config));

	}

	@Test
	public void multipleFileValidTest() throws Exception {
		TorrentInfo info = new TorrentInfo();
		TorrentFile file1 = new TorrentFile();
		file1.setName("VALIDFILE_TYPE.WMV");
		info.getFiles().add(file1);
		TorrentFile file2 = new TorrentFile();
		file2.setName("VALID_FILE_TYPE.FLV");
		info.getFiles().add(file2);

		torrent.setInfo(info);
		Assert.assertEquals(Optional.empty(), validator.validate(torrent, show, config));

	}

	@Test
	public void multipleFileInvalidTest() throws Exception {
		TorrentInfo info = new TorrentInfo();
		TorrentFile file1 = new TorrentFile();
		file1.setName("VALIDFILE_TYPE.rar");
		info.getFiles().add(file1);
		TorrentFile file2 = new TorrentFile();
		file2.setName("VALID_FILE_TYPE.FLV");
		info.getFiles().add(file2);

		torrent.setInfo(info);
		Assert.assertEquals(Optional.of(FeedResultRejection.CONTAINS_COMPRESSED), validator.validate(torrent, show, config));

	}
}
