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

import org.junit.Assert;
import org.junit.Test;

import io.delimeat.torrent.bencode.BencodeException;

public class BencodeExceptionTest {
  
  	@Test
  	public void messageConstructorTest(){
     	BencodeException ex = new BencodeException("TEST");
     	Assert.assertEquals("TEST", ex.getMessage());
   }
  	
  	@Test
  	public void causeConstructorTest(){
     	Throwable throwable = new Throwable("THROWABLE");
     	BencodeException ex = new BencodeException(throwable);
     	Assert.assertEquals(throwable, ex.getCause());
   }
  	
  	@Test
  	public void messageCauseConstructorTest(){
     	Throwable throwable = new Throwable("THROWABLE");
     	BencodeException ex = new BencodeException("TEST", throwable);
     	Assert.assertEquals("TEST", ex.getMessage());
     	Assert.assertEquals(throwable, ex.getCause());     	
   }
}
