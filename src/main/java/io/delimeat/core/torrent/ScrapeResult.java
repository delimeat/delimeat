package io.delimeat.core.torrent;

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
		return "ScrapeResult [seeders=" + seeders + ", leechers=" + leechers
				+ "]";
	}

}
