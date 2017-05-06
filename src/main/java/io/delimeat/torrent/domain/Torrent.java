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

import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.List;

public class Torrent {

	private String tracker;
	private List<String> trackers = new ArrayList<String>();
	private TorrentInfo info;
	private byte[] bytes;

	public String getTracker() {
		return tracker;
	}

	public void setTracker(String tracker) {
		this.tracker = tracker;
	}

	public List<String> getTrackers() {
		return trackers;
	}

	public void setTrackers(List<String> trackers) {
		this.trackers = trackers;
	}

	public TorrentInfo getInfo() {
		return info;
	}

	public void setInfo(TorrentInfo info) {
		this.info = info;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	@Override
	public String toString() {
     	return MoreObjects.toStringHelper(this)
              .add("tracker", tracker)
              .add("trackers", trackers)
              .add("info", info)
              .omitNullValues()
              .toString();
	}

}
