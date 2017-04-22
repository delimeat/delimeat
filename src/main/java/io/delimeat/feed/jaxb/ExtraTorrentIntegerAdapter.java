package io.delimeat.feed.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ExtraTorrentIntegerAdapter extends XmlAdapter<String, Long>  {

	@Override
	public String marshal(Long value) throws Exception {
		return null;
	}

	@Override
	public Long unmarshal(String value) throws Exception {
		if (value == null || value.trim().length() == 0) {
			return null;
		}
		
		try{
			return Long.parseLong(value);
		}catch(NumberFormatException ex){
			return 0L;
		}
	}


}
