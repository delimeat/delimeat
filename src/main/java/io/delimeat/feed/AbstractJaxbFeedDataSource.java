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
package io.delimeat.feed;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.domain.FeedSearch;
import io.delimeat.feed.domain.FeedSource;
import io.delimeat.feed.exception.FeedException;
import io.delimeat.util.DelimeatUtils;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public abstract class AbstractJaxbFeedDataSource implements FeedDataSource {
	
  	private  final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
  	
	private final FeedSource feedSource;
	private final Map<String,Object> properties;
	private final Map<String,String> headers;

	/**
	 * @return the feedSource
	 */
	public FeedSource getFeedSource() {
		return feedSource;
	}

	/**
	 * @return the properties
	 */
	public Map<String, Object> getProperties() {
		return properties;
	}

	/**
	 * @return the headers
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}

	public AbstractJaxbFeedDataSource(FeedSource feedSource, String mediaType, String metadata_source, String contentType){
		properties = new HashMap<String,Object>();
		properties.put("eclipselink.oxm.metadata-source", metadata_source);
		properties.put("eclipselink.media-type", mediaType);
		properties.put("eclipselink.json.include-root", false);
		
		headers = new HashMap<String,String>();
		headers.put("Accept", contentType);
		
		this.feedSource = feedSource;
	}
	
	/**
	 * @return
	 * @throws JAXBException
	 */
	private JAXBContext getContext() throws JAXBException{
		return JAXBContextFactory.createContext(new Class[]{FeedSearch.class,FeedResult.class}, properties);		
	}

	/**
	 * @param title
	 * @return
	 * @throws MalformedURLException
	 */
	public abstract URL generateUrl(String title) throws MalformedURLException;	

	/* (non-Javadoc)
	 * @see io.delimeat.feed.FeedDataSource#read(java.lang.String)
	 */
	@Override
	public List<FeedResult> read(String title) throws FeedException {
		try {
			URL url = generateUrl(DelimeatUtils.urlEscape(title, "UTF-8"));
			LOGGER.info("Opening {}",url);
			
			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder()
									.url(url)
									.headers(Headers.of(headers))
									.build();

			Response response = null;
			try{
				response = client.newCall(request).execute();
			} catch(SocketTimeoutException ex){
				String msg = String.format("Timeout for %s", url);
				throw new FeedException(msg);
			}
			
			if (response.isSuccessful() == false) {
				String msg = String.format("Feed returned code %s with message \"%s\" for url: %s",response.code(), response.message(), url);
				throw new FeedException(msg);
			}
			
			String contentType = response.body().contentType().toString().toLowerCase();
			String acceptHeader = headers.get("Accept").toLowerCase();
			byte[] responseBytes = response.body().bytes();
			if(contentType.contains(acceptHeader) == false){
				LOGGER.debug(new String(responseBytes));
				String msg = String.format("Feed returned content type %s expected %s for url: %s",contentType, acceptHeader, url);
				throw new FeedException(msg);
			}
			
			responseBytes = new String(responseBytes).replace("&tr", "&amp;tr").getBytes();
		
			try{
				StreamSource source = new StreamSource(new ByteArrayInputStream(responseBytes));
				return getContext()
						.createUnmarshaller()
						.unmarshal(source, FeedSearch.class)
						.getValue()
						.getResults();
				
			} catch(JAXBException ex){
				LOGGER.error(new String(responseBytes));
				throw new FeedException(ex);
			}

		} catch (IOException ex) {
			throw new FeedException(ex);
		}
	}
	

}
