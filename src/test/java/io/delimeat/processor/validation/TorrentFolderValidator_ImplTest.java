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
		config.setIgnoreFolders(true);
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
