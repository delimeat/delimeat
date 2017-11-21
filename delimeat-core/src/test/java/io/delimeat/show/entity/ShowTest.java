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
package io.delimeat.show.entity;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ShowTest {

	private Show show;

	@BeforeEach
	public void setUp() throws Exception {
		show = new Show();
	}

	@Test
	public void showIdTest() {
		Assertions.assertNull(show.getShowId());
		show.setShowId(Long.MAX_VALUE);
		Assertions.assertEquals(Long.MAX_VALUE, show.getShowId().longValue());
	}

	@Test
	public void airTimeTest() {
		Assertions.assertNull(show.getAirTime());
		show.setAirTime(LocalTime.NOON);
		Assertions.assertEquals(LocalTime.NOON, show.getAirTime());
	}

	@Test
	public void timezoneTest() {
		Assertions.assertNull(show.getTimezone());
		show.setTimezone("TIMEZONE");
		Assertions.assertEquals("TIMEZONE", show.getTimezone());
	}

	@Test
	public void guideIdTest() {
		Assertions.assertNull(show.getGuideId());
		show.setGuideId("GUIDEID");
		Assertions.assertEquals("GUIDEID", show.getGuideId());
	}

	@Test
	public void titleTest() {
		Assertions.assertNull(show.getTitle());
		show.setTitle("TITLE");
		Assertions.assertEquals("TITLE", show.getTitle());
	}

	@Test
	public void airingTest() {
		Assertions.assertFalse(show.isAiring());
		show.setAiring(true);
		Assertions.assertTrue(show.isAiring());
	}

	@Test
	public void showTypeTest() {
		Assertions.assertNull(show.getShowType());
		show.setShowType(ShowType.ANIMATED);
		Assertions.assertEquals(ShowType.ANIMATED, show.getShowType());
	}

	@Test
	public void lastGuideUpdateTest() throws ParseException {
		Assertions.assertNull(show.getLastGuideUpdate());
		show.setLastGuideUpdate(Instant.EPOCH);
		Assertions.assertEquals(Instant.EPOCH, show.getLastGuideUpdate());
	}

	@Test
	public void lastGuideCheckTest() throws ParseException {
		Assertions.assertNull(show.getLastGuideCheck());
		show.setLastGuideCheck(Instant.EPOCH);
		Assertions.assertEquals(Instant.EPOCH, show.getLastGuideCheck());
	}

	@Test
	public void enabledTest() {
		Assertions.assertFalse(show.isEnabled());
		show.setEnabled(true);
		Assertions.assertTrue(show.isEnabled());
	}

	@Test
	public void versionTest() {
		Assertions.assertEquals(0, show.getVersion());
		show.setVersion(Integer.MAX_VALUE);
		Assertions.assertEquals(Integer.MAX_VALUE, show.getVersion());
	}

	@Test
	public void minSizeTest() {
		Assertions.assertEquals(0, show.getMinSize());
		show.setMinSize(Integer.MAX_VALUE);
		Assertions.assertEquals(Integer.MAX_VALUE, show.getMinSize());
	}

	@Test
	public void maxSizeTest() {
		Assertions.assertEquals(0, show.getMaxSize());
		show.setMaxSize(Integer.MAX_VALUE);
		Assertions.assertEquals(Integer.MAX_VALUE, show.getMaxSize());
	}

	@Test
	public void episodesTest() {
		Episode episode = new Episode();
		Assertions.assertNull(show.getEpisodes());
		show.setEpisodes(Arrays.asList(episode, episode));
		Assertions.assertEquals(2, show.getEpisodes().size());
		Assertions.assertEquals(episode, show.getEpisodes().get(0));
		Assertions.assertEquals(episode, show.getEpisodes().get(1));

	}

	@Test
	public void hashCodeTest() throws ParseException {
		show.setShowId(Long.MAX_VALUE);
		show.setVersion(Integer.MIN_VALUE);
		Assertions.assertEquals(961, show.hashCode());
	}

	@Test
	public void toStringTest() throws ParseException {
		Assertions.assertEquals("Show [airing=false, enabled=false, minSize=0, maxSize=0, version=0]", show.toString());
	}

	@Test
	public void equalsNullTest() {
		Assertions.assertFalse(show.equals(null));
	}

	@Test
	public void equalsSelfTest() {
		Assertions.assertTrue(show.equals(show));
	}

	@Test
	public void equalsOtherObjectTest() {
		Assertions.assertFalse(show.equals(new Object()));
	}

	@Test
	public void equalsTest() {
		show.setShowId(1L);
		show.setVersion(99);
		Show other = new Show();
		other.setShowId(1L);
		other.setVersion(99);
		Assertions.assertTrue(show.equals(other));
	}

	@Test
	public void equalsShowIdTest() {
		show.setShowId(1L);
		show.setVersion(99);
		Show other = new Show();
		other.setShowId(2L);
		other.setVersion(99);
		Assertions.assertFalse(show.equals(other));
	}

	@Test
	public void equalsVersionTest() {
		show.setShowId(1L);
		show.setVersion(99);
		Show other = new Show();
		other.setShowId(1L);
		other.setVersion(98);
		Assertions.assertFalse(show.equals(other));
	}

}
