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
package io.delimeat.guide.util;

import java.util.ArrayList;
import java.util.List;

import io.delimeat.guide.entity.AiringDay;
import io.delimeat.guide.util.TvdbAiringDayAdapter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TvdbAiringDayAdapterTest {

	private TvdbAiringDayAdapter adapter;

	@Before
	public void setUp() {
		adapter = new TvdbAiringDayAdapter();
	}

	@Test
	public void marshalTest() throws Exception {
		List<AiringDay> list = new ArrayList<AiringDay>();
		list.add(AiringDay.MONDAY);
		Assert.assertNull(adapter.marshal(list));
	}

	@Test
	public void AirDayMondayTest() throws Exception {
		Assert.assertEquals(1, adapter.unmarshal("Monday").size());
		Assert.assertEquals(AiringDay.MONDAY, adapter.unmarshal("Monday").get(0));
	}

	@Test
	public void AirDayTuesdayTest() throws Exception {
		Assert.assertEquals(1, adapter.unmarshal("Tuesday").size());
		Assert.assertEquals(AiringDay.TUESDAY, adapter.unmarshal("Tuesday").get(0));
	}

	@Test
	public void AirDayWednesdayTest() throws Exception {
		Assert.assertEquals(1, adapter.unmarshal("Wednesday").size());
		Assert.assertEquals(AiringDay.WEDNESDAY, adapter.unmarshal("Wednesday").get(0));
	}

	@Test
	public void AirDayThursdayTest() throws Exception {
		Assert.assertEquals(1, adapter.unmarshal("Thursday").size());
		Assert.assertEquals(AiringDay.THURSDAY, adapter.unmarshal("Thursday").get(0));
	}

	@Test
	public void AirDayFridayTest() throws Exception {
		Assert.assertEquals(1, adapter.unmarshal("Friday").size());
		Assert.assertEquals(AiringDay.FRIDAY, adapter.unmarshal("Friday").get(0));
	}

	@Test
	public void AirDaySaturdayTest() throws Exception {
		Assert.assertEquals(1, adapter.unmarshal("Saturday").size());
		Assert.assertEquals(AiringDay.SATURDAY, adapter.unmarshal("Saturday").get(0));
	}

	@Test
	public void AirDaySundayTest() throws Exception {
		Assert.assertEquals(1, adapter.unmarshal("Sunday").size());
		Assert.assertEquals(AiringDay.SUNDAY, adapter.unmarshal("Sunday").get(0));
	}

	@Test
	public void AirDayUnknownTest() throws Exception {
		Assert.assertEquals(0, adapter.unmarshal("giberish").size());
	}

	@Test
	public void AirDayDailyTest() throws Exception {
		Assert.assertEquals(5, adapter.unmarshal("daily").size());
		Assert.assertEquals(AiringDay.MONDAY, adapter.unmarshal("daily").get(0));
		Assert.assertEquals(AiringDay.TUESDAY, adapter.unmarshal("daily").get(1));
		Assert.assertEquals(AiringDay.WEDNESDAY, adapter.unmarshal("daily").get(2));
		Assert.assertEquals(AiringDay.THURSDAY, adapter.unmarshal("daily").get(3));
		Assert.assertEquals(AiringDay.FRIDAY, adapter.unmarshal("daily").get(4));
	}

	@Test
	public void unmarshalNullTest() throws Exception {
		Assert.assertNull(adapter.unmarshal(null));
	}

	@Test
	public void unmarshalEmptyStringTest() throws Exception {
		Assert.assertNull(adapter.unmarshal(""));
	}
}
