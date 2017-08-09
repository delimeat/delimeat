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

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.guide.domain.GuideSearch;
import io.delimeat.guide.domain.GuideSearchResult;

public class GuideSearchTest {

	private GuideSearch search;

	@Before
	public void setUp() throws Exception {
		search = new GuideSearch();
	}

	@Test
	public void resultsTest() {
		Assert.assertNotNull(search.getResults());
		Assert.assertEquals(0, search.getResults().size());
		GuideSearchResult result = new GuideSearchResult();
		search.setResults(Arrays.asList(result));
		Assert.assertEquals(1, search.getResults().size());
		Assert.assertEquals(result, search.getResults().get(0));
	}
  
  	@Test
  	public void hashCodeTest(){
   	search.getResults().add(new GuideSearchResult());
		Assert.assertEquals(923583,search.hashCode());
   }
  
  	@Test
  	public void toStringTest(){
		search.getResults().add(new GuideSearchResult());
     	Assert.assertEquals("GuideSearch [results=[GuideSearchResult []]]",search.toString());
   }

  	
	@Test
	public void equalsTest(){
		search.setResults(Arrays.asList(new GuideSearchResult()));
		GuideSearch other = new GuideSearch();
		other.setResults(Arrays.asList(new GuideSearchResult()));
		Assert.assertTrue(search.equals(other));
	}
	
	@Test
	public void equalsSelfTest(){
		Assert.assertTrue(search.equals(search));
	}
	
	@Test
	public void equalsNullTest(){
		Assert.assertFalse(search.equals(null));
	}
	
	@Test
	public void equalsOtherClassTest(){
		Assert.assertFalse(search.equals("STRING"));
	}
	
	@Test
	public void equalsNullOtherNullTest(){
		GuideSearch other = new GuideSearch();
		Assert.assertTrue(search.equals(other));
	}
	
	@Test
	public void equalsNullOtherNotNullTest(){
		GuideSearch other = new GuideSearch();
		other.setResults(Arrays.asList(new GuideSearchResult()));
		Assert.assertFalse(search.equals(other));
	}
	
	@Test
	public void equalsNotNullOtherNullTest(){
		search.setResults(Arrays.asList(new GuideSearchResult()));
		GuideSearch other = new GuideSearch();
		Assert.assertFalse(search.equals(other));
	}
}
