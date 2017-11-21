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
import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EpisodeTest {

	private Episode episode;

	@BeforeEach
	public void setUp() throws Exception {
		episode = new Episode();
	}

	@Test
	public void episodeIdTest() {
		Assertions.assertNull(episode.getEpisodeId());
		episode.setEpisodeId(Long.MAX_VALUE);
		Assertions.assertEquals(Long.MAX_VALUE, episode.getEpisodeId().longValue());
	}

	@Test
	public void titleTest() {
		Assertions.assertNull(episode.getTitle());
		episode.setTitle("TITLE");
		Assertions.assertEquals("TITLE", episode.getTitle());
	}

	@Test
	public void airDateTest() throws ParseException {
		Assertions.assertNull(episode.getAirDate());
		episode.setAirDate(LocalDate.parse("2017-03-31"));
		Assertions.assertEquals(LocalDate.parse("2017-03-31"), episode.getAirDate());
	}

	@Test
	public void seasonNumTest() {
		Assertions.assertEquals(0, episode.getSeasonNum());
		episode.setSeasonNum(Integer.MIN_VALUE);
		Assertions.assertEquals(Integer.MIN_VALUE, episode.getSeasonNum());
	}

	@Test
	public void episodeNumTest() {
		Assertions.assertEquals(0, episode.getEpisodeNum());
		episode.setEpisodeNum(Integer.MIN_VALUE);
		Assertions.assertEquals(Integer.MIN_VALUE, episode.getEpisodeNum());
	}

	@Test
	public void doubleEpisodeTest() {
		Assertions.assertFalse(episode.isDoubleEp());
		episode.setDoubleEp(true);
		Assertions.assertTrue(episode.isDoubleEp());
	}

	public void statusTest() {
		Assertions.assertEquals(EpisodeStatus.PENDING, episode.getStatus());
		episode.setStatus(EpisodeStatus.SKIPPED);
		Assertions.assertEquals(EpisodeStatus.SKIPPED, episode.getStatus());
	}

	@Test
	public void lastFeedUpdateTest() throws ParseException {
		Assertions.assertNull(episode.getLastFeedUpdate());
		Instant instant = Instant.now();
		episode.setLastFeedUpdate(instant);
		Assertions.assertEquals(instant, episode.getLastFeedUpdate());
	}

	@Test
	public void lastFeedCheckTest() throws ParseException {
		Assertions.assertNull(episode.getLastFeedCheck());
		Instant instant = Instant.now();
		episode.setLastFeedCheck(instant);
		Assertions.assertEquals(instant, episode.getLastFeedCheck());
	}

	@Test
	public void versionTest() {
		Assertions.assertEquals(0, episode.getVersion());
		episode.setVersion(Integer.MAX_VALUE);
		Assertions.assertEquals(Integer.MAX_VALUE, episode.getVersion());
	}

	@Test
	public void equalsEpisodeTest() throws ParseException {
		Episode otherEp = new Episode();
		otherEp.setTitle("EP");
		otherEp.setAirDate(LocalDate.parse("2016-01-28"));
		otherEp.setSeasonNum(1);
		otherEp.setEpisodeNum(2);
		otherEp.setEpisodeId(Long.MAX_VALUE);
		otherEp.setVersion(Integer.MIN_VALUE);

		episode.setTitle("EP");
		episode.setAirDate(LocalDate.parse("2016-01-28"));
		episode.setSeasonNum(1);
		episode.setEpisodeNum(2);
		episode.setEpisodeId(Long.MAX_VALUE);
		episode.setVersion(Integer.MIN_VALUE);

		Assertions.assertTrue(episode.equals(otherEp));
	}

	@Test
	public void equalsEpisodeEpisodeIdTest() throws ParseException {
		Episode otherEp = new Episode();
		otherEp.setTitle("EP");
		otherEp.setAirDate(LocalDate.parse("2016-01-28"));
		otherEp.setSeasonNum(1);
		otherEp.setEpisodeNum(2);
		otherEp.setEpisodeId(Long.MAX_VALUE);
		otherEp.setVersion(Integer.MIN_VALUE);

		episode.setTitle("EP");
		episode.setAirDate(LocalDate.parse("2016-01-28"));
		episode.setSeasonNum(1);
		episode.setEpisodeNum(2);
		episode.setEpisodeId(Long.MIN_VALUE);
		episode.setVersion(Integer.MIN_VALUE);

		Assertions.assertFalse(episode.equals(otherEp));
	}

	@Test
	public void showTest() {
		Assertions.assertNull(episode.getShow());
		Show show = new Show();
		episode.setShow(show);
		Assertions.assertEquals(show, episode.getShow());
	}

	@Test
	public void equalsTest() {
		Episode episode = new Episode();
		episode.setEpisodeId(1L);
		episode.setVersion(Integer.MAX_VALUE);

		Episode other = new Episode();
		other.setEpisodeId(1L);
		other.setVersion(Integer.MAX_VALUE);

		Assertions.assertTrue(episode.equals(other));
	}

	@Test
	public void equalsNullTest() {
		Assertions.assertFalse(episode.equals(null));
	}

	@Test
	public void equalsObjectTest() {
		Assertions.assertFalse(episode.equals(new Object()));
	}

	@Test
	public void equalsSelfTest() {
		Assertions.assertTrue(episode.equals(episode));
	}

	@Test
	public void equalsIdNullTest() {
		Episode episode = new Episode();
		episode.setEpisodeId(null);
		episode.setVersion(Integer.MAX_VALUE);

		Episode other = new Episode();
		other.setEpisodeId(1L);
		other.setVersion(Integer.MAX_VALUE);

		Assertions.assertFalse(episode.equals(other));
	}

	@Test
	public void equalsIdTest() {
		Episode episode = new Episode();
		episode.setEpisodeId(2L);
		episode.setVersion(Integer.MAX_VALUE);

		Episode other = new Episode();
		other.setEpisodeId(1L);
		other.setVersion(Integer.MAX_VALUE);

		Assertions.assertFalse(episode.equals(other));
	}

	@Test
	public void equalsVersionTest() {
		Episode episode = new Episode();
		episode.setEpisodeId(1L);
		episode.setVersion(Integer.MAX_VALUE);

		Episode other = new Episode();
		other.setEpisodeId(1L);
		other.setVersion(Integer.MIN_VALUE);

		Assertions.assertFalse(episode.equals(other));
	}

	@Test
	public void hashTest() {
		episode.setEpisodeId(Long.MIN_VALUE);
		episode.setVersion(Integer.MIN_VALUE);

		Assertions.assertEquals(961, episode.hashCode());
	}

	@Test
	public void toStringTest() throws ParseException {
		Assertions.assertEquals("Episode [seasonNum=0, episodeNum=0, doubleEp=false, status=PENDING, version=0, ]",
				episode.toString());
	}

}
