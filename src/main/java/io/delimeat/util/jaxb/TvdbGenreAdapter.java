package io.delimeat.util.jaxb;

import java.util.ArrayList;
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
		if (value == null || "".equals(value)) {
			return null;
		}
		List<String> values = new ArrayList<String>();
		String[] valueArray = value.split("\\Q" + DELIMITER + "\\E");
		for (String val : valueArray) {
			if (val != null && val.length() > 0) {
				values.add(val.toUpperCase());
			}
		}
		return values;
	}

}
