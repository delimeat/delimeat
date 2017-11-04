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
