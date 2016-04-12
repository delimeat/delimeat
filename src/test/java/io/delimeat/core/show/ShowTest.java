package io.delimeat.core.show;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ShowTest {

	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	static{
		SDF.setTimeZone(TimeZone.getTimeZone("UTC"));
	}
  
	private Show show;

	@Before
	public void setUp() throws Exception {
		show = new Show();
	}

	@Test
	public void showIdTest() {
		Assert.assertEquals(0, show.getShowId());
		show.setShowId(Long.MAX_VALUE);
		Assert.assertEquals(Long.MAX_VALUE, show.getShowId());
	}

	@Test
	public void airTimeTest() {
		Assert.assertEquals(0, show.getAirTime());
		show.setAirTime(Integer.MIN_VALUE);
		Assert.assertEquals(Integer.MIN_VALUE, show.getAirTime());
	}

	@Test
	public void timezoneTest() {
		Assert.assertNull(show.getTimezone());
		show.setTimezone("TIMEZONE");
		Assert.assertEquals("TIMEZONE", show.getTimezone());
	}

	@Test
	public void guideIdTest() {
		Assert.assertNull(show.getGuideId());
		show.setGuideId("GUIDEID");
		Assert.assertEquals("GUIDEID", show.getGuideId());
	}

	@Test
	public void titleTest() {
		Assert.assertNull(show.getTitle());
		show.setTitle("TITLE");
		Assert.assertEquals("TITLE", show.getTitle());
	}

	@Test
	public void airingTest() {
		Assert.assertFalse(show.isAiring());
		show.setAiring(true);
		Assert.assertTrue(show.isAiring());
	}

	@Test
	public void showTypeTest() {
		Assert.assertNull(show.getShowType());
		show.setShowType(ShowType.ANIMATED);
		Assert.assertEquals(ShowType.ANIMATED, show.getShowType());
	}

	@Test
	public void lastGuideUpdateTest() throws ParseException {
		Assert.assertNull(show.getLastGuideUpdate());
		show.setLastGuideUpdate(SDF.parse("2015-11-06"));
		Assert.assertEquals("2015-11-06", SDF.format(show.getLastGuideUpdate()));
	}

	@Test
	public void lastFeedUpdateTest() throws ParseException {
		Assert.assertNull(show.getLastFeedUpdate());
		show.setLastFeedUpdate(SDF.parse("2015-11-06"));
		Assert.assertEquals("2015-11-06", SDF.format(show.getLastFeedUpdate()));
	}

	@Test
	public void enabledTest() {
		Assert.assertFalse(show.isEnabled());
		show.setEnabled(true);
		Assert.assertTrue(show.isEnabled());
	}

	@Test
	public void nextEpisodeTest() {
		Assert.assertNull(show.getNextEpisode());
		Episode episode = new Episode();
		show.setNextEpisode(episode);
		Assert.assertEquals(episode, show.getNextEpisode());
	}

	@Test
	public void previousEpisodeTest() {
		Assert.assertNull(show.getPreviousEpisode());
		Episode episode = new Episode();
		show.setPreviousEpisode(episode);
		Assert.assertEquals(episode, show.getPreviousEpisode());
	}

	@Test
	public void includeSpecialsTest() {
		Assert.assertFalse(show.isIncludeSpecials());
		show.setIncludeSpecials(true);
		Assert.assertTrue(show.isIncludeSpecials());
	}

	@Test
	public void versionTest() {
		Assert.assertEquals(0, show.getVersion());
		show.setVersion(Integer.MAX_VALUE);
		Assert.assertEquals(Integer.MAX_VALUE, show.getVersion());
	}
  
	@Test
	public void minSizeTest() {
		Assert.assertEquals(0, show.getMinSize());
		show.setMinSize(Integer.MAX_VALUE);
		Assert.assertEquals(Integer.MAX_VALUE, show.getMinSize());
	}
  
	@Test
	public void maxSizeTest() {
		Assert.assertEquals(0, show.getMaxSize());
		show.setMaxSize(Integer.MAX_VALUE);
		Assert.assertEquals(Integer.MAX_VALUE, show.getMaxSize());
	}

  	@Test
  	public void hashCodeTest() throws ParseException{
		show.setShowId(Long.MAX_VALUE);
		show.setAirTime(Integer.MIN_VALUE);
		show.setTimezone("TIMEZONE");
		show.setGuideId("GUIDEID");
		show.setTitle("TITLE");
		show.setAiring(true);
		show.setShowType(ShowType.ANIMATED);
		show.setLastGuideUpdate(SDF.parse("2015-11-06"));
		show.setLastFeedUpdate(SDF.parse("2015-11-06"));
		show.setEnabled(true);
		Episode episode = new Episode();
		show.setNextEpisode(episode);
     	show.setPreviousEpisode(null);
		show.setIncludeSpecials(true);
		show.setVersion(Integer.MIN_VALUE);
		show.setMinSize(Integer.MAX_VALUE);
		show.setMaxSize(Integer.MAX_VALUE);
		Assert.assertEquals(961, show.hashCode());
   }
  
  	@Test
  	public void toStringTest() throws ParseException{
		Assert.assertEquals("Show{showId=0, title=null, showType=null, enabled=false, airing=false, airTime=0, timezone=null, guideId=null, nextEpisode=null, previousEpisode=null, includeSpecials=false, lastGuideUpdate=null, lastFeedUpdate=null, minSize=0, maxSize=0, version=0}",show.toString());
   }
  
  	@Test
  	public void equalsNullTest(){
     	Assert.assertFalse(show.equals(null));     	
   }
  
  	@Test
  	public void equalsSelfTest(){
     	Assert.assertTrue(show.equals(show));
   }
  
  	@Test
  	public void equalsOtherObjectTest(){
     	Assert.assertFalse(show.equals(new Object()));
   }
  
  	@Test
  	public void equalsTest(){
     	show.setShowId(1);
     	Show other = new Show();
     	other.setShowId(1);
     	Assert.assertTrue(show.equals(other));
   }
  
  	@Test
  	public void equalsNotEqualTest(){
     	show.setShowId(1);
     	Show other = new Show();
     	other.setShowId(2);
     	Assert.assertFalse(show.equals(other));
   }
  
  	@Test
  	public void compareToEqualTest(){
   	Assert.assertEquals(0, show.compareTo(show));
   }
  
  	@Test
  	public void compareToNullTest(){
     	show.setTitle("TITLE");
     	Show other = new Show();
     	other.setTitle(null);
   	Assert.assertEquals(1, show.compareTo(other));
   }
  
  	@Test
  	public void compareToOtherNullTest(){
     	show.setTitle("TITLE");
     	Show other = new Show();
     	other.setTitle(null);
   	Assert.assertEquals(-1, other.compareTo(show));
   }
}
