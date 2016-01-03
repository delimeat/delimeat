package io.delimeat.core.guide;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.delimeat.util.UrlHandler;

public class TvMazeJaxbGuideDao_ImplTest {

	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

	private class SearchJsonGenerator {
		private StringBuffer xml;

		public SearchJsonGenerator() {
			xml = new StringBuffer();
			xml.append("[");
		}

		public void addSeries(String description, String guideid, String title, String firstaired, String tvdbId,
				String tvrageId) {
			xml.append("{\"show\":{");
			xml.append("\"summary\":\"" + description + "\",");
			xml.append("\"id\":\"" + guideid + "\",");
			xml.append("\"name\":\"" + title + "\",");
			xml.append("\"externals\":{");
			xml.append("\"thetvdb\":" + tvdbId + ",");
			xml.append("\"tvrage\":" + tvrageId + "");
			xml.append("},");
			xml.append("\"premiered\":\"" + firstaired + "\"");
			xml.append("}}");
		}

		public String toString() {
			return xml.toString() + "]";
		}

		public InputStream generate() throws Exception {
			return new ByteArrayInputStream(this.toString().getBytes("UTF-8"));
		}
	}

	private class InfoJsonGenerator {
		private StringBuffer xml;

		public InfoJsonGenerator(String description, String guideid, String title, String runtime, String tvdbId,
				String tvrageId, List<String> genres, String status, String timezone, String time, List<String> days) {
			xml = new StringBuffer();
			xml.append("{");
			xml.append("\"summary\":\"" + description + "\",");
			xml.append("\"id\":\"" + guideid + "\",");
			xml.append("\"name\":\"" + title + "\",");
			xml.append("\"externals\":{");
			xml.append("\"thetvdb\":" + tvdbId + ",");
			xml.append("\"tvrage\":" + tvrageId);
			xml.append("},");
			xml.append("\"genres\":[");
			boolean firstGenre = true;
			for (String genre : genres) {
				if (!firstGenre) {
					xml.append(",");
				}
				xml.append("\"" + genre + "\"");
				firstGenre = false;
			}
			xml.append("],");
			xml.append("\"status\":\"" + status + "\",");
			xml.append("\"runtime\":" + runtime + ",");
			xml.append("\"network\":{\"country\":{\"timezone\":\"" + timezone + "\"}},");
			xml.append("\"schedule\":{\"time\":\"" + time + "\",");
			xml.append("\"days\":[");
			boolean firstDay = true;
			for (String day : days) {
				if (!firstDay) {
					xml.append(",");
				}
				xml.append("\"" + day + "\"");
				firstDay = false;
			}
			xml.append("]");
			xml.append("}");
		}

		public String toString() {
			return xml.toString() + "}";
		}

		public InputStream generate() throws Exception {
			return new ByteArrayInputStream(this.toString().getBytes("UTF-8"));
		}
	}

	private class EpisodesJsonGenerator {
		private StringBuffer xml;
		private boolean first = true;

		public EpisodesJsonGenerator() {
			xml = new StringBuffer();
			xml.append("[");

		}

		public void addEpisode(Integer absoluteNumber, Integer airedEpisodeNumber, Integer airedSeason,
				String episodeName, String firstAired) {
			if (!first) {
				xml.append(",");
			}
			xml.append("{");
			xml.append("\"number\":" + airedEpisodeNumber + ",");
			xml.append("\"season\":\"" + airedSeason + "\",");
			xml.append("\"name\":\"" + episodeName + "\",");
			xml.append("\"airdate\":\"" + firstAired + "\"");
			xml.append("}");
			first = false;
		}

		public String toString() {
			return xml.toString() + "]";
		}

		public InputStream generate() throws Exception {
			return new ByteArrayInputStream(this.toString().getBytes("UTF-8"));
		}
	}

	private static final String METADATA = "META-INF/oxm/guide-tvmaze-oxm.xml";

	private TvMazeJaxbGuideDao_Impl dao;

