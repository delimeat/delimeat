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

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.MediaType;

import io.delimeat.guide.entity.GuideEpisode;
import io.delimeat.guide.entity.GuideError;
import io.delimeat.guide.entity.GuideInfo;
import io.delimeat.guide.entity.GuideSearch;
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
import io.delimeat.util.DelimeatUtils;
import io.delimeat.util.jaxrs.MoxyJAXBFeature;

public class TvdbGuideDataSource_Impl implements GuideDataSource {

	private static final String APIKEY = "FE3A3CA0FE707FEF";
	private static final MediaType MEDIA_TYPE = MediaType.APPLICATION_JSON_TYPE;
	private static final String ENCODING = "UTF-8";

	private int tokenValidPeriodInMs = 3600000;
  	private URI baseUri;
	private TvdbToken token = null;	
	
	public Feature getFeature() {
		Map<String,Object> properties = new HashMap<>();
		properties.put("eclipselink.media-type", "application/json");
		properties.put("eclipselink.oxm.metadata-source", "oxm/guide-tvdb-oxm.xml");
		properties.put("eclipselink.json.include-root", false);
		List<Class<?>> classes = Arrays.asList(
				GuideEpisode.class, 
				GuideError.class, 
				GuideInfo.class, 
				GuideSearchResult.class,
				GuideSearch.class, 
				TvdbApiKey.class, 
				TvdbEpisodes.class, 
				TvdbToken.class);
		
		return new MoxyJAXBFeature(properties, classes);
	}
	
	public Client getClient() {		
		return ClientBuilder.newBuilder()
				.register(getFeature())
				.build();
	}

	/**
	 * @return the baseUri
	 */
	public URI getBaseUri() {
		return baseUri;
	}

	/**
	 * @param baseUri the baseUri to set
	 */
	public void setBaseUri(URI baseUri) {
		this.baseUri = baseUri;
	}

	/**
	 * @return the tokenValidPeriodInMs
	 */
	public int getTokenValidPeriodInMs() {
		return tokenValidPeriodInMs;
	}

	/**
	 * @param tokenValidPeriodInMs the tokenValidPeriodInMs to set
	 */
	public void setTokenValidPeriodInMs(int tokenValidPeriodInMs) {
		this.tokenValidPeriodInMs = tokenValidPeriodInMs;
	}

	/**
	 * @param token
	 *            the token to set
	 */
	public void setToken(TvdbToken token) {
		this.token = token;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.delimeat.guide.GuideDataSource#getGuideSource()
	 */
	@Override
	public GuideSource getGuideSource() {
		return GuideSource.TVDB;
	}

	public TvdbToken login() throws GuideNotFoundException, GuideAuthorizationException, GuideTimeoutException, GuideResponseException,GuideResponseBodyException, GuideException {
		TvdbApiKey key = new TvdbApiKey();
		key.setValue(APIKEY);
		
        Entity<TvdbApiKey> entity = Entity.entity(key, MEDIA_TYPE);

        Invocation invocation = getClient()
        		.target(baseUri)
        		.path("login")
        		.request(MEDIA_TYPE)
        		.buildPost(entity);
        
        return invoke(invocation, TvdbToken.class);		
	}

	public TvdbToken refreshToken() throws GuideNotFoundException, GuideAuthorizationException, GuideTimeoutException, GuideResponseException,GuideResponseBodyException, GuideException {
		
        Invocation invocation = getClient()
        		.target(baseUri)
        		.path("refresh_token")
        		.request(MEDIA_TYPE)
				.header("Authorization", "Bearer " + token.getValue())
				.buildGet();

		return invoke(invocation, TvdbToken.class);
		
	}

	public TvdbToken getToken() throws GuideNotFoundException, GuideAuthorizationException, GuideTimeoutException, GuideResponseException,GuideResponseBodyException, GuideException {
		long now = System.currentTimeMillis();
		if (token == null || now >= token.getTime() + tokenValidPeriodInMs) {
			token = login();
		} else if (now >= token.getTime() + tokenValidPeriodInMs - 10 * 60 * 1000) {
			token = refreshToken();
		}
		return token;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.delimeat.guide.GuideDataSource#info(java.lang.String)
	 */
	@Override
	public GuideInfo info(String guideId) throws GuideNotFoundException, GuideAuthorizationException, GuideTimeoutException, GuideResponseException,GuideResponseBodyException, GuideException {
        Invocation invocation = getClient()
        		.target(baseUri)
        		.path("series")
        		.path(DelimeatUtils.urlEscape(guideId, ENCODING))
        		.request(MEDIA_TYPE)
				.header("Authorization", "Bearer " + getToken().getValue())
				.buildGet();

		return invoke(invocation, GuideInfo.class);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.delimeat.guide.GuideDataSource#episodes(java.lang.String)
	 */
	@Override
	public List<GuideEpisode> episodes(String guideId)
			throws GuideNotFoundException, GuideAuthorizationException, GuideTimeoutException, GuideResponseException,GuideResponseBodyException, GuideException {
		String encodedGuideId = DelimeatUtils.urlEscape(guideId, ENCODING);
		List<GuideEpisode> episodes = new ArrayList<GuideEpisode>();
		Integer page = 1;
		do {
			TvdbEpisodes result = episodes(encodedGuideId, page);
			episodes.addAll(result.getEpisodes());
			page = result.getNext();
		} while (page != null && page > 0);

		return episodes;
	}

	/**
	 * @param guideId
	 * @param page
	 * @return
	 * @throws GuideNotFoundException
	 * @throws GuideAuthorizationException
	 * @throws GuideException
	 */
	public TvdbEpisodes episodes(String guideId, int page)
			throws GuideNotFoundException, GuideAuthorizationException, GuideTimeoutException, GuideResponseException,GuideResponseBodyException, GuideException {

        Invocation invocation = getClient()
        		.target(baseUri)
        		.path("series")
        		.path(DelimeatUtils.urlEscape(guideId, ENCODING))
        		.path("episodes")
        		.queryParam("page",page)
        		.request(MEDIA_TYPE)
				.header("Authorization", "Bearer " + getToken().getValue())
				.buildGet();

		return invoke(invocation, TvdbEpisodes.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.delimeat.guide.GuideDataSource#search(java.lang.String)
	 */
	@Override
	public List<GuideSearchResult> search(String title)
			throws GuideNotFoundException, GuideAuthorizationException, GuideTimeoutException, GuideResponseException,GuideResponseBodyException, GuideException {
		
        Invocation invocation = getClient()
        		.target(baseUri)
        		.path("search")
        		.path("series")
        		.queryParam("name", DelimeatUtils.urlEscape(title, ENCODING))
        		.request(MEDIA_TYPE)
				.header("Authorization", "Bearer " + getToken().getValue())
				.buildGet();

		return invoke(invocation, GuideSearch.class)
				.getResults()
				.stream()
				.filter(result -> !result.getTitle().matches("^\\*\\*[\\s\\S]*\\*\\*$")) // filter out invalid series
				.collect(Collectors.toList());		
	}
	
	private <T> T invoke(Invocation invocation, Class<T> responseType) throws GuideAuthorizationException, GuideNotFoundException, GuideException {
		try {
			return invocation.invoke(responseType);
		} catch (NotAuthorizedException ex) {
			GuideError error = ex.getResponse().readEntity(GuideError.class);
			throw new GuideAuthorizationException(error.getMessage(), ex);
		} catch (NotFoundException ex) {
			//TODO better error handling
			throw new GuideNotFoundException(ex.getMessage(), ex);
		} catch (WebApplicationException ex) {
			throw new GuideException(ex);
		}
	}

}
