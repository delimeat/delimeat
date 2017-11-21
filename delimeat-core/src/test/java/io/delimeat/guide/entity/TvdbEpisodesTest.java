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
package io.delimeat.guide.entity;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.delimeat.guide.entity.GuideEpisode;
import io.delimeat.guide.entity.TvdbEpisodes;

public class TvdbEpisodesTest {

	private TvdbEpisodes episodes;

	@BeforeEach
	public void setUp() {
		episodes = new TvdbEpisodes();
	}

	@Test
	public void firstTest() {
		Assertions.assertEquals(0, episodes.getFirst().intValue());
		episodes.setFirst(Integer.MAX_VALUE);
		Assertions.assertEquals(Integer.MAX_VALUE, episodes.getFirst().intValue());
	}

	@Test
	public void lastTest() {
		Assertions.assertEquals(0, episodes.getLast().intValue());
		episodes.setLast(Integer.MAX_VALUE);
		Assertions.assertEquals(Integer.MAX_VALUE, episodes.getLast().intValue());
	}

	@Test
	public void nextTest() {
		Assertions.assertEquals(0, episodes.getNext().intValue());
		episodes.setNext(Integer.MAX_VALUE);
		Assertions.assertEquals(Integer.MAX_VALUE, episodes.getNext().intValue());
	}

	@Test
	public void previousTest() {
		Assertions.assertEquals(0, episodes.getPrevious().intValue());
		episodes.setPrevious(Integer.MAX_VALUE);
		Assertions.assertEquals(Integer.MAX_VALUE, episodes.getPrevious().intValue());
	}

	@Test
	public void episodesTest() {
		Assertions.assertNotNull(episodes.getEpisodes());
		Assertions.assertEquals(0, episodes.getEpisodes().size());
		GuideEpisode episode = new GuideEpisode();
		episodes.setEpisodes(Arrays.asList(episode));
		Assertions.assertEquals(1, episodes.getEpisodes().size());
		Assertions.assertEquals(episode, episodes.getEpisodes().get(0));
	}

	@Test
	public void hashCodeTest() {
		episodes.setFirst(Integer.MAX_VALUE);
		episodes.setLast(Integer.MAX_VALUE);
		episodes.setNext(Integer.MAX_VALUE);
		episodes.setPrevious(Integer.MAX_VALUE);
		GuideEpisode episode = new GuideEpisode();
		episodes.setEpisodes(Arrays.asList(episode));
		Assertions.assertEquals(-139285987, episodes.hashCode());
	}

	@Test
	public void toStringTest() {
		episodes.setEpisodes(Arrays.asList(new GuideEpisode()));
		Assertions.assertEquals("TvdbEpisodes [first=0, last=0, next=0, previous=0, episodes=[GuideEpisode [seasonNum=0, episodeNum=0, productionNum=0, ]]]",
				episodes.toString());
	}

	@Test
	public void equalsTest() {
		episodes.setFirst(Integer.MAX_VALUE);
		episodes.setLast(Integer.MAX_VALUE);
		episodes.setNext(Integer.MAX_VALUE);
		episodes.setPrevious(Integer.MAX_VALUE);
		episodes.setEpisodes(Arrays.asList(new GuideEpisode()));
		TvdbEpisodes other = new TvdbEpisodes();
		other.setFirst(Integer.MAX_VALUE);
		other.setLast(Integer.MAX_VALUE);
		other.setNext(Integer.MAX_VALUE);
		other.setPrevious(Integer.MAX_VALUE);

		other.setEpisodes(Arrays.asList(new GuideEpisode()));
		Assertions.assertTrue(episodes.equals(other));
	}

	@Test
	public void equalsSelfTest() {
		Assertions.assertTrue(episodes.equals(episodes));
	}

	@Test
	public void equalsNullTest() {
		Assertions.assertFalse(episodes.equals(null));
	}

