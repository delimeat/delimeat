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
package io.delimeat.guide.exception;

import org.junit.Assert;
import org.junit.Test;

public class GuideExceptionTest {

	@Test
	public void messageConstructorTest(){
		GuideException ex = new GuideException("MESSAGE");
		
		Assert.assertEquals("MESSAGE", ex.getMessage());
		Assert.assertNull(ex.getCause());
	}
	
	@Test
	public void causeConstructorTest(){
		GuideException ex = new GuideException(new Exception());

		Assert.assertEquals("java.lang.Exception", ex.getMessage());
		Assert.assertEquals(Exception.class, ex.getCause().getClass());
	}
	
	@Test
	public void messageAndCauseConstructorTest(){
		GuideException ex = new GuideException("MESSAGE", new Exception());
		
		Assert.assertEquals("MESSAGE", ex.getMessage());
		Assert.assertEquals(Exception.class, ex.getCause().getClass());
	}
}
