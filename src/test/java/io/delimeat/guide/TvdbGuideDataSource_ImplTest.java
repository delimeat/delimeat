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

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import io.delimeat.guide.domain.AiringDay;
import io.delimeat.guide.domain.GuideEpisode;
import io.delimeat.guide.domain.GuideInfo;
import io.delimeat.guide.domain.GuideSearchResult;
import io.delimeat.guide.domain.GuideSource;
import io.delimeat.guide.domain.TvdbApiKey;
import io.delimeat.guide.domain.TvdbEpisodes;
import io.delimeat.guide.domain.TvdbToken;
import io.delimeat.guide.exception.GuideAuthorizationException;
import io.delimeat.guide.exception.GuideException;
import io.delimeat.guide.exception.GuideNotFoundException;
import io.delimeat.guide.exception.GuideResponseBodyException;
import io.delimeat.guide.exception.GuideResponseException;
import io.delimeat.guide.exception.GuideTimeoutException;
import okhttp3.MediaType;
import okhttp3.Request;
import okio.Buffer;

public class TvdbGuideDataSource_ImplTest {

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8089);

	private TvdbGuideDataSource_Impl dataSource;

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
		
		stubFor(get(urlPathEqualTo("/refresh_token"))
				.withHeader("Accept", equalTo("application/json"))
				.willReturn(aResponse()
							.withStatus(200)
							.withHeader("Content-Type", "application/json")
							.withBody(responseBody)));
		
		URL url = new URL("http://localhost:8089/refresh_token");
		Map<String, String> headers = new HashMap<String,String>();
		headers.put("Accept", "application/json");
		Request request = dataSource.buildGet(url, headers);
		
		TvdbToken token = dataSource.executeRequest(request, TvdbToken.class);
		Assert.assertEquals("NEW_TOKEN", token.getValue());
	}
	
	@Test(expected=GuideTimeoutException.class)
	public void executeRequestTimeoutExceptionTest() throws Exception{
		
		stubFor(get(urlPathEqualTo("/refresh_token"))
				.withHeader("Accept", equalTo("application/json"))
				.willReturn(aResponse()
							.withStatus(200)
							.withHeader("Content-Type", "application/json")
							.withFixedDelay(2500)));
		
		URL url = new URL("http://localhost:8089/refresh_token");
		Map<String, String> headers = new HashMap<String,String>();
		headers.put("Accept", "application/json");
		Request request = dataSource.buildGet(url, headers);
		
		dataSource.executeRequest(request, TvdbToken.class);
		Assert.fail();
	}
	
	@Test(expected=GuideNotFoundException.class)
	public void executeRequestNotFoundExceptionTest() throws Exception{
		
		stubFor(get(urlPathEqualTo("/refresh_token"))
				.withHeader("Accept", equalTo("application/json"))
				.willReturn(aResponse()
							.withStatus(404)
							.withHeader("Content-Type", "application/json")));
		
		URL url = new URL("http://localhost:8089/refresh_token");
		Map<String, String> headers = new HashMap<String,String>();
		headers.put("Accept", "application/json");
		Request request = dataSource.buildGet(url, headers);
		
		dataSource.executeRequest(request, TvdbToken.class);
		Assert.fail();
	}
	
	@Test(expected=GuideAuthorizationException.class)
	public void executeRequestUnauthorisedExceptionTest() throws Exception{
		
		stubFor(get(urlPathEqualTo("/refresh_token"))
				.withHeader("Accept", equalTo("application/json"))
				.willReturn(aResponse()
							.withStatus(401)
							.withHeader("Content-Type", "application/json")));
		
		URL url = new URL("http://localhost:8089/refresh_token");
		Map<String, String> headers = new HashMap<String,String>();
		headers.put("Accept", "application/json");
		Request request = dataSource.buildGet(url, headers);
		
		dataSource.executeRequest(request, TvdbToken.class);
		Assert.fail();
	}
	
	@Test(expected=GuideAuthorizationException.class)
	public void executeRequestForbiddenExceptionTest() throws Exception{
		
		stubFor(get(urlPathEqualTo("/refresh_token"))
				.withHeader("Accept", equalTo("application/json"))
				.willReturn(aResponse()
							.withStatus(403)
							.withHeader("Content-Type", "application/json")));
		
		URL url = new URL("http://localhost:8089/refresh_token");
		Map<String, String> headers = new HashMap<String,String>();
		headers.put("Accept", "application/json");
		Request request = dataSource.buildGet(url, headers);
		
		dataSource.executeRequest(request, TvdbToken.class);
		Assert.fail();
	}
	
	
	@Test(expected=GuideResponseException.class)
	public void executeRequestResponseExceptionTest() throws Exception{
		
		stubFor(get(urlPathEqualTo("/refresh_token"))
				.withHeader("Accept", equalTo("application/json"))
				.willReturn(aResponse()
							.withStatus(500)
							.withHeader("Content-Type", "application/json")));
		
		URL url = new URL("http://localhost:8089/refresh_token");
		Map<String, String> headers = new HashMap<String,String>();
		headers.put("Accept", "application/json");
		Request request = dataSource.buildGet(url, headers);
		
		dataSource.executeRequest(request, TvdbToken.class);
		Assert.fail();
	}
	
	@Test(expected=GuideResponseBodyException.class)
	public void executeRequestResponseBodyExceptionTest() throws Exception{
		
		stubFor(get(urlPathEqualTo("/refresh_token"))
				.withHeader("Accept", equalTo("application/json"))
				.willReturn(aResponse()
							.withStatus(200)
							.withHeader("Content-Type", "application/json")));
		
		URL url = new URL("http://localhost:8089/refresh_token");
		Map<String, String> headers = new HashMap<String,String>();
		headers.put("Accept", "application/json");
		Request request = dataSource.buildGet(url, headers);
		
		dataSource.executeRequest(request, TvdbToken.class);
		Assert.fail();
	}
	
	@Test(expected=GuideException.class)
	public void executeRequestExceptionTest() throws Exception{
		
		stubFor(get(urlPathEqualTo("/refresh_token"))
				.withHeader("Accept", equalTo("application/json"))
				.willReturn(aResponse()
							.withStatus(200)
							.withHeader("Content-Type", "application/json")));
		
		URL url = new URL("http://localhost/refresh_token");
		Map<String, String> headers = new HashMap<String,String>();
		headers.put("Accept", "application/json");
		Request request = dataSource.buildGet(url, headers);
		
		dataSource.executeRequest(request, TvdbToken.class);
		Assert.fail();
	}

	@Test
	public void loginTest() throws Exception {
		String requestBody = "{\"apikey\": \"APIKEY\"}";
		String responseBody = "{\"token\": \"TOKEN\"}";
		
		stubFor(post(urlPathEqualTo("/login"))
				.withHeader("Accept", equalTo("application/json"))
				.withHeader("Content-Type", equalTo("application/json"))
				.withRequestBody(equalToJson(requestBody))
				.willReturn(aResponse()
							.withStatus(200)
							.withHeader("Content-Type", "application/json")
							.withBody(responseBody)));
		
		dataSource.setBaseUri("http://localhost:8089");
		
		TvdbToken token = dataSource.login("APIKEY");
		Assert.assertNotNull(token);
		Assert.assertEquals("TOKEN", token.getValue());
	}

	@Test
	public void refreshTokenTest() throws Exception {
		String responseBody = "{\"token\": \"NEW_TOKEN\"}";
		
		stubFor(get(urlPathEqualTo("/refresh_token"))
				.withHeader("Accept", equalTo("application/json"))
				.withHeader("Authorization", equalTo("Bearer OLD_TOKEN"))
				.willReturn(aResponse()
							.withStatus(200)
							.withHeader("Content-Type", "application/json")
							.withBody(responseBody)));
		
		dataSource.setBaseUri("http://localhost:8089");
		
		TvdbToken oldToken = new TvdbToken();
		oldToken.setValue("OLD_TOKEN");

		dataSource.setToken(oldToken);

		TvdbToken newToken = dataSource.refreshToken();
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
		String requestBody = "{\"apikey\": \"APIKEY\"}";
		String responseBody = "{\"token\": \"TOKEN\"}";		
		
		stubFor(post(urlPathEqualTo("/login"))
				.withHeader("Accept", equalTo("application/json"))
				.withHeader("Content-Type", equalTo("application/json"))
				.withRequestBody(equalToJson(requestBody))
				.willReturn(aResponse()
							.withStatus(200)
							.withHeader("Content-Type", "application/json")
							.withBody(responseBody)));

		dataSource.setBaseUri("http://localhost:8089");		
		
		dataSource.setApiKey("APIKEY");

		TvdbToken token = dataSource.getToken();
		Assert.assertNotNull(token);
		Assert.assertEquals("TOKEN", token.getValue());
	}

	@Test
	public void getTokenRefreshTest() throws Exception {
		String responseBody = "{\"token\": \"NEW_TOKEN\"}";	

		stubFor(get(urlPathEqualTo("/refresh_token"))
				.withHeader("Accept", equalTo("application/json"))
				.withHeader("Authorization",equalTo("Bearer OLD_TOKEN"))
				.willReturn(aResponse()
							.withStatus(200)
							.withHeader("Content-Type", "application/json")
							.withBody(responseBody)));

		dataSource.setBaseUri("http://localhost:8089");
		
		TvdbToken oldToken = new TvdbToken();
		oldToken.setValue("OLD_TOKEN");
		dataSource.setToken(oldToken);

		dataSource.setValidPeriodInMs(100000);

		TvdbToken newToken = dataSource.getToken();
		Assert.assertNotNull(newToken);
		Assert.assertEquals("NEW_TOKEN", newToken.getValue());
	}

	@Test
	public void searchTest() throws Exception {
		String responseBody = "{\"data\": [{\"overview\":\"DESCRIPTION\",\"id\":\"GUIDEID\",\"seriesName\":\"TITLE\",\"firstAired\":\"2005-03-11\"}]}";	

		stubFor(get(urlPathEqualTo("/search/series"))
				.withHeader("Accept", equalTo("application/json"))
				.withHeader("Authorization", equalTo("Bearer TOKEN"))
				.withQueryParam("name", equalTo("TITLE"))
				.willReturn(aResponse()
							.withStatus(200)
							.withHeader("Content-Type", "application/json")
							.withBody(responseBody)));

		dataSource.setBaseUri("http://localhost:8089");
		
		dataSource.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dataSource.setToken(token);

		List<GuideSearchResult> results = dataSource.search("TITLE");
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

		stubFor(get(urlPathEqualTo("/search/series"))
				.withHeader("Accept", equalTo("application/json"))
				.withHeader("Authorization", equalTo("Bearer TOKEN"))
				.withQueryParam("name", equalTo("TITLE"))
				.willReturn(aResponse()
							.withStatus(200)
							.withHeader("Content-Type", "application/json")
							.withBody(responseBody)));

		dataSource.setBaseUri("http://localhost:8089");
		
		dataSource.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dataSource.setToken(token);

		List<GuideSearchResult> results = dataSource.search("TITLE");
		Assert.assertNotNull(results);
		Assert.assertEquals(0, results.size());
	}

	@Test
	public void infoTest() throws Exception {
		String responseText = "{\"data\": {\"overview\":\"DESCRIPTION\",\"id\":\"GUIDEID\",\"seriesName\":\"TITLE\","
				+ "\"firstAired\":\"2015-12-28\",\"status\":\"Ended\",\"network\":\"CBS\",\"runtime\":\"45\","
				+ "\"airsDayOfWeek\":\"Monday\",\"airsTime\":\"8:00pm\",\"imdbId\":\"IMDB\",\"genre\":[\"GENRE\"],"
				+ "\"lastUpdated\":1454486401}}";

		stubFor(get(urlPathEqualTo("/series/GUIDEID"))
				.withHeader("Accept", equalTo("application/json"))
				.withHeader("Authorization", equalTo("Bearer TOKEN"))
				.willReturn(aResponse()
							.withStatus(200)
							.withHeader("Content-Type", "application/json")
							.withBody(responseText)));

		dataSource.setBaseUri("http://localhost:8089");
		
		dataSource.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dataSource.setToken(token);

		GuideInfo info = dataSource.info("GUIDEID");
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

		stubFor(get(urlPathEqualTo("/series/GUIDEID/episodes"))
				.withHeader("Accept", equalTo("application/json"))
				.withHeader("Authorization", equalTo("Bearer TOKEN"))
				.withQueryParam("page", equalTo("1"))
				.willReturn(aResponse()
							.withStatus(200)
							.withHeader("Content-Type", "application/json")
							.withBody(responseBody)));

		dataSource.setBaseUri("http://localhost:8089");
		
		dataSource.setValidPeriodInMs(Integer.MAX_VALUE);
		
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dataSource.setToken(token);

		TvdbEpisodes result = dataSource.episodes("GUIDEID", 1);
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
	

		stubFor(get(urlPathEqualTo("/series/GUIDEID/episodes"))
				.withHeader("Accept", equalTo("application/json"))
				.withHeader("Authorization", equalTo("Bearer TOKEN"))
				.withQueryParam("page", equalTo("1"))
				.willReturn(aResponse()
							.withStatus(200)
							.withHeader("Content-Type", "application/json")
							.withBody(responseBody)));

		 
		dataSource.setBaseUri("http://localhost:8089");
		
		dataSource.setValidPeriodInMs(Integer.MAX_VALUE);
		
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dataSource.setToken(token);

		List<GuideEpisode> episodes = dataSource.episodes("GUIDEID");
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


		stubFor(get(urlPathEqualTo("/series/GUIDEID/episodes"))
				.withHeader("Accept", equalTo("application/json"))
				.withHeader("Authorization", equalTo("Bearer TOKEN"))
				.withQueryParam("page", equalTo("1"))
				.willReturn(aResponse()
							.withStatus(200)
							.withHeader("Content-Type", "application/json")
							.withBody(responseBody_one)));

		stubFor(get(urlPathEqualTo("/series/GUIDEID/episodes"))
				.withHeader("Accept", equalTo("application/json"))
				.withHeader("Authorization", equalTo("Bearer TOKEN"))
				.withQueryParam("page", equalTo("2"))
				.willReturn(aResponse()
							.withStatus(200)
							.withHeader("Content-Type", "application/json")
							.withBody(responseBody_two)));

		dataSource.setBaseUri("http://localhost:8089");
		
		dataSource.setValidPeriodInMs(Integer.MAX_VALUE);
		
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dataSource.setToken(token);

		List<GuideEpisode> episodes = dataSource.episodes("GUIDEID");
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
