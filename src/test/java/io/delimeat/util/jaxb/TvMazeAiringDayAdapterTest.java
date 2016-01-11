package io.delimeat.util.jaxb;

import io.delimeat.core.guide.AiringDay;
import io.delimeat.util.jaxb.TvMazeAiringDayAdapter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TvMazeAiringDayAdapterTest {

	private TvMazeAiringDayAdapter adapter;

	@Before
	public void setUp() {
		adapter = new TvMazeAiringDayAdapter();
	}

	@Test
	public void marshalTest() throws Exception {
		Assert.assertNull(adapter.marshal(AiringDay.MONDAY));
	}

	@Test
	public void AirDayMondayTest() throws Exception {
		Assert.assertEquals(AiringDay.MONDAY, adapter.unmarshal("Monday"));
	}

	@Test
	public void AirDayTuesdayTest() throws Exception {
		Assert.assertEquals(AiringDay.TUESDAY, adapter.unmarshal("Tuesday"));
	}

	@Test
	public void AirDayWednesdayTest() throws Exception {
		Assert.assertEquals(AiringDay.WEDNESDAY, adapter.unmarshal("Wednesday"));
	}

	@Test
	public void AirDayThursdayTest() throws Exception {
		Assert.assertEquals(AiringDay.THURSDAY, adapter.unmarshal("Thursday"));
	}

	@Test
	public void AirDayFridayTest() throws Exception {
		Assert.assertEquals(AiringDay.FRIDAY, adapter.unmarshal("Friday"));
	}

	@Test
	public void AirDaySaturdayTest() throws Exception {
		Assert.assertEquals(AiringDay.SATURDAY, adapter.unmarshal("Saturday"));
	}

	@Test
	public void AirDaySundayTest() throws Exception {
		Assert.assertEquals(AiringDay.SUNDAY, adapter.unmarshal("Sunday"));
	}

	@Test
	public void AirDayUnknownTest() throws Exception {
		Assert.assertNull(adapter.unmarshal("giberish"));
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
