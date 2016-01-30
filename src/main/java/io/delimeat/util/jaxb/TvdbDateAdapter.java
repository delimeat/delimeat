package io.delimeat.util.jaxb;

import io.delimeat.util.DelimeatUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class TvdbDateAdapter extends XmlAdapter<String, Date> {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
    static {
    	SDF.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

	@Override
	public String marshal(Date date) throws Exception {
		return date == null ? null: SDF.format(date);

	}

	@Override
	public Date unmarshal(String value) throws Exception {
		return DelimeatUtils.isEmpty(value) ? null : SDF.parse(value);
	}

}