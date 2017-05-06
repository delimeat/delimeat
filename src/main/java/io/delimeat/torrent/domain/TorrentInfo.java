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
package io.delimeat.torrent.domain;

import com.google.common.base.MoreObjects;

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
     	return MoreObjects.toStringHelper(this)
              .add("infoHash", infoHash)
              .add("name", name)
              .add("length", length)
              .add("files", files)
              .omitNullValues()
              .toString();
	}

}
