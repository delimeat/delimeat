package io.delimeat.core.feed;

import io.delimeat.core.torrent.Torrent;

import java.util.ArrayList;
import java.util.List;

public class FeedResult {

	protected String torrentURL;
	protected String title;
	protected long contentLength;
	protected long seeders;
	protected long leechers;
	protected List<FeedResultRejection> feedResultRejections = new ArrayList<FeedResultRejection>();
	protected List<TorrentRejection> torrentRejections = new ArrayList<TorrentRejection>();
	protected Torrent torrent;

	public String getTorrentURL() {
		return torrentURL;
	}

	public void setTorrentURL(String torrentURL) {
		this.torrentURL = torrentURL;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public long getSeeders() {
		return seeders;
	}

	public void setSeeders(long seeders) {
		this.seeders = seeders;
	}

	public long getLeechers() {
		return leechers;
	}

	public void setLeechers(long leechers) {
		this.leechers = leechers;
	}

	public List<FeedResultRejection> getFeedResultRejections() {
		return feedResultRejections;
	}

	public void setFeedResultRejections(
			List<FeedResultRejection> feedResultRejections) {
		this.feedResultRejections = feedResultRejections;
	}

	public List<TorrentRejection> getTorrentRejections() {
		return torrentRejections;
	}

	public void setTorrentRejections(List<TorrentRejection> torrentRejections) {
		this.torrentRejections = torrentRejections;
	}

	public Torrent getTorrent() {
		return torrent;
	}

	public void setTorrent(Torrent torrent) {
		this.torrent = torrent;
	}

	@Override
	public String toString() {
		return "FeedResult [title=" + title + ", torrentURL=" + torrentURL
				+ ", contentLength=" + contentLength + ", seeders=" + seeders
				+ ", leechers=" + leechers + ", torrent="
				+ (torrent != null ? torrent : null)
				+ ", feedResultRejections=" + feedResultRejections
				+ ", torrentRejections=" + torrentRejections + "]";
	}

}
