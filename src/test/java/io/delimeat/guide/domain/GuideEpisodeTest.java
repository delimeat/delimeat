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
package io.delimeat.guide.domain;

import java.text.ParseException;
import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GuideEpisodeTest {

	private GuideEpisode ep;

	@Before
	public void setUp() {
		ep = new GuideEpisode();
	}

	@Test
	public void setAirDateTest() throws ParseException {
		Assert.assertNull(ep.getAirDate());
		ep.setAirDate(LocalDate.parse("2005-01-02"));
		Assert.assertEquals(LocalDate.parse("2005-01-02"), ep.getAirDate());

	}

	@Test
	public void setTitleTest() {
		Assert.assertEquals(null, ep.getTitle());
		ep.setTitle("TEST TITLE");
		Assert.assertEquals("TEST TITLE", ep.getTitle());
	}

	@Test
	public void setSeasonNumTest() {
		Assert.assertEquals(0, ep.getSeasonNum().intValue());
		ep.setSeasonNum(12);
		Assert.assertEquals(12, ep.getSeasonNum().intValue());
	}

	@Test
	public void setEpisodeNumTest() {
		Assert.assertEquals(0, ep.getEpisodeNum().intValue());
		ep.setEpisodeNum(55);
		Assert.assertEquals(55, ep.getEpisodeNum().intValue());
	}

	@Test
	public void setProductionNumTest() {
		Assert.assertEquals(0, ep.getProductionNum().intValue());
		ep.setProductionNum(208);
		Assert.assertEquals(208, ep.getProductionNum().intValue());
	}

	@Test
	public void equalsNullTest() {
		Assert.assertFalse(ep.equals(null));
	}

	@Test
	public void equalsObjectTest() {
		Assert.assertFalse(ep.equals(new Object()));
	}

	@Test
	public void equalsSelfTest() {
		Assert.assertTrue(ep.equals(ep));
	}

	@Test
	public void equalsGuideEpisodeTest() throws ParseException {
		GuideEpisode otherEp = new GuideEpisode();
		otherEp.setTitle("OTHER");
		otherEp.setAirDate(LocalDate.parse("2016-01-28"));
		otherEp.setSeasonNum(1);
		otherEp.setEpisodeNum(2);

		ep.setTitle("OTHER");
		ep.setAirDate(LocalDate.parse("2016-01-28"));
		ep.setSeasonNum(1);
		ep.setEpisodeNum(2);

		Assert.assertTrue(ep.equals(otherEp));
	}

	@Test
	public void equalsGuideEpisodeSeasonNumTest() throws ParseException {
		GuideEpisode otherEp = new GuideEpisode();
		otherEp.setTitle("OTHER");
		otherEp.setAirDate(LocalDate.parse("2016-01-28"));
		otherEp.setSeasonNum(2);
		otherEp.setEpisodeNum(2);

		ep.setTitle("EP");
		ep.setAirDate(LocalDate.parse("2000-01-28"));
		ep.setSeasonNum(1);
		ep.setEpisodeNum(2);

		Assert.assertFalse(ep.equals(otherEp));
	}

	@Test
	public void equalsGuideEpisodeEpisodeNumTest() throws ParseException {
		GuideEpisode otherEp = new GuideEpisode();
		otherEp.setTitle("OTHER");
		otherEp.setAirDate(LocalDate.parse("2016-01-28"));
		otherEp.setSeasonNum(1);
		otherEp.setEpisodeNum(1);

		ep.setTitle("EP");
		ep.setAirDate(LocalDate.parse("2000-01-28"));
		ep.setSeasonNum(1);
		ep.setEpisodeNum(2);

		Assert.assertFalse(ep.equals(otherEp));
	}

	@Test
	public void hashCodeTest() throws ParseException {
		ep.setTitle("EP");
		ep.setAirDate(LocalDate.parse("2000-01-28"));
		ep.setSeasonNum(1);
		ep.setEpisodeNum(2);

		Assert.assertEquals(-1010516861, ep.hashCode());
	}

	@Test
	public void toStringTest() throws ParseException {
		Assert.assertEquals("GuideEpisode [seasonNum=0, episodeNum=0, productionNum=0, ]", ep.toString());
	}
}
