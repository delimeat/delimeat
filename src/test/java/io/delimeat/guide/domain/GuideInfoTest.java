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
import java.time.LocalTime;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.guide.domain.AiringDay;
import io.delimeat.guide.domain.GuideInfo;

public class GuideInfoTest {
	
	private GuideInfo info;

	@Before
	public void setUp() {
		info = new GuideInfo();
	}

	@Test
	public void setAirDayTest() {
		Assert.assertNotNull(info.getAirDays());
		Assert.assertEquals(0, info.getAirDays().size());
		info.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		Assert.assertNotNull(info.getAirDays());
		Assert.assertEquals(1, info.getAirDays().size());
		Assert.assertEquals(AiringDay.FRIDAY, info.getAirDays().get(0));
	}

	@Test
	public void setDescriptionTest() {

		Assert.assertEquals(null, info.getDescription());
		info.setDescription("DESCRIPTION");
		Assert.assertEquals("DESCRIPTION", info.getDescription());
	}

	@Test
	public void GuideIdTest() {
		Assert.assertNull(info.getGuideId());
		info.setGuideId("GUIDEID");
		Assert.assertEquals("GUIDEID", info.getGuideId());
	}

	@Test
	public void setTitleTest() {
		Assert.assertEquals(null, info.getTitle());
		info.setTitle("TITLE");
		Assert.assertEquals("TITLE", info.getTitle());
	}

	@Test
	public void setTimezoneTest() {
		Assert.assertEquals(null, info.getTimezone());
		info.setTimezone("TIMEZONE");
		Assert.assertEquals("TIMEZONE", info.getTimezone());
	}

	@Test
	public void setRunningTimeTest() {
		Assert.assertEquals(0, info.getRunningTime());
		info.setRunningTime(60);
		Assert.assertEquals(60, info.getRunningTime());
	}

	@Test
	public void setGenresTest() {
		Assert.assertEquals(0, info.getGenres().size());
		info.setGenres(Arrays.asList("GENRE1"));
		Assert.assertEquals(1, info.getGenres().size());
		Assert.assertEquals("GENRE1", info.getGenres().get(0));
	}

	@Test
	public void airingTest() {
		Assert.assertTrue(info.isAiring());
		info.setAiring(false);
		Assert.assertFalse(info.isAiring());
	}

	@Test
	public void setAirTimeTest() {
		Assert.assertNull(info.getAirTime());
		info.setAirTime(LocalTime.NOON);
		Assert.assertEquals(LocalTime.NOON, info.getAirTime());
	}
	
	@Test
	public void firstAiredTest() throws ParseException {

		Assert.assertEquals(null, info.getFirstAired());

		info.setFirstAired(LocalDate.parse("2005-04-03"));
		Assert.assertEquals(LocalDate.parse("2005-04-03"), info.getFirstAired());
	}

	@Test
	public void lasUpdatedTest() throws ParseException {
		Assert.assertEquals(null, info.getLastUpdated());
		info.setLastUpdated(LocalDate.parse("2005-04-03"));
		Assert.assertEquals(LocalDate.parse("2005-04-03"), info.getLastUpdated());
	}
  
	@Test
	public void compareTitleMatchTest() {
		info.setTitle("ABC");
		GuideInfo info2 = new GuideInfo();
		info2.setTitle("ABC");

		Assert.assertEquals(0, info.compareTo(info2));
	}

	@Test
	public void compareNullTitlesTest() {
		Assert.assertNull(info.getTitle());
		GuideInfo info2 = new GuideInfo();
		Assert.assertNull(info2.getTitle());

		Assert.assertEquals(0, info.compareTo(info2));
	}

	@Test
	public void compareTitleOtherNullTest() {
		info.setTitle("ABC");
		Assert.assertNotNull(info.getTitle());
		GuideInfo info2 = new GuideInfo();
		Assert.assertNull(info2.getTitle());

		Assert.assertEquals(1, info.compareTo(info2));
	}

