package io.delimeat.torrent.domain;

import com.google.common.base.MoreObjects;

public class ScrapeResult {

	private final long seeders;
	private final long leechers;

	public ScrapeResult(long seeders, long leechers) {
		this.seeders = seeders;
		this.leechers = leechers;
	}

	public long getSeeders() {
		return seeders;
	}

	public long getLeechers() {
		return leechers;
	}

	@Override
	public String toString() {
     	return MoreObjects.toStringHelper(this)
              .add("seeders", seeders)
              .add("leechers", leechers)
              .omitNullValues()
              .toString();
	}

}
