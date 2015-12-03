package io.delimeat.core.networks;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "networks")
@XmlAccessorType(XmlAccessType.FIELD)
public class Networks {

	@XmlElement(name = "network")
	@XmlList
	private List<Network> networks = new ArrayList<Network>();

	public List<Network> getNetworks() {
		return networks;
	}

	public void setNetworks(List<Network> networks) {
		this.networks = networks;
	}

}
