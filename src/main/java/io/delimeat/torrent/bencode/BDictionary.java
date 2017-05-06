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

import java.util.TreeMap;

public class BDictionary extends TreeMap<BString, BObject> implements BCollection {

	private static final long serialVersionUID = 1L;
	
	private BString key = null;

   public BString getKey(){
     return key;
   }
  
	public BObject put(String key, BObject value) {
		return put(new BString(key), value);
	}
  
	public BObject put(String key, String value) {
		return put(new BString(key), new BString(value));
	}
  
	public BObject put(String key, long value) {
		return put(new BString(key), new BInteger(value));
	}
     
   public BObject get(String key){
     return get(new BString(key));
   }
   
   public BObject get(byte[] key){
     return get(new BString(key));
   }

	public void addValue(BObject value) throws BencodeException {
		if( getKey() != null ){
        put(key,value);
        key = null;
        return;
      }else if( value instanceof BString ){
        key = (BString)value;
        return;
      }
     	throw new BencodeException("Expected Benocded String as Key got " + value.getClass().getName());

	}

}
