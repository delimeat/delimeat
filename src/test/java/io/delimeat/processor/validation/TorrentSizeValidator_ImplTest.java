package io.delimeat.processor.validation;

import io.delimeat.config.domain.Config;
import io.delimeat.feed.domain.FeedResultRejection;
import io.delimeat.processor.validation.TorrentSizeValidator_Impl;
import io.delimeat.show.domain.Show;
import io.delimeat.torrent.domain.Torrent;
import io.delimeat.torrent.domain.TorrentFile;
import io.delimeat.torrent.domain.TorrentInfo;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TorrentSizeValidator_ImplTest {

	private TorrentSizeValidator_Impl validator;
	private Show show;
	private Config config;
	private Torrent torrent;

	@Before
	public void create() {
		validator = new TorrentSizeValidator_Impl();
		show = new Show();
		config = new Config();
		torrent = new Torrent();
	}

	@Test
	public void singleFileBelowMinSizeTest() throws Exception {
		TorrentInfo info = new TorrentInfo();
		info.setLength((long) (90 * 1024 * 1024));

		torrent.setInfo(info);

		show.setMinSize(91);
		show.setMaxSize(Integer.MAX_VALUE);

		Assert.assertEquals(Optional.of(FeedResultRejection.FILE_SIZE_INCORRECT), validator.validate(torrent, show, config));
	}

	@Test
	public void singleFileAboveMaxSizeTest() throws Exception {
		TorrentInfo info = new TorrentInfo();
		info.setLength((long) (90 * 1024 * 1024));

		torrent.setInfo(info);

		show.setMinSize(Integer.MIN_VALUE);
		show.setMaxSize(89);

		Assert.assertEquals(Optional.of(FeedResultRejection.FILE_SIZE_INCORRECT), validator.validate(torrent, show, config));
	}

	@Test
	public void singleFileCorrectSizeTest() throws Exception {
		TorrentInfo info = new TorrentInfo();
		info.setLength((long) (90 * 1024 * 1024));

		torrent.setInfo(info);

		show.setMinSize(89);
		show.setMaxSize(91);

		Assert.assertEquals(Optional.empty(), validator.validate(torrent, show, config));
	}

	@Test
	public void multipleFileBelowMinSize() throws Exception {
		TorrentInfo info = new TorrentInfo();
		TorrentFile file1 = new TorrentFile();
		file1.setLength((long) (44 * 1024 * 1024));
		info.getFiles().add(file1);
		TorrentFile file2 = new TorrentFile();
		file2.setLength((long) (44 * 1024 * 1024));
		info.getFiles().add(file2);

		torrent.setInfo(info);

		show.setMinSize(89);
		show.setMaxSize(91);

		Assert.assertEquals(Optional.of(FeedResultRejection.FILE_SIZE_INCORRECT), validator.validate(torrent, show, config));
	}

	@Test
	public void multipleFileAboveMaxSize() throws Exception {
		TorrentInfo info = new TorrentInfo();
		TorrentFile file1 = new TorrentFile();
		file1.setLength((long) (50 * 1024 * 1024));
		info.getFiles().add(file1);
		TorrentFile file2 = new TorrentFile();
		file2.setLength((long) (50 * 1024 * 1024));
		info.getFiles().add(file2);

		torrent.setInfo(info);

		show.setMinSize(89);
		show.setMaxSize(91);

		Assert.assertEquals(Optional.of(FeedResultRejection.FILE_SIZE_INCORRECT), validator.validate(torrent, show, config));
	}

	@Test
	public void multipleFileCorrectSize() throws Exception {
		TorrentInfo info = new TorrentInfo();
		TorrentFile file1 = new TorrentFile();
		file1.setLength((long) (45 * 1024 * 1024));
		info.getFiles().add(file1);
		TorrentFile file2 = new TorrentFile();
		file2.setLength((long) (45 * 1024 * 1024));
		info.getFiles().add(file2);

		torrent.setInfo(info);

		show.setMinSize(89);
		show.setMaxSize(91);

		Assert.assertEquals(Optional.empty(), validator.validate(torrent, show, config));
	}
}
