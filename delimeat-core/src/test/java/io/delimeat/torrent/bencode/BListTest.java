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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BListTest {

	@Test
	public void NullConstructorTest() {
		BList b_list_one = new BList();
		Assertions.assertEquals(0, b_list_one.size());
	}

	@Test
	public void BValueAddTest() {
		BList b_list_one = new BList();
		BString b_string = new BString("value");
		b_list_one.add(b_string);
		Assertions.assertEquals(1, b_list_one.size());
		Assertions.assertEquals(b_string, b_list_one.get(0));
	}

	@Test
	public void LongAddTest() {
		BList b_list_one = new BList();
		b_list_one.add(1234567890);
		Assertions.assertEquals(1, b_list_one.size());
		Assertions.assertEquals(true, b_list_one.get(0) instanceof BInteger);
		Assertions.assertEquals(1234567890, ((BInteger) b_list_one.get(0)).longValue());
	}

	@Test
	public void StringAddTest() {
		BList b_list_one = new BList();
		b_list_one.add("string_value");
		Assertions.assertEquals(1, b_list_one.size());
		Assertions.assertEquals(true, b_list_one.get(0) instanceof BString);
		Assertions.assertEquals("string_value", ((BString) b_list_one.get(0)).toString());
	}

	@Test
	public void ByteArrayAddTest() {
		BList b_list_one = new BList();
		b_list_one.add("string_value".getBytes());
		Assertions.assertEquals(1, b_list_one.size());
		Assertions.assertEquals(true, b_list_one.get(0) instanceof BString);
		Assertions.assertEquals("string_value", new String(((BString) b_list_one.get(0)).getValue()));
	}

	@Test
	public void BValueRemoveTest() {
		BString b_string = new BString("value");
		BInteger b_integer = new BInteger(1);
		BList b_list_one = new BList();
		b_list_one.add(b_string);
		b_list_one.add(b_integer);
		Assertions.assertEquals(2, b_list_one.size());
		Assertions.assertEquals(true, b_list_one.remove(b_string));
		Assertions.assertEquals(1, b_list_one.size());
		Assertions.assertEquals(b_integer, b_list_one.get(0));
	}

	@Test
	public void IndexRemoveTest() {
		BString b_string = new BString("value");
		BInteger b_integer = new BInteger(1);
		BList b_list_one = new BList();
		b_list_one.add(b_string);
		b_list_one.add(b_integer);
		Assertions.assertEquals(2, b_list_one.size());
		Assertions.assertEquals(b_integer, b_list_one.remove(1));
		Assertions.assertEquals(1, b_list_one.size());
		Assertions.assertEquals(b_string, b_list_one.get(0));
	}
}
