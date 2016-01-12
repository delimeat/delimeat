package io.delimeat.util.bencode;

import java.util.ArrayList;

public class BList extends ArrayList<BObject> implements BObject {

	private static final long serialVersionUID = 1L;

	public boolean add(byte[] value) {
		return add(new BString(value));
	}
	
	public boolean add(long value) {
		return add(new BInteger(value));
	}
	
	public boolean add(String value) {
		return add(new BString(value));
	}

}
