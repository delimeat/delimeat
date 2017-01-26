package io.delimeat.util.jaxb;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import io.delimeat.core.guide.AiringDay;

public class TvdbAiringDayAdapter extends XmlAdapter<String, List<AiringDay>> {

	private static final String MON = "monday";
	private static final String TUE = "tuesday";
	private static final String WED = "wednesday";
	private static final String THU = "thursday";
	private static final String FRI = "friday";
	private static final String SAT = "saturday";
	private static final String SUN = "sunday";
	private static final String DAY = "daily";

	@Override
	public String marshal(List<AiringDay> value) throws Exception {
		return null;
	}

	@Override
	public List<AiringDay> unmarshal(String value) throws Exception {
		if (value == null || value.trim().length() == 0) {
			return null;
		}
     
		final List<AiringDay> list = new ArrayList<AiringDay>();
		switch(value.toLowerCase()){
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
