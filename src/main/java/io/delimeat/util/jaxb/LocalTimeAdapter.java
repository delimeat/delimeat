/*
 * Copyright 2013-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.delimeat.util.jaxb;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalTimeAdapter extends XmlAdapter<String, LocalTime> {

	private static final Logger LOG = LoggerFactory.getLogger(LocalTimeAdapter.class);
	
	/* (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	@Override
	public String marshal(LocalTime time) throws Exception {
		return time == null ? null: DateTimeFormatter.ofPattern("hh:mm a").format(time);
	}

	/* (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	@Override
	public LocalTime unmarshal(String value) throws Exception {
		if(value == null || value.trim().length() == 0 ){
			return null;
		}
		
		final String upperValue = value.toUpperCase().replaceAll(" ", "");
		final String format;
		if (upperValue.matches("(([0-1][0-2]|[1-9])\\s?(AM|PM))")) {
			format = "ha";
		} else if (upperValue.matches("[0-2]?[0-9]:[0-5][0-9]")) {
			format = "HH:m";
		} else {
			format = "h:ma";
		}
		try{
			return LocalTime.parse(upperValue,  DateTimeFormatter.ofPattern(format));
		}catch(DateTimeParseException ex){
			LOG.warn("Encountered time format exception", ex);
			return LocalTime.MIDNIGHT;
		}
	}

}
