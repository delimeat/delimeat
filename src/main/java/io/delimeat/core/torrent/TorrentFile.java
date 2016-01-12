package io.delimeat.core.torrent;


public class TorrentFile {

	private long length;
	private String name;
	

	public long getLength() {
		return length;
	}
	public void setLength(long length){
		this.length = length;
	}

	public String getName() {
		return name;
	}
	public void setName(String name){
		this.name = name;
	}

}
