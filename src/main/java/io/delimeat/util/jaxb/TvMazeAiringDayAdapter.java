package io.delimeat.util.jaxb;

import io.delimeat.core.guide.AiringDay;
import io.delimeat.util.DelimeatUtils;

import javax.xml.bind.annotation.adapters.XmlAdapter;

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
		if (DelimeatUtils.isEmpty(value)) {
			return null;
		}

		if (MON.equalsIgnoreCase(value)) {
			return AiringDay.MONDAY;
		} else if (TUE.equalsIgnoreCase(value)) {
			return AiringDay.TUESDAY;
		} else if (WED.equalsIgnoreCase(value)) {
			return AiringDay.WEDNESDAY;
		} else if (THU.equalsIgnoreCase(value)) {
			return AiringDay.THURSDAY;
		} else if (FRI.equalsIgnoreCase(value)) {
			return AiringDay.FRIDAY;
		} else if (SAT.equalsIgnoreCase(value)) {
			return AiringDay.SATURDAY;
		} else if (SUN.equalsIgnoreCase(value)) {
			return AiringDay.SUNDAY;
		}
		return AiringDay.UNKNOWN;

	}

}
