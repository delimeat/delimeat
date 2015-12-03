package io.delimeat.core.networks;

import java.io.IOException;

public interface NetworksDao {

	public Network read(String id) throws IOException, Exception;
}
