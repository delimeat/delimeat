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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.delimeat.guide.entity.AiringDay;
import io.delimeat.guide.util.TvMazeAiringDayAdapter;

public class TvMazeAiringDayAdapterTest {

	private TvMazeAiringDayAdapter adapter;

	@BeforeEach
	public void setUp() {
		adapter = new TvMazeAiringDayAdapter();
	}

	@Test
	public void marshalTest() throws Exception {
		Assertions.assertNull(adapter.marshal(AiringDay.MONDAY));
	}

	@Test
	public void AirDayMondayTest() throws Exception {
		Assertions.assertEquals(AiringDay.MONDAY, adapter.unmarshal("Monday"));
	}

	@Test
	public void AirDayTuesdayTest() throws Exception {
		Assertions.assertEquals(AiringDay.TUESDAY, adapter.unmarshal("Tuesday"));
	}

	@Test
	public void AirDayWednesdayTest() throws Exception {
		Assertions.assertEquals(AiringDay.WEDNESDAY, adapter.unmarshal("Wednesday"));
	}

	@Test
	public void AirDayThursdayTest() throws Exception {
		Assertions.assertEquals(AiringDay.THURSDAY, adapter.unmarshal("Thursday"));
	}

	@Test
	public void AirDayFridayTest() throws Exception {
		Assertions.assertEquals(AiringDay.FRIDAY, adapter.unmarshal("Friday"));
	}

	@Test
	public void AirDaySaturdayTest() throws Exception {
		Assertions.assertEquals(AiringDay.SATURDAY, adapter.unmarshal("Saturday"));
	}

	@Test
	public void AirDaySundayTest() throws Exception {
		Assertions.assertEquals(AiringDay.SUNDAY, adapter.unmarshal("Sunday"));
	}

	@Test
	public void AirDayUnknownTest() throws Exception {
		Assertions.assertNull(adapter.unmarshal("giberish"));
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
