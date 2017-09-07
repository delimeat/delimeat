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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
import io.delimeat.guide.exception.GuideResponseBodyException;
import io.delimeat.guide.exception.GuideResponseException;
import io.delimeat.guide.exception.GuideTimeoutException;
import io.delimeat.util.DelimeatUtils;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
public class TvdbGuideDataSource_Impl implements GuideDataSource {

	private final Map<String, Object> properties;
	private final Map<String, String> headers;

	@Value("${io.delimeat.guide.tvdb.baseUri}")
	private String baseUri;

	@Value("${io.delimeat.guide.tvdb.validPeriod}")
	private int validPeriodInMs = 0;

	@Value("${io.delimeat.guide.tvdb.apikey}")
	private String apiKey;

	private TvdbToken token;

	public TvdbGuideDataSource_Impl() {
		properties = new HashMap<String, Object>();
		properties.put("eclipselink.oxm.metadata-source", "oxm/guide-tvdb-oxm.xml");
		properties.put("eclipselink.media-type", "application/json");
		properties.put("eclipselink.json.include-root", false);

		headers = new HashMap<String, String>();
		headers.put("Accept", "application/json");

	}

	/**
	 * @return the baseUri
	 */
	public String getBaseUri() {
		return baseUri;
	}

	/**
	 * @param baseUri
	 *            the baseUri to set
	 */
	public void setBaseUri(String baseUri) {
		this.baseUri = baseUri;
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
	 * @return the properties
	 */
	public Map<String, Object> getProperties() {
		return properties;
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

	/**
	 * @param url
	 * @return
	 * @throws GuideException
	 */
	public URL buildUrl(String url) throws GuideException {
		try {
			return new URL(url);
		} catch (MalformedURLException ex) {
			throw new GuideException(ex);
		}
	}

	public Map<String, String> buildRequestHeaders() throws GuideNotFoundException, GuideAuthorizationException, GuideTimeoutException, GuideResponseException,GuideResponseBodyException, GuideException {
		Map<String, String> reqHeaders = headers;
		reqHeaders.put("Authorization", String.format("Bearer %s", getToken().getValue()));
		return reqHeaders;
	}
	
	/**
	 * @return context
	 * @throws JAXBException
	 */
	public JAXBContext getContext() throws JAXBException {
		return JAXBContextFactory.createContext(
				new Class[] { GuideSearchResult.class, GuideSearch.class, GuideEpisode.class, GuideInfo.class,
						TvdbApiKey.class, GuideError.class, TvdbToken.class, TvdbEpisodes.class },
				properties);
	}

	public TvdbToken login(String apiKey) throws GuideNotFoundException, GuideAuthorizationException, GuideTimeoutException, GuideResponseException,GuideResponseBodyException, GuideException {
		TvdbApiKey key = new TvdbApiKey();
		key.setValue(apiKey);
		URL url = buildUrl(String.format("%s/login", baseUri));
		Map<String, String> putHeaders = new HashMap<>();
		putHeaders.putAll(headers);
		putHeaders.put("Content-Type", "application/json");
		return executeRequest(buildPost(url,key,putHeaders), TvdbToken.class);
	}

	public TvdbToken refreshToken() throws GuideNotFoundException, GuideAuthorizationException, GuideTimeoutException, GuideResponseException,GuideResponseBodyException, GuideException {
		URL url = buildUrl(String.format("%s/refresh_token", baseUri));
		Map<String, String> reqHeaders = new HashMap<>();
		reqHeaders.putAll(headers);
		reqHeaders.put("Authorization", String.format("Bearer %s", token.getValue()));
		return executeRequest(buildGet(url, reqHeaders), TvdbToken.class);
	}

	public TvdbToken getToken() throws GuideNotFoundException, GuideAuthorizationException, GuideTimeoutException, GuideResponseException,GuideResponseBodyException, GuideException {
		long now = System.currentTimeMillis();
		if (token == null || now >= token.getTime() + validPeriodInMs) {
			token = login(apiKey);
		} else if (now >= token.getTime() + validPeriodInMs - 10 * 60 * 1000) {
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
		URL url = buildUrl(String.format("%s/series/%s", baseUri, DelimeatUtils.urlEscape(guideId, "UTF-8")));
		return executeRequest(buildGet(url, buildRequestHeaders()), GuideInfo.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.delimeat.guide.GuideDataSource#episodes(java.lang.String)
	 */
	@Override
	public List<GuideEpisode> episodes(String guideId)
			throws GuideNotFoundException, GuideAuthorizationException, GuideTimeoutException, GuideResponseException,GuideResponseBodyException, GuideException {
		String encodedGuideId = DelimeatUtils.urlEscape(guideId, "UTF-8");
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
		URL url = buildUrl(String.format("%s/series/%s/episodes?page=%s", baseUri, guideId, page));
		return executeRequest(buildGet(url, buildRequestHeaders()), TvdbEpisodes.class);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.delimeat.guide.GuideDataSource#search(java.lang.String)
	 */
	@Override
	public List<GuideSearchResult> search(String title)
			throws GuideNotFoundException, GuideAuthorizationException, GuideTimeoutException, GuideResponseException,GuideResponseBodyException, GuideException {
		URL url = buildUrl(String.format("%s/search/series?name=%s", baseUri, DelimeatUtils.urlEscape(title, "UTF-8")));
		return executeRequest(buildGet(url, buildRequestHeaders()), GuideSearch.class)
				.getResults()
				.stream()
				.filter(result -> !result.getTitle().matches("^\\*\\*[\\s\\S]*\\*\\*$")) // filter out invalid series
				.collect(Collectors.toList());
	}
	
	public Request buildGet(URL url, Map<String, String> headers){
		return new Request.Builder()
				.url(url)
				.headers(Headers.of(headers))
				.build();
	}
	
	public <S> Request buildPost(URL url, S entity, Map<String, String> headers) throws GuideException{
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try{
			getContext().createMarshaller().marshal(entity, output);
		}catch(JAXBException ex){
			throw new GuideException(ex);
		}
		
		if(headers.get("Content-Type") == null){
			headers.put("Content-Type", "application/json");
		}
		
		return new Request.Builder()
				.url(url)
				.headers(Headers.of(headers))
				.post(RequestBody.create(MediaType.parse(headers.get("Content-Type")), output.toByteArray()))
				.build();
	}
	
	public <T> T executeRequest(Request request, Class<T> returnType) throws GuideTimeoutException, GuideNotFoundException, GuideAuthorizationException, GuideResponseException, GuideResponseBodyException, GuideException{
		try{
			Response response = null;
			try{
				response = DelimeatUtils.httpClient().newCall(request).execute();
			}catch(SocketTimeoutException ex){
				throw new GuideTimeoutException(request.url().url());
			}
			
			if (response.isSuccessful() == false) {
				switch (response.code()) {
				case 404:
					throw new GuideNotFoundException();
				case 401:
				case 403:
					throw new GuideAuthorizationException();
				default:
					throw new GuideResponseException(response.code(), response.message(), request.url().url());
				}
			}
			
			byte[] responseBytes = response.body().bytes();
			response.body().close();
			try{
				StreamSource source = new StreamSource(new ByteArrayInputStream(responseBytes));
				return getContext().createUnmarshaller().unmarshal(source, returnType).getValue();
			}catch(UnmarshalException ex){
				throw new GuideResponseBodyException(request.url().url(), new String(responseBytes), ex);
			}
		} catch (IOException | JAXBException ex) {
			throw new GuideException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TvdbGuideDataSource_Impl [" + (properties != null ? "properties=" + properties + ", " : "")
				+ (headers != null ? "headers=" + headers + ", " : "")
				+ (baseUri != null ? "baseUri=" + baseUri + ", " : "") + "validPeriodInMs=" + validPeriodInMs + ", "
				+ (apiKey != null ? "apiKey=" + apiKey + ", " : "") + (token != null ? "token=" + token : "") + "]";
	}

}
