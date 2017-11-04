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
package io.delimeat.torrent.bencode;

import java.util.Arrays;

public class BString implements BObject, Comparable<BString> {

	private byte[] value;

	public BString(byte[] value) {
		this.value = value;
	}

	public BString(String value) { 
		this.value = value.getBytes();
	}

	public byte[] getValue() {
		return value;
	}

	@Override
	public String toString() {
		if (value != null) {
			return new String(value);
		} else {
			return new String();
		}
	}

	@Override
	public int compareTo(BString other) {
		if (other != null) {
			String otherString = other.toString();
			String thisString = this.toString();
			return thisString.compareTo(otherString);
		} else {
			return -1;
		}
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof BString) {
			return Arrays.equals(this.value, ((BString) object).value);
		} else if (object instanceof String) {
			String thisString = this.toString();
			return thisString.equals(object);
		} else if (object instanceof byte[]) {
			return Arrays.equals(value, (byte[]) object);
		} else {
			return false;
		}

	}

}
