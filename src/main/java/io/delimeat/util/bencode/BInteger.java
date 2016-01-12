package io.delimeat.util.bencode;

public class BInteger implements BObject, Comparable<BInteger> {

	public static final BInteger BOOLEAN_FALSE = new BInteger(0);
	public static final BInteger BOOLEAN_TRUE = new BInteger(1);

	private long value;

	public BInteger() {
		value = 0;
	}

	public BInteger(long value) {
		this.value = value;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	@Override
	public int compareTo(BInteger other) {
		if (other != null) {
			Long otherValue = other.value;
			Long thisValue = this.value;
			return thisValue.compareTo(otherValue);
		} else {
			return -1;
		}
	}

	@Override
	public boolean equals(Object object) {
		Long thisValue = this.value;
		Long otherValue = null;
		if (object instanceof BInteger) {
			otherValue = new Long(((BInteger) object).getValue());
		} else if (object instanceof Long) {
			otherValue = (Long) object;
		} else if (object instanceof Integer) {
			otherValue = new Long((Integer) object);
		} else {
			return false;
		}
		return thisValue.equals(otherValue);
	}
}
