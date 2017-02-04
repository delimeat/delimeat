package io.delimeat.core.show;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

public class ShowUtils {


	/** Determine an air date time in UTC
	 * 
	 * @param airDate
	 * @param show
	 * @return
	 */
	public static Instant determineAirTime(Date airDate, Show show){
		Objects.requireNonNull(show);
		
		long airDateMillis = airDate != null ? airDate.getTime() : 0;
		long airTimeMillis = show.getAirTime();
		String timeZone = "UTC";
		if(show.getTimezone() != null){
			timeZone = show.getTimezone();
		}
		long airDateTimeMillis = airDateMillis + airTimeMillis;
		int offset = TimeZone.getTimeZone(timeZone).getOffset(airDateTimeMillis);	
		return Instant.ofEpochMilli(airDateTimeMillis-offset);
	}

}
