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


public class TvdbApiKeyTest {

	private TvdbApiKey apiKey;

	@BeforeEach
	public void setUp() {
		apiKey = new TvdbApiKey();
	}

	@Test
	public void valueTest() {
		Assertions.assertNull(apiKey.getValue());
		apiKey.setValue("VALUE");
		Assertions.assertEquals("VALUE", apiKey.getValue());
	}
  
  	@Test
  	public void hashCodeTest(){
     	apiKey.setValue("VALUE");
     	Assertions.assertEquals(81434992, apiKey.hashCode());
   }
   
  	@Test
  	public void toStringTest(){
     	apiKey.setValue("VALUE");
     	Assertions.assertEquals("TvdbApiKey [value=VALUE]", apiKey.toString());
   }
  	
	@Test
	public void equalsTest(){
		apiKey.setValue("VALUE");
		TvdbApiKey other = new TvdbApiKey();
		other.setValue("VALUE");
		Assertions.assertTrue(apiKey.equals(other));
	}
	
	@Test
	public void equalsSelfTest(){
		Assertions.assertTrue(apiKey.equals(apiKey));
	}
	
	@Test
	public void equalsNullTest(){
		Assertions.assertFalse(apiKey.equals(null));
	}
	
	@Test
	public void equalsOtherClassTest(){
		Assertions.assertFalse(apiKey.equals(new Object()));
	}
	
	@Test
	public void equalsNullMessageOtherNullMessageTest(){
		TvdbApiKey other = new TvdbApiKey();
		Assertions.assertTrue(apiKey.equals(other));
	}
	
	@Test
	public void equalsNullMessageOtherNotNullMessageTest(){
		TvdbApiKey other = new TvdbApiKey();
		other.setValue("VALUE");
		Assertions.assertFalse(apiKey.equals(other));
	}
	
	@Test
	public void equalsNotNullMessageOtherNullMessageTest(){
		apiKey.setValue("VALUE");
		TvdbApiKey other = new TvdbApiKey();
		Assertions.assertFalse(apiKey.equals(other));
	}
}
