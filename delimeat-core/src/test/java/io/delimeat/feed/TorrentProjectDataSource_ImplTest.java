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

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.delimeat.feed.entity.FeedSource;

public class TorrentProjectDataSource_ImplTest {

	private TorrentProjectDataSource_Impl client;
	
	@BeforeEach
	public void setUp() {
		client = new TorrentProjectDataSource_Impl();
	}
	
	@Test
	public void baseUriTest() throws URISyntaxException {
		Assertions.assertNull(client.getBaseUri());
		client.setBaseUri(new URI("test://test"));
		Assertions.assertEquals(new URI("test://test"), client.getBaseUri());
	}
	
	@Test
	public void mediaTypeTest() {
		Assertions.assertEquals(MediaType.APPLICATION_XML_TYPE, client.getMediaType());
	}
	
	@Test
	public void feedSourceTest() {
		Assertions.assertEquals(FeedSource.TORRENTPROJECT, client.getFeedSource());
	}
	
	@Test
	public void moxyPropertiesTest() {
		Assertions.assertNotNull(client.getMoxyProperties());
		Assertions.assertEquals(2, client.getMoxyProperties().size());
		Assertions.assertEquals("application/xml", client.getMoxyProperties().get("eclipselink.media-type"));
		Assertions.assertEquals("oxm/feed-torrentproject-oxm.xml", client.getMoxyProperties().get("eclipselink.oxm.metadata-source"));
	}
}
