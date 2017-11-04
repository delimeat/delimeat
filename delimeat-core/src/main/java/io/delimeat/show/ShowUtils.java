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
package io.delimeat.show;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Optional;

import io.delimeat.guide.entity.GuideEpisode;
import io.delimeat.show.entity.Episode;

public class ShowUtils {
	
	/** 
	 * Determine an air date time in UTC
	 * 
	 * @param airDate
	 * @param show
	 * @return instant
	 */
	public static Instant determineAirTime(LocalDate airDate, LocalTime localTime, String timeZone){
		LocalDateTime localDateTime = LocalDateTime.of(airDate, Optional.ofNullable(localTime).orElse(LocalTime.MIDNIGHT));

		timeZone = Optional.ofNullable(timeZone).orElse("UTC");
		ZoneOffset zoneOffset = ZoneId.of(timeZone)
											.getRules()
											.getOffset(localDateTime);
		
		return localDateTime.toInstant(zoneOffset);
	}
	
	/**
	 * Create an Episode from a Guide Episode
	 * 
	 * @param guideEp
	 * @return episode
	 */
	public static Episode fromGuideEpisode(GuideEpisode guideEp){
		Episode ep = new Episode();
		ep.setTitle(guideEp.getTitle());
		ep.setAirDate(guideEp.getAirDate());
		ep.setSeasonNum(Optional.ofNullable(guideEp.getSeasonNum()).orElse(0));
		ep.setEpisodeNum(Optional.ofNullable(guideEp.getEpisodeNum()).orElse(0));
		return ep;
	}

}
