package io.delimeat.util.jaxb;

import io.delimeat.util.DelimeatUtils;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TvdbAirTimeAdapter extends XmlAdapter<String, Integer> {

	private static Log log = LogFactory.getLog(TvdbAirTimeAdapter.class);

	@Override
	public String marshal(Integer value) throws Exception {
		return null;
	}

	@Override
	public Integer unmarshal(String value) throws Exception {
		if (DelimeatUtils.isEmpty(value)) {
			return null;
		}
     
		Integer result = 0;
     // convert to lowercase and remove any whitespace
     String tmpAirtime = value.toLowerCase().replaceAll("\\s", "");

     String format = null;

     if (tmpAirtime.matches("(([0-1][0-2]|[1-9])(am|pm))")) {
       format = "hha";
     } else if (tmpAirtime.matches("(([1][0-2]|[0]?[1-9]):([0-5][0-9])(am|pm))")) {
       format = "hh:mma";
     } else if (tmpAirtime.matches("^([0-2]?[0-9]:[0-5][0-9])$")) {
       format = "HH:mm";
     }
     if(!DelimeatUtils.isEmpty(format)) {
       final SimpleDateFormat sdf = new SimpleDateFormat(format);
         // set the timezone to GMT not the default timezone so its
         // not offset.
         sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
         Long airtimeMS = sdf.parse(tmpAirtime).getTime();
         if (airtimeMS < Integer.MAX_VALUE) {
           result = airtimeMS.intValue();
         }
     } else {
       log.error("unhandled time format for airtime: " + value);
     }
		return result;
	}

}
