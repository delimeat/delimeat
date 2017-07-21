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

import io.delimeat.guide.domain.TvdbToken;

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
}
