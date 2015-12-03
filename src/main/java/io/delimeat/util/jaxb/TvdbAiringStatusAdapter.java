package io.delimeat.util.jaxb;

import io.delimeat.core.guide.AiringStatus;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class TvdbAiringStatusAdapter extends XmlAdapter<String, AiringStatus> {

	private static final String ENDED = "ended";
	private static final String CONTINUING = "continuing";

	@Override
	public String marshal(AiringStatus value) throws Exception {
		return null;
	}

	@Override
	public AiringStatus unmarshal(String value) throws Exception {
		AiringStatus result = AiringStatus.UNKNOWN;
		if (ENDED.equalsIgnoreCase(value)) {
			result = AiringStatus.ENDED;
		} else if (CONTINUING.equalsIgnoreCase(value)) {
			result = AiringStatus.AIRING;
		}
		return result;
	}

}
