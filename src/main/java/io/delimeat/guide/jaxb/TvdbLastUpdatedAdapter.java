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

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class TvdbLastUpdatedAdapter extends XmlAdapter<Long, LocalDate> {

	/* (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	@Override
	public Long marshal(LocalDate arg0) throws Exception {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	@Override
	public LocalDate unmarshal(Long lastUpdated) throws Exception {
		if (lastUpdated == null) {
			return null;
		}
		return Instant.ofEpochMilli(lastUpdated * 1000).atZone(ZoneId.systemDefault()).toLocalDate();
	}

}
