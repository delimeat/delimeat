package io.delimeat.feed.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class TorrentProjectTorrentUrlAdapter extends XmlAdapter<String, String> {

	/* (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	@Override
	public String marshal(String value) throws Exception {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	@Override
	public String unmarshal(String value) throws Exception {
		if (value == null || value.trim().length() == 0) {
			return null;
		}
		
		if (value.toLowerCase().startsWith("http://")) {
			value = value.replaceFirst("http://", "https://");
		}
		return value;
	}
}
