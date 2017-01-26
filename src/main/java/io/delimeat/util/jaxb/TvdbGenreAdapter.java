package io.delimeat.util.jaxb;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class TvdbGenreAdapter extends XmlAdapter<String, List<String>> {

	private static final String DELIMITER = "|";

	@Override
	public String marshal(List<String> value) throws Exception {
		return null;
	}

	@Override
	public List<String> unmarshal(String value) throws Exception {
		if (value == null || value.trim().length() == 0) {
			return null;
		}
		final String[] valueArray = value.toUpperCase().split("\\Q" + DELIMITER + "\\E");
		return Arrays.asList(valueArray);
	}

}
