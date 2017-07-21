/*
 * Copyright 2013-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.delimeat.torrent.domain;

import java.util.Objects;

public class ScrapeResult {

	private final long seeders;
	private final long leechers;
	
	/**
	 * @param seeders
	 * @param leechers
	 */
	public ScrapeResult(long seeders, long leechers) {
		super();
		this.seeders = seeders;
		this.leechers = leechers;
	}
	/**
	 * @return the seeders
	 */
	public long getSeeders() {
		return seeders;
	}
	/**
	 * @return the leechers
	 */
	public long getLeechers() {
		return leechers;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(leechers,seeders);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScrapeResult other = (ScrapeResult) obj;
		if (leechers != other.leechers)
			return false;
		if (seeders != other.seeders)
			return false;
		return true;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ScrapeResult [seeders=" + seeders + ", leechers=" + leechers + "]";
	}
}
