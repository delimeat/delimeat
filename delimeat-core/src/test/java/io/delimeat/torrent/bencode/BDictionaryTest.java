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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BDictionaryTest {

	public BDictionary dictionary;

	@BeforeEach
	public void setUp() {
		dictionary = new BDictionary();
	}

	@Test
	public void putAllTest() {
		Assertions.assertTrue(dictionary.isEmpty());
		Map<BString, BObject> values = new TreeMap<BString, BObject>();
		BString b_string_val = new BString("value");
		BString b_string_key = new BString("key_value");
		values.put(b_string_key, b_string_val);
		dictionary.putAll(values);
		Assertions.assertEquals(1, dictionary.size());
		Assertions.assertEquals(b_string_val, dictionary.get(b_string_key));
	}

	@Test
	public void putBStringTest() {
		Assertions.assertTrue(dictionary.isEmpty());
		BString b_string_val = new BString("value");
		BString b_string_key = new BString("key_value");
		Assertions.assertEquals(null, dictionary.put(b_string_key, b_string_val));
		Assertions.assertEquals(1, dictionary.size());
		Assertions.assertEquals(b_string_val, dictionary.get(b_string_key));
	}

	@Test
	public void putStringBObjectTest() {
		Assertions.assertTrue(dictionary.isEmpty());
		BString b_string_val = new BString("value");
		Assertions.assertEquals(null, dictionary.put("key_value", b_string_val));
		Assertions.assertEquals(1, dictionary.size());
		Assertions.assertEquals(b_string_val, dictionary.get("key_value"));
	}

	@Test
	public void putStringStringTest() {
		Assertions.assertTrue(dictionary.isEmpty());
		Assertions.assertEquals(null, dictionary.put("key_value", "value"));
		Assertions.assertEquals(1, dictionary.size());
		Assertions.assertEquals(new BString("value"), dictionary.get("key_value"));
	}

	@Test
	public void putStringLongTest() {
		Assertions.assertTrue(dictionary.isEmpty());
		Assertions.assertEquals(null, dictionary.put("key_value", Long.MAX_VALUE));
		Assertions.assertEquals(1, dictionary.size());
		Assertions.assertEquals(new BInteger(Long.MAX_VALUE), dictionary.get("key_value"));
	}

	@Test
	public void getByteArrayTest() {
		Assertions.assertTrue(dictionary.isEmpty());
		BString b_string_val = new BString("value");
		BString b_string_key = new BString("key_value");
		dictionary.put(b_string_key, b_string_val);
		Assertions.assertEquals(1, dictionary.size());
		Assertions.assertEquals(b_string_val, dictionary.get("key_value".getBytes()));
	}

	@Test
	public void removeBStringTest() {
		Assertions.assertTrue(dictionary.isEmpty());
		BString b_string_val = new BString("value");
		BString b_string_key = new BString("key_value");
		dictionary.put(b_string_key, b_string_val);
		Assertions.assertEquals(1, dictionary.size());
		Assertions.assertEquals(b_string_val, dictionary.remove(b_string_key));
		Assertions.assertTrue(dictionary.isEmpty());
	}

	@Test
	public void addValueTest() throws BencodeException {
		Assertions.assertTrue(dictionary.isEmpty());
		Assertions.assertNull(dictionary.getKey());
		BString b_string_key = new BString("key_value");
		dictionary.addValue(b_string_key);
		Assertions.assertNotNull(dictionary.getKey());
		Assertions.assertEquals(0, dictionary.size());
		BInteger b_integer_val = new BInteger(1);
		dictionary.addValue(b_integer_val);
		Assertions.assertNull(dictionary.getKey());
		Assertions.assertEquals(1, dictionary.size());
	}

	@Test
	public void InvalidSetValueTest() throws BencodeException {
		Assertions.assertTrue(dictionary.isEmpty());
		Assertions.assertNull(dictionary.getKey());
		BInteger b_integer_val = new BInteger(1);
		BencodeException ex = Assertions.assertThrows(BencodeException.class, () -> {
			dictionary.addValue(b_integer_val);
		});

		Assertions.assertEquals("Expected Benocded String as Key got io.delimeat.torrent.bencode.BInteger", ex.getMessage());

	}
}