	@Test
	public void equalsOtherClassTest() {
		Assertions.assertFalse(episodes.equals(new Object()));
	}
	
	@Test
	public void equalsEpisodesNullTest() {
		episodes.setFirst(Integer.MAX_VALUE);
		episodes.setLast(Integer.MAX_VALUE);
		episodes.setNext(Integer.MAX_VALUE);
		episodes.setPrevious(Integer.MAX_VALUE);
		episodes.setEpisodes(null);
		TvdbEpisodes other = new TvdbEpisodes();
		other.setFirst(Integer.MAX_VALUE);
		other.setLast(Integer.MAX_VALUE);
		other.setNext(Integer.MAX_VALUE);
		other.setPrevious(Integer.MAX_VALUE);

		other.setEpisodes(Arrays.asList(new GuideEpisode()));
		Assertions.assertFalse(episodes.equals(other));
	}
	
	@Test
	public void equalsFirstNullTest() {
		episodes.setFirst(null);
		episodes.setLast(Integer.MAX_VALUE);
		episodes.setNext(Integer.MAX_VALUE);
		episodes.setPrevious(Integer.MAX_VALUE);
		episodes.setEpisodes(Arrays.asList(new GuideEpisode()));
		TvdbEpisodes other = new TvdbEpisodes();
		other.setFirst(Integer.MAX_VALUE);
		other.setLast(Integer.MAX_VALUE);
		other.setNext(Integer.MAX_VALUE);
		other.setPrevious(Integer.MAX_VALUE);

		other.setEpisodes(Arrays.asList(new GuideEpisode()));
		Assertions.assertFalse(episodes.equals(other));
	}
	
	@Test
	public void equalsLastNullTest() {
		episodes.setFirst(Integer.MAX_VALUE);
		episodes.setLast(null);
		episodes.setNext(Integer.MAX_VALUE);
		episodes.setPrevious(Integer.MAX_VALUE);
		episodes.setEpisodes(Arrays.asList(new GuideEpisode()));
		TvdbEpisodes other = new TvdbEpisodes();
		other.setFirst(Integer.MAX_VALUE);
		other.setLast(Integer.MAX_VALUE);
		other.setNext(Integer.MAX_VALUE);
		other.setPrevious(Integer.MAX_VALUE);

		other.setEpisodes(Arrays.asList(new GuideEpisode()));
		Assertions.assertFalse(episodes.equals(other));
	}
	
	@Test
	public void equalsNextNullTest() {
		episodes.setFirst(Integer.MAX_VALUE);
		episodes.setLast(Integer.MAX_VALUE);
		episodes.setNext(null);
		episodes.setPrevious(Integer.MAX_VALUE);
		episodes.setEpisodes(Arrays.asList(new GuideEpisode()));
		TvdbEpisodes other = new TvdbEpisodes();
		other.setFirst(Integer.MAX_VALUE);
		other.setLast(Integer.MAX_VALUE);
		other.setNext(Integer.MAX_VALUE);
		other.setPrevious(Integer.MAX_VALUE);

		other.setEpisodes(Arrays.asList(new GuideEpisode()));
		Assertions.assertFalse(episodes.equals(other));
	}
	
	@Test
	public void equalsPreviousNullTest() {
		episodes.setFirst(Integer.MAX_VALUE);
		episodes.setLast(Integer.MAX_VALUE);
		episodes.setNext(Integer.MAX_VALUE);
		episodes.setPrevious(null);
		episodes.setEpisodes(Arrays.asList(new GuideEpisode()));
		TvdbEpisodes other = new TvdbEpisodes();
		other.setFirst(Integer.MAX_VALUE);
		other.setLast(Integer.MAX_VALUE);
		other.setNext(Integer.MAX_VALUE);
		other.setPrevious(Integer.MAX_VALUE);

		other.setEpisodes(Arrays.asList(new GuideEpisode()));
		Assertions.assertFalse(episodes.equals(other));
	}
	
