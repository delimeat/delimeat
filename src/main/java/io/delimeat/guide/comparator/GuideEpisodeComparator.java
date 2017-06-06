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
package io.delimeat.guide.comparator;

import java.util.Comparator;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

import io.delimeat.guide.domain.GuideEpisode;

public class GuideEpisodeComparator implements Comparator<GuideEpisode> {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(GuideEpisode episode1, GuideEpisode episode2) {
		
		return ComparisonChain.start()
				.compare(episode1.getAirDate(), episode2.getAirDate(), Ordering.natural().nullsFirst())
				.compare(episode1.getSeasonNum(), episode2.getSeasonNum(), Ordering.natural().nullsFirst())
				.compare(episode1.getEpisodeNum(), episode2.getEpisodeNum(), Ordering.natural().nullsFirst())
				.result();
	}

}
