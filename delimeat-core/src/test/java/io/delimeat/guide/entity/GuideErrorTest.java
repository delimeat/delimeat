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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GuideErrorTest {
  
	private GuideError error;

	@Before
	public void setUp() {
		error = new GuideError();
	}

	@Test
	public void messageTest() {
		Assert.assertNull(error.getMessage());
		error.setMessage("MESSAGE");
		Assert.assertEquals("MESSAGE", error.getMessage());
	}
	
	@Test
	public void toStringTest(){
		error.setMessage("MESSAGE");
		Assert.assertEquals("GuideError [message=MESSAGE]", error.toString());
	}
	
	@Test
	public void hashCodeTest() {
		error.setMessage("MESSAGE");

		Assert.assertEquals(1672907782, error.hashCode());
	}
	
	@Test
	public void equalsTest(){
		error.setMessage("MESSAGE");
		GuideError other = new GuideError();
		other.setMessage("MESSAGE");
		Assert.assertTrue(error.equals(other));
	}
	
	@Test
	public void equalsSelfTest(){
		Assert.assertTrue(error.equals(error));
	}
	
	@Test
	public void equalsNullTest(){
		Assert.assertFalse(error.equals(null));
	}
	
	@Test
	public void equalsOtherClassTest(){
		Assert.assertFalse(error.equals("STRING"));
	}
	
	@Test
	public void equalsNullMessageOtherNullMessageTest(){
		GuideError other = new GuideError();
		Assert.assertTrue(error.equals(other));
	}
	
	@Test
	public void equalsNullMessageOtherNotNullMessageTest(){
		GuideError other = new GuideError();
		other.setMessage("NOT NULL");
		Assert.assertFalse(error.equals(other));
	}
	
	@Test
	public void equalsNotNullMessageOtherNullMessageTest(){
		error.setMessage("NOT NULL");
		GuideError other = new GuideError();
		Assert.assertFalse(error.equals(other));
	}
	
	
}
