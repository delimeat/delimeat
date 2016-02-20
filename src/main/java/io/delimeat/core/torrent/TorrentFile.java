package io.delimeat.core.torrent;

import com.google.common.base.MoreObjects;

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
     	return MoreObjects.toStringHelper(this)
              .add("name", name)
              .add("length", length)
              .toString();
	}
	
}
