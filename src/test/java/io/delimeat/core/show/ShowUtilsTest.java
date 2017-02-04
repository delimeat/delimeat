package io.delimeat.core.show;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;

public class ShowUtilsTest {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
    static {
        SDF.setTimeZone(TimeZone.getTimeZone("UTC"));
    }
    
    @Test
    public void determineAirTimeTest() throws Exception{
    	//2016-12-27 22:00 EST = 1016-12-28 03:00 UTC
    	Show show = new Show();
    	show.setAirTime(22*60*60*1000);
    	show.setTimezone("EST");
    	
    	Instant result = ShowUtils.determineAirTime(SDF.parse("2016-12-27"), show);
    	Assert.assertEquals(Instant.parse("2016-12-28T03:00:00Z"), result);
    }
}
