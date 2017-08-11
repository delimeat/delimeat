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
package io.delimeat.guide.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TvdbTokenTest {

	private TvdbToken token;

	@Before
	public void setUp() {
		token = new TvdbToken();
	}

	@Test
	public void valueTest() {
		Assert.assertNull(token.getValue());
		Assert.assertNotEquals(0, token.getTime());
		token.setValue("VALUE");
		Assert.assertEquals("VALUE", token.getValue());
		Assert.assertNotEquals(0, token.getTime());
	}

  	@Test
  	public void hashCodeTest(){
     	token.setValue("VALUE");
     	token.setTime(1455858100917L);
     	Assert.assertEquals(166227467, token.hashCode());
   }
   
  	@Test
  	public void toStringTest(){
     	token.setValue("VALUE");
     	token.setTime(1455858100917L);
     	Assert.assertEquals("TvdbToken [time=1455858100917, value=VALUE]", token.toString());
   }
  	
	@Test
	public void equalsTest() {
     	token.setValue("VALUE");
     	token.setTime(1455858100917L);
     	TvdbToken other = new TvdbToken();
     	other.setValue("VALUE");
     	other.setTime(1455858100917L);

		Assert.assertTrue(token.equals(other));
	}

	@Test
	public void equalsSelfTest() {
		Assert.assertTrue(token.equals(token));
	}

	@Test
	public void equalsNullTest() {
		Assert.assertFalse(token.equals(null));
	}

	@Test
	public void equalsOtherClassTest() {
		Assert.assertFalse(token.equals("STRING"));
	}
	
	@Test
	public void equalsValueNullTest() {
     	token.setValue(null);
     	token.setTime(1455858100917L);
     	TvdbToken other = new TvdbToken();
     	token.setValue("VALUE");
     	token.setTime(1455858100917L);

		Assert.assertFalse(token.equals(other));
	}
	
	@Test
	public void equalsValueTest() {
     	token.setValue("VALUE");
     	token.setTime(1455858100917L);
     	TvdbToken other = new TvdbToken();
     	token.setValue("VALUE_OTHER");
     	token.setTime(1455858100917L);

		Assert.assertFalse(token.equals(other));
	}
	
	@Test
	public void equalsTimeTest() {
     	token.setValue("VALUE");
     	token.setTime(1455858100917L);
     	TvdbToken other = new TvdbToken();
     	token.setValue("VALUE");
     	token.setTime(Long.MIN_VALUE);

		Assert.assertFalse(token.equals(other));
	}
}
