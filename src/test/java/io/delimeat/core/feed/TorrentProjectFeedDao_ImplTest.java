package io.delimeat.core.feed;

import io.delimeat.util.jaxrs.JaxbContextResolver;
import io.delimeat.util.jaxrs.ReplaceContentTypeResponseFilter;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;

import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.moxy.xml.MoxyXmlFeature;
import org.junit.Before;
import org.junit.Test;

public class TorrentProjectFeedDao_ImplTest {

	private static final String METADATA = "META-INF/oxm/feed-torrentproject-oxm.xml";
	
	private TorrentProjectJaxrsFeedDao_Impl dao;
	
	@Before
	public void setUp() {
		dao = new TorrentProjectJaxrsFeedDao_Impl();
	}
	
	@Test
	public void test() throws Exception{
		JaxbContextResolver resolver = new JaxbContextResolver();
		resolver.setClasses(Arrays.asList(FeedResult.class,FeedSearch.class));

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(JAXBContextProperties.OXM_METADATA_SOURCE,Arrays.asList(METADATA));
		//properties.put(JAXBContextProperties.MEDIA_TYPE, "application/xml");
		//properties.put(MarshallerProperties.JSON_INCLUDE_ROOT, false);
		//properties.put(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		JAXBContext context = JAXBContext.newInstance(new Class[] {FeedResult.class, FeedSearch.class}, properties);
		resolver.setContext(context);
		
		final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		MoxyXmlFeature feature = new MoxyXmlFeature(properties, classLoader, true, FeedResult.class, FeedSearch.class);

		Logger LOGGER = Logger.getLogger(LoggingFilter.class.getName());

		ClientConfig configuration = new ClientConfig()
										.register(feature)
										.register(ReplaceContentTypeResponseFilter.class)
										.register(new LoggingFilter(LOGGER, true));
		
		Client client = JerseyClientBuilder.createClient(configuration);
		
		dao.setClient(client);
		dao.setMediaType(MediaType.APPLICATION_XML_TYPE);
		dao.setBaseUri(new URI("https://torrentproject.se/"));
		
		System.out.println(dao.read("ubuntu dvd"));
	}

}
