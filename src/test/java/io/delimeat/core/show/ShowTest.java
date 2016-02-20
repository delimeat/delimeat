package io.delimeat.core.show;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ShowTest {

	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

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
	public void guideSourcesTest() {
		Assert.assertNotNull(show.getGuideSources());
		Assert.assertEquals(0, show.getGuideSources().size());
		ShowGuideSource source = new ShowGuideSource();
		show.setGuideSources(Arrays.asList(source));
		Assert.assertEquals(1, show.getGuideSources().size());
		Assert.assertEquals(source, show.getGuideSources().get(0));
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
		ShowGuideSource source = new ShowGuideSource();
		show.setGuideSources(Arrays.asList(source));
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
		show.setShowId(Long.MAX_VALUE);
		show.setAirTime(Integer.MIN_VALUE);
		show.setTimezone("TIMEZONE");
		ShowGuideSource source = new ShowGuideSource();
		show.setGuideSources(Arrays.asList(source));
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
		show.setVersion(Integer.MAX_VALUE);
		show.setMinSize(Integer.MAX_VALUE);
		show.setMaxSize(Integer.MAX_VALUE);
     	System.out.println(show.toString());
		Assert.assertEquals("Show{showId=9223372036854775807, title=TITLE, showType=ANIMATED, enabled=true, airing=true, airTime=-2147483648, timezone=TIMEZONE, guideSources=[ShowGuideSource [pk=null, guideId=null, version=0]], nextEpisode=Episode{episodeId=0, title=null, airDate=null, seasonNum=0, episodeNum=0, doubleEp=false, showId=null, results=[], version=0}, previousEpisode=null, includeSpecials=true, lastGuideUpdate=Fri Nov 06 00:00:00 UTC 2015, lastFeedUpdate=Fri Nov 06 00:00:00 UTC 2015, minSize=2147483647, maxSize=2147483647, version=2147483647}",show.toString());
   }
}
