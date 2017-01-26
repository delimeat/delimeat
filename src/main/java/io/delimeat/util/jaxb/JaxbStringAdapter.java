package io.delimeat.util.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class JaxbStringAdapter extends XmlAdapter<String, String> {

	private static final Whitelist WHITELIST = Whitelist.none();

	@Override
	public String marshal(String v) throws Exception {
		return null;
	}

	@Override
	public String unmarshal(String value) throws Exception {
		if (value == null || value.trim().length() == 0) {
			return null;
		}
		return Jsoup.clean(value, WHITELIST);
	}

}
