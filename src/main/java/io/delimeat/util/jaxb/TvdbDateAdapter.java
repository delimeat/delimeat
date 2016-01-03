package io.delimeat.util.jaxb;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class TvdbDateAdapter extends XmlAdapter<String, Date> {

	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public String marshal(Date date) throws Exception {
		if(date==null){
			return null;
		}
		return FORMAT.format(date);
	}

	@Override
	public Date unmarshal(String string) throws Exception {
		if (string == null || "".equals(string)) {
			return null;
		} else {
			return FORMAT.parse(string);
		}
	}

}