package io.delimeat.util.jaxb;

import io.delimeat.util.DelimeatUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class TvdbDateAdapter extends XmlAdapter<String, Date> {

   private static final String FORMAT = "yyyy-MM-dd";

	@Override
	public String marshal(Date date) throws Exception {
		return date == null ? null: new SimpleDateFormat(FORMAT).format(date);

	}

	@Override
	public Date unmarshal(String value) throws Exception {
		return DelimeatUtils.isEmpty(value) ? null : new SimpleDateFormat(FORMAT).parse(value);
	}

}