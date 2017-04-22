package io.delimeat.util.jaxb;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

	/* (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	@Override
	public String marshal(LocalDate date) throws Exception {
		return date == null ? null: DateTimeFormatter.ISO_DATE.format(date);
	}

	/* (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	@Override
	public LocalDate unmarshal(String value) throws Exception {
		if(value == null || value.trim().length() == 0){
			return null;
		}
		return LocalDate.parse(value, DateTimeFormatter.ISO_DATE);
	}

}
