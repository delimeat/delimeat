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

import java.text.ParseException;
import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.delimeat.guide.entity.GuideSearchResult;

public class GuideSearchResultTest {

	private GuideSearchResult result;

	@BeforeEach
	public void setUp() {
		result = new GuideSearchResult();
	}

	@Test
	public void descriptionTest() {
		Assertions.assertEquals(null, result.getDescription());
		result.setDescription("DESCRIPTION");
		Assertions.assertEquals("DESCRIPTION", result.getDescription());
	}

	@Test
	public void GuideIdsTest() {
		Assertions.assertNull(result.getGuideId());
		result.setGuideId("GUIDEID");
		Assertions.assertEquals("GUIDEID", result.getGuideId());
	}

	@Test
	public void firstAiredTest() throws ParseException {
		Assertions.assertEquals(null, result.getFirstAired());
		result.setFirstAired(LocalDate.parse("2005-04-03"));
		Assertions.assertEquals(LocalDate.parse("2005-04-03"), result.getFirstAired());
	}

	@Test
	public void titleTest() {
		Assertions.assertEquals(null, result.getTitle());
		result.setTitle("TITLE");
		Assertions.assertEquals("TITLE", result.getTitle());
	}

	@Test
	public void hashCodeTest() throws ParseException {
		result.setTitle("TITLE");
		result.setFirstAired(LocalDate.parse("2016-02-19"));
		Assertions.assertEquals(-246322804, result.hashCode());
	}

	@Test
	public void toStringTest() throws ParseException {
		Assertions.assertEquals("GuideSearchResult []", result.toString());
	}

	@Test
	public void equalsNullTest() {
		Assertions.assertFalse(result.equals(null));
	}

	@Test
	public void equalsSelfTest() {
		Assertions.assertTrue(result.equals(result));
	}

	@Test
	public void equalsOtherObjectTest() {
		Assertions.assertFalse(result.equals(new Object()));
	}

	@Test
	public void equalsTest() {
		result.setTitle("TITLE");
		result.setDescription("DESCRIPTION");
		result.setGuideId("GUIDEID");
		result.setFirstAired(LocalDate.ofEpochDay(0));

		GuideSearchResult other = new GuideSearchResult();
		other.setTitle("TITLE");
		other.setDescription("DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setFirstAired(LocalDate.ofEpochDay(0));

		Assertions.assertTrue(result.equals(other));
	}

	@Test
	public void equalsTitleTest() {
		result.setTitle("TITLE");
		result.setDescription("DESCRIPTION");
		result.setGuideId("GUIDEID");
		result.setFirstAired(LocalDate.ofEpochDay(0));

		GuideSearchResult other = new GuideSearchResult();
		other.setTitle("OTHER_TITLE");
		other.setDescription("DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setFirstAired(LocalDate.ofEpochDay(0));

		Assertions.assertFalse(result.equals(other));
	}

	@Test
	public void equalsDescriptionTest() {
		result.setTitle("TITLE");
		result.setDescription("DESCRIPTION");
		result.setGuideId("GUIDEID");
		result.setFirstAired(LocalDate.ofEpochDay(0));

		GuideSearchResult other = new GuideSearchResult();
		other.setTitle("TITLE");
		other.setDescription("OTHER_DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setFirstAired(LocalDate.ofEpochDay(0));

		Assertions.assertFalse(result.equals(other));
	}

	@Test
	public void equalsGuideIdTest() {
		result.setTitle("TITLE");
		result.setDescription("DESCRIPTION");
		result.setGuideId("GUIDEID");
		result.setFirstAired(LocalDate.ofEpochDay(0));

		GuideSearchResult other = new GuideSearchResult();
		other.setTitle("TITLE");
		other.setDescription("DESCRIPTION");
		other.setGuideId("OTHER_GUIDEID");
		other.setFirstAired(LocalDate.ofEpochDay(0));

		Assertions.assertFalse(result.equals(other));
	}

	@Test
	public void equalsFirstAiredTest() {
		result.setTitle("TITLE");
		result.setDescription("DESCRIPTION");
		result.setGuideId("GUIDEID");
		result.setFirstAired(LocalDate.ofEpochDay(0));

		GuideSearchResult other = new GuideSearchResult();
		other.setTitle("TITLE");
		other.setDescription("DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setFirstAired(LocalDate.ofEpochDay(1));

		Assertions.assertFalse(result.equals(other));
	}

	@Test
	public void equalsTitleNullTest() {
		result.setTitle(null);
		result.setDescription("DESCRIPTION");
		result.setGuideId("GUIDEID");
		result.setFirstAired(LocalDate.ofEpochDay(0));

		GuideSearchResult other = new GuideSearchResult();
		other.setTitle("TITLE");
		other.setDescription("DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setFirstAired(LocalDate.ofEpochDay(0));

		Assertions.assertFalse(result.equals(other));
	}

	@Test
	public void equalsDescriptionNullTest() {
		result.setTitle("TITLE");
		result.setDescription(null);
		result.setGuideId("GUIDEID");
		result.setFirstAired(LocalDate.ofEpochDay(0));

		GuideSearchResult other = new GuideSearchResult();
		other.setTitle("TITLE");
		other.setDescription("DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setFirstAired(LocalDate.ofEpochDay(0));

		Assertions.assertFalse(result.equals(other));
	}

	@Test
	public void equalsGuideIdNullTest() {
		result.setTitle("TITLE");
		result.setDescription("DESCRIPTION");
		result.setGuideId(null);
		result.setFirstAired(LocalDate.ofEpochDay(0));

		GuideSearchResult other = new GuideSearchResult();
		other.setTitle("TITLE");
		other.setDescription("DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setFirstAired(LocalDate.ofEpochDay(0));

		Assertions.assertFalse(result.equals(other));
	}

	@Test
	public void equalsFirstAiredNullTest() {
		result.setTitle("TITLE");
		result.setDescription("DESCRIPTION");
		result.setGuideId("GUIDEID");
		result.setFirstAired(null);

		GuideSearchResult other = new GuideSearchResult();
		other.setTitle("TITLE");
		other.setDescription("DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setFirstAired(LocalDate.ofEpochDay(0));

		Assertions.assertFalse(result.equals(other));
	}

}
