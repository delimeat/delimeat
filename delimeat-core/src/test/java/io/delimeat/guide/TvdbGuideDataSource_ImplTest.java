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

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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

	@BeforeClass
	public static void beforeClass() throws IOException{
		mockedServer.start(PORT);
	}
	
	@AfterClass
	public static void tearDown() throws IOException{
		mockedServer.shutdown();
	}
	
	@Before
	public void setUp() throws Exception {
		dataSource = new TvdbGuideDataSource_Impl();
	}

	@Test
	public void baseUriTest() throws URISyntaxException {
		Assert.assertNull(dataSource.getBaseUri());
		dataSource.setBaseUri("http://test.com");
		Assert.assertEquals("http://test.com", dataSource.getBaseUri());
	}

	@Test
	public void guideSourceTest() {
		Assert.assertEquals(GuideSource.TVDB, dataSource.getGuideSource());
	}
		
	@Test
	public void propertiesTest(){
		Assert.assertEquals(3,dataSource.getProperties().size());
		Assert.assertEquals("oxm/guide-tvdb-oxm.xml", dataSource.getProperties().get("eclipselink.oxm.metadata-source"));
		Assert.assertEquals("application/json", dataSource.getProperties().get("eclipselink.media-type"));
		Assert.assertEquals("false",dataSource.getProperties().get("eclipselink.json.include-root").toString());
	}
	
	@Test
	public void toStringTest(){
		Assert.assertEquals("TvdbGuideDataSource_Impl [properties={eclipselink.json.include-root=false, eclipselink.oxm.metadata-source=oxm/guide-tvdb-oxm.xml, eclipselink.media-type=application/json}, headers={Accept=application/json}, validPeriodInMs=0, ]", dataSource.toString());
	}

	@Test
	public void apikeyTest() {
		Assert.assertNull(dataSource.getApiKey());
		dataSource.setApiKey("VALUE");
		Assert.assertEquals("VALUE", dataSource.getApiKey());
	}

	@Test
	public void validPeriodInMsTest() {
		Assert.assertEquals(0, dataSource.getValidPeriodInMs());
		dataSource.setValidPeriodInMs(Integer.MAX_VALUE);
		Assert.assertEquals(Integer.MAX_VALUE, dataSource.getValidPeriodInMs());
	}
	
	@Test(expected=GuideException.class)
	public void buildUrlExceptionTest() throws GuideException{
		dataSource.buildUrl("JIBBERISH");
	}
	
	@Test
	public void buildRequestHeadersTest() throws Exception{
		dataSource.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken oldToken = new TvdbToken();
		oldToken.setValue("TOKEN");

		dataSource.setToken(oldToken);
		
		Map<String, String> headers = dataSource.buildRequestHeaders();
		Assert.assertEquals(2, headers.size());
		Assert.assertEquals("application/json", headers.get("Accept"));
		Assert.assertEquals("Bearer TOKEN", headers.get("Authorization"));
	}

	@Test
	public void buildGetTest() throws Exception{
		URL url = new URL("http://test.com");
		Map<String, String> headers = new HashMap<String,String>();
		headers.put("HEADER", "VALUE");
		Request request = dataSource.buildGet(url,headers);
		Assert.assertEquals("http://test.com/", request.url().toString());
		Assert.assertEquals(1,request.headers().size());
		Assert.assertEquals("VALUE", request.header("HEADER"));
		Assert.assertEquals("GET", request.method());
		Assert.assertNull(request.body());
	}
	
	@Test
	public void buildPostTest() throws Exception{
		URL url = new URL("http://test.com");
		TvdbApiKey key = new TvdbApiKey();
		key.setValue("VALUE");
		Map<String, String> headers = new HashMap<String,String>();
		headers.put("Content-Type", "application/xml");
		Request request = dataSource.buildPost(url,key,headers);
		Assert.assertEquals("http://test.com/", request.url().url().toString());
		Assert.assertEquals(1,request.headers().size());
		Assert.assertEquals("application/xml", request.header("Content-Type"));
		Assert.assertEquals("POST", request.method());
		Assert.assertEquals(MediaType.parse("application/xml"),request.body().contentType());
		Assert.assertEquals(18, request.body().contentLength());
		Buffer buffer = new Buffer();
		request.body().writeTo(buffer);
		Assert.assertEquals("[text={\"apikey\":\"VALUE\"}]", buffer.readByteString().toString());
	}
	
	@Test
	public void buildPostNoContentTypeTest() throws Exception{
		URL url = new URL("http://test.com");
		TvdbApiKey key = new TvdbApiKey();
		key.setValue("VALUE");
		Map<String, String> headers = new HashMap<String,String>();
		Request request = dataSource.buildPost(url,key,headers);
		Assert.assertEquals("http://test.com/", request.url().url().toString());
		Assert.assertEquals(1,request.headers().size());
		Assert.assertEquals("application/json", request.header("Content-Type"));
		Assert.assertEquals("POST", request.method());
		Assert.assertEquals(MediaType.parse("application/json"),request.body().contentType());
		Assert.assertEquals(18, request.body().contentLength());
		Buffer buffer = new Buffer();
		request.body().writeTo(buffer);
		Assert.assertEquals("[text={\"apikey\":\"VALUE\"}]", buffer.readByteString().toString());
	}
	
	@Test(expected=GuideException.class)
	public void buildPostBodyExceptionTest() throws Exception{
		URL url = new URL("http://test.com");
		Map<String, String> headers = new HashMap<String,String>();
		dataSource.buildPost(url,new Object(),headers);
	}
	
	@Test
	public void executeRequestTest() throws Exception{
		String responseBody = "{\"token\": \"NEW_TOKEN\"}";
		
		MockResponse mockResponse = new MockResponse()
				.setResponseCode(200)
			    .addHeader("Content-Type", "application/json")
			    .setBody(responseBody);
		
		mockedServer.enqueue(mockResponse);
		
		URL url = new URL("http://localhost:8089/refresh_token");
		Map<String, String> headers = new HashMap<String,String>();
		headers.put("Accept", "application/json");
		Request getRequest = dataSource.buildGet(url, headers);
		
		TvdbToken token = dataSource.executeRequest(getRequest, TvdbToken.class);
		RecordedRequest request = mockedServer.takeRequest();
		Assert.assertEquals("/refresh_token", request.getPath());
		Assert.assertEquals("application/json", request.getHeader("Accept"));
		
		Assert.assertEquals("NEW_TOKEN", token.getValue());
	}
	
	@Test
	public void executeRequestTimeoutExceptionTest() throws Exception{
		
		MockResponse mockResponse = new MockResponse()
			    .setSocketPolicy(SocketPolicy.NO_RESPONSE);
		
		mockedServer.enqueue(mockResponse);
		
		URL url = new URL("http://localhost:8089/refresh_token");
		Map<String, String> headers = new HashMap<String,String>();
		headers.put("Accept", "application/json");
		Request getRequest = dataSource.buildGet(url, headers);
		
		try{
			dataSource.executeRequest(getRequest, TvdbToken.class);
		}catch(GuideTimeoutException ex){
			RecordedRequest request = mockedServer.takeRequest();
			Assert.assertEquals("/refresh_token", request.getPath());
			Assert.assertEquals("application/json", request.getHeader("Accept"));
			return;
		}
		Assert.fail();
	}
	
	@Test
	public void executeRequestNotFoundExceptionTest() throws Exception{
		
		MockResponse mockResponse = new MockResponse()
				.setResponseCode(404);
		
		mockedServer.enqueue(mockResponse);
		
		URL url = new URL("http://localhost:8089/refresh_token");
		Map<String, String> headers = new HashMap<String,String>();
		headers.put("Accept", "application/json");
		Request getRequest = dataSource.buildGet(url, headers);
		
		try{
			dataSource.executeRequest(getRequest, TvdbToken.class);
		}catch(GuideNotFoundException ex){
			RecordedRequest request = mockedServer.takeRequest();
			Assert.assertEquals("/refresh_token", request.getPath());
			Assert.assertEquals("application/json", request.getHeader("Accept"));
			return;
		}
		Assert.fail();
	}
	
	@Test
	public void executeRequestUnauthorisedExceptionTest() throws Exception{
		
		MockResponse mockResponse = new MockResponse()
				.setResponseCode(401);
		
		mockedServer.enqueue(mockResponse);
		
		URL url = new URL("http://localhost:8089/refresh_token");
		Map<String, String> headers = new HashMap<String,String>();
		headers.put("Accept", "application/json");
		Request getRequest = dataSource.buildGet(url, headers);
		
		try{
			dataSource.executeRequest(getRequest, TvdbToken.class);
		}catch(GuideAuthorizationException ex){
			RecordedRequest request = mockedServer.takeRequest();
			Assert.assertEquals("/refresh_token", request.getPath());
			Assert.assertEquals("application/json", request.getHeader("Accept"));
			return;
		}
		Assert.fail();
	}
	
	@Test
	public void executeRequestForbiddenExceptionTest() throws Exception{
		
		MockResponse mockResponse = new MockResponse()
				.setResponseCode(403);
		
		mockedServer.enqueue(mockResponse);
		
		URL url = new URL("http://localhost:8089/refresh_token");
		Map<String, String> headers = new HashMap<String,String>();
		headers.put("Accept", "application/json");
		Request getRequest = dataSource.buildGet(url, headers);
		
		try{
			dataSource.executeRequest(getRequest, TvdbToken.class);
		}catch(GuideAuthorizationException ex){
			RecordedRequest request = mockedServer.takeRequest();
			Assert.assertEquals("/refresh_token", request.getPath());
			Assert.assertEquals("application/json", request.getHeader("Accept"));
			return;
		}
		Assert.fail();
	}
	
	@Test
	public void executeRequestResponseExceptionTest() throws Exception{
		
		MockResponse mockResponse = new MockResponse()
				.setResponseCode(500);
		
		mockedServer.enqueue(mockResponse);
		
		URL url = new URL("http://localhost:8089/refresh_token");
		Map<String, String> headers = new HashMap<String,String>();
		headers.put("Accept", "application/json");
		Request getRequest = dataSource.buildGet(url, headers);
		
		try{
			dataSource.executeRequest(getRequest, TvdbToken.class);
		}catch(GuideResponseException ex){
			RecordedRequest request = mockedServer.takeRequest();
			Assert.assertEquals("/refresh_token", request.getPath());
			Assert.assertEquals("application/json", request.getHeader("Accept"));
			return;
		}
		Assert.fail();
	}
	
	@Test
	public void executeRequestResponseBodyExceptionTest() throws Exception{
		
		MockResponse mockResponse = new MockResponse()
				.setResponseCode(200)
				.setHeader("Content-Type", "application/json");
		
		mockedServer.enqueue(mockResponse);
		
		URL url = new URL("http://localhost:8089/refresh_token");
		Map<String, String> headers = new HashMap<String,String>();
		headers.put("Accept", "application/json");
		Request getRequest = dataSource.buildGet(url, headers);
		
		try{
			dataSource.executeRequest(getRequest, TvdbToken.class);
		}catch(GuideResponseBodyException ex){
			RecordedRequest request = mockedServer.takeRequest();
			Assert.assertEquals("/refresh_token", request.getPath());
			Assert.assertEquals("application/json", request.getHeader("Accept"));
			return;
		}
		Assert.fail();
	}

	
	@Test
	public void executeRequestExceptionTest() throws Exception{		
		
		URL url = new URL("http://localhost:9090/refresh_token");
		Map<String, String> headers = new HashMap<String,String>();
		headers.put("Accept", "application/json");
		Request getRequest = dataSource.buildGet(url, headers);
		
		try{
			dataSource.executeRequest(getRequest, TvdbToken.class);
		}catch(GuideException ex){
			return;
		}
		Assert.fail();
	}

	@Test
	public void loginTest() throws Exception {
		String requestBody = "{\"apikey\":\"APIKEY\"}";
		String responseBody = "{\"token\":\"TOKEN\"}";
		
		MockResponse mockResponse = new MockResponse()
				.setResponseCode(200)
			    .addHeader("Content-Type", "application/json")
			    .setBody(responseBody);
		
		mockedServer.enqueue(mockResponse);
		
		dataSource.setBaseUri("http://localhost:8089");
		
		TvdbToken token = dataSource.login("APIKEY");
		RecordedRequest request = mockedServer.takeRequest();
		Assert.assertEquals("/login", request.getPath());
		Assert.assertEquals("POST", request.getMethod());
		Assert.assertEquals("application/json", request.getHeader("Accept"));
		Assert.assertEquals(requestBody, request.getBody().readUtf8());
		
		
		Assert.assertNotNull(token);
		Assert.assertEquals("TOKEN", token.getValue());
	}

	@Test
	public void refreshTokenTest() throws Exception {
		
		String responseBody = "{\"token\":\"NEW_TOKEN\"}";
		
		MockResponse mockResponse = new MockResponse()
				.setResponseCode(200)
			    .addHeader("Content-Type", "application/json")
			    .setBody(responseBody);
		
		mockedServer.enqueue(mockResponse);
		
		dataSource.setBaseUri("http://localhost:8089");
		
		TvdbToken oldToken = new TvdbToken();
		oldToken.setValue("OLD_TOKEN");

		dataSource.setToken(oldToken);

		TvdbToken newToken = dataSource.refreshToken();
		RecordedRequest request = mockedServer.takeRequest();
		Assert.assertEquals("/refresh_token", request.getPath());
		Assert.assertEquals("GET", request.getMethod());
		Assert.assertEquals("application/json", request.getHeader("Accept"));
		Assert.assertEquals("Bearer OLD_TOKEN", request.getHeader("Authorization"));
		
		Assert.assertNotNull(newToken);
		Assert.assertEquals("NEW_TOKEN", newToken.getValue());
	}

	@Test
	public void getTokenUseExistingTest() throws Exception {
		dataSource.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dataSource.setToken(token);
		TvdbToken returnToken = dataSource.getToken();
		Assert.assertEquals(token, returnToken);
	}

	@Test
	public void getTokenLoginTest() throws Exception {
		String requestBody = "{\"apikey\":\"APIKEY\"}";
		String responseBody = "{\"token\":\"TOKEN\"}";
		
		MockResponse mockResponse = new MockResponse()
				.setResponseCode(200)
			    .addHeader("Content-Type", "application/json")
			    .setBody(responseBody);
		
		mockedServer.enqueue(mockResponse);
		
		dataSource.setBaseUri("http://localhost:8089");
		
		dataSource.setApiKey("APIKEY");

		TvdbToken token = dataSource.getToken();
		RecordedRequest request = mockedServer.takeRequest();
		Assert.assertEquals("/login", request.getPath());
		Assert.assertEquals("POST", request.getMethod());
		Assert.assertEquals("application/json", request.getHeader("Accept"));
		Assert.assertEquals(requestBody, request.getBody().readUtf8());
		
		Assert.assertNotNull(token);
		Assert.assertEquals("TOKEN", token.getValue());
	}

	@Test
	public void getTokenRefreshTest() throws Exception {
		String responseBody = "{\"token\": \"NEW_TOKEN\"}";	
		
		MockResponse mockResponse = new MockResponse()
				.setResponseCode(200)
			    .addHeader("Content-Type", "application/json")
			    .setBody(responseBody);
		
		mockedServer.enqueue(mockResponse);
		
		dataSource.setBaseUri("http://localhost:8089");
		
		TvdbToken oldToken = new TvdbToken();
		oldToken.setValue("OLD_TOKEN");
		dataSource.setToken(oldToken);

		dataSource.setValidPeriodInMs(100000);

		TvdbToken newToken = dataSource.getToken();
		RecordedRequest request = mockedServer.takeRequest();
		Assert.assertEquals("/refresh_token", request.getPath());
		Assert.assertEquals("GET", request.getMethod());
		Assert.assertEquals("application/json", request.getHeader("Accept"));
		Assert.assertEquals("Bearer OLD_TOKEN", request.getHeader("Authorization"));
		
		Assert.assertNotNull(newToken);
		Assert.assertEquals("NEW_TOKEN", newToken.getValue());
	}

	@Test
	public void searchTest() throws Exception {
		String responseBody = "{\"data\": [{\"overview\":\"DESCRIPTION\",\"id\":\"GUIDEID\",\"seriesName\":\"TITLE\",\"firstAired\":\"2005-03-11\"}]}";	

		MockResponse mockResponse = new MockResponse()
				.setResponseCode(200)
			    .addHeader("Content-Type", "application/json")
			    .setBody(responseBody);
		
		mockedServer.enqueue(mockResponse);
		
		dataSource.setBaseUri("http://localhost:8089");
		
		dataSource.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dataSource.setToken(token);

		List<GuideSearchResult> results = dataSource.search("TITLE");
		RecordedRequest request = mockedServer.takeRequest();
		Assert.assertEquals("/search/series?name=TITLE", request.getPath());
		Assert.assertEquals("GET", request.getMethod());
		Assert.assertEquals("application/json", request.getHeader("Accept"));
		Assert.assertEquals("Bearer TOKEN", request.getHeader("Authorization"));
		
		Assert.assertNotNull(results);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals("GUIDEID", results.get(0).getGuideId());
		Assert.assertEquals("DESCRIPTION", results.get(0).getDescription());
		Assert.assertEquals("TITLE", results.get(0).getTitle());
		Assert.assertEquals(LocalDate.parse("2005-03-11"),results.get(0).getFirstAired());
	}
	
	@Test
	public void searchInvalidSeriesTest() throws Exception {
		String responseBody = "{\"data\": [{\"overview\":\"DESCRIPTION\",\"id\":\"GUIDEID\",\"seriesName\":\"** Invalid Series **\",\"firstAired\":\"2005-03-11\"}]}";		

		MockResponse mockResponse = new MockResponse()
				.setResponseCode(200)
			    .addHeader("Content-Type", "application/json")
			    .setBody(responseBody);
		
		mockedServer.enqueue(mockResponse);
		
		dataSource.setBaseUri("http://localhost:8089");
		
		dataSource.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dataSource.setToken(token);

		List<GuideSearchResult> results = dataSource.search("TITLE");
		RecordedRequest request = mockedServer.takeRequest();
		Assert.assertEquals("/search/series?name=TITLE", request.getPath());
		Assert.assertEquals("GET", request.getMethod());
		Assert.assertEquals("application/json", request.getHeader("Accept"));
		Assert.assertEquals("Bearer TOKEN", request.getHeader("Authorization"));
		
		Assert.assertNotNull(results);
		Assert.assertEquals(0, results.size());
	}

	@Test
	public void infoTest() throws Exception {
		String responseBody = "{\"data\": {\"overview\":\"DESCRIPTION\",\"id\":\"GUIDEID\",\"seriesName\":\"TITLE\","
				+ "\"firstAired\":\"2015-12-28\",\"status\":\"Ended\",\"network\":\"CBS\",\"runtime\":\"45\","
				+ "\"airsDayOfWeek\":\"Monday\",\"airsTime\":\"8:00pm\",\"imdbId\":\"IMDB\",\"genre\":[\"GENRE\"],"
				+ "\"lastUpdated\":1454486401}}";

		MockResponse mockResponse = new MockResponse()
				.setResponseCode(200)
			    .addHeader("Content-Type", "application/json")
			    .setBody(responseBody);
		
		mockedServer.enqueue(mockResponse);
		
		dataSource.setBaseUri("http://localhost:8089");
		
		dataSource.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dataSource.setToken(token);

		GuideInfo info = dataSource.info("GUIDEID");
		RecordedRequest request = mockedServer.takeRequest();
		Assert.assertEquals("/series/GUIDEID", request.getPath());
		Assert.assertEquals("GET", request.getMethod());
		Assert.assertEquals("application/json", request.getHeader("Accept"));
		Assert.assertEquals("Bearer TOKEN", request.getHeader("Authorization"));
		
		Assert.assertNotNull(info);
		Assert.assertEquals("GUIDEID", info.getGuideId());
		Assert.assertEquals("DESCRIPTION", info.getDescription());
		Assert.assertEquals("TITLE", info.getTitle());
		Assert.assertEquals(LocalDate.parse("2015-12-28"), info.getFirstAired());
		Assert.assertEquals(1, info.getAirDays().size());
		Assert.assertEquals(AiringDay.MONDAY, info.getAirDays().get(0));
		Assert.assertFalse(info.isAiring());
		Assert.assertEquals(LocalTime.parse("20:00"), info.getAirTime());
		Assert.assertEquals(1, info.getGenres().size());
		Assert.assertEquals("GENRE", info.getGenres().get(0));
		Assert.assertEquals("America/Los_Angeles", info.getTimezone());
		Assert.assertEquals(45, info.getRunningTime());
		Assert.assertEquals(LocalDate.parse("2016-02-03"), info.getLastUpdated());
	}

	@Test
	public void episodesTest() throws Exception {
		String responseBody = "{\"links\": {\"first\": null,\"last\": 0,\"next\": 0,\"prev\": 0},"
				+ "\"data\": [{\"absoluteNumber\":0,\"airedEpisodeNumber\":-2147483648,"
				+ "\"airedSeason\":\"2147483647\",\"episodeName\":\"TITLE\",\"firstAired\":\"2015-09-29\"}]};";

		MockResponse mockResponse = new MockResponse()
				.setResponseCode(200)
			    .addHeader("Content-Type", "application/json")
			    .setBody(responseBody);
		
		mockedServer.enqueue(mockResponse);
		
		dataSource.setBaseUri("http://localhost:8089");
		
		dataSource.setValidPeriodInMs(Integer.MAX_VALUE);
		
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dataSource.setToken(token);

		TvdbEpisodes result = dataSource.episodes("GUIDEID", 1);
		RecordedRequest request = mockedServer.takeRequest();
		Assert.assertEquals("/series/GUIDEID/episodes?page=1", request.getPath());
		Assert.assertEquals("GET", request.getMethod());
		Assert.assertEquals("application/json", request.getHeader("Accept"));
		Assert.assertEquals("Bearer TOKEN", request.getHeader("Authorization"));
		
		Assert.assertNotNull(result);
		Assert.assertNull(result.getFirst());
		Assert.assertEquals(0, result.getLast().intValue());
		Assert.assertEquals(0, result.getNext().intValue());
		Assert.assertEquals(0, result.getPrevious().intValue());
		Assert.assertNotNull(result.getEpisodes());
		Assert.assertEquals(1, result.getEpisodes().size());
		Assert.assertEquals(0, result.getEpisodes().get(0).getProductionNum().intValue());
		Assert.assertEquals(Integer.MIN_VALUE, result.getEpisodes().get(0).getEpisodeNum().intValue());
		Assert.assertEquals(Integer.MAX_VALUE, result.getEpisodes().get(0).getSeasonNum().intValue());
		Assert.assertEquals("TITLE", result.getEpisodes().get(0).getTitle());
		Assert.assertEquals(LocalDate.parse("2015-09-29"),result.getEpisodes().get(0).getAirDate());
	}
	
	@Test
	public void episodeListNoNextTest() throws Exception {
		String responseBody = "{\"links\": {\"first\": 1,\"last\": 1,\"next\": 0,\"prev\": 0},"
				+ "\"data\": [{\"absoluteNumber\":0,\"airedEpisodeNumber\":-2147483648,"
				+ "\"airedSeason\":\"2147483647\",\"episodeName\":\"TITLE\",\"firstAired\":\"2015-09-29\"}]};";
		
		MockResponse mockResponse = new MockResponse()
				.setResponseCode(200)
			    .addHeader("Content-Type", "application/json")
			    .setBody(responseBody);
		
		mockedServer.enqueue(mockResponse);
		
		dataSource.setBaseUri("http://localhost:8089");
		
		dataSource.setValidPeriodInMs(Integer.MAX_VALUE);
		
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dataSource.setToken(token);

		List<GuideEpisode> episodes = dataSource.episodes("GUIDEID");
		RecordedRequest request = mockedServer.takeRequest();
		Assert.assertEquals("/series/GUIDEID/episodes?page=1", request.getPath());
		Assert.assertEquals("GET", request.getMethod());
		Assert.assertEquals("application/json", request.getHeader("Accept"));
		Assert.assertEquals("Bearer TOKEN", request.getHeader("Authorization"));
		
		Assert.assertNotNull(episodes);
		Assert.assertEquals(1, episodes.size());
		Assert.assertEquals(0, episodes.get(0).getProductionNum().intValue());
		Assert.assertEquals(Integer.MIN_VALUE, episodes.get(0).getEpisodeNum().intValue());
		Assert.assertEquals(Integer.MAX_VALUE, episodes.get(0).getSeasonNum().intValue());
		Assert.assertEquals("TITLE", episodes.get(0).getTitle());
		Assert.assertEquals(LocalDate.parse("2015-09-29"),episodes.get(0).getAirDate());
	}

	@Test
	public void episodeListHasNextTest() throws Exception {
		String responseBody_one = "{\"links\": {\"first\": 1,\"last\": 2,\"next\": 2,\"prev\": 0},"
				+ "\"data\": [{\"absoluteNumber\":0,\"airedEpisodeNumber\":-2147483648,"
				+ "\"airedSeason\":\"2147483647\",\"episodeName\":\"TITLE\",\"firstAired\":\"2015-09-29\"}]};";
		
		String responseBody_two = "{\"links\": {\"first\": 2,\"last\": 2,\"next\": 0,\"prev\": 1},"
				+ "\"data\": [{\"absoluteNumber\":0,\"airedEpisodeNumber\":-2147483648,"
				+ "\"airedSeason\":\"2147483647\",\"episodeName\":\"TITLE_TWO\",\"firstAired\":\"2005-09-29\"}]};";

		MockResponse mockResponse_one = new MockResponse()
				.setResponseCode(200)
			    .addHeader("Content-Type", "application/json")
			    .setBody(responseBody_one);
		
		mockedServer.enqueue(mockResponse_one);
		
		MockResponse mockResponse_two = new MockResponse()
				.setResponseCode(200)
			    .addHeader("Content-Type", "application/json")
			    .setBody(responseBody_two);
		
		mockedServer.enqueue(mockResponse_two);
		
		dataSource.setBaseUri("http://localhost:8089");
		
		dataSource.setValidPeriodInMs(Integer.MAX_VALUE);
		
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dataSource.setToken(token);

		List<GuideEpisode> episodes = dataSource.episodes("GUIDEID");
		RecordedRequest request_one = mockedServer.takeRequest();
		Assert.assertEquals("/series/GUIDEID/episodes?page=1", request_one.getPath());
		Assert.assertEquals("GET", request_one.getMethod());
		Assert.assertEquals("application/json", request_one.getHeader("Accept"));
		Assert.assertEquals("Bearer TOKEN", request_one.getHeader("Authorization"));
		
		RecordedRequest request_two = mockedServer.takeRequest();
		Assert.assertEquals("/series/GUIDEID/episodes?page=2", request_two.getPath());
		Assert.assertEquals("GET", request_two.getMethod());
		Assert.assertEquals("application/json", request_two.getHeader("Accept"));
		Assert.assertEquals("Bearer TOKEN", request_two.getHeader("Authorization"));
		
		Assert.assertNotNull(episodes);
		Assert.assertEquals(2, episodes.size());
		Assert.assertEquals(0, episodes.get(0).getProductionNum().intValue());
		Assert.assertEquals(Integer.MIN_VALUE, episodes.get(0).getEpisodeNum().intValue());
		Assert.assertEquals(Integer.MAX_VALUE, episodes.get(0).getSeasonNum().intValue());
		Assert.assertEquals("TITLE", episodes.get(0).getTitle());
		Assert.assertEquals(LocalDate.parse("2015-09-29"),episodes.get(0).getAirDate());
		Assert.assertEquals(0, episodes.get(1).getProductionNum().intValue());
		Assert.assertEquals(Integer.MIN_VALUE, episodes.get(1).getEpisodeNum().intValue());
		Assert.assertEquals(Integer.MAX_VALUE, episodes.get(1).getSeasonNum().intValue());
		Assert.assertEquals("TITLE_TWO", episodes.get(1).getTitle());
		Assert.assertEquals(LocalDate.parse("2005-09-29"),episodes.get(1).getAirDate());
	}
}
