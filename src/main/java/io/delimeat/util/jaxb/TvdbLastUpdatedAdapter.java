package io.delimeat.util.jaxb;

import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class TvdbLastUpdatedAdapter extends XmlAdapter<Long, Date>{

	@Override
	public Long marshal(Date arg0) throws Exception {
		return null;
	}

	@Override
	public Date unmarshal(Long lastUpdated) throws Exception {
		if(lastUpdated == null){
			return null;
		}
		return new Date(lastUpdated*1000);
	}

}
