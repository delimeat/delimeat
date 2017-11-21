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
package io.delimeat.feed.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FeedExceptionTest {
  
  	@Test
  	public void messageConstructorTest(){
     	FeedException ex = new FeedException("TEST");
     	Assertions.assertEquals("TEST", ex.getMessage());
   }
  	
  	@Test
  	public void causeConstructorTest(){
     	Throwable throwable = new Throwable("THROWABLE");
     	FeedException ex = new FeedException(throwable);
     	Assertions.assertEquals(throwable, ex.getCause());
   }
  	
  	@Test
  	public void messageCauseConstructorTest(){
     	Throwable throwable = new Throwable("THROWABLE");
     	FeedException ex = new FeedException("TEST", throwable);
     	Assertions.assertEquals("TEST", ex.getMessage());
     	Assertions.assertEquals(throwable, ex.getCause());     	
   }
}
