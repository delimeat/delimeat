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
package io.delimeat.show.domain;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EpisodeTest {
	
    private Episode episode;

    @Before
    public void setUp() throws Exception {
        episode = new Episode();
    }

    @Test
    public void episodeIdTest() {
        Assert.assertNull(episode.getEpisodeId());
        episode.setEpisodeId(Long.MAX_VALUE);
        Assert.assertEquals(Long.MAX_VALUE, episode.getEpisodeId().longValue());
    }

    @Test
    public void titleTest() {
        Assert.assertNull(episode.getTitle());
        episode.setTitle("TITLE");
        Assert.assertEquals("TITLE", episode.getTitle());
    }

    @Test
    public void airDateTest() throws ParseException {
        Assert.assertNull(episode.getAirDate());
        episode.setAirDate(LocalDate.parse("2017-03-31"));
        Assert.assertEquals(LocalDate.parse("2017-03-31"), episode.getAirDate());
    }

    @Test
    public void seasonNumTest() {
        Assert.assertEquals(0, episode.getSeasonNum());
        episode.setSeasonNum(Integer.MIN_VALUE);
        Assert.assertEquals(Integer.MIN_VALUE, episode.getSeasonNum());
    }

    @Test
    public void episodeNumTest() {
        Assert.assertEquals(0, episode.getEpisodeNum());
        episode.setEpisodeNum(Integer.MIN_VALUE);
        Assert.assertEquals(Integer.MIN_VALUE, episode.getEpisodeNum());
    }
    
    @Test
    public void doubleEpisodeTest() {
        Assert.assertFalse(episode.isDoubleEp());
        episode.setDoubleEp(true);
        Assert.assertTrue(episode.isDoubleEp());
    }
    

    public void statusTest() {
        Assert.assertEquals(EpisodeStatus.PENDING, episode.getStatus());
        episode.setStatus(EpisodeStatus.SKIPPED);
        Assert.assertEquals(EpisodeStatus.SKIPPED, episode.getStatus());
    }
    
	
	@Test
	public void lastFeedUpdateTest() throws ParseException {
		Assert.assertNull(episode.getLastFeedUpdate());
		Instant instant = Instant.now();
		episode.setLastFeedUpdate(instant);
		Assert.assertEquals(instant, episode.getLastFeedUpdate());
	}
	
	@Test
	public void lastFeedCheckTest() throws ParseException {
		Assert.assertNull(episode.getLastFeedCheck());
		Instant instant = Instant.now();
		episode.setLastFeedCheck(instant);
		Assert.assertEquals(instant, episode.getLastFeedCheck());
	}

    @Test
    public void versionTest() {
        Assert.assertEquals(0, episode.getVersion());
        episode.setVersion(Integer.MAX_VALUE);
        Assert.assertEquals(Integer.MAX_VALUE, episode.getVersion());
    }
    
  @Test
  public void equalsEpisodeTest() throws ParseException{
    Episode otherEp = new Episode();
    otherEp.setTitle("EP");
    otherEp.setAirDate(LocalDate.parse("2016-01-28"));
    otherEp.setSeasonNum(1);
    otherEp.setEpisodeNum(2);
    otherEp.setEpisodeId(Long.MAX_VALUE);
    otherEp.setVersion(Integer.MIN_VALUE);
    
    episode.setTitle("EP");
    episode.setAirDate(LocalDate.parse("2016-01-28"));
    episode.setSeasonNum(1);
    episode.setEpisodeNum(2);
    episode.setEpisodeId(Long.MAX_VALUE);
    episode.setVersion(Integer.MIN_VALUE);
    
    Assert.assertTrue(episode.equals(otherEp));
  }
  
  @Test
  public void equalsEpisodeEpisodeIdTest() throws ParseException{
    Episode otherEp = new Episode();
    otherEp.setTitle("EP");
    otherEp.setAirDate(LocalDate.parse("2016-01-28"));
    otherEp.setSeasonNum(1);
    otherEp.setEpisodeNum(2);
    otherEp.setEpisodeId(Long.MAX_VALUE);
    otherEp.setVersion(Integer.MIN_VALUE);
    
    episode.setTitle("EP");
    episode.setAirDate(LocalDate.parse("2016-01-28"));
    episode.setSeasonNum(1);
    episode.setEpisodeNum(2);
    episode.setEpisodeId(Long.MIN_VALUE);
    episode.setVersion(Integer.MIN_VALUE);
    
    Assert.assertFalse(episode.equals(otherEp));
  }

    @Test
    public void showTest() {
        Assert.assertNull(episode.getShow());
        Show show = new Show();
        episode.setShow(show);
        Assert.assertEquals(show, episode.getShow());
    }

    @Test
    public void equalsTest(){
		Episode episode = new Episode();
		episode.setEpisodeId(1L);
		episode.setVersion(Integer.MAX_VALUE);
		
		Episode other = new Episode();
		other.setEpisodeId(1L);
		other.setVersion(Integer.MAX_VALUE);
		
		Assert.assertTrue(episode.equals(other));
    }
    @Test
    public void equalsNullTest() {
        Assert.assertFalse(episode.equals(null));
    }

    @Test
    public void equalsObjectTest() {
        Assert.assertFalse(episode.equals(new Object()));
    }

    @Test
    public void equalsSelfTest() {
        Assert.assertTrue(episode.equals(episode));
    }
    
    @Test
    public void equalsIdNullTest(){
		Episode episode = new Episode();
		episode.setEpisodeId(null);
		episode.setVersion(Integer.MAX_VALUE);
		
		Episode other = new Episode();
		other.setEpisodeId(1L);
		other.setVersion(Integer.MAX_VALUE);
		
		Assert.assertFalse(episode.equals(other));
    }
    
    @Test
    public void equalsIdTest(){
		Episode episode = new Episode();
		episode.setEpisodeId(2L);
		episode.setVersion(Integer.MAX_VALUE);
		
		Episode other = new Episode();
		other.setEpisodeId(1L);
		other.setVersion(Integer.MAX_VALUE);
		
		Assert.assertFalse(episode.equals(other));
    }
    
    @Test
    public void equalsVersionTest(){
		Episode episode = new Episode();
		episode.setEpisodeId(1L);
		episode.setVersion(Integer.MAX_VALUE);
		
		Episode other = new Episode();
		other.setEpisodeId(1L);
		other.setVersion(Integer.MIN_VALUE);
		
		Assert.assertFalse(episode.equals(other));
    }


    @Test
    public void hashTest() {
        episode.setEpisodeId(Long.MIN_VALUE);
        episode.setVersion(Integer.MIN_VALUE);

        Assert.assertEquals(961, episode.hashCode());
    }

    @Test
    public void toStringTest() throws ParseException {
        Assert.assertEquals("Episode [seasonNum=0, episodeNum=0, doubleEp=false, status=PENDING, version=0, ]", episode.toString());
    }

}
