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
package io.delimeat.util.jaxb;

import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.util.jaxb.LocalTimeAdapter;

public class LocalTimeAdapterTest {
	
	private LocalTimeAdapter adapter;
	
	@Before
	public void setUp() throws Exception {
		adapter = new LocalTimeAdapter();
	}

	@Test
	public void marshalPMTest() throws Exception {
		Assert.assertEquals("12:45 PM", adapter.marshal(LocalTime.parse("12:45:00")));
	}
	
	@Test
	public void marshalAMTest() throws Exception {
		Assert.assertEquals("08:45 AM", adapter.marshal(LocalTime.parse("08:45:00")));
	}
  
	@Test
	public void marshalNullTest() throws Exception {
		Assert.assertNull( adapter.marshal(null));
	}

	@Test
	public void unmarshalTest() throws Exception {
		Assert.assertEquals(LocalTime.parse("12:45"), adapter.unmarshal("12:45 PM"));
	}
	
	@Test
	public void airTime12HourMinutesTest() throws Exception {
		Assert.assertEquals(LocalTime.parse("20:00"), adapter.unmarshal("8:00 PM"));
	}
	
	@Test
	public void airTime12HourMinutes2DigitsTest() throws Exception {
		Assert.assertEquals(LocalTime.parse("20:00"), adapter.unmarshal("08:00 PM"));
	}

	@Test
	public void airTime12HourNoMinutesPMTest() throws Exception {
		Assert.assertEquals(LocalTime.parse("20:00"), adapter.unmarshal("8 PM"));
	}
	@Test
	public void airTime12HourNoMinutesAMTest() throws Exception {
		Assert.assertEquals(LocalTime.parse("11:00"), adapter.unmarshal("11 am"));
	}

	@Test
	public void airTime24HourTest() throws Exception {
		Assert.assertEquals(LocalTime.parse("20:00"), adapter.unmarshal("20:00"));
	}
	
	@Test
	public void airTime24Hour1DigitTest() throws Exception {
		Assert.assertEquals(LocalTime.parse("08:00"), adapter.unmarshal("08:00"));
	}

	@Test
	public void airTimeUnknownTest() throws Exception {
		Assert.assertEquals(LocalTime.MIDNIGHT, adapter.unmarshal("20:00 PM"));
	}

	@Test
	public void unmarshalEmptyStringTest() throws Exception {
		Assert.assertNull(adapter.unmarshal(""));
	}
  
	@Test
	public void unmarshalNullTest() throws Exception {
		Assert.assertNull( adapter.unmarshal(null));
	}
}
