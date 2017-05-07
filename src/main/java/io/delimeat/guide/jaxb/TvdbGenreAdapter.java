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
package io.delimeat.guide.jaxb;

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