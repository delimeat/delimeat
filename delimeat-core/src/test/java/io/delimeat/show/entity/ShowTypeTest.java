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
package io.delimeat.show.entity;

import org.junit.Assert;
import org.junit.Test;

public class ShowTypeTest {
  
  	@Test
  	public void unknownTest(){
     	Assert.assertEquals(0, ShowType.UNKNOWN.getValue());
   }
  
  	@Test
  	public void animatedTest(){
     	Assert.assertEquals(1, ShowType.ANIMATED.getValue());
   } 
  
  	@Test
  	public void dailyTest(){
     	Assert.assertEquals(2, ShowType.DAILY.getValue());
   } 
  
  	@Test
  	public void miniSeriesTest(){
     	Assert.assertEquals(3, ShowType.MINI_SERIES.getValue());
   } 
  
  	@Test
  	public void seasonTest(){
     	Assert.assertEquals(4, ShowType.SEASON.getValue());
   }
}
