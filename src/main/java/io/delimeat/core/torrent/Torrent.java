package io.delimeat.core.torrent;

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
		return "Torrent [tracker=" + tracker + ", trackers=" + trackers
				+ ", info=" + (info != null ? info : null) + "]";
	}

}
