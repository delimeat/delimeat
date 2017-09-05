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

import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.config.domain.Config;
import io.delimeat.processor.domain.FeedProcessUnitRejection;
import io.delimeat.show.domain.Show;
import io.delimeat.torrent.domain.Torrent;
import io.delimeat.torrent.domain.TorrentFile;
import io.delimeat.torrent.domain.TorrentInfo;

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
	public void rejectionTest(){
		Assert.assertEquals(FeedProcessUnitRejection.CONTAINS_EXCLUDED_FILE_TYPES, validator.getRejection());
	}
	
	@Test
	public void nullFileTypesTest() throws Exception {
		config.setIgnoredFileTypes(null);
		Assert.assertTrue( validator.validate(torrent, show, config));
	}

	@Test
	public void emptyFileTypesTest() throws Exception {
		config.setIgnoredFileTypes(Collections.<String>emptyList());
		Assert.assertTrue( validator.validate(torrent, show, config));
	}

	@Test
	public void singleFileValidTest() throws Exception {
		config.getIgnoredFileTypes().add("MP4");
		config.getIgnoredFileTypes().add("AVI");

		TorrentInfo info = new TorrentInfo();
		info.setName("MP4_VALIDFILE_AVI_TYPE.WMV");

		torrent.setInfo(info);

		Assert.assertTrue( validator.validate(torrent, show, config));
	}

	@Test
	public void singleFileInvalidTest() throws Exception {
		config.getIgnoredFileTypes().add("MP4");
		config.getIgnoredFileTypes().add("AVI");

		TorrentInfo info = new TorrentInfo();
		info.setName("INVALIDFILE_TYPE.AVI");

		torrent.setInfo(info);

		Assert.assertFalse( validator.validate(torrent, show, config));
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

		Assert.assertTrue( validator.validate(torrent, show, config));
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

		Assert.assertFalse( validator.validate(torrent, show, config));
	}

}
