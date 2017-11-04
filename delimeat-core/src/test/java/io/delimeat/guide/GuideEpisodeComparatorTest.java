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
package io.delimeat.guide;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.guide.entity.GuideEpisode;

public class GuideEpisodeComparatorTest {

	private GuideEpisodeComparator comparator;
	private GuideEpisode ep1;
	private GuideEpisode ep2;
	
	@Before
	public void setUp(){
		comparator = new GuideEpisodeComparator();
		ep1 = new GuideEpisode();
		ep2 = new GuideEpisode();
	}

	@Test
	public void compareToNoAirDateTest() {
		ep1.setAirDate(null);
		ep2.setAirDate(null);

		Assert.assertEquals(0, comparator.compare(ep1,ep2));
	}

	@Test
	public void compareToOtherNoAirDateTest() throws Exception {
		ep1.setAirDate(LocalDate.parse("2005-01-02"));
		ep2.setAirDate(null);

		Assert.assertTrue(comparator.compare(ep1,ep2) >= 1);
	}

	@Test
	public void compareToThisNoAirDateTest() throws Exception {
		ep1.setAirDate(null);
		
		ep2.setAirDate(LocalDate.parse("2005-01-02"));

		Assert.assertTrue(comparator.compare(ep1,ep2) <= -1);
	}

	@Test
	public void compareToAirDateBeforeTest() throws Exception {
		ep1.setAirDate(LocalDate.parse("2005-01-02"));
		
		ep2.setAirDate(LocalDate.parse("2005-02-02"));

		Assert.assertEquals(-1, comparator.compare(ep1,ep2));
	}

	@Test
	public void compareToAirDateAfterTest() throws Exception {
		ep1.setAirDate(LocalDate.parse("2005-03-02"));
		ep2.setAirDate(LocalDate.parse("2005-02-02"));

		Assert.assertEquals(1, comparator.compare(ep1,ep2));
	}

	@Test
	public void compareToSeasonAfter() throws Exception {
		ep1.setAirDate(LocalDate.parse("2005-03-02"));
		ep1.setSeasonNum(2);
		ep1.setEpisodeNum(1);

		ep2.setAirDate(LocalDate.parse("2005-03-02"));
		ep2.setSeasonNum(1);
		ep2.setEpisodeNum(1);

		Assert.assertEquals(1, comparator.compare(ep1,ep2));
	}

	@Test
	public void compareToSeasonBefore() throws Exception {
		ep1.setAirDate(LocalDate.parse("2005-03-02"));
		ep1.setSeasonNum(1);
		ep1.setEpisodeNum(1);
		
		ep2.setAirDate(LocalDate.parse("2005-03-02"));
		ep2.setSeasonNum(2);
		ep2.setEpisodeNum(1);

		Assert.assertEquals(-1, comparator.compare(ep1,ep2));
	}

	@Test
	public void compareToSeasonNull() throws Exception {
		ep1.setAirDate(LocalDate.parse("2005-03-02"));
		ep1.setSeasonNum(null);
		ep1.setEpisodeNum(1);

		ep2.setAirDate(LocalDate.parse("2005-03-02"));
		ep2.setSeasonNum(1);
		ep2.setEpisodeNum(1);

		Assert.assertEquals(-1, comparator.compare(ep1,ep2));
	}

	@Test
	public void compareToEpisodeAfter() throws Exception {
		ep1.setAirDate(LocalDate.parse("2005-03-02"));
		ep1.setSeasonNum(2);
		ep1.setEpisodeNum(2);

		ep2.setAirDate(LocalDate.parse("2005-03-02"));
		ep2.setSeasonNum(1);
		ep2.setEpisodeNum(1);

		Assert.assertEquals(1, comparator.compare(ep1,ep2));
	}

	@Test
	public void compareToEpisodeBefore() throws Exception {
		ep1.setAirDate(LocalDate.parse("2005-03-02"));
		ep1.setSeasonNum(2);
		ep1.setEpisodeNum(1);

		ep2.setAirDate(LocalDate.parse("2005-03-02"));
		ep2.setSeasonNum(2);
		ep2.setEpisodeNum(2);

		Assert.assertEquals(-1, comparator.compare(ep1,ep2));
	}

	@Test
	public void compareToEpisodeNull() throws Exception {
		ep1.setAirDate(LocalDate.parse("2005-03-02"));
		ep1.setSeasonNum(1);
		ep1.setEpisodeNum(null);

		ep2.setAirDate(LocalDate.parse("2005-03-02"));
		ep2.setSeasonNum(1);
		ep2.setEpisodeNum(1);

		Assert.assertEquals(-1, comparator.compare(ep1,ep2));
	}

	@Test
	public void compareToEpisodeMatch() throws Exception {
		ep1.setAirDate(LocalDate.parse("2005-03-02"));
		ep1.setSeasonNum(2);
		ep1.setEpisodeNum(2);

		ep2.setAirDate(LocalDate.parse("2005-03-02"));
		ep2.setSeasonNum(2);
		ep2.setEpisodeNum(2);

		Assert.assertEquals(0, comparator.compare(ep1,ep2));
	}
}
