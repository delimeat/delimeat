/*
 * Copyright 2013-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.delimeat.torrent.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TorrentInfo  {
	
	private List<TorrentFile> files = new ArrayList<TorrentFile>();
	private InfoHash infoHash;
	protected long length;
	protected String name;
	/**
	 * @return the files
	 */
	public List<TorrentFile> getFiles() {
		return files;
	}
	/**
	 * @param files the files to set
	 */
	public void setFiles(List<TorrentFile> files) {
		this.files = files;
	}
	/**
	 * @return the infoHash
	 */
	public InfoHash getInfoHash() {
		return infoHash;
	}
	/**
	 * @param infoHash the infoHash to set
	 */
	public void setInfoHash(InfoHash infoHash) {
		this.infoHash = infoHash;
	}
	/**
	 * @return the length
	 */
	public long getLength() {
		return length;
	}
	/**
	 * @param length the length to set
	 */
	public void setLength(long length) {
		this.length = length;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(files,infoHash,length, name);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TorrentInfo other = (TorrentInfo) obj;
		if (files == null) {
			if (other.files != null)
				return false;
		} else if (!files.equals(other.files))
			return false;
		if (infoHash == null) {
			if (other.infoHash != null)
				return false;
		} else if (!infoHash.equals(other.infoHash))
			return false;
		if (length != other.length)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TorrentInfo [" + (files != null ? "files=" + files + ", " : "")
				+ (infoHash != null ? "infoHash=" + infoHash + ", " : "") + "length=" + length + ", "
				+ (name != null ? "name=" + name : "") + "]";
	}
}
