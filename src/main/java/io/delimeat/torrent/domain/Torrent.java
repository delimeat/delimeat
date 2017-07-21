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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Torrent {

	private String tracker;
	private List<String> trackers = new ArrayList<String>();
	private TorrentInfo info;
	private byte[] bytes;
	/**
	 * @return the tracker
	 */
	public String getTracker() {
		return tracker;
	}
	/**
	 * @param tracker the tracker to set
	 */
	public void setTracker(String tracker) {
		this.tracker = tracker;
	}
	/**
	 * @return the trackers
	 */
	public List<String> getTrackers() {
		return trackers;
	}
	/**
	 * @param trackers the trackers to set
	 */
	public void setTrackers(List<String> trackers) {
		this.trackers = trackers;
	}
	/**
	 * @return the info
	 */
	public TorrentInfo getInfo() {
		return info;
	}
	/**
	 * @param info the info to set
	 */
	public void setInfo(TorrentInfo info) {
		this.info = info;
	}
	/**
	 * @return the bytes
	 */
	public byte[] getBytes() {
		return bytes;
	}
	/**
	 * @param bytes the bytes to set
	 */
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(info,tracker,trackers);
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
		Torrent other = (Torrent) obj;
		if (info == null) {
			if (other.info != null)
				return false;
		} else if (!info.equals(other.info))
			return false;
		if (tracker == null) {
			if (other.tracker != null)
				return false;
		} else if (!tracker.equals(other.tracker))
			return false;
		if (trackers == null) {
			if (other.trackers != null)
				return false;
		} else if (!trackers.equals(other.trackers))
			return false;
		return true;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Torrent [" + (tracker != null ? "tracker=" + tracker + ", " : "")
				+ (trackers != null ? "trackers=" + trackers + ", " : "") + (info != null ? "info=" + info : "") + "]";
	}

}
