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
package io.delimeat.processor.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FeedProcessUnitRejectionTest {

	@Test
	public void containsFolersTest() {
		Assertions.assertEquals(1, FeedProcessUnitRejection.CONTAINS_FOLDERS.getValue());
	}

	@Test
	public void containsCompressedTest() {
		Assertions.assertEquals(2, FeedProcessUnitRejection.CONTAINS_COMPRESSED.getValue());
	}

	@Test
	public void containsExcludedFileTypesTest() {
		Assertions.assertEquals(3, FeedProcessUnitRejection.CONTAINS_EXCLUDED_FILE_TYPES.getValue());
	}

	@Test
	public void fileSizeIncorrectTest() {
		Assertions.assertEquals(4, FeedProcessUnitRejection.FILE_SIZE_INCORRECT.getValue());
	}

	@Test
	public void insufficientSeedersTest() {
		Assertions.assertEquals(5, FeedProcessUnitRejection.INSUFFICENT_SEEDERS.getValue());
	}

	@Test
	public void unableToGetTorrentTest() {
		Assertions.assertEquals(6, FeedProcessUnitRejection.UNNABLE_TO_GET_TORRENT.getValue());
	}
}
