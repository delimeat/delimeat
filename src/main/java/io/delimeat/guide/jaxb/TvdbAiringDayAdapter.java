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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import io.delimeat.guide.domain.AiringDay;

public class TvdbAiringDayAdapter extends XmlAdapter<String, List<AiringDay>> {

	private static final String MON = "monday";
	private static final String TUE = "tuesday";
	private static final String WED = "wednesday";
	private static final String THU = "thursday";
	private static final String FRI = "friday";
	private static final String SAT = "saturday";
	private static final String SUN = "sunday";
	private static final String DAY = "daily";

	/* (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	@Override
	public String marshal(List<AiringDay> value) throws Exception {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	@Override
	public List<AiringDay> unmarshal(String value) throws Exception {
		if (value == null || value.trim().length() == 0) {
			return null;
		}

		final List<AiringDay> list = new ArrayList<AiringDay>();
		switch (value.toLowerCase()) {
		case MON:
			list.add(AiringDay.MONDAY);
			break;
		case TUE:
			list.add(AiringDay.TUESDAY);
			break;
		case WED:
			list.add(AiringDay.WEDNESDAY);
			break;
		case THU:
			list.add(AiringDay.THURSDAY);
			break;
		case FRI:
			list.add(AiringDay.FRIDAY);
			break;
		case SAT:
			list.add(AiringDay.SATURDAY);
			break;
		case SUN:
			list.add(AiringDay.SUNDAY);
			break;
		case DAY:
			list.add(AiringDay.MONDAY);
			list.add(AiringDay.TUESDAY);
			list.add(AiringDay.WEDNESDAY);
			list.add(AiringDay.THURSDAY);
			list.add(AiringDay.FRIDAY);
			break;
		}
		return list;
	}

}
