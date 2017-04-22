package io.delimeat.processor.validation;

import io.delimeat.config.domain.Config;
import io.delimeat.feed.domain.FeedResultRejection;
import io.delimeat.processor.validation.TorrentFileTypeValidator_Impl;
import io.delimeat.show.domain.Show;
import io.delimeat.torrent.domain.Torrent;
import io.delimeat.torrent.domain.TorrentFile;
import io.delimeat.torrent.domain.TorrentInfo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Optional;

public class TorrentFileTypeValidator_ImplTest {

	private TorrentFileTypeValidator_Impl validator;
	private Show show;
	private Config config;
	private Torrent torrent;

	@Before
	public void setUp() {
		validator = new TorrentFileTypeValidator_Impl();
		show = new Show();
		config = new Config();
		torrent = new Torrent();
	}

	@Test
	public void nullFileTypesTest() throws Exception {
		config.setIgnoredFileTypes(null);
		Assert.assertEquals(Optional.empty(), validator.validate(torrent, show, config));
	}

	@Test
	public void emptyFileTypesTest() throws Exception {
		config.setIgnoredFileTypes(Collections.<String>emptyList());
		Assert.assertEquals(Optional.empty(), validator.validate(torrent, show, config));
	}

	@Test
	public void singleFileValidTest() throws Exception {
		config.getIgnoredFileTypes().add("MP4");
		config.getIgnoredFileTypes().add("AVI");

		TorrentInfo info = new TorrentInfo();
		info.setName("MP4_VALIDFILE_AVI_TYPE.WMV");

		torrent.setInfo(info);

		Assert.assertEquals(Optional.empty(), validator.validate(torrent, show, config));
	}

	@Test
	public void singleFileInvalidTest() throws Exception {
		config.getIgnoredFileTypes().add("MP4");
		config.getIgnoredFileTypes().add("AVI");

		TorrentInfo info = new TorrentInfo();
		info.setName("INVALIDFILE_TYPE.AVI");

		torrent.setInfo(info);

		Assert.assertEquals(Optional.of(FeedResultRejection.CONTAINS_EXCLUDED_FILE_TYPES), validator.validate(torrent, show, config));
	}

	@Test
	public void multipleFileValidTest() throws Exception {
		config.getIgnoredFileTypes().add("MP4");
		config.getIgnoredFileTypes().add("AVI");

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
		config.getIgnoredFileTypes().add("MP4");
		config.getIgnoredFileTypes().add("AVI");

		TorrentInfo info = new TorrentInfo();
		TorrentFile file1 = new TorrentFile();
		file1.setName("VALIDFILE_TYPE.WMV");
		info.getFiles().add(file1);
		TorrentFile file2 = new TorrentFile();
		file2.setName("INVALID_FILE_TYPE.AVI");
		info.getFiles().add(file2);

		torrent.setInfo(info);

		Assert.assertEquals(Optional.of(FeedResultRejection.CONTAINS_EXCLUDED_FILE_TYPES), validator.validate(torrent, show, config));
	}

}
