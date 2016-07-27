package io.delimeat.util.jaxb;

import io.delimeat.common.util.DelimeatUtils;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class TorrentDownloadsUrlAdapter extends XmlAdapter<String, String> {

	@Override
	public String marshal(String value) throws Exception {
		return null;
	}

	@Override
	public String unmarshal(String value) throws Exception {
		if (DelimeatUtils.isEmpty(value)) {
			return null;
		}
		return "http://itorrents.org/torrent/"+value+".torrent";
	}

}
