package io.delimeat.util.jaxb;

import io.delimeat.util.DelimeatUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class AirTimeAdapter extends XmlAdapter<String, Integer> {

	private static final String FORMAT = "hh:mm a";

	@Override
	public String marshal(Integer value) throws Exception {
		if(value==null){
			return null;
		}
		final SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		return sdf.format(new Date(value));
	}

	@Override
	public Integer unmarshal(String value) throws Exception {
		if(DelimeatUtils.isEmpty(value)){
			return null;
		}
		int result = 0;
		final SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		Long formatedVal = sdf.parse(value).getTime();
		if(formatedVal<= Integer.MAX_VALUE){
			result = formatedVal.intValue();
		}
		return result;
	}

}
