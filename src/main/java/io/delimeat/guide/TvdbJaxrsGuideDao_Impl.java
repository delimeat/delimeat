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
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

import org.glassfish.jersey.logging.LoggingFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.net.UrlEscapers;

import io.delimeat.guide.domain.GuideEpisode;
import io.delimeat.guide.domain.GuideError;
import io.delimeat.guide.domain.GuideInfo;
import io.delimeat.guide.domain.GuideSearch;
import io.delimeat.guide.domain.GuideSearchResult;
import io.delimeat.guide.domain.GuideSource;
import io.delimeat.guide.domain.TvdbApiKey;
import io.delimeat.guide.domain.TvdbEpisodes;
import io.delimeat.guide.domain.TvdbToken;
import io.delimeat.guide.exception.GuideAuthorizationException;
import io.delimeat.guide.exception.GuideException;
import io.delimeat.guide.exception.GuideNotFoundException;
import io.delimeat.guide.jaxrs.GuideJaxrsUtils;
import io.delimeat.util.jaxrs.JaxbContextResolver;

@Component
public class TvdbJaxrsGuideDao_Impl implements GuideDao {
	
	private final MediaType mediaType;
	private final Client client;
	
	@Value("${io.delimeat.guide.tvdb.apikey}")
	private String apiKey;
    
	@Value("${io.delimeat.guide.tvdb.validPeriod}")
    private int validPeriodInMs = 0;

	@Value("${io.delimeat.guide.tvdb.baseUri}")
	private URI baseUri;
	
    private TvdbToken token;
    
	public TvdbJaxrsGuideDao_Impl(){
		mediaType =  MediaType.APPLICATION_JSON_TYPE;
		client = ClientBuilder.newClient();
		JaxbContextResolver resolver = new JaxbContextResolver();
		resolver.setClasses(GuideSearchResult.class,GuideSearch.class,GuideEpisode.class,GuideInfo.class,TvdbApiKey.class,GuideError.class,TvdbToken.class,TvdbEpisodes.class);
		Map<String,Object> properties = new HashMap<String,Object>();
		properties.put("eclipselink.oxm.metadata-source", "META-INF/oxm/guide-tvdb-oxm.xml");
		properties.put("eclipselink.media-type", "application/json");
		properties.put("eclipselink.json.include-root", false);
		resolver.setProperties(properties);
		client.register(resolver);
		client.register(new LoggingFeature(Logger.getLogger(LoggingFeature.class.getName()), java.util.logging.Level.SEVERE, LoggingFeature.Verbosity.PAYLOAD_ANY, LoggingFeature.DEFAULT_MAX_ENTITY_SIZE));
	}
    
    public void setBaseUri(URI baseUri){
    	this.baseUri = baseUri;
    }
    
    public URI getBaseUri(){
    	return baseUri;
    }

	@Override
	public GuideSource getGuideSource() {
		return GuideSource.TVDB;
	}

	/* (non-Javadoc)
	 * @see io.delimeat.common.guide.GuideDao#info(java.lang.String)
	 */
	@Override
	public GuideInfo info(String guideId) throws GuideNotFoundException, GuideAuthorizationException, GuideException {
		return GuideJaxrsUtils.get(
				client.target(baseUri)
        		.path("series")
          		.path(UrlEscapers.urlPathSegmentEscaper().escape(guideId))
          		.request(mediaType)
          		.header("Authorization", getAuthorization()), new GenericType<GuideInfo>(){});
	}

	/* (non-Javadoc)
	 * @see io.delimeat.common.guide.GuideDao#episodes(java.lang.String)
	 */
	@Override
	public List<GuideEpisode> episodes(String guideId)
			throws GuideNotFoundException, GuideAuthorizationException, GuideException {
        String encodedGuideId = UrlEscapers.urlPathSegmentEscaper().escape(guideId);
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
    public TvdbEpisodes episodes(String guideId, int page) throws GuideNotFoundException, GuideAuthorizationException, GuideException {
		return GuideJaxrsUtils.get(client.target(baseUri)
				.path("series")
				.path(guideId)
				.path("episodes")
				.queryParam("page", page)
				.request(mediaType)
          		.header("Authorization", getAuthorization()), new GenericType<TvdbEpisodes>(){});
    }

	/* (non-Javadoc)
	 * @see io.delimeat.common.guide.GuideDao#search(java.lang.String)
	 */
	@Override
	public List<GuideSearchResult> search(String title)
			throws GuideNotFoundException, GuideAuthorizationException, GuideException {
		
		return GuideJaxrsUtils.get(client.target(baseUri)
				.path("search")
				.path("series")
				.queryParam("name", title)
				.request(mediaType)
				.header("Authorization", getAuthorization()), new GenericType<GuideSearch>(){}).getResults();
	}
	
    public TvdbToken login(String apiKey) throws GuideAuthorizationException, GuideException {
    	 TvdbApiKey key = new TvdbApiKey();
         key.setValue(apiKey);
         Entity<TvdbApiKey> entity = Entity.entity(key, mediaType);
         return GuideJaxrsUtils.put(client.target(baseUri)
        		 .path("login")
              	.request(mediaType), entity, new GenericType<TvdbToken>(){});
    }

    public TvdbToken refreshToken(TvdbToken token) throws GuideAuthorizationException, GuideException {
    	return GuideJaxrsUtils.get(client.target(baseUri)
				.path("refresh_token")
				.request(mediaType)
				.header("Authorization", "Bearer "+ token.getValue()), new GenericType<TvdbToken>(){});
    }

    /**
     * @return the apiKey
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * @param apiKey
     *            the apiKey to set
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * @return the token
     * @throws IOException
     * @throws JAXBException
     */
    public TvdbToken getToken() throws GuideAuthorizationException, GuideException {
        long now = System.currentTimeMillis();
        if (token == null || now >= token.getTime() + validPeriodInMs) {
            token = login(apiKey);
        } else if (now >= token.getTime() + validPeriodInMs - 10 * 60 * 1000) {
            token = refreshToken(token);
        }
        return token;
    }
    
    /**
     * @return the authorisation
     * @throws GuideAuthorizationException
     * @throws GuideException
     */
    public String getAuthorization() throws GuideAuthorizationException, GuideException{
    	return String.format("Bearer %s", getToken().getValue());
    }

    /**
     * @param token
     *            the token to set
     */
    public void setToken(TvdbToken token) {
        this.token = token;
    }

    /**
     * @return the validPeriodInMs
     */
    public int getValidPeriodInMs() {
        return validPeriodInMs;
    }

    /**
     * @param validPeriodInMs
     *            the validPeriodInMs to set
     */
    public void setValidPeriodInMs(int validPeriodInMs) {
        this.validPeriodInMs = validPeriodInMs;
    }
    
}