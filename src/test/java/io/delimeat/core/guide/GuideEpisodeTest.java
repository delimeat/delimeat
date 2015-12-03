package io.delimeat.core.guide;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class GuideEpisodeTest {

	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

	private GuideEpisode ep;

	@Before
	public void setUp() {
		ep = new GuideEpisode();
	}

	@Test
	public void setAirDateTest() throws ParseException {
		Assert.assertNull(ep.getAirDate());
		ep.setAirDate(SDF.parse("2005-01-02"));
		Assert.assertEquals("2005-01-02", SDF.format(ep.getAirDate()));

	}

	@Test
	public void setTitleTest() {
		Assert.assertEquals(null, ep.getTitle());
		ep.setTitle("TEST TITLE");
		Assert.assertEquals("TEST TITLE", ep.getTitle());
	}

	@Test
	public void setSeasonNumTest() {
		Assert.assertEquals(0, ep.getSeasonNum().intValue());
		ep.setSeasonNum(12);
		Assert.assertEquals(12, ep.getSeasonNum().intValue());
	}

	@Test
	public void setEpisodeNumTest() {
		Assert.assertEquals(0, ep.getEpisodeNum().intValue());
		ep.setEpisodeNum(55);
		Assert.assertEquals(55, ep.getEpisodeNum().intValue());
	}

	@Test
	public void setProductionNumTest() {
		Assert.assertEquals(0, ep.getProductionNum().intValue());
		ep.setProductionNum(208);
		Assert.assertEquals(208, ep.getProductionNum().intValue());
	}

	@Test
	public void compareToNoAirDateTest() {
		ep.setAirDate(null);

		GuideEpisode ep2 = new GuideEpisode();
		ep2.setAirDate(null);

		Assert.assertEquals(0, ep.compareTo(ep2));
	}

	@Test
	public void compareToOtherNoAirDateTest() throws Exception {
		ep.setAirDate(SDF.parse("2005-01-02"));

		GuideEpisode mockedEpisode2 = Mockito.mock(GuideEpisode.class);
		Mockito.when(mockedEpisode2.getAirDate()).thenReturn(null);

		Assert.assertEquals(1, ep.compareTo(mockedEpisode2));
	}

	@Test
	public void compareToThisNoAirDateTest() throws Exception {
		ep.setAirDate(null);

		GuideEpisode mockedEpisode2 = Mockito.mock(GuideEpisode.class);
		Mockito.when(mockedEpisode2.getAirDate()).thenReturn(SDF.parse("2005-01-02"));

		Assert.assertEquals(-1, ep.compareTo(mockedEpisode2));
	}

	@Test
	public void compareToAirDateBeforeTest() throws Exception {
		ep.setAirDate(SDF.parse("2005-01-02"));

		GuideEpisode mockedEpisode2 = Mockito.mock(GuideEpisode.class);
		Mockito.when(mockedEpisode2.getAirDate()).thenReturn(SDF.parse("2005-02-02"));

		Assert.assertEquals(-1, ep.compareTo(mockedEpisode2));
	}

	@Test
	public void compareToAirDateAfterTest() throws Exception {
		ep.setAirDate(SDF.parse("2005-03-02"));

		GuideEpisode mockedEpisode2 = Mockito.mock(GuideEpisode.class);
		Mockito.when(mockedEpisode2.getAirDate()).thenReturn(SDF.parse("2005-02-02"));

		Assert.assertEquals(1, ep.compareTo(mockedEpisode2));
	}

	@Test
	public void compareToProductionMatch() throws Exception {
		ep.setAirDate(SDF.parse("2005-03-02"));
		ep.setProductionNum(101);
		ep.setSeasonNum(-1);
		ep.setEpisodeNum(-1);

		GuideEpisode mockedEpisode2 = Mockito.mock(GuideEpisode.class);
		Mockito.when(mockedEpisode2.getAirDate()).thenReturn(SDF.parse("2005-03-02"));
		Mockito.when(mockedEpisode2.getProductionNum()).thenReturn(101);
		Mockito.when(mockedEpisode2.getSeasonNum()).thenReturn(-1);
		Mockito.when(mockedEpisode2.getEpisodeNum()).thenReturn(-1);

		Assert.assertEquals(0, ep.compareTo(mockedEpisode2));
	}

	@Test
	public void compareToProductionAfter() throws Exception {

		ep.setAirDate(SDF.parse("2005-03-02"));
		ep.setProductionNum(102);
		ep.setSeasonNum(-1);
		ep.setEpisodeNum(-1);

		GuideEpisode mockedEpisode2 = Mockito.mock(GuideEpisode.class);
		Mockito.when(mockedEpisode2.getAirDate()).thenReturn(SDF.parse("2005-03-02"));
		Mockito.when(mockedEpisode2.getProductionNum()).thenReturn(101);
		Mockito.when(mockedEpisode2.getSeasonNum()).thenReturn(-1);
		Mockito.when(mockedEpisode2.getEpisodeNum()).thenReturn(-1);

		Assert.assertEquals(1, ep.compareTo(mockedEpisode2));
	}

	@Test
	public void compareToProductionBefore() throws Exception {

		ep.setAirDate(SDF.parse("2005-03-02"));
		ep.setProductionNum(101);
		ep.setSeasonNum(-1);
		ep.setEpisodeNum(-1);

		GuideEpisode mockedEpisode2 = Mockito.mock(GuideEpisode.class);
		Mockito.when(mockedEpisode2.getAirDate()).thenReturn(SDF.parse("2005-03-02"));
		Mockito.when(mockedEpisode2.getProductionNum()).thenReturn(102);
		Mockito.when(mockedEpisode2.getSeasonNum()).thenReturn(-1);
		Mockito.when(mockedEpisode2.getEpisodeNum()).thenReturn(-1);

		Assert.assertEquals(-1, ep.compareTo(mockedEpisode2));
	}

	@Test
	public void compareToSeasonAfter() throws Exception {

		ep.setAirDate(SDF.parse("2005-03-02"));
		ep.setProductionNum(101);
		ep.setSeasonNum(2);
		ep.setEpisodeNum(1);

		GuideEpisode mockedEpisode2 = Mockito.mock(GuideEpisode.class);
		Mockito.when(mockedEpisode2.getAirDate()).thenReturn(SDF.parse("2005-03-02"));
		Mockito.when(mockedEpisode2.getProductionNum()).thenReturn(101);
		Mockito.when(mockedEpisode2.getSeasonNum()).thenReturn(1);
		Mockito.when(mockedEpisode2.getEpisodeNum()).thenReturn(1);

		Assert.assertEquals(1, ep.compareTo(mockedEpisode2));
	}

	@Test
	public void compareToSeasonBefore() throws Exception {

		ep.setAirDate(SDF.parse("2005-03-02"));
		ep.setProductionNum(101);
		ep.setSeasonNum(1);
		ep.setEpisodeNum(1);

		GuideEpisode mockedEpisode2 = Mockito.mock(GuideEpisode.class);
		Mockito.when(mockedEpisode2.getAirDate()).thenReturn(SDF.parse("2005-03-02"));
		Mockito.when(mockedEpisode2.getProductionNum()).thenReturn(101);
		Mockito.when(mockedEpisode2.getSeasonNum()).thenReturn(2);
		Mockito.when(mockedEpisode2.getEpisodeNum()).thenReturn(1);

		Assert.assertEquals(-1, ep.compareTo(mockedEpisode2));
	}

	@Test
	public void compareToEpisodeAfter() throws Exception {

		ep.setAirDate(SDF.parse("2005-03-02"));
		ep.setProductionNum(101);
		ep.setSeasonNum(2);
		ep.setEpisodeNum(2);

		GuideEpisode mockedEpisode2 = Mockito.mock(GuideEpisode.class);
		Mockito.when(mockedEpisode2.getAirDate()).thenReturn(SDF.parse("2005-03-02"));
		Mockito.when(mockedEpisode2.getProductionNum()).thenReturn(101);
		Mockito.when(mockedEpisode2.getSeasonNum()).thenReturn(2);
		Mockito.when(mockedEpisode2.getEpisodeNum()).thenReturn(1);

		Assert.assertEquals(1, ep.compareTo(mockedEpisode2));
	}

	@Test
	public void compareToEpisodeBefore() throws Exception {

		ep.setAirDate(SDF.parse("2005-03-02"));
		ep.setProductionNum(101);
		ep.setSeasonNum(2);
		ep.setEpisodeNum(1);

		GuideEpisode mockedEpisode2 = Mockito.mock(GuideEpisode.class);
		Mockito.when(mockedEpisode2.getAirDate()).thenReturn(SDF.parse("2005-03-02"));
		Mockito.when(mockedEpisode2.getProductionNum()).thenReturn(101);
		Mockito.when(mockedEpisode2.getSeasonNum()).thenReturn(2);
		Mockito.when(mockedEpisode2.getEpisodeNum()).thenReturn(2);

		Assert.assertEquals(-1, ep.compareTo(mockedEpisode2));
	}

	@Test
	public void compareToEpisodeMatch() throws Exception {

		ep.setAirDate(SDF.parse("2005-03-02"));
		ep.setProductionNum(101);
		ep.setSeasonNum(2);
		ep.setEpisodeNum(2);

		GuideEpisode mockedEpisode2 = Mockito.mock(GuideEpisode.class);
		Mockito.when(mockedEpisode2.getAirDate()).thenReturn(SDF.parse("2005-03-02"));
		Mockito.when(mockedEpisode2.getProductionNum()).thenReturn(101);
		Mockito.when(mockedEpisode2.getSeasonNum()).thenReturn(2);
		Mockito.when(mockedEpisode2.getEpisodeNum()).thenReturn(2);

		Assert.assertEquals(0, ep.compareTo(mockedEpisode2));
	}
}
