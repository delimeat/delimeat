package io.delimeat.core.torrent;

public class TorrentFile {

	protected long length;
	protected String name;

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "TorrentFile [length=" + length + ", name=" + name + "]";
	}
	
}
