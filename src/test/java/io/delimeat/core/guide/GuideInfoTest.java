package io.delimeat.core.guide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GuideInfoTest {

	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

	private GuideInfo info;

	@Before
	public void setUp() {
		info = new GuideInfo();
	}

	@Test
	public void setAirDayTest() {
		Assert.assertNotNull(info.getAirDays());
		Assert.assertEquals(0, info.getAirDays().size());
		List<AiringDay> list = new ArrayList<AiringDay>();
		list.add(AiringDay.FRIDAY);
		info.setAirDays(list);
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
	public void GuideIdsTest() {
		Assert.assertNotNull(info.getGuideIds());
		Assert.assertEquals(0, info.getGuideIds().size());
		List<GuideIdentifier> guideIds = new ArrayList<GuideIdentifier>();
		GuideIdentifier id = new GuideIdentifier();
		id.setSource(GuideSource.TVDB);
		id.setValue("GUIDEID");
		guideIds.add(id);
		info.setGuideIds(guideIds);
		Assert.assertEquals(1, info.getGuideIds().size());
		Assert.assertEquals("GUIDEID", info.getGuideIds().get(0).getValue());
		Assert.assertEquals(GuideSource.TVDB, info.getGuideIds().get(0).getSource());

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
		info.setTimezone("SciFi");
		Assert.assertEquals("SciFi", info.getTimezone());
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
		List<String> genres = new ArrayList<String>();
		genres.add("GENRE1");
		info.setGenres(genres);
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
	/*
	 * @Test public void episodesTest(){ Assert.assertEquals(0,
	 * info.getEpisodes().size()); GuideEpisode ep1 =mock(GuideEpisode.class);
	 * when(ep1.getAirDate()).thenReturn(new Date()); GuideEpisode ep2
	 * =mock(GuideEpisode.class); when(ep2.getAirDate()).thenReturn(null);
	 * 
	 * info.getEpisodes().add(ep1);
	 * 
	 * Assert.assertEquals(1, info.getEpisodes().size());
	 * 
	 * info.getEpisodes().add(ep2);
	 * 
	 * Assert.assertEquals(2, info.getEpisodes().size()); }
	 */

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

}
