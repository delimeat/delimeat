package io.delimeat.util.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import io.delimeat.core.guide.AiringDay;

public class TvMazeAiringDayAdapter extends XmlAdapter<String, AiringDay> {

	private static final String MON = "monday";
	private static final String TUE = "tuesday";
	private static final String WED = "wednesday";
	private static final String THU = "thursday";
	private static final String FRI = "friday";
	private static final String SAT = "saturday";
	private static final String SUN = "sunday";

	@Override
	public String marshal(AiringDay value) throws Exception {
		return null;
	}

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