	@Test
	public void equalsEpisodesTest() {
		episodes.setFirst(Integer.MAX_VALUE);
		episodes.setLast(Integer.MAX_VALUE);
		episodes.setNext(Integer.MAX_VALUE);
		episodes.setPrevious(Integer.MAX_VALUE);
		episodes.setEpisodes(Arrays.asList(new GuideEpisode(),new GuideEpisode()));
		TvdbEpisodes other = new TvdbEpisodes();
		other.setFirst(Integer.MAX_VALUE);
		other.setLast(Integer.MAX_VALUE);
		other.setNext(Integer.MAX_VALUE);
		other.setPrevious(Integer.MAX_VALUE);

		other.setEpisodes(Arrays.asList(new GuideEpisode()));
		Assertions.assertFalse(episodes.equals(other));
	}
	
	@Test
	public void equalsFirstTest() {
		episodes.setFirst(Integer.MIN_VALUE);
		episodes.setLast(Integer.MAX_VALUE);
		episodes.setNext(Integer.MAX_VALUE);
		episodes.setPrevious(Integer.MAX_VALUE);
		episodes.setEpisodes(Arrays.asList(new GuideEpisode()));
		TvdbEpisodes other = new TvdbEpisodes();
		other.setFirst(Integer.MAX_VALUE);
		other.setLast(Integer.MAX_VALUE);
		other.setNext(Integer.MAX_VALUE);
		other.setPrevious(Integer.MAX_VALUE);

		other.setEpisodes(Arrays.asList(new GuideEpisode()));
		Assertions.assertFalse(episodes.equals(other));
	}
	
	@Test
	public void equalsLastTest() {
		episodes.setFirst(Integer.MAX_VALUE);
		episodes.setLast(Integer.MIN_VALUE);
		episodes.setNext(Integer.MAX_VALUE);
		episodes.setPrevious(Integer.MAX_VALUE);
		episodes.setEpisodes(Arrays.asList(new GuideEpisode()));
		TvdbEpisodes other = new TvdbEpisodes();
		other.setFirst(Integer.MAX_VALUE);
		other.setLast(Integer.MAX_VALUE);
		other.setNext(Integer.MAX_VALUE);
		other.setPrevious(Integer.MAX_VALUE);

		other.setEpisodes(Arrays.asList(new GuideEpisode()));
		Assertions.assertFalse(episodes.equals(other));
	}
	
	@Test
	public void equalsNextTest() {
		episodes.setFirst(Integer.MAX_VALUE);
		episodes.setLast(Integer.MAX_VALUE);
		episodes.setNext(Integer.MIN_VALUE);
		episodes.setPrevious(Integer.MAX_VALUE);
		episodes.setEpisodes(Arrays.asList(new GuideEpisode()));
		TvdbEpisodes other = new TvdbEpisodes();
		other.setFirst(Integer.MAX_VALUE);
		other.setLast(Integer.MAX_VALUE);
		other.setNext(Integer.MAX_VALUE);
		other.setPrevious(Integer.MAX_VALUE);

		other.setEpisodes(Arrays.asList(new GuideEpisode()));
		Assertions.assertFalse(episodes.equals(other));
	}
	
	@Test
	public void equalsPreviousTest() {
		episodes.setFirst(Integer.MAX_VALUE);
		episodes.setLast(Integer.MAX_VALUE);
		episodes.setNext(Integer.MAX_VALUE);
		episodes.setPrevious(Integer.MIN_VALUE);
		episodes.setEpisodes(Arrays.asList(new GuideEpisode()));
		TvdbEpisodes other = new TvdbEpisodes();
		other.setFirst(Integer.MAX_VALUE);
		other.setLast(Integer.MAX_VALUE);
		other.setNext(Integer.MAX_VALUE);
		other.setPrevious(Integer.MAX_VALUE);

		other.setEpisodes(Arrays.asList(new GuideEpisode()));
		Assertions.assertFalse(episodes.equals(other));
	}
}
