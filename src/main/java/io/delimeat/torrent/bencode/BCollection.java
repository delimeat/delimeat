package io.delimeat.torrent.bencode;

public interface BCollection extends BObject {
  
  void addValue(BObject value) throws BencodeException;
  
}
