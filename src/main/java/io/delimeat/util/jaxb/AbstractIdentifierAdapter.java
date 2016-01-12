package io.delimeat.util.jaxb;

import io.delimeat.core.guide.GuideIdentifier;
import io.delimeat.core.guide.GuideSource;
import io.delimeat.util.DelimeatUtils;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public abstract class AbstractIdentifierAdapter extends XmlAdapter<String, GuideIdentifier> {
  
  
   public abstract GuideSource getSource();
  
  	@Override
	public String marshal(GuideIdentifier v) throws Exception {
		return null;
	}
  
	@Override
	public GuideIdentifier unmarshal(String value) throws Exception {
      if (DelimeatUtils.isEmpty(value)) {
        	return null;
		}
		GuideIdentifier id = new GuideIdentifier();
		id.setSource(getSource());
		id.setValue(value);
		return id;
	}
  
}
