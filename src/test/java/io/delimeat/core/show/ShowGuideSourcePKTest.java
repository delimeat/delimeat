package io.delimeat.core.show;

import io.delimeat.core.guide.GuideSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ShowGuideSourcePKTest {

	private ShowGuideSourcePK pk;

	@Before
	public void setUp() throws Exception {
		pk = new ShowGuideSourcePK();
	}

	@Test
	public void showIdTest() {
		Assert.assertEquals(0, pk.getShowId());
		pk.setShowId(Long.MAX_VALUE);
		Assert.assertEquals(Long.MAX_VALUE, pk.getShowId());
	}

	@Test
	public void guideSourceTest() {
		Assert.assertNull(pk.getGuideSource());
		pk.setGuideSource(GuideSource.IMDB);
		Assert.assertEquals(GuideSource.IMDB, pk.getGuideSource());

	}

}
