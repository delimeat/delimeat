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
package io.delimeat.guide.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import io.delimeat.guide.entity.AiringDay;

public class TvMazeAiringDayAdapter extends XmlAdapter<String, AiringDay> {

	private static final String MON = "monday";
	private static final String TUE = "tuesday";
	private static final String WED = "wednesday";
	private static final String THU = "thursday";
	private static final String FRI = "friday";
	private static final String SAT = "saturday";
	private static final String SUN = "sunday";

	/* (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	@Override
	public String marshal(AiringDay value) throws Exception {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	@Override
	public AiringDay unmarshal(String value) throws Exception {
		if (value == null || value.trim().length() == 0) {
			return null;
		}

      switch(value.toLowerCase()){
        case MON:
        		return AiringDay.MONDAY;
        case TUE:
        		return AiringDay.TUESDAY;
        case WED:
        		return AiringDay.WEDNESDAY;
        case THU:
        		return AiringDay.THURSDAY;
        case FRI:
        		return AiringDay.FRIDAY;
        case SAT:
        		return AiringDay.SATURDAY;
        case SUN:
        		return AiringDay.SUNDAY;
		  default:
        		return null;
      }
	}

}
