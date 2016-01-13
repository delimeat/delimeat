package io.delimeat.util.bencode;

import java.math.BigInteger;

public class BInteger extends BigInteger implements BObject {

  private static final long serialVersionUID = 1L;

  public BInteger(String value){
    super(value);
  }

  public BInteger(long value){
    super(Long.toString(value));
  }
 
  
}
