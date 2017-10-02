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
package io.delimeat.feed.domain;

import java.util.Objects;

public class FeedResult {

	private String torrentURL;
	private String title;
	private long contentLength;
	private long seeders;
	private long leechers;
	private String infoHashHex;
	private FeedSource source;

	/**
	 * @return the torrentURL
	 */
	public String getTorrentURL() {
		return torrentURL;
	}
	/**
	 * @param torrentURL the torrentURL to set
	 */
	public void setTorrentURL(String torrentURL) {
		this.torrentURL = torrentURL;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the contentLength
	 */
	public long getContentLength() {
		return contentLength;
	}
	/**
	 * @param contentLength the contentLength to set
	 */
	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}
	/**
	 * @return the seeders
	 */
	public long getSeeders() {
		return seeders;
	}
	/**
	 * @param seeders the seeders to set
	 */
	public void setSeeders(long seeders) {
		this.seeders = seeders;
	}
	/**
	 * @return the leechers
	 */
	public long getLeechers() {
		return leechers;
	}
	/**
	 * @param leechers the leechers to set
	 */
	public void setLeechers(long leechers) {
		this.leechers = leechers;
	}
	/**
	 * @return the infoHashHex
	 */
	public String getInfoHashHex() {
		return infoHashHex;
	}
	/**
	 * @param infoHashHex the infoHashHex to set
	 */
	public void setInfoHashHex(String infoHashHex) {
		this.infoHashHex = infoHashHex;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(contentLength,leechers,seeders,title,torrentURL,infoHashHex);
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
		FeedResult other = (FeedResult) obj;
		if (contentLength != other.contentLength)
			return false;
		if (infoHashHex == null) {
			if (other.infoHashHex != null)
				return false;
		} else if (!infoHashHex.equals(other.infoHashHex))
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
		if (torrentURL == null) {
			if (other.torrentURL != null)
				return false;
		} else if (!torrentURL.equals(other.torrentURL))
			return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FeedResult [" + (source != null ? "source=" + source + ", " : "")
				+ (title != null ? "title=" + title + ", " : "")
				+ (infoHashHex != null ? "infoHashHex=" + infoHashHex + ", " : "")
				+ (torrentURL != null ? "torrentURL=" + torrentURL + ", " : "") + "contentLength=" + contentLength
				+ ", seeders=" + seeders + ", leechers=" + leechers + "]";
	}


	

	
	

}
