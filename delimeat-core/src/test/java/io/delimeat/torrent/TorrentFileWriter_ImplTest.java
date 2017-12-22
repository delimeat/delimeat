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
package io.delimeat.torrent;

import java.io.IOException;
import java.net.URL;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.delimeat.config.entity.Config;
import io.delimeat.torrent.exception.TorrentException;

public class TorrentFileWriter_ImplTest {

	private TorrentFileWriter_Impl writer;

	@BeforeEach
	public void setUp() {
		writer = new TorrentFileWriter_Impl();
	}

	@Test
	public void buildUrlDirectoryTest() throws IOException, TorrentException {
		Config config = new Config();
		config.setOutputDirectory("OUTPUT");

		Assertions.assertEquals(new URL("file:OUTPUT/FILENAME"), writer.buildUrl("FILENAME", config));
	}

	@Test
	public void buildUrlNullDirectoryTest() throws IOException, TorrentException {
		Config config = new Config();
		config.setOutputDirectory(null);

		String dir = System.getProperty("user.home");

		Assertions.assertEquals(new URL("file:" + dir + "/FILENAME"), writer.buildUrl("FILENAME", config));
	}

	@Test
	public void buildUrlEmptyDirectoryTest() throws IOException, TorrentException {
		Config config = new Config();
		config.setOutputDirectory("");

		String dir = System.getProperty("user.home");

		Assertions.assertEquals(new URL("file:" + dir + "/FILENAME"), writer.buildUrl("FILENAME", config));
	}

	@Test
	public void writeTest() throws TorrentException {
		Exception ex = Assertions.assertThrows(TorrentException.class, () -> {
			writer.write("", "JIBBERISH".getBytes(), new Config());
		});

		Assertions.assertEquals("Unnable to write ", ex.getMessage());
	}

}
