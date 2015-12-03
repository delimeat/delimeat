package io.delimeat.core.show;

import io.delimeat.core.feed.FeedSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class EpisodeResultTest {

	private EpisodeResult result;

	@Before
	public void setUp() throws Exception {
		result = new EpisodeResult();
	}

	@Test
	public void episodeResultIdTest() {
		Assert.assertEquals(0, result.getEpisodeResultId());
		result.setEpisodeResultId(Integer.MAX_VALUE);
		Assert.assertEquals(Integer.MAX_VALUE, result.getEpisodeResultId());
	}

	@Test
	public void episodeResultTest() {
		Assert.assertNull(result.getEpisode());
		Episode mockedEpisode = Mockito.mock(Episode.class);
		result.setEpisode(mockedEpisode);
		Assert.assertEquals(mockedEpisode, result.getEpisode());
	}

	@Test
	public void sourceTest() {
		Assert.assertNull(result.getSource());
		result.setSource(FeedSource.KAT);
		Assert.assertEquals(FeedSource.KAT, result.getSource());
	}

	@Test
	public void urlTest() {
		Assert.assertNull(result.getUrl());
		result.setUrl("URL");
		Assert.assertEquals("URL", result.getUrl());
	}

	@Test
	public void resultTest() {
		Assert.assertNull(result.getResult());
		result.setResult("RESULT");
		Assert.assertEquals("RESULT", result.getResult());
	}

	@Test
	public void vaildTest() {
		Assert.assertFalse(result.isValid());
		result.setValid(true);
		Assert.assertTrue(result.isValid());
	}

	@Test
	public void versionTest() {
		Assert.assertEquals(0, result.getVersion());
		result.setVersion(99);
		Assert.assertEquals(99, result.getVersion());
	}

}
