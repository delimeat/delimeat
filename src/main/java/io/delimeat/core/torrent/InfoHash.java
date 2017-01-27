package io.delimeat.core.torrent;

import java.util.Arrays;

import com.google.common.base.MoreObjects;
import com.google.common.io.BaseEncoding;

public class InfoHash {

	private byte[] sha1Bytes;

	public InfoHash(byte[] sha1Bytes) {
		this.sha1Bytes = sha1Bytes;
	}

	public byte[] getBytes() {
		return sha1Bytes;
	}

	public String getHex() {
		return BaseEncoding.base16().lowerCase().encode(sha1Bytes);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("value", getHex())
				.toString();
	}

	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;

		if (this == other)
			return true;

		if (other instanceof InfoHash) {
			InfoHash otherInfoHash = (InfoHash) other;
			return Arrays.equals(sha1Bytes, otherInfoHash.sha1Bytes);
		}
		return false;
	}
}