	@Test
	public void compareTitleNullOtherNotNullTest() {
		Assert.assertNull(info.getTitle());
		GuideInfo info2 = new GuideInfo();
		info2.setTitle("ABC");
		Assert.assertNotNull(info2.getTitle());

		Assert.assertEquals(-1, info.compareTo(info2));
	}

	@Test
	public void compareTitleBeforeTest() {
		info.setTitle("ABC");
		GuideInfo info2 = new GuideInfo();
		info2.setTitle("BCD");

		Assert.assertEquals(-1, info.compareTo(info2));
	}

	@Test
	public void compareTitleAfterTest() {
		info.setTitle("BCD");
		GuideInfo info2 = new GuideInfo();
		info2.setTitle("ABC");

		Assert.assertEquals(1, info.compareTo(info2));
	}
  
  	
	@Test
  	public void hashCodeTest() throws ParseException{
     	info.setTitle("TITLE");
     	info.setLastUpdated(LocalDate.parse("2016-02-19"));
     	Assert.assertEquals(-1815994084, info.hashCode());
   }
  
  	@Test
  	public void toStringTest() throws ParseException{
  		Assert.assertEquals("GuideInfo{airing=true, airDays=[], genres=[], runningTime=0}",info.toString());
  	}
  
  	@Test
  	public void equalsNullTest(){
   	Assert.assertFalse(info.equals(null));
   }
  
  	@Test
  	public void equalsSelfTest(){
     	Assert.assertTrue(info.equals(info));
   }
  
  	@Test
  	public void equalsOtherObjectTest(){
     	Assert.assertFalse(info.equals(new Object()));
   }
  	
