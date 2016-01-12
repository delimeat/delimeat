package io.delimeat.util.bencode;

import java.util.Arrays;

public class BString implements BObject, Comparable<BString> {

	private byte[] value;

	public BString() {
		// do nothing
	}

	public BString(byte[] value) {
		this.value = value;
	}

	public BString(String value) {
		byte[] bytes = value.getBytes();
		this.value = bytes;
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

	public void setValue(byte[] value) {
		this.value = value;
	}

	public void setValue(String value) {
		this.value = value.getBytes();
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
