package io.delimeat.core.feed;

import com.google.common.base.MoreObjects;
import io.delimeat.core.torrent.Torrent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FeedResult {

	private String torrentURL;
	private String title;
	private long contentLength;
	private long seeders;
	private long leechers;	
	private List<FeedResultRejection> feedResultRejections = new ArrayList<FeedResultRejection>();
	private Torrent torrent;

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

	public Torrent getTorrent() {
		return torrent;
	}

	public void setTorrent(Torrent torrent) {
		this.torrent = torrent;
	}

	@Override
	public String toString() {
     	return MoreObjects.toStringHelper(this)
              .add("title", title)  
              .add("torrentURL", torrentURL)
              .add("contentLength", contentLength)
              .add("seeders", seeders)
              .add("leechers", leechers)
              .add("torrent", torrent)
              .add("feedResultRejections", feedResultRejections)
              .toString();
	}

  @Override 
  public int hashCode() {
    return Objects.hash(title, torrentURL, contentLength, seeders, leechers, torrent);
  }

}
