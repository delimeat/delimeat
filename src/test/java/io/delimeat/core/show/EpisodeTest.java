package io.delimeat.core.show;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class EpisodeTest {

	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

	private Episode episode;

	@Before
	public void setUp() throws Exception {
		episode = new Episode();
	}

	@Test
	public void episodeIdTest() {
		Assert.assertEquals(0, episode.getEpisodeId());
		episode.setEpisodeId(Long.MAX_VALUE);
		Assert.assertEquals(Long.MAX_VALUE, episode.getEpisodeId());
	}

	@Test
	public void titleTest() {
		Assert.assertNull(episode.getTitle());
		episode.setTitle("TITLE");
		Assert.assertEquals("TITLE", episode.getTitle());
	}

	@Test
	public void airDateTimeTest() throws ParseException {
		Assert.assertNull(episode.getAirDate());
		episode.setAirDate(SDF.parse("2015-11-06"));
		Assert.assertEquals("2015-11-06", SDF.format(episode.getAirDate()));
	}

	@Test
	public void seasonNumTest() {
		Assert.assertEquals(0, episode.getSeasonNum());
		episode.setSeasonNum(Integer.MIN_VALUE);
		Assert.assertEquals(Integer.MIN_VALUE, episode.getSeasonNum());
	}

	@Test
	public void episodeNumTest() {
		Assert.assertEquals(0, episode.getEpisodeNum());
		episode.setEpisodeNum(Integer.MIN_VALUE);
		Assert.assertEquals(Integer.MIN_VALUE, episode.getEpisodeNum());
	}

	@Test
	public void doubleEpisodeTest() {
		Assert.assertFalse(episode.isDoubleEp());
		episode.setDoubleEp(true);
		Assert.assertTrue(episode.isDoubleEp());
	}

	@Test
	public void versionTest() {
		Assert.assertEquals(0, episode.getVersion());
		episode.setVersion(Integer.MAX_VALUE);
		Assert.assertEquals(Integer.MAX_VALUE, episode.getVersion());
	}

	@Test
	public void showTest() {
		Assert.assertNull(episode.getShow());
		Show mockedShow = Mockito.mock(Show.class);
		episode.setShow(mockedShow);
		Assert.assertEquals(mockedShow, episode.getShow());
	}

	@Test
	public void resultsTest() {
		Assert.assertNotNull(episode.getResults());
		Assert.assertEquals(0, episode.getResults().size());
		List<EpisodeResult> results = new ArrayList<EpisodeResult>();
		EpisodeResult mockedEpisodeResult = Mockito.mock(EpisodeResult.class);
		results.add(mockedEpisodeResult);
		episode.setResults(results);
		Assert.assertEquals(1, episode.getResults().size());
		Assert.assertEquals(mockedEpisodeResult, episode.getResults().get(0));

	}

}
