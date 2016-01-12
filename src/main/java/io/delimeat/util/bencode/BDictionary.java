package io.delimeat.util.bencode;

import java.io.IOException;
import java.util.TreeMap;

public class BDictionary extends TreeMap<BString, BObject> implements BObject {

	private static final long serialVersionUID = 1L;
	
	private BString key = null;

	boolean expectingKey() {
		return (key == null);
	}

	public BObject get(byte[] key) {
		return get(new BString(key));
	}

	public BObject get(String key) {
		return get(new BString(key));
	}

	public BObject put(BString key, byte[] value) {
		return put(key, new BString(value));
	}

	public BObject put(BString key, String value) {
		return put(key, new BString(value));
	}

	public BObject put(BString key, long value) {
		return put(key, new BInteger(value));
	}

	public BObject put(byte[] key, BObject value) {
		return put(new BString(key), value);
	}

	public BObject put(byte[] key, byte[] value) {
		return put(new BString(key), new BString(value));
	}

	public BObject put(byte[] key, String value) {
		return put(new BString(key), new BString(value));
	}

	public BObject put(byte[] key, long value) {
		return put(new BString(key), new BInteger(value));
	}

	public BObject put(String key, BObject value) {
		return put(new BString(key), value);
	}

	public BObject put(String key, byte[] value) {
		return put(new BString(key), new BString(value));
	}

	public BObject put(String key, String value) {
		return put(new BString(key), new BString(value));
	}

	public BObject put(String key, long value) {
		return put(new BString(key), new BInteger(value));
	}

	public BObject remove(String key) {
		return remove(new BString(key));
	}

	public BObject remove(byte[] key) {
		return remove(new BString(key));
	}

	void setValue(BObject value) throws BencodeExpectedKeyException {
		if (expectingKey() && !(value instanceof BString)) {
			throw new BencodeExpectedKeyException("ExpectedKeyException " + value.getClass().getName());
		} else if (expectingKey()) {
			key = (BString) value;
		} else {
			put(key, value);
			key = null;
		}
	}

	public byte[] getBytes() throws IOException, BencodeException {
		return BencodeUtils.encode(this);
	}
}
