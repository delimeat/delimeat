package io.delimeat.core.torrent;

import java.util.ArrayList;
import java.util.List;


public class TorrentInfo extends TorrentFile {
	
	private List<TorrentFile> files = new ArrayList<TorrentFile>();
	private InfoHash infoHash;
	

	public List<TorrentFile> getFiles() {
		return files;
	}
	public void setFiles(List<TorrentFile> files){
		this.files = files;
	}

	public InfoHash getInfoHash() {
		return infoHash;
	}

	public void setInfoHash(InfoHash infoHash){
		this.infoHash = infoHash;
	}
	
	@Override
	public String toString() {
		return "TorrentInfo [ infoHash:" + infoHash + ", name=" + name + ", length="
				+ length + " files=" + files + "]";
	}

}
