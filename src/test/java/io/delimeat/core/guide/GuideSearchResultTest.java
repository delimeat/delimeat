package io.delimeat.core.guide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Before;
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
  
  	@Test
  	public void toStringTest() throws ParseException{
     	result.setTitle("TITLE");
		result.setDescription("DESCRIPTION");
		result.setGuideId("GUIDEID");
		result.setFirstAired(SDF.parse("2005-04-03"));
     	result.setLastUpdated(SDF.parse("2016-02-19"));
     	Assert.assertEquals("GuideSearchResult{title=TITLE, firstAired=Sun Apr 03 00:00:00 UTC 2005, guideId=GUIDEID, description=DESCRIPTION, lastUpdated=Fri Feb 19 00:00:00 UTC 2016}", result.toString());
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
		result.setFirstAired(new Date(0));
     	result.setLastUpdated(new Date(0));
     
     	GuideSearchResult other = new GuideSearchResult();
     	other.setTitle("TITLE");
		other.setDescription("DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setFirstAired(new Date(0));
     	other.setLastUpdated(new Date(0));
     
     	Assert.assertTrue(result.equals(other));
   }
  
  	@Test
  	public void equalsTitleTest(){
     	result.setTitle("TITLE");
		result.setDescription("DESCRIPTION");
		result.setGuideId("GUIDEID");
		result.setFirstAired(new Date(0));
     	result.setLastUpdated(new Date(0));
     
     	GuideSearchResult other = new GuideSearchResult();
     	other.setTitle("OTHER_TITLE");
		other.setDescription("DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setFirstAired(new Date(0));
     	other.setLastUpdated(new Date(0));
     
     	Assert.assertFalse(result.equals(other));
   }
  
  	@Test
  	public void equalsDescriptionTest(){
     	result.setTitle("TITLE");
		result.setDescription("DESCRIPTION");
		result.setGuideId("GUIDEID");
		result.setFirstAired(new Date(0));
     	result.setLastUpdated(new Date(0));
     
     	GuideSearchResult other = new GuideSearchResult();
     	other.setTitle("TITLE");
		other.setDescription("OTHER_DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setFirstAired(new Date(0));
     	other.setLastUpdated(new Date(0));
     
     	Assert.assertFalse(result.equals(other));
   }
  
  	@Test
  	public void equalsGuideIdTest(){
     	result.setTitle("TITLE");
		result.setDescription("DESCRIPTION");
		result.setGuideId("GUIDEID");
		result.setFirstAired(new Date(0));
     	result.setLastUpdated(new Date(0));
     
     	GuideSearchResult other = new GuideSearchResult();
     	other.setTitle("TITLE");
		other.setDescription("DESCRIPTION");
		other.setGuideId("OTHER_GUIDEID");
		other.setFirstAired(new Date(0));
     	other.setLastUpdated(new Date(0));
     
     	Assert.assertFalse(result.equals(other));
   }
  
  	@Test
  	public void equalsFirstAiredTest(){
     	result.setTitle("TITLE");
		result.setDescription("DESCRIPTION");
		result.setGuideId("GUIDEID");
		result.setFirstAired(new Date(0));
     	result.setLastUpdated(new Date(0));
     
     	GuideSearchResult other = new GuideSearchResult();
     	other.setTitle("TITLE");
		other.setDescription("DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setFirstAired(new Date(1));
     	other.setLastUpdated(new Date(0));
     
     	Assert.assertFalse(result.equals(other));
   }
  
  	@Test
  	public void equalsLastUpdatedTest(){
     	result.setTitle("TITLE");
		result.setDescription("DESCRIPTION");
		result.setGuideId("GUIDEID");
		result.setFirstAired(new Date(0));
     	result.setLastUpdated(new Date(0));
     
     	GuideSearchResult other = new GuideSearchResult();
     	other.setTitle("TITLE");
		other.setDescription("DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setFirstAired(new Date(0));
     	other.setLastUpdated(new Date(999));
     
     	Assert.assertFalse(result.equals(other));
   }
  	@Test
  	public void equalsTitleNullTest(){
     	result.setTitle("TITLE");
		result.setDescription("DESCRIPTION");
		result.setGuideId("GUIDEID");
		result.setFirstAired(new Date(0));
     	result.setLastUpdated(new Date(0));
     
     	GuideSearchResult other = new GuideSearchResult();
     	other.setTitle(null);
		other.setDescription("DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setFirstAired(new Date(0));
     	other.setLastUpdated(new Date(0));
     
     	Assert.assertFalse(result.equals(other));
   }
  
  	@Test
  	public void equalsDescriptionNullTest(){
     	result.setTitle("TITLE");
		result.setDescription("DESCRIPTION");
		result.setGuideId("GUIDEID");
		result.setFirstAired(new Date(0));
     	result.setLastUpdated(new Date(0));
     
     	GuideSearchResult other = new GuideSearchResult();
     	other.setTitle("TITLE");
		other.setDescription(null);
		other.setGuideId("GUIDEID");
		other.setFirstAired(new Date(0));
     	other.setLastUpdated(new Date(0));
     
     	Assert.assertFalse(result.equals(other));
   }
  
  	@Test
  	public void equalsGuideIdNullTest(){
     	result.setTitle("TITLE");
		result.setDescription("DESCRIPTION");
		result.setGuideId("GUIDEID");
		result.setFirstAired(new Date(0));
     	result.setLastUpdated(new Date(0));
     
     	GuideSearchResult other = new GuideSearchResult();
     	other.setTitle("TITLE");
		other.setDescription("DESCRIPTION");
		other.setGuideId(null);
		other.setFirstAired(new Date(0));
     	other.setLastUpdated(new Date(0));
     
     	Assert.assertFalse(result.equals(other));
   }
  
  	@Test
  	public void equalsFirstAiredNullTest(){
     	result.setTitle("TITLE");
		result.setDescription("DESCRIPTION");
		result.setGuideId("GUIDEID");
		result.setFirstAired(new Date(0));
     	result.setLastUpdated(new Date(0));
     
     	GuideSearchResult other = new GuideSearchResult();
     	other.setTitle("TITLE");
		other.setDescription("DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setFirstAired(null);
     	other.setLastUpdated(new Date(0));
     
     	Assert.assertFalse(result.equals(other));
   }
  
  	@Test
  	public void equalsLastUpdatedNullTest(){
     	result.setTitle("TITLE");
		result.setDescription("DESCRIPTION");
		result.setGuideId("GUIDEID");
		result.setFirstAired(new Date(0));
     	result.setLastUpdated(new Date(0));
     
     	GuideSearchResult other = new GuideSearchResult();
     	other.setTitle("TITLE");
		other.setDescription("DESCRIPTION");
		other.setGuideId("GUIDEID");
		other.setFirstAired(new Date(0));
     	other.setLastUpdated(null);
     
     	Assert.assertFalse(result.equals(other));
   }
}
