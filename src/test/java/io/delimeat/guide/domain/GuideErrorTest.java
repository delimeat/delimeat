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

import io.delimeat.guide.domain.GuideError;

public class GuideErrorTest {
  
	private GuideError error;

	@Before
	public void setUp() {
		error = new GuideError();
	}

	@Test
	public void messageTest() {
		Assert.assertNull(error.getMessage());
		error.setMessage("MESSAGE");
		Assert.assertEquals("MESSAGE", error.getMessage());
	}
	
	@Test
	public void toStringTest(){
		error.setMessage("MESSAGE");
		Assert.assertEquals("GuideError(message=MESSAGE)", error.toString());
	}
}
