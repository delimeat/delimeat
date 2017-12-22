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
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.WireMockServer;

import io.delimeat.guide.entity.AiringDay;
import io.delimeat.guide.entity.GuideEpisode;
import io.delimeat.guide.entity.GuideInfo;
import io.delimeat.guide.entity.GuideSearchResult;
import io.delimeat.guide.entity.TvdbEpisodes;
import io.delimeat.guide.entity.TvdbToken;

public class TvdbGuideDataSource_ImplIT {

	private static WireMockServer server = new WireMockServer(8089);
	
	private TvdbGuideDataSource_Impl dataSource;

	@BeforeAll
	public static void setUpClass() {
		server.start();
	}
	
	@AfterAll
	public static void tearDownClass() {
		server.stop();
	}
	
	@BeforeEach
	public void setUp() throws Exception {
		dataSource = new TvdbGuideDataSource_Impl();
			
		dataSource.setBaseUri(new URI("http://localhost:8089"));	
	}

	@Test
	public void loginTest() throws Exception {
				
		server.stubFor(post(urlEqualTo("/login"))
				.withRequestBody(equalToJson("{\"apikey\":\"FE3A3CA0FE707FEF\"}"))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody("{\"token\":\"TOKEN\"}")));

		TvdbToken token = dataSource.login();

		Assertions.assertNotNull(token);
		Assertions.assertEquals("TOKEN", token.getValue());
	}

	@Test
	public void refreshTokenTest() throws Exception {
		
		server.stubFor(get(urlEqualTo("/refresh_token"))
				.withHeader("Authorization", matching("Bearer OLD_TOKEN"))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody("{\"token\":\"NEW_TOKEN\"}")));
		

		TvdbToken oldToken = new TvdbToken();
		oldToken.setValue("OLD_TOKEN");

		dataSource.setToken(oldToken);

		TvdbToken newToken = dataSource.refreshToken();
		
		Assertions.assertNotNull(newToken);
		Assertions.assertEquals("NEW_TOKEN", newToken.getValue());
	}

	
	@Test
	public void getTokenUseExistingTest() throws Exception {
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dataSource.setToken(token);
		TvdbToken returnToken = dataSource.getToken();
		Assertions.assertEquals(token, returnToken);
	}

	
	@Test
	public void getTokenLoginTest() throws Exception {

		server.stubFor(post(urlEqualTo("/login"))
				.withRequestBody(equalToJson("{\"apikey\":\"FE3A3CA0FE707FEF\"}"))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody("{\"token\":\"TOKEN\"}")));

		TvdbToken token = dataSource.getToken();

		Assertions.assertNotNull(token);
		Assertions.assertEquals("TOKEN", token.getValue());
	}

	
	@Test
	public void getTokenRefreshTest() throws Exception {
		
		server.stubFor(get(urlEqualTo("/refresh_token"))
				.withHeader("Authorization", matching("Bearer OLD_TOKEN"))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody("{\"token\":\"NEW_TOKEN\"}")));
		

		TvdbToken oldToken = new TvdbToken();
		oldToken.setValue("OLD_TOKEN");

		dataSource.setToken(oldToken);
		dataSource.setTokenValidPeriodInMs(100000);

		TvdbToken newToken = dataSource.getToken();

		Assertions.assertNotNull(newToken);
		Assertions.assertEquals("NEW_TOKEN", newToken.getValue());
	}
	
	@Test
	public void searchTest() throws Exception {
		String responseBody = "{\"data\": [{\"overview\":\"DESCRIPTION\",\"id\":\"GUIDEID\",\"seriesName\":\"TITLE\",\"firstAired\":\"2005-03-11\"}]}";

		server.stubFor(get(urlPathEqualTo("/search/series"))
				.withQueryParam("name", matching("TITLE"))
				.withHeader("Authorization", matching("Bearer TOKEN"))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody(responseBody)));
		

		dataSource.setTokenValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dataSource.setToken(token);

		List<GuideSearchResult> results = dataSource.search("TITLE");

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

		server.stubFor(get(urlPathEqualTo("/search/series"))
				.withQueryParam("name", matching("TITLE"))
				.withHeader("Authorization", matching("Bearer TOKEN"))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody(responseBody)));

		List<GuideSearchResult> results = dataSource.search("TITLE");

		Assertions.assertNotNull(results);
		Assertions.assertEquals(0, results.size());
	}
	
	@Test
	public void infoTest() throws Exception {
		String responseBody = "{\"data\": {\"overview\":\"DESCRIPTION\",\"id\":\"GUIDEID\",\"seriesName\":\"TITLE\","
				+ "\"firstAired\":\"2015-12-28\",\"status\":\"Ended\",\"network\":\"CBS\",\"runtime\":\"45\","
				+ "\"airsDayOfWeek\":\"Monday\",\"airsTime\":\"8:00pm\",\"imdbId\":\"IMDB\",\"genre\":[\"GENRE\"],"
				+ "\"lastUpdated\":1454486401}}";

		server.stubFor(get(urlPathEqualTo("/series/GUIDEID"))
				.withHeader("Authorization", matching("Bearer TOKEN"))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody(responseBody)));

		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dataSource.setToken(token);

		GuideInfo info = dataSource.info("GUIDEID");

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

		server.stubFor(get(urlPathEqualTo("/series/GUIDEID/episodes"))
				.withQueryParam("page", matching("1"))
				.withHeader("Authorization", matching("Bearer TOKEN"))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody(responseBody)));

		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dataSource.setToken(token);

		TvdbEpisodes result = dataSource.episodes("GUIDEID", 1);

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

		server.stubFor(get(urlPathEqualTo("/series/GUIDEID/episodes"))
				.withQueryParam("page", matching("1"))
				.withHeader("Authorization", matching("Bearer TOKEN"))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody(responseBody)));

		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dataSource.setToken(token);

		List<GuideEpisode> episodes = dataSource.episodes("GUIDEID");

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

		server.stubFor(get(urlPathEqualTo("/series/GUIDEID/episodes"))
				.withQueryParam("page", matching("1"))
				.withHeader("Authorization", matching("Bearer TOKEN"))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody(responseBody_one)));
		
		server.stubFor(get(urlPathEqualTo("/series/GUIDEID/episodes"))
				.withQueryParam("page", matching("2"))
				.withHeader("Authorization", matching("Bearer TOKEN"))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody(responseBody_two)));
		
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dataSource.setToken(token);

		List<GuideEpisode> episodes = dataSource.episodes("GUIDEID");

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
