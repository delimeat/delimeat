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
package io.delimeat.guide.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GuideErrorTest {

	private GuideError error;

	@BeforeEach
	public void setUp() {
		error = new GuideError();
	}

	@Test
	public void messageTest() {
		Assertions.assertNull(error.getMessage());
		error.setMessage("MESSAGE");
		Assertions.assertEquals("MESSAGE", error.getMessage());
	}

	@Test
	public void toStringTest() {
		error.setMessage("MESSAGE");
		Assertions.assertEquals("GuideError [message=MESSAGE]", error.toString());
	}

	@Test
	public void hashCodeTest() {
		error.setMessage("MESSAGE");

		Assertions.assertEquals(1672907782, error.hashCode());
	}

	@Test
	public void equalsTest() {
		error.setMessage("MESSAGE");
		GuideError other = new GuideError();
		other.setMessage("MESSAGE");
		Assertions.assertTrue(error.equals(other));
	}

	@Test
	public void equalsSelfTest() {
		Assertions.assertTrue(error.equals(error));
	}

	@Test
	public void equalsNullTest() {
		Assertions.assertFalse(error.equals(null));
	}

	@Test
	public void equalsOtherClassTest() {
		Assertions.assertFalse(error.equals(new Object()));
	}

	@Test
	public void equalsNullMessageOtherNullMessageTest() {
		GuideError other = new GuideError();
		Assertions.assertTrue(error.equals(other));
	}

	@Test
	public void equalsNullMessageOtherNotNullMessageTest() {
		GuideError other = new GuideError();
		other.setMessage("NOT NULL");
		Assertions.assertFalse(error.equals(other));
	}

	@Test
	public void equalsNotNullMessageOtherNullMessageTest() {
		error.setMessage("NOT NULL");
		GuideError other = new GuideError();
		Assertions.assertFalse(error.equals(other));
	}

}
