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
package io.delimeat.processor.domain;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.delimeat.feed.domain.FeedSource;
import io.delimeat.torrent.domain.InfoHash;
import io.delimeat.torrent.domain.ScrapeResult;
import io.delimeat.torrent.domain.Torrent;

public class FeedProcessUnit {

	private URI downloadUri;
	private List<FeedProcessUnitRejection> rejections = new ArrayList<FeedProcessUnitRejection>();
	private InfoHash infoHash;
	private String title;
	private ScrapeResult scrape;
	private Torrent torrent;
	private FeedSource source;

	/**
	 * @return the downloadUri
	 */
	public URI getDownloadUri() {
		return downloadUri;
	}

	/**
	 * @return the feedResultRejections
	 */
	public List<FeedProcessUnitRejection> getRejections() {
		return rejections;
	}

	/**
	 * @return the infoHash
	 */
	public InfoHash getInfoHash() {
		return infoHash;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the torrent
	 */
	public Torrent getTorrent() {
		return torrent;
	}

	/**
	 * @param downloadUri the downloadUri to set
	 */
	public void setDownloadUri(URI downloadUri) {
		this.downloadUri = downloadUri;
	}

	/**
	 * @param infoHash the infoHash to set
	 */
	public void setInfoHash(InfoHash infoHash) {
		this.infoHash = infoHash;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param torrent the torrent to set
	 */
	public void setTorrent(Torrent torrent) {
		this.torrent = torrent;
	}

	/**
	 * @return the source
	 */
	public FeedSource getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(FeedSource source) {
		this.source = source;
	}

	/**
	 * @return the scrape
	 */
	public ScrapeResult getScrape() {
		return scrape;
	}

	/**
	 * @param scrape the scrape to set
	 */
	public void setScrape(ScrapeResult scrape) {
		this.scrape = scrape;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(downloadUri,infoHash,title,scrape,torrent);
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
		FeedProcessUnit other = (FeedProcessUnit) obj;
		if (downloadUri == null) {
			if (other.downloadUri != null)
				return false;
		} else if (!downloadUri.equals(other.downloadUri))
			return false;
		if (infoHash == null) {
			if (other.infoHash != null)
				return false;
		} else if (!infoHash.equals(other.infoHash))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (scrape == null) {
			if (other.scrape != null)
				return false;
		} else if (!scrape.equals(other.scrape))
			return false;
		if (torrent == null) {
			if (other.torrent != null)
				return false;
		} else if (!torrent.equals(other.torrent))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FeedProcessUnit [source=" + source + ", title=" + title + ", infoHash=" + infoHash + ", downloadUri="
				+ downloadUri + ", scrape=" + scrape + ", torrent=" + torrent + ", rejections=" + rejections + "]";
	}
	
}
