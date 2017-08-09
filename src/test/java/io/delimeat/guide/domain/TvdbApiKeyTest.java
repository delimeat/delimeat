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

import io.delimeat.guide.domain.TvdbApiKey;

public class TvdbApiKeyTest {

	private TvdbApiKey apiKey;

	@Before
	public void setUp() {
		apiKey = new TvdbApiKey();
	}

	@Test
	public void valueTest() {
		Assert.assertNull(apiKey.getValue());
		apiKey.setValue("VALUE");
		Assert.assertEquals("VALUE", apiKey.getValue());
	}
  
  	@Test
  	public void hashCodeTest(){
     	apiKey.setValue("VALUE");
     	Assert.assertEquals(81434992, apiKey.hashCode());
   }
   
  	@Test
  	public void toStringTest(){
     	apiKey.setValue("VALUE");
     	Assert.assertEquals("TvdbApiKey [value=VALUE]", apiKey.toString());
   }
  	
	@Test
	public void equalsTest(){
		apiKey.setValue("VALUE");
		TvdbApiKey other = new TvdbApiKey();
		other.setValue("VALUE");
		Assert.assertTrue(apiKey.equals(other));
	}
	
	@Test
	public void equalsSelfTest(){
		Assert.assertTrue(apiKey.equals(apiKey));
	}
	
	@Test
	public void equalsNullTest(){
		Assert.assertFalse(apiKey.equals(null));
	}
	
	@Test
	public void equalsOtherClassTest(){
		Assert.assertFalse(apiKey.equals("STRING"));
	}
	
	@Test
	public void equalsNullMessageOtherNullMessageTest(){
		TvdbApiKey other = new TvdbApiKey();
		Assert.assertTrue(apiKey.equals(other));
	}
	
	@Test
	public void equalsNullMessageOtherNotNullMessageTest(){
		TvdbApiKey other = new TvdbApiKey();
		other.setValue("VALUE");
		Assert.assertFalse(apiKey.equals(other));
	}
	
	@Test
	public void equalsNotNullMessageOtherNullMessageTest(){
		apiKey.setValue("VALUE");
		TvdbApiKey other = new TvdbApiKey();
		Assert.assertFalse(apiKey.equals(other));
	}
}
