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
import org.junit.Test;

import io.delimeat.guide.domain.GuideSource;

public class GuideSourceTest {
  
  	@Test
  	public void tvdbTest(){
     	Assert.assertEquals(0, GuideSource.TVDB.getValue());
   }
  
  	@Test
  	public void imdbTest(){
     	Assert.assertEquals(1, GuideSource.IMDB.getValue());
   }
  
  	@Test
  	public void tvrageTest(){
     	Assert.assertEquals(2, GuideSource.TVRAGE.getValue());
   }
  
  	@Test
  	public void tmdbTest(){
     	Assert.assertEquals(3, GuideSource.TMDB.getValue());
   }
  
  	@Test
  	public void tvmazeTest(){
     	Assert.assertEquals(4, GuideSource.TVMAZE.getValue());
   }
}