	@Before
	public void before() {
		dao = new TvMazeJaxbGuideDao_Impl();
	}

	@Test
	public void baseUriTest() throws URISyntaxException {
		Assert.assertNull(dao.getBaseUri());
		URI uri = new URI("http://test.com");
		dao.setBaseUri(uri);
		Assert.assertEquals(uri, dao.getBaseUri());
	}

	@Test
	public void unmarshallerTest() {
		Assert.assertNull(dao.getUnmarshaller());
		Unmarshaller mockedUnmarshaller = Mockito.mock(Unmarshaller.class);
		dao.setUnmarshaller(mockedUnmarshaller);
		Assert.assertEquals(mockedUnmarshaller, dao.getUnmarshaller());
	}

	@Test
	public void marshallerTest() {
		Assert.assertNull(dao.getMarshaller());
		Marshaller mockedMarshaller = Mockito.mock(Marshaller.class);
		dao.setMarshaller(mockedMarshaller);
		Assert.assertEquals(mockedMarshaller, dao.getMarshaller());
	}

	@Test
	public void urlHandlerTest() {
		Assert.assertNull(dao.getUrlHandler());
		UrlHandler mockedUrlHandler = Mockito.mock(UrlHandler.class);
		dao.setUrlHandler(mockedUrlHandler);
		Assert.assertEquals(mockedUrlHandler, dao.getUrlHandler());
	}

