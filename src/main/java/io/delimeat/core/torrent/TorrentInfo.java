package io.delimeat.core.torrent;

import java.util.ArrayList;
import java.util.List;


public class TorrentInfo extends TorrentFile {
	
	private List<TorrentFile> files = new ArrayList<TorrentFile>();
	private byte[] infoHash;
	

	public List<TorrentFile> getFiles() {
		return files;
	}
	public void setFiles(List<TorrentFile> files){
		this.files = files;
	}

	public byte[] getInfoHash() {
		return infoHash;
	}

	public void setInfoHash(byte[] infoHash){
		this.infoHash = infoHash;
	}
	
	@Override
	public String toString() {
		return "TorrentInfo [name=" + name + ", length="
				+ length + " files=" + files + "]";
	}

}
