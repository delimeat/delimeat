package io.delimeat.core.torrent;

import java.io.IOException;
import java.util.Arrays;

import io.delimeat.util.DelimeatUtils;
import io.delimeat.util.bencode.BDictionary;
import io.delimeat.util.bencode.BencodeException;
import io.delimeat.util.bencode.BencodeUtils;

public class InfoHash {
  
  	private final byte[] sha1Bytes;
  
  	public InfoHash(byte[] rawBytes){
		this.sha1Bytes = DelimeatUtils.getSHA1(rawBytes);
   }
  
  	public InfoHash(BDictionary dictionary) throws IOException, BencodeException{
     	byte[] rawBytes = BencodeUtils.encode(dictionary);
     	this.sha1Bytes = DelimeatUtils.getSHA1(rawBytes); 
     
   }
  
  	public byte[] getBytes(){
   	return sha1Bytes;
   }
  
  	public String getHex(){
     return toString();
   }
  
  	@Override
  	public String toString(){
     return DelimeatUtils.toHex(sha1Bytes);
   }

    @Override
    public boolean equals(Object other) {
        if(other == null || !(other instanceof InfoHash)  )
          return false;

        if(this == other)
          return true;
        InfoHash otherInfoHash = (InfoHash)other;
        return Arrays.equals(sha1Bytes,otherInfoHash.sha1Bytes);
    }
}
