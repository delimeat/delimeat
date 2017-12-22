package io.delimeat.feed;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.delimeat.feed.entity.FeedSource;

public class JaxrsFeedClient_ImplTest {

	private JaxrsFeedDataSource_Impl client;
	
	@BeforeEach
	public void setUp() {
		client = new JaxrsFeedDataSource_Impl();
	}
	
	@Test
	public void baseUriTest() throws URISyntaxException {
		Assertions.assertNull(client.getBaseUri());
		client.setBaseUri(new URI("test://test"));
		Assertions.assertEquals(new URI("test://test"), client.getBaseUri());
	}

	@Test
	public void mediaTypeTest() {
		Assertions.assertNull(client.getMediaType());
		client.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		Assertions.assertEquals(MediaType.APPLICATION_JSON_TYPE, client.getMediaType());
	}
	
	@Test
	public void feedSourceTest() {
		Assertions.assertNull(client.getFeedSource());
		client.setFeedSource(FeedSource.BITSNOOP);
		Assertions.assertEquals(FeedSource.BITSNOOP, client.getFeedSource());
	}

	@Test
	public void targetFactoryTest() {
		Assertions.assertNull(client.getTargetFactory());
		FeedTargetFactory factory = Mockito.mock(FeedTargetFactory.class);
		client.setTargetFactory(factory);
		Assertions.assertEquals(factory, client.getTargetFactory());
	}	
	
}
