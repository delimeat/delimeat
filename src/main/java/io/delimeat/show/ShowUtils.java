package io.delimeat.show;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Optional;

import io.delimeat.guide.domain.GuideEpisode;
import io.delimeat.show.domain.Episode;

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
