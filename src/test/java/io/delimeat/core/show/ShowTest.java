package io.delimeat.core.show;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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
		ShowGuideSource mockedSource = Mockito.mock(ShowGuideSource.class);
		show.setGuideSources(Arrays.asList(new ShowGuideSource[] { mockedSource }));
		Assert.assertEquals(1, show.getGuideSources().size());
		Assert.assertEquals(mockedSource, show.getGuideSources().get(0));
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
		Episode mockedEpisode = Mockito.mock(Episode.class);
		show.setNextEpisode(mockedEpisode);
		Assert.assertEquals(mockedEpisode, show.getNextEpisode());
	}

	@Test
	public void previousEpisodeTest() {
		Assert.assertNull(show.getPreviousEpisode());
		Episode mockedEpisode = Mockito.mock(Episode.class);
		show.setPreviousEpisode(mockedEpisode);
		Assert.assertEquals(mockedEpisode, show.getPreviousEpisode());
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

}
