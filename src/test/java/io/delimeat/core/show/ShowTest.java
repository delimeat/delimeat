package io.delimeat.core.show;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
  	public void constructorTest(){
     	Episode nextEp = new Episode();
     	Episode prevEp = new Episode();
     	show = new Show(Long.MAX_VALUE,Integer.MAX_VALUE, "TIMEZONE", "GUIDEID", "TITLE",true, ShowType.ANIMATED, new Date(0), new Date(1), false, nextEp, prevEp,true, 100, 101, Integer.MIN_VALUE);
		Assert.assertEquals(Long.MAX_VALUE, show.getShowId());
  		Assert.assertEquals(Integer.MAX_VALUE, show.getAirTime());
     	Assert.assertEquals("TIMEZONE", show.getTimezone());
     	Assert.assertEquals("GUIDEID", show.getGuideId());
     	Assert.assertEquals("TITLE", show.getTitle());
		Assert.assertTrue(show.isAiring());
     	Assert.assertEquals(ShowType.ANIMATED, show.getShowType());
     	Assert.assertEquals(new Date(0), show.getLastFeedUpdate());
     	Assert.assertEquals(new Date(1), show.getLastGuideUpdate());
     	Assert.assertFalse(show.isEnabled());
     	Assert.assertEquals(nextEp, show.getNextEpisode());
     	Assert.assertEquals(prevEp, show.getPreviousEpisode());
     	Assert.assertTrue(show.isIncludeSpecials());
     	Assert.assertEquals(100, show.getMinSize());
     	Assert.assertEquals(101, show.getMaxSize());
     	Assert.assertEquals(Integer.MIN_VALUE, show.getVersion());
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
	public void lastGuideCheckTest() throws ParseException {
		Assert.assertNull(show.getLastGuideCheck());
		show.setLastGuideCheck(SDF.parse("2015-11-06"));
		Assert.assertEquals("2015-11-06", SDF.format(show.getLastGuideCheck()));
	}
	
	@Test
	public void lastFeedUpdateTest() throws ParseException {
		Assert.assertNull(show.getLastFeedUpdate());
		show.setLastFeedUpdate(SDF.parse("2015-11-06"));
		Assert.assertEquals("2015-11-06", SDF.format(show.getLastFeedUpdate()));
	}
	
	@Test
	public void lastFeedCheckTest() throws ParseException {
		Assert.assertNull(show.getLastFeedCheck());
		show.setLastFeedCheck(SDF.parse("2015-11-06"));
		Assert.assertEquals("2015-11-06", SDF.format(show.getLastFeedCheck()));
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
		show.setVersion(Integer.MIN_VALUE);
		Assert.assertEquals(961, show.hashCode());
   }
  
  	@Test
  	public void toStringTest() throws ParseException{
		Assert.assertEquals("Show{showId=0, title=null, showType=null, enabled=false, airing=false, airTime=0, timezone=null, guideId=null, nextEpisode=null, previousEpisode=null, includeSpecials=false, lastGuideUpdate=null, lastGuideCheck=null, lastFeedUpdate=null, lastGuideCheck=null, minSize=0, maxSize=0, version=0}",show.toString());
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
     	show.setVersion(99);
     	Show other = new Show();
     	other.setShowId(1);
     	other.setVersion(99);
     	Assert.assertTrue(show.equals(other));
   }
  
  	@Test
  	public void equalsShowIdTest(){
     	show.setShowId(1);
     	show.setVersion(99);
     	Show other = new Show();
     	other.setShowId(2);
     	other.setVersion(99);
     	Assert.assertFalse(show.equals(other));
   }
  	@Test
  	public void equalsVersionTest(){
     	show.setShowId(1);
      show.setVersion(99);
     	Show other = new Show();
     	other.setShowId(1);
      other.setVersion(98);
     	Assert.assertFalse(show.equals(other));
   }

}
