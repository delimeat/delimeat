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

public class MagnetScrapeRequestHandler_ImplTest {

	private MagnetScrapeRequestHandler_Impl scraper;

	@BeforeEach
	public void setUp() {
		scraper = new MagnetScrapeRequestHandler_Impl();
	}

	@Test
	public void defaultTrackerTest() throws Exception {
		Assertions.assertNull(scraper.getDefaultTracker());
		scraper.setDefaultTracker(new URI("TRACKER"));
		Assertions.assertEquals(new URI("TRACKER"), scraper.getDefaultTracker());
	}
}
