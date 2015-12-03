package io.delimeat.core.guide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GuideSearchResultTest {

	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

	private GuideSearchResult result;

	@Before
	public void setUp() {
		result = new GuideSearchResult();
	}

	@Test
	public void descriptionTest() {

		Assert.assertEquals(null, result.getDescription());
		result.setDescription("DESCRIPTION");
		Assert.assertEquals("DESCRIPTION", result.getDescription());
	}

	@Test
	public void GuideIdsTest() {
		Assert.assertNotNull(result.getGuideIds());
		Assert.assertEquals(0, result.getGuideIds().size());
		List<GuideIdentifier> guideIds = new ArrayList<GuideIdentifier>();
		GuideIdentifier id = new GuideIdentifier();
		id.setSource(GuideSource.TVDB);
		id.setValue("GUIDEID");
		guideIds.add(id);
		result.setGuideIds(guideIds);
		Assert.assertEquals(1, result.getGuideIds().size());
		Assert.assertEquals(GuideSource.TVDB, result.getGuideIds().get(0).getSource());
		Assert.assertEquals("GUIDEID", result.getGuideIds().get(0).getValue());
	}

	@Test
	public void firstAiredTest() throws ParseException {

		Assert.assertEquals(null, result.getFirstAired());

		result.setFirstAired(SDF.parse("2005-04-03"));
		Assert.assertEquals("2005-04-03", SDF.format(result.getFirstAired()));
	}

	@Test
	public void titleTest() {

		Assert.assertEquals(null, result.getTitle());
		result.setTitle("TITLE");
		Assert.assertEquals("TITLE", result.getTitle());
	}

	@Test
	public void compareTitleMatchTest() {
		result.setTitle("ABC");
		GuideSearchResult result2 = new GuideSearchResult();
		result2.setTitle("ABC");

		Assert.assertEquals(0, result.compareTo(result2));
	}

	@Test
	public void compareNullTitlesTest() {
		Assert.assertNull(result.getTitle());
		GuideSearchResult result2 = new GuideSearchResult();
		Assert.assertNull(result2.getTitle());

		Assert.assertEquals(0, result.compareTo(result2));
	}

	@Test
	public void compareTitleOtherNullTest() {
		result.setTitle("ABC");
		Assert.assertNotNull(result.getTitle());
		GuideSearchResult result2 = new GuideSearchResult();
		Assert.assertNull(result2.getTitle());

		Assert.assertEquals(1, result.compareTo(result2));
	}

	@Test
	public void compareTitleNullOtherNotNullTest() {
		Assert.assertNull(result.getTitle());
		GuideSearchResult result2 = new GuideSearchResult();
		result2.setTitle("ABC");
		Assert.assertNotNull(result2.getTitle());

		Assert.assertEquals(-1, result.compareTo(result2));
	}

	@Test
	public void compareTitleBeforeTest() {
		result.setTitle("ABC");
		GuideSearchResult result2 = new GuideSearchResult();
		result2.setTitle("BCD");

		Assert.assertEquals(-1, result.compareTo(result2));
	}

	@Test
	public void compareTitleAfterTest() {
		result.setTitle("BCD");
		GuideSearchResult result2 = new GuideSearchResult();
		result2.setTitle("ABC");

		Assert.assertEquals(1, result.compareTo(result2));
	}
}
