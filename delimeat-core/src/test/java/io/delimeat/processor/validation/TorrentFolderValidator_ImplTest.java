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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.delimeat.config.entity.Config;
import io.delimeat.processor.entity.FeedProcessUnitRejection;
import io.delimeat.torrent.entity.Torrent;
import io.delimeat.torrent.entity.TorrentFile;
import io.delimeat.torrent.entity.TorrentInfo;
import io.delimeat.show.entity.Show;

public class TorrentFolderValidator_ImplTest {

	private TorrentFolderValidator_Impl validator;
	private Show show;
	private Config config;
	private Torrent torrent;

	@BeforeEach
	public void setUp() {
		validator = new TorrentFolderValidator_Impl();
		show = new Show();
		config = new Config();
		torrent = new Torrent();
	}

	@Test
	public void rejectionTest(){
		Assertions.assertEquals(FeedProcessUnitRejection.CONTAINS_FOLDERS, validator.getRejection());
	}
	
	@Test
	public void singleFileTest() throws Exception {
		TorrentInfo info = new TorrentInfo();
		torrent.setInfo(info);
		Assertions.assertTrue(validator.validate(torrent, show, config));

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

		Assertions.assertFalse(validator.validate(torrent, show, config));
	}
}
