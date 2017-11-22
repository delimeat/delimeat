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

public class BStringTest {

	@Test
	public void stringConstructorTest() {
		BString b_string = new BString("string_value");
		Assertions.assertEquals("string_value", b_string.toString());
	}

	@Test
	public void byteArrayConstructorTest() {
		BString b_string = new BString("string_value".getBytes());
		Assertions.assertEquals("string_value", b_string.toString());
	}

	@Test
	public void nullConstructorTest() {
		byte[] bytes = null;
		BString b_string = new BString(bytes);
		Assertions.assertEquals("", b_string.toString());
	}

	@Test
	public void sameComparableTest() {
		BString b_string_one = new BString("match");
		BString b_string_two = new BString("match");
		Assertions.assertEquals(true, b_string_one.compareTo(b_string_two) == 0);
	}

	@Test
	public void notSameComparableTest() {
		BString b_string_one = new BString("match");
		BString b_string_two = new BString("no match");
		Assertions.assertEquals(true, b_string_one.compareTo(b_string_two) != 0);
	}

	@Test
	public void nullComparableTest() {
		BString b_string_one = new BString("match");
		Assertions.assertEquals(true, b_string_one.compareTo(null) != 0);
	}

	@Test
	public void emptyEqualsTest() {
		BString b_string_one = new BString("");
		BString b_string_two = new BString("equals");
		Assertions.assertEquals(false, b_string_two.equals(b_string_one));
	}

	@Test
	public void equalEqualsTest() {
		BString b_string_one = new BString("match");
		BString b_string_two = new BString("match");
		Assertions.assertEquals(true, b_string_one.equals(b_string_two));
	}

	@Test
	public void notEqualBStringEqualsTest() {
		BString b_string_one = new BString("match");
		BString b_string_two = new BString("no match");
		Assertions.assertEquals(false, b_string_one.equals(b_string_two));
	}

	@Test
	public void nullEqualsTest() {
		BString b_string_one = new BString("match");
		Assertions.assertEquals(false, b_string_one.equals(null));
	}

	@Test
	public void equalStringEqualsTest() {
		String string_one = "match";
		BString b_string_one = new BString("match");
		Assertions.assertEquals(true, b_string_one.equals(string_one));
	}

	@Test
	public void notEqaulStringEqualsTest() {
		String string_one = "no match";
		BString b_string_one = new BString("match");
		Assertions.assertEquals(false, b_string_one.equals(string_one));
	}

	@Test
	public void equalsByteArrayTest() {
		BString b_string_one = new BString("match");
		Assertions.assertEquals(true, b_string_one.equals("match".getBytes()));
	}

}
