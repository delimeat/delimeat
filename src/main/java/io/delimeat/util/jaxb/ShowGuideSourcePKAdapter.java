package io.delimeat.util.jaxb;

import io.delimeat.core.guide.GuideSource;
import io.delimeat.core.show.ShowGuideSourcePK;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ShowGuideSourcePKAdapter extends XmlAdapter<GuideSource, ShowGuideSourcePK> {

	@Override
	public GuideSource marshal(ShowGuideSourcePK value) throws Exception {
		if (value == null) {
			return null;
		}
		return value.getGuideSource();
	}

	@Override
	public ShowGuideSourcePK unmarshal(GuideSource value) throws Exception {
		if (value == null) {
			return null;
		}
		ShowGuideSourcePK pk = new ShowGuideSourcePK();
		pk.setGuideSource(value);
		return pk;
	}

}
