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
package io.delimeat.show.domain;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.show.domain.Show;
import io.delimeat.show.domain.ShowType;

public class ShowTest {
  
	private Show show;

	@Before
	public void setUp() throws Exception {
		show = new Show();
	}

	@Test
	public void showIdTest() {
		Assert.assertNull(show.getShowId());
		show.setShowId(Long.MAX_VALUE);
		Assert.assertEquals(Long.MAX_VALUE, show.getShowId().longValue());
	}

	@Test
	public void airTimeTest() {
		Assert.assertNull(show.getAirTime());
		show.setAirTime(LocalTime.NOON);
		Assert.assertEquals(LocalTime.NOON, show.getAirTime());
	}

	@Test
	public void timezoneTest() {
		Assert.assertNull(show.getTimezone());
		show.setTimezone("TIMEZONE");
		Assert.assertEquals("TIMEZONE", show.getTimezone());
	}

	@Test
	public void guideIdTest() {
		Assert.assertNull(show.getGuideId());
		show.setGuideId("GUIDEID");
		Assert.assertEquals("GUIDEID", show.getGuideId());
	}

	@Test
	public void titleTest() {
		Assert.assertNull(show.getTitle());
		show.setTitle("TITLE");
		Assert.assertEquals("TITLE", show.getTitle());
	}

	@Test
	public void airingTest() {
		Assert.assertFalse(show.isAiring());
		show.setAiring(true);
		Assert.assertTrue(show.isAiring());
	}

	@Test
	public void showTypeTest() {
		Assert.assertNull(show.getShowType());
		show.setShowType(ShowType.ANIMATED);
		Assert.assertEquals(ShowType.ANIMATED, show.getShowType());
	}

	@Test
	public void lastGuideUpdateTest() throws ParseException {
		Assert.assertNull(show.getLastGuideUpdate());
		show.setLastGuideUpdate(Instant.EPOCH);
		Assert.assertEquals(Instant.EPOCH, show.getLastGuideUpdate());
	}

	@Test
	public void lastGuideCheckTest() throws ParseException {
		Assert.assertNull(show.getLastGuideCheck());
		show.setLastGuideCheck(Instant.EPOCH);
		Assert.assertEquals(Instant.EPOCH, show.getLastGuideCheck());
	}

	
	@Test
	public void enabledTest() {
		Assert.assertFalse(show.isEnabled());
		show.setEnabled(true);
		Assert.assertTrue(show.isEnabled());
	}

	@Test
	public void versionTest() {
		Assert.assertEquals(0, show.getVersion());
		show.setVersion(Integer.MAX_VALUE);
		Assert.assertEquals(Integer.MAX_VALUE, show.getVersion());
	}
  
	@Test
	public void minSizeTest() {
		Assert.assertEquals(0, show.getMinSize());
		show.setMinSize(Integer.MAX_VALUE);
		Assert.assertEquals(Integer.MAX_VALUE, show.getMinSize());
	}
  
	@Test
	public void maxSizeTest() {
		Assert.assertEquals(0, show.getMaxSize());
		show.setMaxSize(Integer.MAX_VALUE);
		Assert.assertEquals(Integer.MAX_VALUE, show.getMaxSize());
	}
	
	@Test
	public void episodesTest(){
		Episode episode = new Episode();
		Assert.assertNull(show.getEpisodes());
		show.setEpisodes(Arrays.asList(episode,episode));
		Assert.assertEquals(2, show.getEpisodes().size());
		Assert.assertEquals(episode, show.getEpisodes().get(0));
		Assert.assertEquals(episode, show.getEpisodes().get(1));

	}

  	@Test
  	public void hashCodeTest() throws ParseException{
		show.setShowId(Long.MAX_VALUE);
		show.setVersion(Integer.MIN_VALUE);
		Assert.assertEquals(961, show.hashCode());
   }
  
  	@Test
  	public void toStringTest() throws ParseException{
		Assert.assertEquals("Show [airing=false, enabled=false, minSize=0, maxSize=0, version=0]",show.toString());
   }
  
  	@Test
  	public void equalsNullTest(){
     	Assert.assertFalse(show.equals(null));     	
   }
  
  	@Test
  	public void equalsSelfTest(){
     	Assert.assertTrue(show.equals(show));
   }
  
  	@Test
  	public void equalsOtherObjectTest(){
     	Assert.assertFalse(show.equals(new Object()));
   }
  
  	@Test
  	public void equalsTest(){
     	show.setShowId(1L);
     	show.setVersion(99);
     	Show other = new Show();
     	other.setShowId(1L);
     	other.setVersion(99);
     	Assert.assertTrue(show.equals(other));
   }
  
  	@Test
  	public void equalsShowIdTest(){
     	show.setShowId(1L);
     	show.setVersion(99);
     	Show other = new Show();
     	other.setShowId(2L);
     	other.setVersion(99);
     	Assert.assertFalse(show.equals(other));
   }
  	@Test
  	public void equalsVersionTest(){
     	show.setShowId(1L);
      show.setVersion(99);
     	Show other = new Show();
     	other.setShowId(1L);
      other.setVersion(98);
     	Assert.assertFalse(show.equals(other));
   }

}
