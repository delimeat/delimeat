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

import io.delimeat.torrent.domain.InfoHash;
import io.delimeat.torrent.domain.Torrent;

public class FeedProcessUnit {

	private long contentLength;
	private URI downloadUri;
	private List<FeedProcessUnitRejection> rejections = new ArrayList<FeedProcessUnitRejection>();
	private InfoHash infoHash;
	private long leechers;
	private long seeders;
	private String title;
	private Torrent torrent;

	/**
	 * @return the contentLength
	 */
	public long getContentLength() {
		return contentLength;
	}

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
	 * @return the leechers
	 */
	public long getLeechers() {
		return leechers;
	}

	/**
	 * @return the seeders
	 */
	public long getSeeders() {
		return seeders;
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
	 * @param contentLength the contentLength to set
	 */
	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
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
	 * @param leechers the leechers to set
	 */
	public void setLeechers(long leechers) {
		this.leechers = leechers;
	}

	/**
	 * @param seeders the seeders to set
	 */
	public void setSeeders(long seeders) {
		this.seeders = seeders;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(contentLength,downloadUri,infoHash,leechers,seeders,title,torrent);
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
		if (contentLength != other.contentLength)
			return false;
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
		if (leechers != other.leechers)
			return false;
		if (seeders != other.seeders)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (torrent == null) {
			if (other.torrent != null)
				return false;
		} else if (!torrent.equals(other.torrent))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FeedProcessUnit [contentLength=" + contentLength + ", "
				+ (downloadUri != null ? "downloadUri=" + downloadUri + ", " : "")
				+ (rejections != null ? "rejections=" + rejections + ", " : "")
				+ (infoHash != null ? "infoHash=" + infoHash + ", " : "") + "leechers=" + leechers + ", "
				+ "seeders=" + seeders + ", "
				+ (title != null ? "title=" + title + ", " : "") + (torrent != null ? "torrent=" + torrent : "") + "]";
	}
	
	

	
}
