package io.delimeat.guide.jaxb;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class TvdbLastUpdatedAdapter extends XmlAdapter<Long, LocalDate>{

	@Override
	public Long marshal(LocalDate arg0) throws Exception {
		return null;
	}

	@Override
	public LocalDate unmarshal(Long lastUpdated) throws Exception {
		if(lastUpdated == null){
			return null;
		}
		return Instant.ofEpochMilli(lastUpdated*1000).atZone(ZoneId.systemDefault()).toLocalDate();
	}

}
