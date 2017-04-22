package io.delimeat.feed.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class TorrentDownloadsUrlAdapter extends XmlAdapter<String, String> {

	@Override
	public String marshal(String value) throws Exception {
		return null;
	}

	@Override
	public String unmarshal(String value) throws Exception {
		if (value == null || value.trim().length() == 0) {
			return null;
		}
		return "http://itorrents.org/torrent/"+value+".torrent";
	}

}