  	@Test
  	public void equalsTest(){
     	info.setTitle("TITLE");
     	info.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		info.setDescription("DESCRIPTION");
		info.setGuideId("GUIDEID");
		info.setTimezone("TIMEZONE");
		info.setRunningTime(60);
		info.setGenres(Arrays.asList("GENRE1"));
		info.setAiring(false);
		info.setAirTime(LocalTime.NOON);
		info.setFirstAired(LocalDate.ofEpochDay(0));
     	info.setLastUpdated(LocalDate.ofEpochDay(0));
     
    	GuideInfo other = new GuideInfo();
     	other.setTitle("TITLE");
     	other.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		other.setDescription("DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setTimezone("TIMEZONE");
		other.setRunningTime(60);
		other.setGenres(Arrays.asList("GENRE1"));
		other.setAiring(false);
		other.setAirTime(LocalTime.NOON);
		other.setFirstAired(LocalDate.ofEpochDay(0));
     	other.setLastUpdated(LocalDate.ofEpochDay(0));
     
     	Assert.assertTrue(info.equals(other));     
   }
  
  	@Test	
  	public void equalsTitleTest() throws Exception{
     	info.setTitle("TITLE");
     	info.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		info.setDescription("DESCRIPTION");
		info.setGuideId("GUIDEID");
		info.setTimezone("TIMEZONE");
		info.setRunningTime(60);
		info.setGenres(Arrays.asList("GENRE1"));
		info.setAiring(false);
		info.setAirTime(LocalTime.NOON);
		info.setFirstAired(LocalDate.parse("2005-04-03"));
     	info.setLastUpdated(LocalDate.parse("2016-02-19"));
     
    	GuideInfo other = new GuideInfo();
     	other.setTitle("OTHER_TITLE");
     	other.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		other.setDescription("DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setTimezone("TIMEZONE");
		other.setRunningTime(60);
		other.setGenres(Arrays.asList("GENRE1"));
		other.setAiring(false);
		other.setAirTime(LocalTime.NOON);
		other.setFirstAired(LocalDate.parse("2005-04-03"));
     	other.setLastUpdated(LocalDate.parse("2016-02-19"));
     
     	Assert.assertFalse(info.equals(other));
   }
  
  	@Test	
  	public void equalsAirDaysTest() throws Exception{
     	info.setTitle("TITLE");
     	info.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		info.setDescription("DESCRIPTION");
		info.setGuideId("GUIDEID");
		info.setTimezone("TIMEZONE");
		info.setRunningTime(60);
		info.setGenres(Arrays.asList("GENRE1"));
		info.setAiring(false);
		info.setAirTime(LocalTime.NOON);
		info.setFirstAired(LocalDate.parse("2005-04-03"));
     	info.setLastUpdated(LocalDate.parse("2016-02-19"));
     
    	GuideInfo other = new GuideInfo();
     	other.setTitle("TITLE");
     	other.setAirDays(Arrays.asList(AiringDay.FRIDAY,AiringDay.MONDAY));
		other.setDescription("DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setTimezone("TIMEZONE");
		other.setRunningTime(60);
		other.setGenres(Arrays.asList("GENRE1"));
		other.setAiring(false);
		other.setAirTime(LocalTime.NOON);
		other.setFirstAired(LocalDate.parse("2005-04-03"));
     	other.setLastUpdated(LocalDate.parse("2016-02-19"));
     
     	Assert.assertFalse(info.equals(other));
   }
  
  	@Test	
  	public void equalsDescriptionTest() throws Exception{
     	info.setTitle("TITLE");
     	info.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		info.setDescription("DESCRIPTION");
		info.setGuideId("GUIDEID");
		info.setTimezone("TIMEZONE");
		info.setRunningTime(60);
		info.setGenres(Arrays.asList("GENRE1"));
		info.setAiring(false);
		info.setAirTime(LocalTime.NOON);
		info.setFirstAired(LocalDate.parse("2005-04-03"));
     	info.setLastUpdated(LocalDate.parse("2016-02-19"));
     
    	GuideInfo other = new GuideInfo();
     	other.setTitle("TITLE");
     	other.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		other.setDescription("OTHER_DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setTimezone("TIMEZONE");
		other.setRunningTime(60);
		other.setGenres(Arrays.asList("GENRE1"));
		other.setAiring(false);
		other.setAirTime(LocalTime.NOON);
		other.setFirstAired(LocalDate.parse("2005-04-03"));
     	other.setLastUpdated(LocalDate.parse("2016-02-19"));
     
     	Assert.assertFalse(info.equals(other));
   }

  	@Test	
  	public void equalsGuideIdTest() throws Exception{
     	info.setTitle("TITLE");
     	info.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		info.setDescription("DESCRIPTION");
		info.setGuideId("GUIDEID");
		info.setTimezone("TIMEZONE");
		info.setRunningTime(60);
		info.setGenres(Arrays.asList("GENRE1"));
		info.setAiring(false);
		info.setAirTime(LocalTime.NOON);
		info.setFirstAired(LocalDate.parse("2005-04-03"));
     	info.setLastUpdated(LocalDate.parse("2016-02-19"));
     
    	GuideInfo other = new GuideInfo();
     	other.setTitle("TITLE");
     	other.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		other.setDescription("DESCRIPTION");
		other.setGuideId("OTHER_GUIDEID");
		other.setTimezone("TIMEZONE");
		other.setRunningTime(60);
		other.setGenres(Arrays.asList("GENRE1"));
		other.setAiring(false);
		other.setAirTime(LocalTime.NOON);
		other.setFirstAired(LocalDate.parse("2005-04-03"));
     	other.setLastUpdated(LocalDate.parse("2016-02-19"));
     
     	Assert.assertFalse(info.equals(other));
   }

  	@Test	
  	public void equalsTimezoneTest() throws Exception{
     	info.setTitle("TITLE");
     	info.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		info.setDescription("DESCRIPTION");
		info.setGuideId("GUIDEID");
		info.setTimezone("TIMEZONE");
		info.setRunningTime(60);
		info.setGenres(Arrays.asList("GENRE1"));
		info.setAiring(false);
		info.setAirTime(LocalTime.NOON);
		info.setFirstAired(LocalDate.parse("2005-04-03"));
     	info.setLastUpdated(LocalDate.parse("2016-02-19"));
     
    	GuideInfo other = new GuideInfo();
     	other.setTitle("TITLE");
     	other.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		other.setDescription("DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setTimezone("OTHER_TIMEZONE");
		other.setRunningTime(60);
		other.setGenres(Arrays.asList("GENRE1"));
		other.setAiring(false);
		other.setAirTime(LocalTime.NOON);
		other.setFirstAired(LocalDate.parse("2005-04-03"));
     	other.setLastUpdated(LocalDate.parse("2016-02-19"));
     
     	Assert.assertFalse(info.equals(other));
   }

  	@Test	
  	public void equalsRunningTimeTest() throws Exception{
     	info.setTitle("TITLE");
     	info.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		info.setDescription("DESCRIPTION");
		info.setGuideId("GUIDEID");
		info.setTimezone("TIMEZONE");
		info.setRunningTime(60);
		info.setGenres(Arrays.asList("GENRE1"));
		info.setAiring(false);
		info.setAirTime(LocalTime.NOON);
		info.setFirstAired(LocalDate.parse("2005-04-03"));
     	info.setLastUpdated(LocalDate.parse("2016-02-19"));
     
    	GuideInfo other = new GuideInfo();
     	other.setTitle("TITLE");
     	other.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		other.setDescription("DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setTimezone("TIMEZONE");
		other.setRunningTime(61);
		other.setGenres(Arrays.asList("GENRE1"));
		other.setAiring(false);
		other.setAirTime(LocalTime.NOON);
		other.setFirstAired(LocalDate.parse("2005-04-03"));
     	other.setLastUpdated(LocalDate.parse("2016-02-19"));
     
     	Assert.assertFalse(info.equals(other));
   }

  	@Test	
  	public void equalsGenresTest() throws Exception{
     	info.setTitle("TITLE");
     	info.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		info.setDescription("DESCRIPTION");
		info.setGuideId("GUIDEID");
		info.setTimezone("TIMEZONE");
		info.setRunningTime(60);
		info.setGenres(Arrays.asList("GENRE1"));
		info.setAiring(false);
		info.setAirTime(LocalTime.NOON);
		info.setFirstAired(LocalDate.parse("2005-04-03"));
     	info.setLastUpdated(LocalDate.parse("2016-02-19"));
     
    	GuideInfo other = new GuideInfo();
     	other.setTitle("TITLE");
     	other.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		other.setDescription("DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setTimezone("TIMEZONE");
		other.setRunningTime(60);
		other.setGenres(Arrays.asList("GENRE1","GENRE2"));
		other.setAiring(false);
		other.setAirTime(LocalTime.NOON);
		other.setFirstAired(LocalDate.parse("2005-04-03"));
     	other.setLastUpdated(LocalDate.parse("2016-02-19"));
     
     	Assert.assertFalse(info.equals(other));
   }

  	@Test	
  	public void equalsAiringTest() throws Exception{
     	info.setTitle("TITLE");
     	info.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		info.setDescription("DESCRIPTION");
		info.setGuideId("GUIDEID");
		info.setTimezone("TIMEZONE");
		info.setRunningTime(60);
		info.setGenres(Arrays.asList("GENRE1"));
		info.setAiring(false);
		info.setAirTime(LocalTime.NOON);
		info.setFirstAired(LocalDate.parse("2005-04-03"));
     	info.setLastUpdated(LocalDate.parse("2016-02-19"));
     
    	GuideInfo other = new GuideInfo();
     	other.setTitle("TITLE");
     	other.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		other.setDescription("DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setTimezone("TIMEZONE");
		other.setRunningTime(60);
		other.setGenres(Arrays.asList("GENRE1"));
		other.setAiring(true);
		other.setAirTime(LocalTime.NOON);
		other.setFirstAired(LocalDate.parse("2005-04-03"));
     	other.setLastUpdated(LocalDate.parse("2016-02-19"));
     
     	Assert.assertFalse(info.equals(other));
   }

  	@Test	
  	public void equalsAirtimeTest() throws Exception{
     	info.setTitle("TITLE");
     	info.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		info.setDescription("DESCRIPTION");
		info.setGuideId("GUIDEID");
		info.setTimezone("TIMEZONE");
		info.setRunningTime(60);
		info.setGenres(Arrays.asList("GENRE1"));
		info.setAiring(false);
		info.setAirTime(LocalTime.NOON);
		info.setFirstAired(LocalDate.parse("2005-04-03"));
     	info.setLastUpdated(LocalDate.parse("2016-02-19"));
     
    	GuideInfo other = new GuideInfo();
     	other.setTitle("TITLE");
     	other.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		other.setDescription("DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setTimezone("TIMEZONE");
		other.setRunningTime(60);
		other.setGenres(Arrays.asList("GENRE1"));
		other.setAiring(false);
		other.setAirTime(LocalTime.MIDNIGHT);
		other.setFirstAired(LocalDate.parse("2005-04-03"));
     	other.setLastUpdated(LocalDate.parse("2016-02-19"));
     
     	Assert.assertFalse(info.equals(other));
   }
  
  	@Test	
  	public void equalsFirstAiredTest() throws Exception{
     	info.setTitle("TITLE");
     	info.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		info.setDescription("DESCRIPTION");
		info.setGuideId("GUIDEID");
		info.setTimezone("TIMEZONE");
		info.setRunningTime(60);
		info.setGenres(Arrays.asList("GENRE1"));
		info.setAiring(false);
		info.setAirTime(LocalTime.NOON);
		info.setFirstAired(LocalDate.parse("2005-04-03"));
     	info.setLastUpdated(LocalDate.parse("2016-02-19"));
     
    	GuideInfo other = new GuideInfo();
     	other.setTitle("TITLE");
     	other.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		other.setDescription("DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setTimezone("TIMEZONE");
		other.setRunningTime(60);
		other.setGenres(Arrays.asList("GENRE1"));
		other.setAiring(false);
		other.setAirTime(LocalTime.NOON);
		other.setFirstAired(LocalDate.ofEpochDay(0));
     	other.setLastUpdated(LocalDate.parse("2016-02-19"));
     
     	Assert.assertFalse(info.equals(other));
   }
  	@Test	
  	public void equalsLastUpdatedTest() throws Exception{
     	info.setTitle("TITLE");
     	info.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		info.setDescription("DESCRIPTION");
		info.setGuideId("GUIDEID");
		info.setTimezone("TIMEZONE");
		info.setRunningTime(60);
		info.setGenres(Arrays.asList("GENRE1"));
		info.setAiring(false);
		info.setAirTime(LocalTime.NOON);
		info.setFirstAired(LocalDate.parse("2005-04-03"));
     	info.setLastUpdated(LocalDate.parse("2016-02-19"));
     
    	GuideInfo other = new GuideInfo();
     	other.setTitle("TITLE");
     	other.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		other.setDescription("DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setTimezone("TIMEZONE");
		other.setRunningTime(60);
		other.setGenres(Arrays.asList("GENRE1"));
		other.setAiring(false);
		other.setAirTime(LocalTime.NOON);
		other.setFirstAired(LocalDate.parse("2005-04-03"));
     	other.setLastUpdated(LocalDate.ofEpochDay(0));
     
     	Assert.assertFalse(info.equals(other));
   }
  
  	@Test	
  	public void equalsTitleNullTest() throws Exception{
     	info.setTitle("TITLE");
     	info.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		info.setDescription("DESCRIPTION");
		info.setGuideId("GUIDEID");
		info.setTimezone("TIMEZONE");
		info.setRunningTime(60);
		info.setGenres(Arrays.asList("GENRE1"));
		info.setAiring(false);
		info.setAirTime(LocalTime.NOON);
		info.setFirstAired(LocalDate.parse("2005-04-03"));
     	info.setLastUpdated(LocalDate.parse("2016-02-19"));
     
    	GuideInfo other = new GuideInfo();
     	other.setTitle(null);
     	other.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		other.setDescription("DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setTimezone("TIMEZONE");
		other.setRunningTime(60);
		other.setGenres(Arrays.asList("GENRE1"));
		other.setAiring(false);
		other.setAirTime(LocalTime.NOON);
		other.setFirstAired(LocalDate.parse("2005-04-03"));
     	other.setLastUpdated(LocalDate.parse("2016-02-19"));
     
     	Assert.assertFalse(info.equals(other));
   }
  
  	@Test	
  	public void equalsAirDaysNullTest() throws Exception{
     	info.setTitle("TITLE");
     	info.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		info.setDescription("DESCRIPTION");
		info.setGuideId("GUIDEID");
		info.setTimezone("TIMEZONE");
		info.setRunningTime(60);
		info.setGenres(Arrays.asList("GENRE1"));
		info.setAiring(false);
		info.setAirTime(LocalTime.NOON);
		info.setFirstAired(LocalDate.parse("2005-04-03"));
     	info.setLastUpdated(LocalDate.parse("2016-02-19"));
     
    	GuideInfo other = new GuideInfo();
     	other.setTitle("TITLE");
     	other.setAirDays(null);
		other.setDescription("DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setTimezone("TIMEZONE");
		other.setRunningTime(60);
		other.setGenres(Arrays.asList("GENRE1"));
		other.setAiring(false);
		other.setAirTime(LocalTime.NOON);
		other.setFirstAired(LocalDate.parse("2005-04-03"));
     	other.setLastUpdated(LocalDate.parse("2016-02-19"));
     
     	Assert.assertFalse(info.equals(other));
   }
  
  	@Test	
  	public void equalsDescriptionNullTest() throws Exception{
     	info.setTitle("TITLE");
     	info.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		info.setDescription("DESCRIPTION");
		info.setGuideId("GUIDEID");
		info.setTimezone("TIMEZONE");
		info.setRunningTime(60);
		info.setGenres(Arrays.asList("GENRE1"));
		info.setAiring(false);
		info.setAirTime(LocalTime.NOON);
		info.setFirstAired(LocalDate.parse("2005-04-03"));
     	info.setLastUpdated(LocalDate.parse("2016-02-19"));
     
    	GuideInfo other = new GuideInfo();
     	other.setTitle("TITLE");
     	other.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		other.setDescription(null);
		other.setGuideId("GUIDEID");
		other.setTimezone("TIMEZONE");
		other.setRunningTime(60);
		other.setGenres(Arrays.asList("GENRE1"));
		other.setAiring(false);
		other.setAirTime(LocalTime.NOON);
		other.setFirstAired(LocalDate.parse("2005-04-03"));
     	other.setLastUpdated(LocalDate.parse("2016-02-19"));
     
     	Assert.assertFalse(info.equals(other));
   }

  	@Test	
  	public void equalsGuideIdNullTest() throws Exception{
     	info.setTitle("TITLE");
     	info.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		info.setDescription("DESCRIPTION");
		info.setGuideId("GUIDEID");
		info.setTimezone("TIMEZONE");
		info.setRunningTime(60);
		info.setGenres(Arrays.asList("GENRE1"));
		info.setAiring(false);
		info.setAirTime(LocalTime.NOON);
		info.setFirstAired(LocalDate.parse("2005-04-03"));
     	info.setLastUpdated(LocalDate.parse("2016-02-19"));
     
    	GuideInfo other = new GuideInfo();
     	other.setTitle("TITLE");
     	other.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		other.setDescription("DESCRIPTION");
		other.setGuideId(null);
		other.setTimezone("TIMEZONE");
		other.setRunningTime(60);
		other.setGenres(Arrays.asList("GENRE1"));
		other.setAiring(false);
		other.setAirTime(LocalTime.NOON);
		other.setFirstAired(LocalDate.parse("2005-04-03"));
     	other.setLastUpdated(LocalDate.parse("2016-02-19"));
     
     	Assert.assertFalse(info.equals(other));
   }

  	@Test	
  	public void equalsTimezoneNullTest() throws Exception{
     	info.setTitle("TITLE");
     	info.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		info.setDescription("DESCRIPTION");
		info.setGuideId("GUIDEID");
		info.setTimezone("TIMEZONE");
		info.setRunningTime(60);
		info.setGenres(Arrays.asList("GENRE1"));
		info.setAiring(false);
		info.setAirTime(LocalTime.NOON);
		info.setFirstAired(LocalDate.parse("2005-04-03"));
     	info.setLastUpdated(LocalDate.parse("2016-02-19"));
     
    	GuideInfo other = new GuideInfo();
     	other.setTitle("TITLE");
     	other.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		other.setDescription("DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setTimezone(null);
		other.setRunningTime(60);
		other.setGenres(Arrays.asList("GENRE1"));
		other.setAiring(false);
		other.setAirTime(LocalTime.NOON);
		other.setFirstAired(LocalDate.parse("2005-04-03"));
     	other.setLastUpdated(LocalDate.parse("2016-02-19"));
     
     	Assert.assertFalse(info.equals(other));
   }

  	@Test	
  	public void equalsGenresNullTest() throws Exception{
     	info.setTitle("TITLE");
     	info.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		info.setDescription("DESCRIPTION");
		info.setGuideId("GUIDEID");
		info.setTimezone("TIMEZONE");
		info.setRunningTime(60);
		info.setGenres(Arrays.asList("GENRE1"));
		info.setAiring(false);
		info.setAirTime(LocalTime.NOON);
		info.setFirstAired(LocalDate.parse("2005-04-03"));
     	info.setLastUpdated(LocalDate.parse("2016-02-19"));
     
    	GuideInfo other = new GuideInfo();
     	other.setTitle("TITLE");
     	other.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		other.setDescription("DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setTimezone("TIMEZONE");
		other.setRunningTime(60);
		other.setGenres(null);
		other.setAiring(false);
		other.setAirTime(LocalTime.NOON);
		other.setFirstAired(LocalDate.parse("2005-04-03"));
     	other.setLastUpdated(LocalDate.parse("2016-02-19"));
     
     	Assert.assertFalse(info.equals(other));
   }

  	@Test	
  	public void equalsFirstAiredNullTest() throws Exception{
     	info.setTitle("TITLE");
     	info.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		info.setDescription("DESCRIPTION");
		info.setGuideId("GUIDEID");
		info.setTimezone("TIMEZONE");
		info.setRunningTime(60);
		info.setGenres(Arrays.asList("GENRE1"));
		info.setAiring(false);
		info.setAirTime(LocalTime.NOON);
		info.setFirstAired(LocalDate.parse("2005-04-03"));
     	info.setLastUpdated(LocalDate.parse("2016-02-19"));
     
    	GuideInfo other = new GuideInfo();
     	other.setTitle("TITLE");
     	other.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		other.setDescription("DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setTimezone("TIMEZONE");
		other.setRunningTime(60);
		other.setGenres(Arrays.asList("GENRE1"));
		other.setAiring(false);
		other.setAirTime(LocalTime.NOON);
		other.setFirstAired(null);
     	other.setLastUpdated(LocalDate.parse("2016-02-19"));
     
     	Assert.assertFalse(info.equals(other));
   }
  	@Test	
  	public void equalsLastUpdatedNullTest() throws Exception{
     	info.setTitle("TITLE");
     	info.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		info.setDescription("DESCRIPTION");
		info.setGuideId("GUIDEID");
		info.setTimezone("TIMEZONE");
		info.setRunningTime(60);
		info.setGenres(Arrays.asList("GENRE1"));
		info.setAiring(false);
		info.setAirTime(LocalTime.NOON);
		info.setFirstAired(LocalDate.parse("2005-04-03"));
     	info.setLastUpdated(LocalDate.parse("2016-02-19"));
     
    	GuideInfo other = new GuideInfo();
     	other.setTitle("TITLE");
     	other.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		other.setDescription("DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setTimezone("TIMEZONE");
		other.setRunningTime(60);
		other.setGenres(Arrays.asList("GENRE1"));
		other.setAiring(false);
		other.setAirTime(LocalTime.NOON);
		other.setFirstAired(LocalDate.parse("2005-04-03"));
     	other.setLastUpdated(null);
     
     	Assert.assertFalse(info.equals(other));
   }
}
