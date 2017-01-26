package io.delimeat.util.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class TorrentProjectTorrentUrlAdapter  extends XmlAdapter<String, String> {

    @Override
    public String marshal(String value) throws Exception {
        return null;
    }

    @Override
    public String unmarshal(String value) throws Exception {
		if (value == null || value.trim().length() == 0) {
			return null;
		}
      if(value.toLowerCase().startsWith("http://")){
        value = value.replaceFirst("http://", "https://");
      }
      return value;
    }
}
