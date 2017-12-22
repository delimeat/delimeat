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

import java.net.URI;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.delimeat.torrent.exception.TorrentException;

public class MagnetTorrentReader_ImplTest {

	private MagnetTorrentReader_Impl reader;

	@BeforeEach
	public void setUp() {
		reader = new MagnetTorrentReader_Impl();
	}
	
	@Test
	public void downloadUriTemplateTest() {
		Assertions.assertNull(reader.getDownloadUriTemplate());
		reader.setDownloadUriTemplate("template");
		Assertions.assertEquals("template", reader.getDownloadUriTemplate());
	}

	@Test
	public void readUriSyntaxExceptionTest() throws Exception {
		reader.setDownloadUriTemplate("\\//");

		Exception ex = Assertions.assertThrows(TorrentException.class, () -> {
			reader.read(new URI(
					"magnet:?xt=urn:btih:df706cf16f45e8c0fd226223509c7e97b4ffec13&tr=udp://tracker.coppersurfer.tk:6969/announce"));
		});
		Assertions.assertEquals("Encountered an error creating uri for df706cf16f45e8c0fd226223509c7e97b4ffec13",
				ex.getMessage());
	}
}
