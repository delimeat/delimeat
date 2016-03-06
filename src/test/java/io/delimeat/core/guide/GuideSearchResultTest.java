package io.delimeat.core.guide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class GuideSearchResultTest {

	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	static{
		SDF.setTimeZone(TimeZone.getTimeZone("UTC"));
	}
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
		result.setFirstAired(SDF.parse("2005-04-03"));
		Assert.assertEquals("2005-04-03", SDF.format(result.getFirstAired()));
	}

	@Test
	public void titleTest() {
		Assert.assertEquals(null, result.getTitle());
		result.setTitle("TITLE");
		Assert.assertEquals("TITLE", result.getTitle());
	}

	@Test
	public void lasUpdatedTest() throws ParseException {
		Assert.assertEquals(null, result.getLastUpdated());
		result.setLastUpdated(SDF.parse("2005-04-03"));
		Assert.assertEquals("2005-04-03", SDF.format(result.getLastUpdated()));
	}
  
	@Test
	public void compareTitleMatchTest() {
		result.setTitle("ABC");
		GuideSearchResult result2 = new GuideSearchResult();
		result2.setTitle("ABC");
     
		Assert.assertEquals(0, result.compareTo(result2));
	}

	@Test
	public void compareNullTitlesTest() {
		Assert.assertNull(result.getTitle());
		GuideSearchResult result2 = new GuideSearchResult();
		Assert.assertNull(result2.getTitle());

		Assert.assertEquals(0, result.compareTo(result2));
	}

	@Test
	public void compareTitleOtherNullTest() {
		result.setTitle("ABC");
		Assert.assertNotNull(result.getTitle());
		GuideSearchResult result2 = new GuideSearchResult();
		Assert.assertNull(result2.getTitle());

		Assert.assertEquals(1, result.compareTo(result2));
	}

	@Test
	public void compareTitleNullOtherNotNullTest() {
		Assert.assertNull(result.getTitle());
		GuideSearchResult result2 = new GuideSearchResult();
		result2.setTitle("ABC");
		Assert.assertNotNull(result2.getTitle());

		Assert.assertEquals(-1, result.compareTo(result2));
	}

	@Test
	public void compareTitleBeforeTest() {
		result.setTitle("ABC");
		GuideSearchResult result2 = new GuideSearchResult();
		result2.setTitle("BCD");

		Assert.assertEquals(-1, result.compareTo(result2));
	}

	@Test
	public void compareTitleAfterTest() {
		result.setTitle("BCD");
		GuideSearchResult result2 = new GuideSearchResult();
		result2.setTitle("ABC");

		Assert.assertEquals(1, result.compareTo(result2));
	}
  
	@Test
  	public void hashCodeTest() throws ParseException{
     	result.setTitle("TITLE");
     	result.setLastUpdated(SDF.parse("2016-02-19"));
     	Assert.assertEquals(-1974036005, result.hashCode());
   }
  
	@Ignore
  	@Test
  	public void toStringTest() throws ParseException{
     	result.setTitle("TITLE");
		result.setDescription("DESCRIPTION");
		result.setGuideId("GUIDEID");
		result.setFirstAired(SDF.parse("2005-04-03"));
     	result.setLastUpdated(SDF.parse("2016-02-19"));
     	Assert.assertEquals("GuideSearchResult{title=TITLE, firstAired=1112486400000, guideId=GUIDEID, description=DESCRIPTION, lastUpdated=1455840000000}", result.toString());
   }
}
