package io.delimeat.core.networks;

import io.delimeat.util.jaxb.AbstractJaxbHelper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class JaxbNetworksDao_Impl extends AbstractJaxbHelper implements NetworksDao {

	private URI uri;
	private Networks networks = null;

	@Override
	public Network read(String id) throws IOException, Exception {
		if (networks == null) {
			InputStream input = getUrlHandler().openInput(getUri().toURL());
			networks = unmarshal(input, Networks.class);
		}
		for (Network network : networks.getNetworks()) {
			if (network.getName().equalsIgnoreCase(id)) {
				return network;
			}
		}
		return null;
	}

	/**
	 * @return the uri
	 */
	public URI getUri() {
		return uri;
	}

	/**
	 * @param uri
	 *            the uri to set
	 */
	public void setUri(URI uri) {
		this.uri = uri;
	}

}
