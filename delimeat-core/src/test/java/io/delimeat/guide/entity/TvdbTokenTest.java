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

public class TvdbTokenTest {

	private TvdbToken token;

	@BeforeEach
	public void setUp() {
		token = new TvdbToken();
	}

	@Test
	public void valueTest() {
		Assertions.assertNull(token.getValue());
		Assertions.assertNotEquals(0, token.getTime());
		token.setValue("VALUE");
		Assertions.assertEquals("VALUE", token.getValue());
		Assertions.assertNotEquals(0, token.getTime());
	}

  	@Test
  	public void hashCodeTest(){
     	token.setValue("VALUE");
     	token.setTime(1455858100917L);
     	Assertions.assertEquals(166227467, token.hashCode());
   }
   
  	@Test
  	public void toStringTest(){
     	token.setValue("VALUE");
     	token.setTime(1455858100917L);
     	Assertions.assertEquals("TvdbToken [time=1455858100917, value=VALUE]", token.toString());
   }
  	
	@Test
	public void equalsTest() {
     	token.setValue("VALUE");
     	token.setTime(1455858100917L);
     	TvdbToken other = new TvdbToken();
     	other.setValue("VALUE");
     	other.setTime(1455858100917L);

		Assertions.assertTrue(token.equals(other));
	}

	@Test
	public void equalsSelfTest() {
		Assertions.assertTrue(token.equals(token));
	}

	@Test
	public void equalsNullTest() {
		Assertions.assertFalse(token.equals(null));
	}

	@Test
	public void equalsOtherClassTest() {
		Assertions.assertFalse(token.equals(new Object()));
	}
	
	@Test
	public void equalsValueNullTest() {
     	token.setValue(null);
     	token.setTime(1455858100917L);
     	TvdbToken other = new TvdbToken();
     	other.setValue("VALUE");
     	other.setTime(1455858100917L);

		Assertions.assertFalse(token.equals(other));
	}
	
	@Test
	public void equalsValueTest() {
     	token.setValue("VALUE");
     	token.setTime(1455858100917L);
     	TvdbToken other = new TvdbToken();
     	other.setValue("VALUE_OTHER");
     	other.setTime(1455858100917L);

		Assertions.assertFalse(token.equals(other));
	}
	
	@Test
	public void equalsTimeTest() {
     	token.setValue("VALUE");
     	token.setTime(1455858100917L);
     	TvdbToken other = new TvdbToken();
     	other.setValue("VALUE");
     	other.setTime(Long.MIN_VALUE);

		Assertions.assertFalse(token.equals(other));
	}
}
