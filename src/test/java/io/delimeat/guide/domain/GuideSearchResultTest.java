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

import io.delimeat.guide.domain.GuideSearchResult;

public class GuideSearchResultTest {

	private GuideSearchResult result;

	@Before
	public void setUp() {
		result = new GuideSearchResult();
	}

	@Test
	public void descriptionTest() {
		Assert.assertEquals(null, result.getDescription());
		result.setDescription("DESCRIPTION");
		Assert.assertEquals("DESCRIPTION", result.getDescription());
	}

	@Test
	public void GuideIdsTest() {
		Assert.assertNull(result.getGuideId());
		result.setGuideId("GUIDEID");
		Assert.assertEquals("GUIDEID", result.getGuideId());
	}

	@Test
	public void firstAiredTest() throws ParseException {
		Assert.assertEquals(null, result.getFirstAired());
		result.setFirstAired(LocalDate.parse("2005-04-03"));
		Assert.assertEquals(LocalDate.parse("2005-04-03"), result.getFirstAired());
	}

	@Test
	public void titleTest() {
		Assert.assertEquals(null, result.getTitle());
		result.setTitle("TITLE");
		Assert.assertEquals("TITLE", result.getTitle());
	}
  
	@Test
  	public void hashCodeTest() throws ParseException{
     	result.setTitle("TITLE");
     	result.setFirstAired(LocalDate.parse("2016-02-19"));
     	Assert.assertEquals(1588636078, result.hashCode());
   }
  
  	@Test
  	public void toStringTest() throws ParseException{
     	Assert.assertEquals("GuideSearchResult(description=null, firstAired=null, guideId=null, title=null)", result.toString());
   }
  
  	@Test
  	public void equalsNullTest(){
   	Assert.assertFalse(result.equals(null));
   }
  
  	@Test
  	public void equalsSelfTest(){
     	Assert.assertTrue(result.equals(result));
   }
  
  	@Test
  	public void equalsOtherObjectTest(){
     	Assert.assertFalse(result.equals(new Object()));
   }
  
  	@Test
  	public void equalsTest(){
     	result.setTitle("TITLE");
		result.setDescription("DESCRIPTION");
		result.setGuideId("GUIDEID");
		result.setFirstAired(LocalDate.ofEpochDay(0));
     
     	GuideSearchResult other = new GuideSearchResult();
     	other.setTitle("TITLE");
		other.setDescription("DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setFirstAired(LocalDate.ofEpochDay(0));
     
     	Assert.assertTrue(result.equals(other));
   }
  
  	@Test
  	public void equalsTitleTest(){
     	result.setTitle("TITLE");
		result.setDescription("DESCRIPTION");
		result.setGuideId("GUIDEID");
		result.setFirstAired(LocalDate.ofEpochDay(0));
     
     	GuideSearchResult other = new GuideSearchResult();
     	other.setTitle("OTHER_TITLE");
		other.setDescription("DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setFirstAired(LocalDate.ofEpochDay(0));
     
     	Assert.assertFalse(result.equals(other));
   }
  
  	@Test
  	public void equalsDescriptionTest(){
     	result.setTitle("TITLE");
		result.setDescription("DESCRIPTION");
		result.setGuideId("GUIDEID");
		result.setFirstAired(LocalDate.ofEpochDay(0));
     
     	GuideSearchResult other = new GuideSearchResult();
     	other.setTitle("TITLE");
		other.setDescription("OTHER_DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setFirstAired(LocalDate.ofEpochDay(0));
     
     	Assert.assertFalse(result.equals(other));
   }
  
  	@Test
  	public void equalsGuideIdTest(){
     	result.setTitle("TITLE");
		result.setDescription("DESCRIPTION");
		result.setGuideId("GUIDEID");
		result.setFirstAired(LocalDate.ofEpochDay(0));
     
     	GuideSearchResult other = new GuideSearchResult();
     	other.setTitle("TITLE");
		other.setDescription("DESCRIPTION");
		other.setGuideId("OTHER_GUIDEID");
		other.setFirstAired(LocalDate.ofEpochDay(0));
     
     	Assert.assertFalse(result.equals(other));
   }
  
  	@Test
  	public void equalsFirstAiredTest(){
     	result.setTitle("TITLE");
		result.setDescription("DESCRIPTION");
		result.setGuideId("GUIDEID");
		result.setFirstAired(LocalDate.ofEpochDay(0));
     
     	GuideSearchResult other = new GuideSearchResult();
     	other.setTitle("TITLE");
		other.setDescription("DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setFirstAired(LocalDate.ofEpochDay(1));
     
     	Assert.assertFalse(result.equals(other));
   }

  	@Test
  	public void equalsTitleNullTest(){
     	result.setTitle("TITLE");
		result.setDescription("DESCRIPTION");
		result.setGuideId("GUIDEID");
		result.setFirstAired(LocalDate.ofEpochDay(0));
     
     	GuideSearchResult other = new GuideSearchResult();
     	other.setTitle(null);
		other.setDescription("DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setFirstAired(LocalDate.ofEpochDay(0));
     
     	Assert.assertFalse(result.equals(other));
   }
  
  	@Test
  	public void equalsDescriptionNullTest(){
     	result.setTitle("TITLE");
		result.setDescription("DESCRIPTION");
		result.setGuideId("GUIDEID");
		result.setFirstAired(LocalDate.ofEpochDay(0));
     
     	GuideSearchResult other = new GuideSearchResult();
     	other.setTitle("TITLE");
		other.setDescription(null);
		other.setGuideId("GUIDEID");
		other.setFirstAired(LocalDate.ofEpochDay(0));
     
     	Assert.assertFalse(result.equals(other));
   }
  
  	@Test
  	public void equalsGuideIdNullTest(){
     	result.setTitle("TITLE");
		result.setDescription("DESCRIPTION");
		result.setGuideId("GUIDEID");
		result.setFirstAired(LocalDate.ofEpochDay(0));
     
     	GuideSearchResult other = new GuideSearchResult();
     	other.setTitle("TITLE");
		other.setDescription("DESCRIPTION");
		other.setGuideId(null);
		other.setFirstAired(LocalDate.ofEpochDay(0));
     
     	Assert.assertFalse(result.equals(other));
   }
  
  	@Test
  	public void equalsFirstAiredNullTest(){
     	result.setTitle("TITLE");
		result.setDescription("DESCRIPTION");
		result.setGuideId("GUIDEID");
		result.setFirstAired(LocalDate.ofEpochDay(0));
     
     	GuideSearchResult other = new GuideSearchResult();
     	other.setTitle("TITLE");
		other.setDescription("DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setFirstAired(null);
     
     	Assert.assertFalse(result.equals(other));
   }

}
