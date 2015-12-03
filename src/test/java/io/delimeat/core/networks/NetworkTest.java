package io.delimeat.core.networks;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NetworkTest {

	private Network network;

	@Before
	public void setUp() {
		network = new Network();
	}

	@Test
	public void nameTest() {
		Assert.assertNull(network.getName());
		network.setName("NETWORK_NAME");
		Assert.assertEquals("NETWORK_NAME", network.getName());
	}

	@Test
	public void timezoneTest() {
		Assert.assertNull(network.getTimezone());
		network.setTimezone("TIMEZONE_VALUE");
		Assert.assertEquals("TIMEZONE_VALUE", network.getTimezone());
	}
}