	@Test
	public void propertiesTest() {
		Assert.assertNull(dao.getProperties());
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("KEY", "VALUE");
		dao.setProperties(properties);
		Assert.assertEquals(1, dao.getProperties().size());
		Assert.assertEquals("VALUE", dao.getProperties().get("KEY"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void searchTest() throws IOException, Exception {
		SearchJsonGenerator generator = new SearchJsonGenerator();
		generator.addSeries("DESCRIPTION", "GUIDEID", "TITLE", "2015-09-29", "123", "432");
		
		System.out.println(generator.toString());
		
		UrlHandler mockedHandler = Mockito.mock(UrlHandler.class);
		Mockito.when(mockedHandler.openInput(Mockito.any(URL.class), Mockito.any(Map.class)))
				.thenReturn(generator.generate());
		dao.setUrlHandler(mockedHandler);

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(JAXBContextProperties.OXM_METADATA_SOURCE, METADATA);
		JAXBContext jc = JAXBContext.newInstance(new Class[] { GuideSearchResult.class },
				properties);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		unmarshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
		unmarshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
		dao.setUnmarshaller(unmarshaller);

		URI baseUri = new URI("http://test.com");
		dao.setBaseUri(baseUri);

		List<GuideSearchResult> results = dao.search("TITLE");
		Assert.assertNotNull(results);
		Assert.assertEquals(1, results.size());
		Assert.assertNotNull(results.get(0).getGuideIds());
		Assert.assertEquals(3, results.get(0).getGuideIds().size());
		Assert.assertEquals(GuideSource.TVMAZE, results.get(0).getGuideIds().get(0).getSource());
		Assert.assertEquals("GUIDEID", results.get(0).getGuideIds().get(0).getValue());
		Assert.assertEquals(GuideSource.TVDB, results.get(0).getGuideIds().get(1).getSource());
		Assert.assertEquals("123", results.get(0).getGuideIds().get(1).getValue());
		Assert.assertEquals(GuideSource.TVRAGE, results.get(0).getGuideIds().get(2).getSource());
		Assert.assertEquals("432", results.get(0).getGuideIds().get(2).getValue());
		Assert.assertEquals("DESCRIPTION", results.get(0).getDescription());
		Assert.assertEquals("TITLE", results.get(0).getTitle());
		Assert.assertEquals("2015-09-29", SDF.format(results.get(0).getFirstAired()));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void infoTest() throws IOException, Exception {
		List<String> genres = new ArrayList<String>();
		genres.add("GENRE1");
		genres.add("GENRE2");
		List<String> days = new ArrayList<String>();
		days.add("Monday");
		days.add("Friday");
		InfoJsonGenerator generator = new InfoJsonGenerator("DESCRIPTION", "GUIDEID", "TITLE", "60", "123", "432",
				genres, "Ended", "TIMEZONE", "20:00", days);

		UrlHandler mockedHandler = Mockito.mock(UrlHandler.class);
		Mockito.when(mockedHandler.openInput(Mockito.any(URL.class), Mockito.any(Map.class)))
				.thenReturn(generator.generate());
		dao.setUrlHandler(mockedHandler);

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(JAXBContextProperties.OXM_METADATA_SOURCE, METADATA);
		JAXBContext jc = JAXBContext.newInstance(new Class[] { GuideSearch.class, GuideSearchResult.class },
				properties);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		unmarshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
		unmarshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
		dao.setUnmarshaller(unmarshaller);

		URI baseUri = new URI("http://test.com");
		dao.setBaseUri(baseUri);

		GuideInfo info = dao.info("ID");
		Assert.assertEquals("DESCRIPTION", info.getDescription());
		Assert.assertEquals("TITLE", info.getTitle());
		Assert.assertEquals(60, info.getRunningTime());
		Assert.assertEquals(AiringStatus.ENDED, info.getAirStatus());
		Assert.assertEquals("TIMEZONE", info.getTimezone());
		Assert.assertNotNull(info.getGuideIds());
		Assert.assertEquals(3, info.getGuideIds().size());
		Assert.assertEquals(GuideSource.TVMAZE, info.getGuideIds().get(0).getSource());
		Assert.assertEquals("GUIDEID", info.getGuideIds().get(0).getValue());
		Assert.assertEquals(GuideSource.TVDB, info.getGuideIds().get(1).getSource());
		Assert.assertEquals("123", info.getGuideIds().get(1).getValue());
		Assert.assertEquals(GuideSource.TVRAGE, info.getGuideIds().get(2).getSource());
		Assert.assertEquals("432", info.getGuideIds().get(2).getValue());
		Assert.assertEquals(72000000, info.getAirTime());
		Assert.assertEquals(2, info.getGenres().size());
		Assert.assertEquals("GENRE1", info.getGenres().get(0));
		Assert.assertEquals("GENRE2", info.getGenres().get(1));

		Assert.assertEquals(2, info.getAirDays().size());
		Assert.assertEquals(AiringDay.MONDAY, info.getAirDays().get(0));
		Assert.assertEquals(AiringDay.FRIDAY, info.getAirDays().get(1));

	}

	@SuppressWarnings("unchecked")
	@Test
	public void episodesTest() throws Exception {
		EpisodesJsonGenerator generator = new EpisodesJsonGenerator();
		generator.addEpisode(0, Integer.MIN_VALUE, Integer.MAX_VALUE, "TITLE", "2015-09-29");

		UrlHandler mockedHandler = Mockito.mock(UrlHandler.class);
		Mockito.when(mockedHandler.openInput(Mockito.any(URL.class), Mockito.any(Map.class)))
				.thenReturn(generator.generate());
		dao.setUrlHandler(mockedHandler);

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(JAXBContextProperties.OXM_METADATA_SOURCE, METADATA);
		JAXBContext jc = JAXBContext.newInstance(new Class[] { GuideEpisode.class }, properties);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		unmarshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
		unmarshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
		dao.setUnmarshaller(unmarshaller);

		URI baseUri = new URI("http://test.com");
		dao.setBaseUri(baseUri);

		List<GuideEpisode> result = dao.episodes("ID");
		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(0, result.get(0).getProductionNum().intValue());
		Assert.assertEquals(Integer.MIN_VALUE, result.get(0).getEpisodeNum().intValue());
		Assert.assertEquals(Integer.MAX_VALUE, result.get(0).getSeasonNum().intValue());
		Assert.assertEquals("TITLE", result.get(0).getTitle());
		Assert.assertEquals("2015-09-29", SDF.format(result.get(0).getAirDate()));
	}

}
