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
import org.junit.jupiter.api.Test;


public class TvdbApiKeyTest {

	@Test
	public void valueTest() {
		TvdbApiKey apiKey = new TvdbApiKey("VALUE");
		Assertions.assertEquals("VALUE", apiKey.getValue());
	}
  
  	@Test
  	public void hashCodeTest(){
  		TvdbApiKey apiKey = new TvdbApiKey("VALUE");
     	Assertions.assertEquals(81434992, apiKey.hashCode());
   }
   
  	@Test
  	public void toStringTest(){
  		TvdbApiKey apiKey = new TvdbApiKey("VALUE");
     	Assertions.assertEquals("TvdbApiKey [value=VALUE]", apiKey.toString());
   }
  	
	@Test
	public void equalsTest(){
		TvdbApiKey apiKey = new TvdbApiKey("VALUE");
		TvdbApiKey other = new TvdbApiKey("VALUE");
		Assertions.assertTrue(apiKey.equals(other));
	}
	
	@Test
	public void equalsSelfTest(){
		TvdbApiKey apiKey = new TvdbApiKey("VALUE");
		Assertions.assertTrue(apiKey.equals(apiKey));
	}
	
	@Test
	public void equalsNullTest(){
		TvdbApiKey apiKey = new TvdbApiKey("VALUE");
		Assertions.assertFalse(apiKey.equals(null));
	}
	
	@Test
	public void equalsOtherClassTest(){
		TvdbApiKey apiKey = new TvdbApiKey("VALUE");
		Assertions.assertFalse(apiKey.equals(new Object()));
	}
	
	@Test
	public void equalsNullMessageOtherNullMessageTest(){
		TvdbApiKey apiKey = new TvdbApiKey(null);
		TvdbApiKey other = new TvdbApiKey(null);
		Assertions.assertTrue(apiKey.equals(other));
	}
	
	@Test
	public void equalsNullMessageOtherNotNullMessageTest(){
		TvdbApiKey apiKey = new TvdbApiKey(null);
		TvdbApiKey other = new TvdbApiKey("VALUE");
		Assertions.assertFalse(apiKey.equals(other));
	}
	
	@Test
	public void equalsNotNullMessageOtherNullMessageTest(){
		TvdbApiKey apiKey = new TvdbApiKey("VALUE");
		TvdbApiKey other = new TvdbApiKey(null);
		Assertions.assertFalse(apiKey.equals(other));
	}
}
