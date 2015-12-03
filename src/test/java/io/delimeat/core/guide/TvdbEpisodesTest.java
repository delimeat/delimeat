package io.delimeat.core.guide;

import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TvdbEpisodesTest {

	private TvdbEpisodes episodes;

	@Before
	public void setUp() {
		episodes = new TvdbEpisodes();
	}

	@Test
	public void firstTest() {
		Assert.assertEquals(0, episodes.getFirst().intValue());
		episodes.setFirst(Integer.MAX_VALUE);
		Assert.assertEquals(Integer.MAX_VALUE, episodes.getFirst().intValue());
	}

	@Test
	public void lastTest() {
		Assert.assertEquals(0, episodes.getLast().intValue());
		episodes.setLast(Integer.MAX_VALUE);
		Assert.assertEquals(Integer.MAX_VALUE, episodes.getLast().intValue());
	}

	@Test
	public void nextTest() {
		Assert.assertEquals(0, episodes.getNext().intValue());
		episodes.setNext(Integer.MAX_VALUE);
		Assert.assertEquals(Integer.MAX_VALUE, episodes.getNext().intValue());
	}

	@Test
	public void previousTest() {
		Assert.assertEquals(0, episodes.getPrevious().intValue());
		episodes.setPrevious(Integer.MAX_VALUE);
		Assert.assertEquals(Integer.MAX_VALUE, episodes.getPrevious().intValue());
	}

	@Test
	public void episodesTest() {
		Assert.assertNotNull(episodes.getEpisodes());
		Assert.assertEquals(0, episodes.getEpisodes().size());
		List<GuideEpisode> list = new ArrayList<GuideEpisode>();
		GuideEpisode mockedEpisode = mock(GuideEpisode.class);
		list.add(mockedEpisode);
		episodes.setEpisodes(list);
		Assert.assertEquals(1, episodes.getEpisodes().size());
		Assert.assertEquals(mockedEpisode, episodes.getEpisodes().get(0));
	}
}
