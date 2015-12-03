package io.delimeat.util.jaxb;

import io.delimeat.core.guide.GuideIdentifier;
import io.delimeat.core.guide.GuideSource;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class TvRageIdentifierAdapter extends XmlAdapter<String, GuideIdentifier> {

	private static final GuideSource SOURCE = GuideSource.TVRAGE;

	@Override
	public String marshal(GuideIdentifier v) throws Exception {
		return null;
	}

	@Override
	public GuideIdentifier unmarshal(String value) throws Exception {
		if (value == null || "".equals(value)) {
			return null;
		}
		GuideIdentifier id = new GuideIdentifier();
		id.setSource(SOURCE);
		id.setValue(value);
		return id;
	}

}
