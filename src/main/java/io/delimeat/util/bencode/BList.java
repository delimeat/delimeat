package io.delimeat.util.bencode;

import io.delimeat.util.bencode.BObject;
import io.delimeat.util.bencode.BencodeException;

import java.util.ArrayList;

public class BList extends ArrayList<BObject> implements BCollection {

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

   @Override
   public void addValue(BObject value) throws BencodeException {
   	add(value);  
   }

}
