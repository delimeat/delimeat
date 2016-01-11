package io.delimeat.util.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class TvdbAiringStatusAdapter extends XmlAdapter<String, Boolean> {

	private static final String CONTINUING = "continuing";

	@Override
	public String marshal(Boolean value) throws Exception {
		return null;
	}

	@Override
	public Boolean unmarshal(String value) throws Exception {
		return CONTINUING.equalsIgnoreCase(value);
	}

}
