/*
 * Copyright 2013-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.delimeat.guide;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.delimeat.guide.entity.AiringDay;
import io.delimeat.guide.entity.GuideEpisode;
import io.delimeat.guide.entity.GuideInfo;
import io.delimeat.guide.entity.GuideSearchResult;
import io.delimeat.guide.entity.GuideSource;
import io.delimeat.guide.entity.TvdbApiKey;
import io.delimeat.guide.entity.TvdbEpisodes;
import io.delimeat.guide.entity.TvdbToken;
import io.delimeat.guide.exception.GuideAuthorizationException;
import io.delimeat.guide.exception.GuideException;
import io.delimeat.guide.exception.GuideNotFoundException;
import io.delimeat.guide.exception.GuideResponseBodyException;
import io.delimeat.guide.exception.GuideResponseException;
import io.delimeat.guide.exception.GuideTimeoutException;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okhttp3.mockwebserver.SocketPolicy;
import okio.Buffer;

public class TvdbGuideDataSource_ImplTest {

	private static final int PORT = 8089;
	private static MockWebServer mockedServer = new MockWebServer();

	private TvdbGuideDataSource_Impl dataSource;

	@BeforeAll
	public static void beforeClass() throws IOException {
		mockedServer.start(PORT);
	}

	@AfterAll
	public static void tearDown() throws IOException {
		mockedServer.shutdown();
	}

	@BeforeEach
	public void setUp() throws Exception {
		dataSource = new TvdbGuideDataSource_Impl();
	}

	@Test
	public void baseUriTest() throws URISyntaxException {
		Assertions.assertNull(dataSource.getBaseUri());
		dataSource.setBaseUri("http://test.com");
		Assertions.assertEquals("http://test.com", dataSource.getBaseUri());
	}

	@Test
	public void guideSourceTest() {
		Assertions.assertEquals(GuideSource.TVDB, dataSource.getGuideSource());
	}

	@Test
	public void propertiesTest() {
		Assertions.assertEquals(3, dataSource.getProperties().size());
		Assertions.assertEquals("oxm/guide-tvdb-oxm.xml",
				dataSource.getProperties().get("eclipselink.oxm.metadata-source"));
		Assertions.assertEquals("application/json", dataSource.getProperties().get("eclipselink.media-type"));
		Assertions.assertEquals("false", dataSource.getProperties().get("eclipselink.json.include-root").toString());
	}

	@Test
	public void toStringTest() {
		Assertions.assertEquals(
				"TvdbGuideDataSource_Impl [properties={eclipselink.json.include-root=false, eclipselink.oxm.metadata-source=oxm/guide-tvdb-oxm.xml, eclipselink.media-type=application/json}, headers={Accept=application/json}, validPeriodInMs=0, ]",
				dataSource.toString());
	}

	@Test
	public void apikeyTest() {
		Assertions.assertNull(dataSource.getApiKey());
		dataSource.setApiKey("VALUE");
		Assertions.assertEquals("VALUE", dataSource.getApiKey());
	}

	@Test
	public void validPeriodInMsTest() {
		Assertions.assertEquals(0, dataSource.getValidPeriodInMs());
		dataSource.setValidPeriodInMs(Integer.MAX_VALUE);
		Assertions.assertEquals(Integer.MAX_VALUE, dataSource.getValidPeriodInMs());
	}

	@Test
	public void buildUrlExceptionTest() throws GuideException {
		GuideException ex = Assertions.assertThrows(GuideException.class, () -> {
			dataSource.buildUrl("JIBBERISH");
		});
		Assertions.assertEquals("java.net.MalformedURLException: no protocol: JIBBERISH", ex.getMessage());
	}

	@Test
	public void buildRequestHeadersTest() throws Exception {
		dataSource.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken oldToken = new TvdbToken();
		oldToken.setValue("TOKEN");

		dataSource.setToken(oldToken);

		Map<String, String> headers = dataSource.buildRequestHeaders();
		Assertions.assertEquals(2, headers.size());
		Assertions.assertEquals("application/json", headers.get("Accept"));
		Assertions.assertEquals("Bearer TOKEN", headers.get("Authorization"));
	}

	@Test
	public void buildGetTest() throws Exception {
		URL url = new URL("http://test.com");
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("HEADER", "VALUE");
		Request request = dataSource.buildGet(url, headers);
		Assertions.assertEquals("http://test.com/", request.url().toString());
		Assertions.assertEquals(1, request.headers().size());
		Assertions.assertEquals("VALUE", request.header("HEADER"));
		Assertions.assertEquals("GET", request.method());
		Assertions.assertNull(request.body());
	}

	@Test
	public void buildPostTest() throws Exception {
		URL url = new URL("http://test.com");
		TvdbApiKey key = new TvdbApiKey();
		key.setValue("VALUE");
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/xml");
		Request request = dataSource.buildPost(url, key, headers);
		Assertions.assertEquals("http://test.com/", request.url().url().toString());
		Assertions.assertEquals(1, request.headers().size());
		Assertions.assertEquals("application/xml", request.header("Content-Type"));
		Assertions.assertEquals("POST", request.method());
		Assertions.assertEquals(MediaType.parse("application/xml"), request.body().contentType());
		Assertions.assertEquals(18, request.body().contentLength());
		Buffer buffer = new Buffer();
		request.body().writeTo(buffer);
		Assertions.assertEquals("[text={\"apikey\":\"VALUE\"}]", buffer.readByteString().toString());
	}

	@Test
	public void buildPostNoContentTypeTest() throws Exception {
		URL url = new URL("http://test.com");
		TvdbApiKey key = new TvdbApiKey();
		key.setValue("VALUE");
		Map<String, String> headers = new HashMap<String, String>();
		Request request = dataSource.buildPost(url, key, headers);
		Assertions.assertEquals("http://test.com/", request.url().url().toString());
		Assertions.assertEquals(1, request.headers().size());
		Assertions.assertEquals("application/json", request.header("Content-Type"));
		Assertions.assertEquals("POST", request.method());
		Assertions.assertEquals(MediaType.parse("application/json"), request.body().contentType());
		Assertions.assertEquals(18, request.body().contentLength());
		Buffer buffer = new Buffer();
		request.body().writeTo(buffer);
		Assertions.assertEquals("[text={\"apikey\":\"VALUE\"}]", buffer.readByteString().toString());
	}

	@Test
	public void buildPostBodyExceptionTest() throws Exception {
		URL url = new URL("http://test.com");
		Map<String, String> headers = new HashMap<String, String>();
		GuideException ex = Assertions.assertThrows(GuideException.class, () -> {
			dataSource.buildPost(url, new Object(), headers);
		});

		Assertions.assertEquals("Unable to marshal class java.lang.Object", ex.getMessage());

	}

	@Test
	public void executeRequestTest() throws Exception {
		String responseBody = "{\"token\": \"NEW_TOKEN\"}";

		MockResponse mockResponse = new MockResponse().setResponseCode(200)
				.addHeader("Content-Type", "application/json").setBody(responseBody);

		mockedServer.enqueue(mockResponse);

		URL url = new URL("http://localhost:8089/refresh_token");
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Accept", "application/json");
		Request getRequest = dataSource.buildGet(url, headers);

		TvdbToken token = dataSource.executeRequest(getRequest, TvdbToken.class);
		RecordedRequest request = mockedServer.takeRequest();
		Assertions.assertEquals("/refresh_token", request.getPath());
		Assertions.assertEquals("application/json", request.getHeader("Accept"));

		Assertions.assertEquals("NEW_TOKEN", token.getValue());
	}

	@Test
	public void executeRequestTimeoutExceptionTest() throws Exception {

		MockResponse mockResponse = new MockResponse().setSocketPolicy(SocketPolicy.NO_RESPONSE);

		mockedServer.enqueue(mockResponse);

		URL url = new URL("http://localhost:8089/refresh_token");
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Accept", "application/json");
		Request getRequest = dataSource.buildGet(url, headers);

		GuideTimeoutException ex = Assertions.assertThrows(GuideTimeoutException.class, () -> {
			dataSource.executeRequest(getRequest, TvdbToken.class);
		});

		RecordedRequest request = mockedServer.takeRequest();
		Assertions.assertEquals("/refresh_token", request.getPath());
		Assertions.assertEquals("application/json", request.getHeader("Accept"));
		Assertions.assertEquals("Timeout for http://localhost:8089/refresh_token", ex.getMessage());
	}

	@Test
	public void executeRequestNotFoundExceptionTest() throws Exception {

		MockResponse mockResponse = new MockResponse().setResponseCode(404);

		mockedServer.enqueue(mockResponse);

		URL url = new URL("http://localhost:8089/refresh_token");
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Accept", "application/json");
		Request getRequest = dataSource.buildGet(url, headers);

		GuideNotFoundException ex = Assertions.assertThrows(GuideNotFoundException.class, () -> {
			dataSource.executeRequest(getRequest, TvdbToken.class);
		});

		RecordedRequest request = mockedServer.takeRequest();
		Assertions.assertEquals("/refresh_token", request.getPath());
		Assertions.assertEquals("application/json", request.getHeader("Accept"));
		// TODO better error message
		Assertions.assertNull(ex.getMessage());
	}

	@Test
	public void executeRequestUnauthorisedExceptionTest() throws Exception {

		MockResponse mockResponse = new MockResponse().setResponseCode(401);

		mockedServer.enqueue(mockResponse);

		URL url = new URL("http://localhost:8089/refresh_token");
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Accept", "application/json");
		Request getRequest = dataSource.buildGet(url, headers);

		GuideAuthorizationException ex = Assertions.assertThrows(GuideAuthorizationException.class, () -> {
			dataSource.executeRequest(getRequest, TvdbToken.class);
		});

		RecordedRequest request = mockedServer.takeRequest();
		Assertions.assertEquals("/refresh_token", request.getPath());
		Assertions.assertEquals("application/json", request.getHeader("Accept"));
		// TODO better message
		//Assertions.assertEquals("", ex.getMessage());
	}

	@Test
	public void executeRequestForbiddenExceptionTest() throws Exception {

		MockResponse mockResponse = new MockResponse().setResponseCode(403);

		mockedServer.enqueue(mockResponse);

		URL url = new URL("http://localhost:8089/refresh_token");
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Accept", "application/json");
		Request getRequest = dataSource.buildGet(url, headers);

		GuideAuthorizationException ex = Assertions.assertThrows(GuideAuthorizationException.class, () -> {
			dataSource.executeRequest(getRequest, TvdbToken.class);
		});

		RecordedRequest request = mockedServer.takeRequest();
		Assertions.assertEquals("/refresh_token", request.getPath());
		Assertions.assertEquals("application/json", request.getHeader("Accept"));
		// TODO better message
		// Assertions.assertEquals("", ex.getMessage());
	}

	@Test
	public void executeRequestResponseExceptionTest() throws Exception {

		MockResponse mockResponse = new MockResponse().setResponseCode(500);

		mockedServer.enqueue(mockResponse);

		URL url = new URL("http://localhost:8089/refresh_token");
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Accept", "application/json");
		Request getRequest = dataSource.buildGet(url, headers);

		GuideResponseException ex = Assertions.assertThrows(GuideResponseException.class, () -> {
			dataSource.executeRequest(getRequest, TvdbToken.class);
		});

		RecordedRequest request = mockedServer.takeRequest();
		Assertions.assertEquals("/refresh_token", request.getPath());
		Assertions.assertEquals("application/json", request.getHeader("Accept"));
		Assertions.assertEquals(
				"HTTP response code 500 with message \"Server Error\" for url http://localhost:8089/refresh_token",
				ex.getMessage());
	}

	@Test
	public void executeRequestResponseBodyExceptionTest() throws Exception {

		MockResponse mockResponse = new MockResponse()
				.setResponseCode(200)
				.setHeader("Content-Type","application/json");

		mockedServer.enqueue(mockResponse);

		URL url = new URL("http://localhost:8089/refresh_token");
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Accept", "application/json");
		Request getRequest = dataSource.buildGet(url, headers);

		GuideResponseBodyException ex = Assertions.assertThrows(GuideResponseBodyException.class, () -> {
			dataSource.executeRequest(getRequest, TvdbToken.class);
		});

		RecordedRequest request = mockedServer.takeRequest();
		Assertions.assertEquals("/refresh_token", request.getPath());
		Assertions.assertEquals("application/json", request.getHeader("Accept"));
		Assertions.assertEquals("Unable to parse response for url http://localhost:8089/refresh_token \n",
				ex.getMessage());
	}

	@Test
	public void executeRequestExceptionTest() throws Exception {

		URL url = new URL("http://localhost:8089/refresh_token");
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Accept", "application/json");
		Request getRequest = dataSource.buildGet(url, headers);

		MockResponse mockResponse = new MockResponse()
				.setResponseCode(300)
				.setHeader("Content-Type","application/json");

		mockedServer.enqueue(mockResponse);
		
		GuideException ex = Assertions.assertThrows(GuideException.class, () -> {
			dataSource.executeRequest(getRequest, TvdbToken.class);
		});

		RecordedRequest request = mockedServer.takeRequest();
		Assertions.assertEquals("/refresh_token", request.getPath());
		Assertions.assertEquals("application/json", request.getHeader("Accept"));
		Assertions.assertEquals("HTTP response code 300 with message \"Redirection\" for url http://localhost:8089/refresh_token", ex.getMessage());
	}

	@Test
	public void loginTest() throws Exception {
		String requestBody = "{\"apikey\":\"APIKEY\"}";
		String responseBody = "{\"token\":\"TOKEN\"}";

		MockResponse mockResponse = new MockResponse().setResponseCode(200)
				.addHeader("Content-Type", "application/json").setBody(responseBody);

		mockedServer.enqueue(mockResponse);

		dataSource.setBaseUri("http://localhost:8089");

		TvdbToken token = dataSource.login("APIKEY");
		RecordedRequest request = mockedServer.takeRequest();
		Assertions.assertEquals("/login", request.getPath());
		Assertions.assertEquals("POST", request.getMethod());
		Assertions.assertEquals("application/json", request.getHeader("Accept"));
		Assertions.assertEquals(requestBody, request.getBody().readUtf8());

		Assertions.assertNotNull(token);
		Assertions.assertEquals("TOKEN", token.getValue());
	}

	@Test
	public void refreshTokenTest() throws Exception {

		String responseBody = "{\"token\":\"NEW_TOKEN\"}";

		MockResponse mockResponse = new MockResponse().setResponseCode(200)
				.addHeader("Content-Type", "application/json").setBody(responseBody);

		mockedServer.enqueue(mockResponse);

		dataSource.setBaseUri("http://localhost:8089");

		TvdbToken oldToken = new TvdbToken();
		oldToken.setValue("OLD_TOKEN");

		dataSource.setToken(oldToken);

		TvdbToken newToken = dataSource.refreshToken();
		RecordedRequest request = mockedServer.takeRequest();
		Assertions.assertEquals("/refresh_token", request.getPath());
		Assertions.assertEquals("GET", request.getMethod());
		Assertions.assertEquals("application/json", request.getHeader("Accept"));
		Assertions.assertEquals("Bearer OLD_TOKEN", request.getHeader("Authorization"));

		Assertions.assertNotNull(newToken);
		Assertions.assertEquals("NEW_TOKEN", newToken.getValue());
	}

	@Test
	public void getTokenUseExistingTest() throws Exception {
		dataSource.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dataSource.setToken(token);
		TvdbToken returnToken = dataSource.getToken();
		Assertions.assertEquals(token, returnToken);
	}

	@Test
	public void getTokenLoginTest() throws Exception {
		String requestBody = "{\"apikey\":\"APIKEY\"}";
		String responseBody = "{\"token\":\"TOKEN\"}";

		MockResponse mockResponse = new MockResponse().setResponseCode(200)
				.addHeader("Content-Type", "application/json").setBody(responseBody);

		mockedServer.enqueue(mockResponse);

		dataSource.setBaseUri("http://localhost:8089");

		dataSource.setApiKey("APIKEY");

		TvdbToken token = dataSource.getToken();
		RecordedRequest request = mockedServer.takeRequest();
		Assertions.assertEquals("/login", request.getPath());
		Assertions.assertEquals("POST", request.getMethod());
		Assertions.assertEquals("application/json", request.getHeader("Accept"));
		Assertions.assertEquals(requestBody, request.getBody().readUtf8());

		Assertions.assertNotNull(token);
		Assertions.assertEquals("TOKEN", token.getValue());
	}

	@Test
	public void getTokenRefreshTest() throws Exception {
		String responseBody = "{\"token\": \"NEW_TOKEN\"}";

		MockResponse mockResponse = new MockResponse().setResponseCode(200)
				.addHeader("Content-Type", "application/json").setBody(responseBody);

		mockedServer.enqueue(mockResponse);

		dataSource.setBaseUri("http://localhost:8089");

		TvdbToken oldToken = new TvdbToken();
		oldToken.setValue("OLD_TOKEN");
		dataSource.setToken(oldToken);

		dataSource.setValidPeriodInMs(100000);

		TvdbToken newToken = dataSource.getToken();
		RecordedRequest request = mockedServer.takeRequest();
		Assertions.assertEquals("/refresh_token", request.getPath());
		Assertions.assertEquals("GET", request.getMethod());
		Assertions.assertEquals("application/json", request.getHeader("Accept"));
		Assertions.assertEquals("Bearer OLD_TOKEN", request.getHeader("Authorization"));

		Assertions.assertNotNull(newToken);
		Assertions.assertEquals("NEW_TOKEN", newToken.getValue());
	}

	@Test
	public void searchTest() throws Exception {
		String responseBody = "{\"data\": [{\"overview\":\"DESCRIPTION\",\"id\":\"GUIDEID\",\"seriesName\":\"TITLE\",\"firstAired\":\"2005-03-11\"}]}";

		MockResponse mockResponse = new MockResponse().setResponseCode(200)
				.addHeader("Content-Type", "application/json").setBody(responseBody);

		mockedServer.enqueue(mockResponse);

		dataSource.setBaseUri("http://localhost:8089");

		dataSource.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dataSource.setToken(token);

		List<GuideSearchResult> results = dataSource.search("TITLE");
		RecordedRequest request = mockedServer.takeRequest();
		Assertions.assertEquals("/search/series?name=TITLE", request.getPath());
		Assertions.assertEquals("GET", request.getMethod());
		Assertions.assertEquals("application/json", request.getHeader("Accept"));
		Assertions.assertEquals("Bearer TOKEN", request.getHeader("Authorization"));

		Assertions.assertNotNull(results);
		Assertions.assertEquals(1, results.size());
		Assertions.assertEquals("GUIDEID", results.get(0).getGuideId());
		Assertions.assertEquals("DESCRIPTION", results.get(0).getDescription());
		Assertions.assertEquals("TITLE", results.get(0).getTitle());
		Assertions.assertEquals(LocalDate.parse("2005-03-11"), results.get(0).getFirstAired());
	}

	@Test
	public void searchInvalidSeriesTest() throws Exception {
		String responseBody = "{\"data\": [{\"overview\":\"DESCRIPTION\",\"id\":\"GUIDEID\",\"seriesName\":\"** Invalid Series **\",\"firstAired\":\"2005-03-11\"}]}";

		MockResponse mockResponse = new MockResponse().setResponseCode(200)
				.addHeader("Content-Type", "application/json").setBody(responseBody);

		mockedServer.enqueue(mockResponse);

		dataSource.setBaseUri("http://localhost:8089");

		dataSource.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dataSource.setToken(token);

		List<GuideSearchResult> results = dataSource.search("TITLE");
		RecordedRequest request = mockedServer.takeRequest();
		Assertions.assertEquals("/search/series?name=TITLE", request.getPath());
		Assertions.assertEquals("GET", request.getMethod());
		Assertions.assertEquals("application/json", request.getHeader("Accept"));
		Assertions.assertEquals("Bearer TOKEN", request.getHeader("Authorization"));

		Assertions.assertNotNull(results);
		Assertions.assertEquals(0, results.size());
	}

	@Test
	public void infoTest() throws Exception {
		String responseBody = "{\"data\": {\"overview\":\"DESCRIPTION\",\"id\":\"GUIDEID\",\"seriesName\":\"TITLE\","
				+ "\"firstAired\":\"2015-12-28\",\"status\":\"Ended\",\"network\":\"CBS\",\"runtime\":\"45\","
				+ "\"airsDayOfWeek\":\"Monday\",\"airsTime\":\"8:00pm\",\"imdbId\":\"IMDB\",\"genre\":[\"GENRE\"],"
				+ "\"lastUpdated\":1454486401}}";

		MockResponse mockResponse = new MockResponse().setResponseCode(200)
				.addHeader("Content-Type", "application/json").setBody(responseBody);

		mockedServer.enqueue(mockResponse);

		dataSource.setBaseUri("http://localhost:8089");

		dataSource.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dataSource.setToken(token);

		GuideInfo info = dataSource.info("GUIDEID");
		RecordedRequest request = mockedServer.takeRequest();
		Assertions.assertEquals("/series/GUIDEID", request.getPath());
		Assertions.assertEquals("GET", request.getMethod());
		Assertions.assertEquals("application/json", request.getHeader("Accept"));
		Assertions.assertEquals("Bearer TOKEN", request.getHeader("Authorization"));

		Assertions.assertNotNull(info);
		Assertions.assertEquals("GUIDEID", info.getGuideId());
		Assertions.assertEquals("DESCRIPTION", info.getDescription());
		Assertions.assertEquals("TITLE", info.getTitle());
		Assertions.assertEquals(LocalDate.parse("2015-12-28"), info.getFirstAired());
		Assertions.assertEquals(1, info.getAirDays().size());
		Assertions.assertEquals(AiringDay.MONDAY, info.getAirDays().get(0));
		Assertions.assertFalse(info.isAiring());
		Assertions.assertEquals(LocalTime.parse("20:00"), info.getAirTime());
		Assertions.assertEquals(1, info.getGenres().size());
		Assertions.assertEquals("GENRE", info.getGenres().get(0));
		Assertions.assertEquals("America/Los_Angeles", info.getTimezone());
		Assertions.assertEquals(45, info.getRunningTime());
		Assertions.assertEquals(LocalDate.parse("2016-02-03"), info.getLastUpdated());
	}

	@Test
	public void episodesTest() throws Exception {
		String responseBody = "{\"links\": {\"first\": null,\"last\": 0,\"next\": 0,\"prev\": 0},"
				+ "\"data\": [{\"absoluteNumber\":0,\"airedEpisodeNumber\":-2147483648,"
				+ "\"airedSeason\":\"2147483647\",\"episodeName\":\"TITLE\",\"firstAired\":\"2015-09-29\"}]};";

		MockResponse mockResponse = new MockResponse().setResponseCode(200)
				.addHeader("Content-Type", "application/json").setBody(responseBody);

		mockedServer.enqueue(mockResponse);

		dataSource.setBaseUri("http://localhost:8089");

		dataSource.setValidPeriodInMs(Integer.MAX_VALUE);

		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dataSource.setToken(token);

		TvdbEpisodes result = dataSource.episodes("GUIDEID", 1);
		RecordedRequest request = mockedServer.takeRequest();
		Assertions.assertEquals("/series/GUIDEID/episodes?page=1", request.getPath());
		Assertions.assertEquals("GET", request.getMethod());
		Assertions.assertEquals("application/json", request.getHeader("Accept"));
		Assertions.assertEquals("Bearer TOKEN", request.getHeader("Authorization"));

		Assertions.assertNotNull(result);
		Assertions.assertNull(result.getFirst());
		Assertions.assertEquals(0, result.getLast().intValue());
		Assertions.assertEquals(0, result.getNext().intValue());
		Assertions.assertEquals(0, result.getPrevious().intValue());
		Assertions.assertNotNull(result.getEpisodes());
		Assertions.assertEquals(1, result.getEpisodes().size());
		Assertions.assertEquals(0, result.getEpisodes().get(0).getProductionNum().intValue());
		Assertions.assertEquals(Integer.MIN_VALUE, result.getEpisodes().get(0).getEpisodeNum().intValue());
		Assertions.assertEquals(Integer.MAX_VALUE, result.getEpisodes().get(0).getSeasonNum().intValue());
		Assertions.assertEquals("TITLE", result.getEpisodes().get(0).getTitle());
		Assertions.assertEquals(LocalDate.parse("2015-09-29"), result.getEpisodes().get(0).getAirDate());
	}

	@Test
	public void episodeListNoNextTest() throws Exception {
		String responseBody = "{\"links\": {\"first\": 1,\"last\": 1,\"next\": 0,\"prev\": 0},"
				+ "\"data\": [{\"absoluteNumber\":0,\"airedEpisodeNumber\":-2147483648,"
				+ "\"airedSeason\":\"2147483647\",\"episodeName\":\"TITLE\",\"firstAired\":\"2015-09-29\"}]};";

		MockResponse mockResponse = new MockResponse().setResponseCode(200)
				.addHeader("Content-Type", "application/json").setBody(responseBody);

		mockedServer.enqueue(mockResponse);

		dataSource.setBaseUri("http://localhost:8089");

		dataSource.setValidPeriodInMs(Integer.MAX_VALUE);

		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dataSource.setToken(token);

		List<GuideEpisode> episodes = dataSource.episodes("GUIDEID");
		RecordedRequest request = mockedServer.takeRequest();
		Assertions.assertEquals("/series/GUIDEID/episodes?page=1", request.getPath());
		Assertions.assertEquals("GET", request.getMethod());
		Assertions.assertEquals("application/json", request.getHeader("Accept"));
		Assertions.assertEquals("Bearer TOKEN", request.getHeader("Authorization"));

		Assertions.assertNotNull(episodes);
		Assertions.assertEquals(1, episodes.size());
		Assertions.assertEquals(0, episodes.get(0).getProductionNum().intValue());
		Assertions.assertEquals(Integer.MIN_VALUE, episodes.get(0).getEpisodeNum().intValue());
		Assertions.assertEquals(Integer.MAX_VALUE, episodes.get(0).getSeasonNum().intValue());
		Assertions.assertEquals("TITLE", episodes.get(0).getTitle());
		Assertions.assertEquals(LocalDate.parse("2015-09-29"), episodes.get(0).getAirDate());
	}

	@Test
	public void episodeListHasNextTest() throws Exception {
		String responseBody_one = "{\"links\": {\"first\": 1,\"last\": 2,\"next\": 2,\"prev\": 0},"
				+ "\"data\": [{\"absoluteNumber\":0,\"airedEpisodeNumber\":-2147483648,"
				+ "\"airedSeason\":\"2147483647\",\"episodeName\":\"TITLE\",\"firstAired\":\"2015-09-29\"}]};";

		String responseBody_two = "{\"links\": {\"first\": 2,\"last\": 2,\"next\": 0,\"prev\": 1},"
				+ "\"data\": [{\"absoluteNumber\":0,\"airedEpisodeNumber\":-2147483648,"
				+ "\"airedSeason\":\"2147483647\",\"episodeName\":\"TITLE_TWO\",\"firstAired\":\"2005-09-29\"}]};";

		MockResponse mockResponse_one = new MockResponse().setResponseCode(200)
				.addHeader("Content-Type", "application/json").setBody(responseBody_one);

		mockedServer.enqueue(mockResponse_one);

		MockResponse mockResponse_two = new MockResponse().setResponseCode(200)
				.addHeader("Content-Type", "application/json").setBody(responseBody_two);

		mockedServer.enqueue(mockResponse_two);

		dataSource.setBaseUri("http://localhost:8089");

		dataSource.setValidPeriodInMs(Integer.MAX_VALUE);

		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dataSource.setToken(token);

		List<GuideEpisode> episodes = dataSource.episodes("GUIDEID");
		RecordedRequest request_one = mockedServer.takeRequest();
		Assertions.assertEquals("/series/GUIDEID/episodes?page=1", request_one.getPath());
		Assertions.assertEquals("GET", request_one.getMethod());
		Assertions.assertEquals("application/json", request_one.getHeader("Accept"));
		Assertions.assertEquals("Bearer TOKEN", request_one.getHeader("Authorization"));

		RecordedRequest request_two = mockedServer.takeRequest();
		Assertions.assertEquals("/series/GUIDEID/episodes?page=2", request_two.getPath());
		Assertions.assertEquals("GET", request_two.getMethod());
		Assertions.assertEquals("application/json", request_two.getHeader("Accept"));
		Assertions.assertEquals("Bearer TOKEN", request_two.getHeader("Authorization"));

		Assertions.assertNotNull(episodes);
		Assertions.assertEquals(2, episodes.size());
		Assertions.assertEquals(0, episodes.get(0).getProductionNum().intValue());
		Assertions.assertEquals(Integer.MIN_VALUE, episodes.get(0).getEpisodeNum().intValue());
		Assertions.assertEquals(Integer.MAX_VALUE, episodes.get(0).getSeasonNum().intValue());
		Assertions.assertEquals("TITLE", episodes.get(0).getTitle());
		Assertions.assertEquals(LocalDate.parse("2015-09-29"), episodes.get(0).getAirDate());
		Assertions.assertEquals(0, episodes.get(1).getProductionNum().intValue());
		Assertions.assertEquals(Integer.MIN_VALUE, episodes.get(1).getEpisodeNum().intValue());
		Assertions.assertEquals(Integer.MAX_VALUE, episodes.get(1).getSeasonNum().intValue());
		Assertions.assertEquals("TITLE_TWO", episodes.get(1).getTitle());
		Assertions.assertEquals(LocalDate.parse("2005-09-29"), episodes.get(1).getAirDate());
	}
}
