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
package io.delimeat.guide.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.delimeat.guide.entity.GuideSource;

public class GuideSourceTest {

	@Test
	public void tvdbTest() {
		Assertions.assertEquals(0, GuideSource.TVDB.getValue());
	}

	@Test
	public void imdbTest() {
		Assertions.assertEquals(1, GuideSource.IMDB.getValue());
	}

	@Test
	public void tvrageTest() {
		Assertions.assertEquals(2, GuideSource.TVRAGE.getValue());
	}

	@Test
	public void tmdbTest() {
		Assertions.assertEquals(3, GuideSource.TMDB.getValue());
	}

	@Test
	public void tvmazeTest() {
		Assertions.assertEquals(4, GuideSource.TVMAZE.getValue());
	}
}
