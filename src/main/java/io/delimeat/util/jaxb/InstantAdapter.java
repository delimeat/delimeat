package io.delimeat.util.jaxb;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class InstantAdapter extends XmlAdapter<String, Instant>  {

	/* (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	@Override
	public String marshal(Instant instant) throws Exception {
		return instant == null ? null : DateTimeFormatter.ISO_INSTANT.format(instant);
	}

	/* (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	@Override
	public Instant unmarshal(String value) throws Exception {
		if(value == null || value.trim().length() == 0){
			return null;
		}
		return  Instant.from(DateTimeFormatter.ISO_INSTANT.parse(value));
	}

}
