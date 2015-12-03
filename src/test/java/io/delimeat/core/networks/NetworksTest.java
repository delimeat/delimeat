package io.delimeat.core.networks;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class NetworksTest {

	private Networks networks;

	@Before
	public void setUp() {
		networks = new Networks();
	}

	@Test
	public void networksTest() {
		Assert.assertEquals(0, networks.getNetworks().size());
		List<Network> values = new ArrayList<Network>();
		Network mockedNetwork = Mockito.mock(Network.class);
		values.add(mockedNetwork);
		networks.setNetworks(values);
		Assert.assertEquals(1, networks.getNetworks().size());
		Assert.assertEquals(mockedNetwork, networks.getNetworks().get(0));
	}

}
