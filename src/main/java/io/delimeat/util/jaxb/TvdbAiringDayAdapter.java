package io.delimeat.util.jaxb;

import java.util.ArrayList;
import java.util.List;

import io.delimeat.core.guide.AiringDay;
import io.delimeat.util.DelimeatUtils;

import javax.xml.bind.annotation.adapters.XmlAdapter;

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
		if (DelimeatUtils.isEmpty(value)) {
			return null;
		}
		List<AiringDay> list = new ArrayList<AiringDay>();

		if (MON.equalsIgnoreCase(value)) {
			list.add(AiringDay.MONDAY);
		} else if (TUE.equalsIgnoreCase(value)) {
			list.add(AiringDay.TUESDAY);
		} else if (WED.equalsIgnoreCase(value)) {
			list.add(AiringDay.WEDNESDAY);
		} else if (THU.equalsIgnoreCase(value)) {
			list.add(AiringDay.THURSDAY);
		} else if (FRI.equalsIgnoreCase(value)) {
			list.add(AiringDay.FRIDAY);
		} else if (SAT.equalsIgnoreCase(value)) {
			list.add(AiringDay.SATURDAY);
		} else if (SUN.equalsIgnoreCase(value)) {
			list.add(AiringDay.SUNDAY);
		} else if (DAY.equalsIgnoreCase(value)) {
			list.add(AiringDay.MONDAY);
			list.add(AiringDay.TUESDAY);
			list.add(AiringDay.WEDNESDAY);
			list.add(AiringDay.THURSDAY);
			list.add(AiringDay.FRIDAY);
		} else {
			list.add(AiringDay.UNKNOWN);
		}
		return list;
	}

}
