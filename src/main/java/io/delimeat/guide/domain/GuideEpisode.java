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

import java.time.LocalDate;
import java.util.Objects;

import com.google.common.collect.ComparisonChain;

import io.delimeat.show.domain.Episode;
import lombok.Data;

@Data
//TODO move episode equality somewhere else (comparator?)
public class GuideEpisode {

	private LocalDate airDate;
	private Integer seasonNum = 0;
	private Integer episodeNum = 0;
	private Integer productionNum = 0;
	private String title;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (object == null) {
			return false;
		}

		if (this == object) {
			return true;
		}

		if (object instanceof Episode) {
			Episode otherEp = (Episode) object;
			return ComparisonChain.start()
									.compare(this.seasonNum, new Integer(otherEp.getSeasonNum()))
									.compare(this.episodeNum, new Integer(otherEp.getEpisodeNum()))
									.result() == 0 ? true : false;
		} else if (object instanceof GuideEpisode) {
			GuideEpisode otherEp = (GuideEpisode) object;
			return ComparisonChain.start()
									.compare(this.seasonNum, otherEp.getSeasonNum())
									.compare(this.episodeNum, otherEp.getEpisodeNum())
									.result() == 0 ? true : false;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(title, airDate, seasonNum, episodeNum);
	}

}
