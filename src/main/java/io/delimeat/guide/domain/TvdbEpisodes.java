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

import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TvdbEpisodes {

	private Integer first = 0;

	private Integer last = 0;

	private Integer next = 0;

	private Integer previous = 0;

	private List<GuideEpisode> episodes = new ArrayList<GuideEpisode>();

	/**
	 * @return the first
	 */
	public Integer getFirst() {
		return first;
	}

	/**
	 * @param first
	 *            the first to set
	 */
	public void setFirst(Integer first) {
		this.first = first;
	}

	/**
	 * @return the last
	 */
	public Integer getLast() {
		return last;
	}

	/**
	 * @param last
	 *            the last to set
	 */
	public void setLast(Integer last) {
		this.last = last;
	}

	/**
	 * @return the next
	 */
	public Integer getNext() {
		return next;
	}

	/**
	 * @param next
	 *            the next to set
	 */
	public void setNext(Integer next) {
		this.next = next;
	}

	/**
	 * @return the previous
	 */
	public Integer getPrevious() {
		return previous;
	}

	/**
	 * @param previous
	 *            the previous to set
	 */
	public void setPrevious(Integer previous) {
		this.previous = previous;
	}

	/**
	 * @return the episodes
	 */
	public List<GuideEpisode> getEpisodes() {
		return episodes;
	}

	/**
	 * @param episodes
	 *            the episodes to set
	 */
	public void setEpisodes(List<GuideEpisode> episodes) {
		this.episodes = episodes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("first", first)
				.add("last", last)  
				.add("next", next)
				.add("previous", previous)
				.add("episodes", episodes)
				.omitNullValues()
				.toString(); 
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override 
	public int hashCode() {
		return Objects.hash(first,last,next,previous,episodes);
	}

}
