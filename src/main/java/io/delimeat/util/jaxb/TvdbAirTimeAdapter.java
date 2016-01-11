package io.delimeat.util.jaxb;

import io.delimeat.util.DelimeatUtils;

import java.text.ParseException;
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
		int result = 0;
		String airtime = (String) value;
		if (airtime.length() > 0) {
			// convert to lowercase and remove any whitespace
			String tmpAirtime = airtime.toLowerCase().replaceAll("\\s", "");

			SimpleDateFormat sdf = null;
			if (tmpAirtime.matches("(([0-1][0-2]|[1-9])(am|pm))")) {
				sdf = new SimpleDateFormat("hha");
			} else if (tmpAirtime.matches("(([1][0-2]|[0]?[1-9]):([0-5][0-9])(am|pm))")) {
				sdf = new SimpleDateFormat("hh:mma");
			} else if (tmpAirtime.matches("^([0-2]?[0-9]:[0-5][0-9])$")) {
				sdf = new SimpleDateFormat("HH:mm");
			}
			if (sdf != null) {
				try {
					// set the timezone to GMT not the default timezone so its
					// not offset.
					sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
					long airtimeMS = sdf.parse(tmpAirtime).getTime();
					if (airtimeMS < Integer.MAX_VALUE) {
						result = (int) airtimeMS;
					}
				} catch (ParseException e) {
					log.error("encountered an error trying to parse airing time: " + airtime + " using: "
							+ sdf.toPattern(), e);
				}
			} else {
				log.error("unhandled time format for airtime: " + airtime);
			}
		}
		return result;
	}

}
