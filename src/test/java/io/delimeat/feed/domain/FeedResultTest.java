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
package io.delimeat.feed.domain;

import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.domain.FeedResultRejection;
import io.delimeat.torrent.domain.Torrent;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FeedResultTest {

	private FeedResult result;
	
	@Before
	public void setUp(){
		result = new FeedResult();
	}
	
	@Test
	public void torrentUrlTest(){
		Assert.assertEquals(null, result.getTorrentURL());
		result.setTorrentURL("TORRENT_LOCATION");
		Assert.assertEquals("TORRENT_LOCATION", result.getTorrentURL());
	}
	
	@Test
	public void titleTest(){
		Assert.assertEquals(null, result.getTitle());
		result.setTitle("FILE_NAME");
		Assert.assertEquals("FILE_NAME", result.getTitle());
	}
	
	@Test
	public void contentLengthTest(){
		Assert.assertEquals(0, result.getContentLength());
		result.setContentLength(Long.MAX_VALUE);
		Assert.assertEquals(Long.MAX_VALUE, result.getContentLength());
	}
	
	@Test
	public void seedersTest(){
		Assert.assertEquals(0,result.getSeeders());
		result.setSeeders(Long.MIN_VALUE);
		Assert.assertEquals(Long.MIN_VALUE, result.getSeeders());
	}
	
	@Test
	public void leechersTest(){
		Assert.assertEquals(0,result.getLeechers());
		result.setLeechers(Long.MAX_VALUE);
		Assert.assertEquals(Long.MAX_VALUE, result.getLeechers());		
	}
	
	@Test
	public void feedResultRejectionsTest(){
		Assert.assertNotNull(result.getFeedResultRejections());
		Assert.assertEquals(0, result.getFeedResultRejections().size());
		
		result.setFeedResultRejections(Arrays.asList(FeedResultRejection.CONTAINS_COMPRESSED,FeedResultRejection.INCORRECT_TITLE));
		
		Assert.assertEquals(2, 0, result.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.CONTAINS_COMPRESSED, result.getFeedResultRejections().get(0));
		Assert.assertEquals(FeedResultRejection.INCORRECT_TITLE, result.getFeedResultRejections().get(1));
	}
	
	@Test
	public void torrentTest(){
		Assert.assertNull(result.getTorrent());
		Torrent torrent = new Torrent();
		result.setTorrent(torrent);
		Assert.assertEquals(torrent, result.getTorrent());
	}
  
  	@Test
  	public void hashCodeTest(){
		result.setTorrentURL("TORRENT_LOCATION");
		result.setTitle("FILE_NAME");
		result.setContentLength(Long.MAX_VALUE);
		result.setSeeders(Long.MIN_VALUE);
		result.setLeechers(Long.MAX_VALUE);
		result.getFeedResultRejections().add(FeedResultRejection.CONTAINS_COMPRESSED);
     	Assert.assertEquals(293493943,result.hashCode());
   }
  
  	@Test
  	public void toStringTest(){
     	Assert.assertEquals("FeedResult{contentLength=0, seeders=0, leechers=0, feedResultRejections=[]}",result.toString());
   }
}
