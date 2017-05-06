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
		return MoreObjects.toStringHelper(this)
				.add("value", getHex())
				.omitNullValues()
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
