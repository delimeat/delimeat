package io.delimeat.core.networks;

import javax.xml.bind.annotation.XmlElement;

public class Network {

	@XmlElement
	private String name;
	@XmlElement
	private String timezone;

	public String getName() {
		return name;
	}

	public String getTimezone() {
		return timezone;
	}

	/**
	 * Set the name of the network
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Set the timezone of the network
	 * 
	 * @param timezone
	 */
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String toString() {
		return "name: " + name + " timezone: " + timezone;
	}
}
