/*
 * Copyright 2013-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.delimeat.processor.validation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.config.entity.Config;
import io.delimeat.processor.entity.FeedProcessUnitRejection;
import io.delimeat.torrent.entity.Torrent;
import io.delimeat.torrent.entity.TorrentFile;
import io.delimeat.torrent.entity.TorrentInfo;
import io.delimeat.show.entity.Show;

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
	public void rejectionTest(){
		Assert.assertEquals(FeedProcessUnitRejection.FILE_SIZE_INCORRECT, validator.getRejection());
	}

	@Test
	public void singleFileBelowMinSizeTest() throws Exception {
		TorrentInfo info = new TorrentInfo();
		info.setLength((long) (90 * 1024 * 1024));

		torrent.setInfo(info);

		show.setMinSize(91);
		show.setMaxSize(Integer.MAX_VALUE);

		Assert.assertFalse(validator.validate(torrent, show, config));
	}

	@Test
	public void singleFileAboveMaxSizeTest() throws Exception {
		TorrentInfo info = new TorrentInfo();
		info.setLength((long) (90 * 1024 * 1024));

		torrent.setInfo(info);

		show.setMinSize(Integer.MIN_VALUE);
		show.setMaxSize(89);

		Assert.assertFalse(validator.validate(torrent, show, config));
	}

	@Test
	public void singleFileCorrectSizeTest() throws Exception {
		TorrentInfo info = new TorrentInfo();
		info.setLength((long) (90 * 1024 * 1024));

		torrent.setInfo(info);

		show.setMinSize(89);
		show.setMaxSize(91);

		Assert.assertTrue(validator.validate(torrent, show, config));
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

		Assert.assertFalse(validator.validate(torrent, show, config));
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

		Assert.assertFalse(validator.validate(torrent, show, config));
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

		Assert.assertTrue(validator.validate(torrent, show, config));
	}
}
