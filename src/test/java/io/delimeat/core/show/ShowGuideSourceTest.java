package io.delimeat.core.show;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ShowGuideSourceTest {

	private ShowGuideSource source;

	@Before
	public void setUp() throws Exception {
		source = new ShowGuideSource();
	}

	@Test
	public void pkTest() {
		Assert.assertNull(source.getId());
		ShowGuideSourcePK mockedPK = Mockito.mock(ShowGuideSourcePK.class);
		source.setId(mockedPK);
		Assert.assertEquals(mockedPK, source.getId());
	}

	@Test
	public void guideIdTest() {
		Assert.assertNull(source.getGuideId());
		source.setGuideId("GUIDEID");
		Assert.assertEquals("GUIDEID", source.getGuideId());
	}

	@Test
	public void versionTest() {
		Assert.assertEquals(0, source.getVersion());
		source.setVersion(Integer.MAX_VALUE);
		Assert.assertEquals(Integer.MAX_VALUE, source.getVersion());
	}

	@Test
	public void showTest() {
		Assert.assertNull(source.getShow());
		Show mockedShow = Mockito.mock(Show.class);
		source.setShow(mockedShow);
		Assert.assertEquals(mockedShow, source.getShow());
	}

}
