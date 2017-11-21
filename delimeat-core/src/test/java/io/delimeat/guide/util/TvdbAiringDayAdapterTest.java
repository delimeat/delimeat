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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.delimeat.guide.entity.AiringDay;
import io.delimeat.guide.util.TvdbAiringDayAdapter;

public class TvdbAiringDayAdapterTest {

	private TvdbAiringDayAdapter adapter;

	@BeforeEach
	public void setUp() {
		adapter = new TvdbAiringDayAdapter();
	}

	@Test
	public void marshalTest() throws Exception {
		List<AiringDay> list = new ArrayList<AiringDay>();
		list.add(AiringDay.MONDAY);
		Assertions.assertNull(adapter.marshal(list));
	}

	@Test
	public void AirDayMondayTest() throws Exception {
		Assertions.assertEquals(1, adapter.unmarshal("Monday").size());
		Assertions.assertEquals(AiringDay.MONDAY, adapter.unmarshal("Monday").get(0));
	}

	@Test
	public void AirDayTuesdayTest() throws Exception {
		Assertions.assertEquals(1, adapter.unmarshal("Tuesday").size());
		Assertions.assertEquals(AiringDay.TUESDAY, adapter.unmarshal("Tuesday").get(0));
	}

	@Test
	public void AirDayWednesdayTest() throws Exception {
		Assertions.assertEquals(1, adapter.unmarshal("Wednesday").size());
		Assertions.assertEquals(AiringDay.WEDNESDAY, adapter.unmarshal("Wednesday").get(0));
	}

	@Test
	public void AirDayThursdayTest() throws Exception {
		Assertions.assertEquals(1, adapter.unmarshal("Thursday").size());
		Assertions.assertEquals(AiringDay.THURSDAY, adapter.unmarshal("Thursday").get(0));
	}

	@Test
	public void AirDayFridayTest() throws Exception {
		Assertions.assertEquals(1, adapter.unmarshal("Friday").size());
		Assertions.assertEquals(AiringDay.FRIDAY, adapter.unmarshal("Friday").get(0));
	}

	@Test
	public void AirDaySaturdayTest() throws Exception {
		Assertions.assertEquals(1, adapter.unmarshal("Saturday").size());
		Assertions.assertEquals(AiringDay.SATURDAY, adapter.unmarshal("Saturday").get(0));
	}

	@Test
	public void AirDaySundayTest() throws Exception {
		Assertions.assertEquals(1, adapter.unmarshal("Sunday").size());
		Assertions.assertEquals(AiringDay.SUNDAY, adapter.unmarshal("Sunday").get(0));
	}

	@Test
	public void AirDayUnknownTest() throws Exception {
		Assertions.assertEquals(0, adapter.unmarshal("giberish").size());
	}

	@Test
	public void AirDayDailyTest() throws Exception {
		Assertions.assertEquals(5, adapter.unmarshal("daily").size());
		Assertions.assertEquals(AiringDay.MONDAY, adapter.unmarshal("daily").get(0));
		Assertions.assertEquals(AiringDay.TUESDAY, adapter.unmarshal("daily").get(1));
		Assertions.assertEquals(AiringDay.WEDNESDAY, adapter.unmarshal("daily").get(2));
		Assertions.assertEquals(AiringDay.THURSDAY, adapter.unmarshal("daily").get(3));
		Assertions.assertEquals(AiringDay.FRIDAY, adapter.unmarshal("daily").get(4));
	}

	@Test
	public void unmarshalNullTest() throws Exception {
		Assertions.assertNull(adapter.unmarshal(null));
	}

	@Test
	public void unmarshalEmptyStringTest() throws Exception {
		Assertions.assertNull(adapter.unmarshal(""));
	}
}
