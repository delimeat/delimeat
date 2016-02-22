package io.delimeat.core.guide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GuideInfoTest {

	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	static{
		SDF.setTimeZone(TimeZone.getTimeZone("UTC"));
	}
	
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
		Assert.assertEquals(0, info.getAirTime());
		info.setAirTime(10000);
		Assert.assertEquals(10000, info.getAirTime());
	}
	
	@Test
	public void firstAiredTest() throws ParseException {

		Assert.assertEquals(null, info.getFirstAired());

		info.setFirstAired(SDF.parse("2005-04-03"));
		Assert.assertEquals("2005-04-03", SDF.format(info.getFirstAired()));
	}

	@Test
	public void lasUpdatedTest() throws ParseException {
		Assert.assertEquals(null, info.getLastUpdated());
		info.setLastUpdated(SDF.parse("2005-04-03"));
		Assert.assertEquals("2005-04-03", SDF.format(info.getLastUpdated()));
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
     	info.setLastUpdated(SDF.parse("2016-02-19"));
     	Assert.assertEquals(-1974036005, info.hashCode());
   }
  
  	@Test
  	public void toStringTest() throws ParseException{
     	info.setTitle("TITLE");
     	info.setAirDays(Arrays.asList(AiringDay.FRIDAY));
		info.setDescription("DESCRIPTION");
		info.setGuideId("GUIDEID");
		info.setTimezone("TIMEZONE");
		info.setRunningTime(60);
		info.setGenres(Arrays.asList("GENRE1"));
		info.setAiring(false);
		info.setAirTime(10000);
		info.setFirstAired(SDF.parse("2005-04-03"));
     	info.setLastUpdated(SDF.parse("2016-02-19"));
     	Assert.assertEquals("GuideInfo{title=TITLE, guideId=GUIDEID, airing=false, airDays=[FRIDAY], airTime=10000, genres=[GENRE1], runningTime=60, timezone=TIMEZONE, description=DESCRIPTION, lastUpdated=1455840000000}", info.toString());
   }
}
