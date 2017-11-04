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

import java.util.Map;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BDictionaryTest {

   public BDictionary dictionary;
  
   @Before
   public void setUp(){
     dictionary = new BDictionary();
   }

	@Test
	public void putAllTest() {
      Assert.assertTrue(dictionary.isEmpty());
		Map<BString, BObject> values = new TreeMap<BString, BObject>();
		BString b_string_val = new BString("value");
		BString b_string_key = new BString("key_value");
		values.put(b_string_key, b_string_val);
		dictionary.putAll(values);
		Assert.assertEquals(1, dictionary.size());
		Assert.assertEquals(b_string_val, dictionary.get(b_string_key));
	}

	@Test
	public void putBStringTest() {
      Assert.assertTrue(dictionary.isEmpty());
		BString b_string_val = new BString("value");
		BString b_string_key = new BString("key_value");
		Assert.assertEquals(null, dictionary.put(b_string_key, b_string_val));
		Assert.assertEquals(1, dictionary.size());
     	Assert.assertEquals(b_string_val, dictionary.get(b_string_key));
	}

	@Test
	public void putStringBObjectTest() {
      Assert.assertTrue(dictionary.isEmpty());
		BString b_string_val = new BString("value");
		Assert.assertEquals(null, dictionary.put("key_value", b_string_val));
		Assert.assertEquals(1, dictionary.size());
      Assert.assertEquals(b_string_val, dictionary.get("key_value"));
	}

	@Test
	public void putStringStringTest() {
      Assert.assertTrue(dictionary.isEmpty());
		Assert.assertEquals(null, dictionary.put("key_value", "value"));
		Assert.assertEquals(1, dictionary.size());
      Assert.assertEquals(new BString("value"), dictionary.get("key_value"));
	}
  
	@Test
	public void putStringLongTest() {
      Assert.assertTrue(dictionary.isEmpty());
		Assert.assertEquals(null, dictionary.put("key_value", Long.MAX_VALUE));
		Assert.assertEquals(1, dictionary.size());
      Assert.assertEquals(new BInteger(Long.MAX_VALUE), dictionary.get("key_value"));
	}

	@Test
	public void getByteArrayTest() {
      Assert.assertTrue(dictionary.isEmpty());
		BString b_string_val = new BString("value");
		BString b_string_key = new BString("key_value");
		dictionary.put(b_string_key, b_string_val);
		Assert.assertEquals(1, dictionary.size());
		Assert.assertEquals(b_string_val,dictionary.get("key_value".getBytes()));
	}

	@Test
	public void removeBStringTest() {
      Assert.assertTrue(dictionary.isEmpty());
		BString b_string_val = new BString("value");
		BString b_string_key = new BString("key_value");
		dictionary.put(b_string_key, b_string_val);
		Assert.assertEquals(1, dictionary.size());
		Assert.assertEquals(b_string_val, dictionary.remove(b_string_key));
		Assert.assertTrue(dictionary.isEmpty());
	}

	@Test
	public void addValueTest() throws BencodeException {
      Assert.assertTrue(dictionary.isEmpty());
		Assert.assertNull(dictionary.getKey());
		BString b_string_key = new BString("key_value");
		dictionary.addValue(b_string_key);
		Assert.assertNotNull(dictionary.getKey());
		Assert.assertEquals(0, dictionary.size());
		BInteger b_integer_val = new BInteger(1);
		dictionary.addValue(b_integer_val);
		Assert.assertNull(dictionary.getKey());
		Assert.assertEquals(1, dictionary.size());
	}

	@Test(expected = BencodeException.class)
	public void InvalidSetValueTest() throws BencodeException {
      Assert.assertTrue(dictionary.isEmpty());
		Assert.assertNull(dictionary.getKey());
		BInteger b_integer_val = new BInteger(1);
		dictionary.addValue(b_integer_val);
	}
}
