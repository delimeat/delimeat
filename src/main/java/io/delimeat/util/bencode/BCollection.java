package io.delimeat.util.bencode;

public interface BCollection extends BObject {
  
  void addValue(BObject value) throws BencodeException;
  
}
