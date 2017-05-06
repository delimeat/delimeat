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

import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FeedSearch {

	private List<FeedResult> results = new ArrayList<FeedResult>();

	public List<FeedResult> getResults() {
		return results;
	}

	public void setResults(List<FeedResult> results) {
		this.results = results;
	}

	@Override
	public String toString() {
     	return MoreObjects.toStringHelper(this)
              .add("results", results) 
              .omitNullValues()
              .toString();
	}

  @Override 
  public int hashCode() {
    return Objects.hash(results);
  }

}
