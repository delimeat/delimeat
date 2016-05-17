package io.delimeat.core.feed;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;

import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.moxy.xml.MoxyXmlFeature;

import io.delimeat.util.jaxrs.AbstractJaxrsClientHelper;
import io.delimeat.util.jaxrs.ReplaceContentTypeResponseFilter;

public class TorrentProjectJaxrsFeedDao_Impl extends AbstractJaxrsClientHelper implements FeedDao{

	public TorrentProjectJaxrsFeedDao_Impl(){
		final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(JAXBContextProperties.OXM_METADATA_SOURCE,Arrays.asList("META-INF/oxm/feed-torrentproject-oxm.xml"));
		MoxyXmlFeature feature = new MoxyXmlFeature(properties, classLoader, true, FeedResult.class, FeedSearch.class);

		Logger LOGGER = Logger.getLogger(LoggingFilter.class.getName());

		ClientConfig configuration = new ClientConfig()
										.register(feature)
										.register(ReplaceContentTypeResponseFilter.class)
										.register(new LoggingFilter(LOGGER, true));
				
		Client client = JerseyClientBuilder.createClient(configuration);
		setClient(client);
	}
	
	@Override
	public FeedSource getFeedSource() {
		return FeedSource.STRIKE;
	}
	
	@Override
	public List<FeedResult> read(String title) throws FeedException {
        String encodedTitle;
        try {
            encodedTitle = URLEncoder.encode(title, getEncoding());
        } catch (UnsupportedEncodingException ex) {
        	throw new RuntimeException(ex);
        }
        try {
            return getTarget()
            		.queryParam("s", encodedTitle)
            		.queryParam("out", "rss")
              		.request(getMediaType())
              		.get(FeedSearch.class)
              		.getResults();
          
        } catch (WebApplicationException ex) {
            throw new FeedException(ex);
        }
	}


}
