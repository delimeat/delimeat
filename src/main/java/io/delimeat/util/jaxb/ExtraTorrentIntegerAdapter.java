package io.delimeat.util.jaxb;

import io.delimeat.util.DelimeatUtils;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ExtraTorrentIntegerAdapter extends XmlAdapter<String, Long>  {

	@Override
	public String marshal(Long value) throws Exception {
		return null;
	}

	@Override
	public Long unmarshal(String value) throws Exception {
		if (DelimeatUtils.isEmpty(value)) {
			return null;
		}
		
		try{
			return Long.parseLong(value);
		}catch(NumberFormatException ex){
			return 0L;
		}
	}


}
