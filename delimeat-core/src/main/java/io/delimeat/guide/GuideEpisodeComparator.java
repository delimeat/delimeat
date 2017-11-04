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
package io.delimeat.guide;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;

import io.delimeat.guide.entity.GuideEpisode;

public class GuideEpisodeComparator implements Comparator<GuideEpisode> {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(GuideEpisode episode1, GuideEpisode episode2) {
		LocalDate airDate1 = Optional.ofNullable(episode1.getAirDate()).orElse(LocalDate.MIN);
		LocalDate airDate2 = Optional.ofNullable(episode2.getAirDate()).orElse(LocalDate.MIN);
		int result = airDate1.compareTo(airDate2);
		if(result == 0){
			Integer seasonNum1 = Optional.ofNullable(episode1.getSeasonNum()).orElse(0);
			Integer seasonNum2 = Optional.ofNullable(episode2.getSeasonNum()).orElse(0);
			result = seasonNum1.compareTo(seasonNum2);
		}
		
		if(result == 0){
			Integer episodeNum1 = Optional.ofNullable(episode1.getEpisodeNum()).orElse(0);
			Integer episodeNum2 = Optional.ofNullable(episode2.getEpisodeNum()).orElse(0);
			result = episodeNum1.compareTo(episodeNum2);
		}
		
		return result;
	}

}
