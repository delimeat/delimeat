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
package io.delimeat.guide.domain;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.guide.domain.GuideEpisode;
import io.delimeat.guide.domain.TvdbEpisodes;

public class TvdbEpisodesTest {

	private TvdbEpisodes episodes;

	@Before
	public void setUp() {
		episodes = new TvdbEpisodes();
	}

	@Test
	public void firstTest() {
		Assert.assertEquals(0, episodes.getFirst().intValue());
		episodes.setFirst(Integer.MAX_VALUE);
		Assert.assertEquals(Integer.MAX_VALUE, episodes.getFirst().intValue());
	}

	@Test
	public void lastTest() {
		Assert.assertEquals(0, episodes.getLast().intValue());
		episodes.setLast(Integer.MAX_VALUE);
		Assert.assertEquals(Integer.MAX_VALUE, episodes.getLast().intValue());
	}

	@Test
	public void nextTest() {
		Assert.assertEquals(0, episodes.getNext().intValue());
		episodes.setNext(Integer.MAX_VALUE);
		Assert.assertEquals(Integer.MAX_VALUE, episodes.getNext().intValue());
	}

	@Test
	public void previousTest() {
		Assert.assertEquals(0, episodes.getPrevious().intValue());
		episodes.setPrevious(Integer.MAX_VALUE);
		Assert.assertEquals(Integer.MAX_VALUE, episodes.getPrevious().intValue());
	}

	@Test
	public void episodesTest() {
		Assert.assertNotNull(episodes.getEpisodes());
		Assert.assertEquals(0, episodes.getEpisodes().size());
		GuideEpisode episode = new GuideEpisode();
		episodes.setEpisodes(Arrays.asList(episode));
		Assert.assertEquals(1, episodes.getEpisodes().size());
		Assert.assertEquals(episode, episodes.getEpisodes().get(0));
	}
  
  	@Test	
  	public void hashCodeTest(){
		episodes.setFirst(Integer.MAX_VALUE);
		episodes.setLast(Integer.MAX_VALUE);
		episodes.setNext(Integer.MAX_VALUE);
		episodes.setPrevious(Integer.MAX_VALUE);
		GuideEpisode episode = new GuideEpisode();
		episodes.setEpisodes(Arrays.asList(episode)); 
     	Assert.assertEquals(11908441,episodes.hashCode());
   }
  
  	@Test	
  	public void toStringTest(){
		episodes.setEpisodes(Arrays.asList(new GuideEpisode())); 
     	Assert.assertEquals("TvdbEpisodes(first=0, last=0, next=0, previous=0, episodes=[GuideEpisode(airDate=null, seasonNum=0, episodeNum=0, productionNum=0, title=null)])",episodes.toString());
   }
}
