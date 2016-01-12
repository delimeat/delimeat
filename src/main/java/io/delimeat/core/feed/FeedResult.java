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
	

	public Torrent getTorrent() {
		return torrent;
	}

	public void setTorrent(Torrent torrent) {
		this.torrent = torrent;
	}


	public void addFeedResultRejection(FeedResultRejection rejection) {
		feedResultRejections.add(rejection);
	}


	public void addTorrentRejection(TorrentRejection rejection) {
		torrentRejections.add(rejection);
		
	}


	public long getContentLength() {
		return contentLength;
	}


	public List<FeedResultRejection> getFeedResultRejections() {
		return feedResultRejections;
	}


	public long getLeechers() {
		return leechers;
	}


	public long getSeeders() {

		return seeders;
	}


	public String getTitle() {
		return title;
	}


	public List<TorrentRejection> getTorrentRejections() {
		return torrentRejections;
	}


	public String getTorrentURL() {
		return torrentURL;
	}

	public void setLeechers(long leechers) {
		this.leechers = leechers;
		
	}
	
	public void setSeeders(long seeders) {
		this.seeders = seeders;
		
	}
	
	public void setTitle(String title){
		this.title = title;
	}

	public void setTorrentURL(String torrentURL){
		this.torrentURL = torrentURL;
	}

	public void setContentLength(Long contentLength){
		this.contentLength = contentLength;
	}

}
