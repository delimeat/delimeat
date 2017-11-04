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
package io.delimeat.feed.entity;

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
	public void infoHasHexTest(){
		Assert.assertNull(result.getInfoHashHex());
		result.setInfoHashHex("INFO_HASH");
		Assert.assertEquals("INFO_HASH", result.getInfoHashHex());	
	}
	@Test
	public void equalsTest(){
		result.setTorrentURL("TORRENT_LOCATION");
		result.setTitle("FILE_NAME");
		result.setContentLength(Long.MAX_VALUE);
		result.setSeeders(Long.MIN_VALUE);
		result.setLeechers(Long.MAX_VALUE);
		result.setInfoHashHex("INFO_HASH");
		result.setSource(FeedSource.KAT);
		
		FeedResult other = new FeedResult();
		other.setTorrentURL("TORRENT_LOCATION");
		other.setTitle("FILE_NAME");
		other.setContentLength(Long.MAX_VALUE);
		other.setSeeders(Long.MIN_VALUE);
		other.setLeechers(Long.MAX_VALUE);
		other.setInfoHashHex("INFO_HASH");
		other.setSource(FeedSource.KAT);

		Assert.assertTrue(result.equals(other));
	}
	
	@Test
	public void equalsNullTest(){
		Assert.assertFalse(result.equals(null));
	}
	
	@Test
	public void equalsObjectTest(){
		Assert.assertFalse(result.equals(new Object()));
	}
	
	@Test
	public void equalsInfoHashHexNullTest(){
		result.setTorrentURL("TORRENT_LOCATION");
		result.setTitle("FILE_NAME");
		result.setContentLength(Long.MAX_VALUE);
		result.setSeeders(Long.MIN_VALUE);
		result.setLeechers(Long.MAX_VALUE);
		result.setInfoHashHex(null);
		result.setSource(FeedSource.KAT);
		
		FeedResult other = new FeedResult();
		other.setTorrentURL("TORRENT_LOCATION");
		other.setTitle("FILE_NAME");
		other.setContentLength(Long.MAX_VALUE);
		other.setSeeders(Long.MIN_VALUE);
		other.setLeechers(Long.MAX_VALUE);
		other.setInfoHashHex("INFO_HASH");
		other.setSource(FeedSource.KAT);

		Assert.assertFalse(result.equals(other));
	}
	
	@Test
	public void equalsTitleNullTest(){
		result.setTorrentURL("TORRENT_LOCATION");
		result.setTitle(null);
		result.setContentLength(Long.MAX_VALUE);
		result.setSeeders(Long.MIN_VALUE);
		result.setLeechers(Long.MAX_VALUE);
		result.setInfoHashHex("INFO_HASH");
		result.setSource(FeedSource.KAT);
		
		FeedResult other = new FeedResult();
		other.setTorrentURL("TORRENT_LOCATION");
		other.setTitle("FILE_NAME");
		other.setContentLength(Long.MAX_VALUE);
		other.setSeeders(Long.MIN_VALUE);
		other.setLeechers(Long.MAX_VALUE);
		other.setInfoHashHex("INFO_HASH");
		other.setSource(FeedSource.KAT);

		Assert.assertFalse(result.equals(other));
	}
	
	@Test
	public void equalsTorrentURLNullTest(){
		result.setTorrentURL(null);
		result.setTitle("FILE_NAME");
		result.setContentLength(Long.MAX_VALUE);
		result.setSeeders(Long.MIN_VALUE);
		result.setLeechers(Long.MAX_VALUE);
		result.setInfoHashHex("INFO_HASH");
		result.setSource(FeedSource.KAT);
		
		FeedResult other = new FeedResult();
		other.setTorrentURL("TORRENT_LOCATION");
		other.setTitle("FILE_NAME");
		other.setContentLength(Long.MAX_VALUE);
		other.setSeeders(Long.MIN_VALUE);
		other.setLeechers(Long.MAX_VALUE);
		other.setInfoHashHex("INFO_HASH");
		other.setSource(FeedSource.KAT);

		Assert.assertFalse(result.equals(other));
	}
	
	@Test
	public void equalsContentLengthTest(){
		result.setTorrentURL("TORRENT_LOCATION");
		result.setTitle("FILE_NAME");
		result.setContentLength(Long.MIN_VALUE);
		result.setSeeders(Long.MIN_VALUE);
		result.setLeechers(Long.MAX_VALUE);
		result.setInfoHashHex("INFO_HASH");
		result.setSource(FeedSource.KAT);
		
		FeedResult other = new FeedResult();
		other.setTorrentURL("TORRENT_LOCATION");
		other.setTitle("FILE_NAME");
		other.setContentLength(Long.MAX_VALUE);
		other.setSeeders(Long.MIN_VALUE);
		other.setLeechers(Long.MAX_VALUE);
		other.setInfoHashHex("INFO_HASH");
		other.setSource(FeedSource.KAT);

		Assert.assertFalse(result.equals(other));
	}
	
	@Test
	public void equalsInfoHashHexTest(){
		result.setTorrentURL("TORRENT_LOCATION");
		result.setTitle("FILE_NAME");
		result.setContentLength(Long.MAX_VALUE);
		result.setSeeders(Long.MIN_VALUE);
		result.setLeechers(Long.MAX_VALUE);
		result.setInfoHashHex("INFO_HASH_OTHER");
		result.setSource(FeedSource.KAT);
		
		FeedResult other = new FeedResult();
		other.setTorrentURL("TORRENT_LOCATION");
		other.setTitle("FILE_NAME");
		other.setContentLength(Long.MAX_VALUE);
		other.setSeeders(Long.MIN_VALUE);
		other.setLeechers(Long.MAX_VALUE);
		other.setInfoHashHex("INFO_HASH");
		other.setSource(FeedSource.KAT);

		Assert.assertFalse(result.equals(other));
	}

	@Test
	public void equalsLeechersTest(){
		result.setTorrentURL("TORRENT_LOCATION");
		result.setTitle("FILE_NAME");
		result.setContentLength(Long.MAX_VALUE);
		result.setSeeders(Long.MIN_VALUE);
		result.setLeechers(Long.MIN_VALUE);
		result.setInfoHashHex("INFO_HASH");
		result.setSource(FeedSource.KAT);
		
		FeedResult other = new FeedResult();
		other.setTorrentURL("TORRENT_LOCATION");
		other.setTitle("FILE_NAME");
		other.setContentLength(Long.MAX_VALUE);
		other.setSeeders(Long.MIN_VALUE);
		other.setLeechers(Long.MAX_VALUE);
		other.setInfoHashHex("INFO_HASH");
		other.setSource(FeedSource.KAT);

		Assert.assertFalse(result.equals(other));
	}
	
	@Test
	public void equalsSeedersTest(){
		result.setTorrentURL("TORRENT_LOCATION");
		result.setTitle("FILE_NAME");
		result.setContentLength(Long.MAX_VALUE);
		result.setSeeders(Long.MAX_VALUE);
		result.setLeechers(Long.MAX_VALUE);
		result.setInfoHashHex("INFO_HASH");
		result.setSource(FeedSource.KAT);
		
		FeedResult other = new FeedResult();
		other.setTorrentURL("TORRENT_LOCATION");
		other.setTitle("FILE_NAME");
		other.setContentLength(Long.MAX_VALUE);
		other.setSeeders(Long.MIN_VALUE);
		other.setLeechers(Long.MAX_VALUE);
		other.setInfoHashHex("INFO_HASH");
		other.setSource(FeedSource.KAT);

		Assert.assertFalse(result.equals(other));
	}
	
	@Test
	public void equalsTitleTest(){
		result.setTorrentURL("TORRENT_LOCATION");
		result.setTitle("FILE_NAME_OTHER");
		result.setContentLength(Long.MAX_VALUE);
		result.setSeeders(Long.MIN_VALUE);
		result.setLeechers(Long.MAX_VALUE);
		result.setInfoHashHex("INFO_HASH");
		result.setSource(FeedSource.KAT);
		
		FeedResult other = new FeedResult();
		other.setTorrentURL("TORRENT_LOCATION");
		other.setTitle("FILE_NAME");
		other.setContentLength(Long.MAX_VALUE);
		other.setSeeders(Long.MIN_VALUE);
		other.setLeechers(Long.MAX_VALUE);
		other.setInfoHashHex("INFO_HASH");
		other.setSource(FeedSource.KAT);

		Assert.assertFalse(result.equals(other));
	}
	
	
	
	@Test
	public void equalsTorrentURLTest(){
		result.setTorrentURL("TORRENT_LOCATION_OTHER");
		result.setTitle("FILE_NAME");
		result.setContentLength(Long.MAX_VALUE);
		result.setSeeders(Long.MIN_VALUE);
		result.setLeechers(Long.MAX_VALUE);
		result.setInfoHashHex("INFO_HASH");
		result.setSource(FeedSource.KAT);
		
		FeedResult other = new FeedResult();
		other.setTorrentURL("TORRENT_LOCATION");
		other.setTitle("FILE_NAME");
		other.setContentLength(Long.MAX_VALUE);
		other.setSeeders(Long.MIN_VALUE);
		other.setLeechers(Long.MAX_VALUE);
		other.setInfoHashHex("INFO_HASH");
		other.setSource(FeedSource.KAT);

		Assert.assertFalse(result.equals(other));
	}
  
  	@Test
  	public void hashCodeTest(){
		result.setTorrentURL("TORRENT_LOCATION");
		result.setTitle("FILE_NAME");
		result.setContentLength(Long.MAX_VALUE);
		result.setSeeders(Long.MIN_VALUE);
		result.setLeechers(Long.MAX_VALUE);
		result.setInfoHashHex("INFO_HASH");
     	Assert.assertEquals(-345026678,result.hashCode());
   }
  
  	@Test
  	public void toStringTest(){
     	Assert.assertEquals("FeedResult [contentLength=0, seeders=0, leechers=0]",result.toString());
   }
}
