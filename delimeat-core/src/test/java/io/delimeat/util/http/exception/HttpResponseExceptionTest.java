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
package io.delimeat.util.http.exception;

import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

public class HttpResponseExceptionTest {

	@Test
	public void constuctorTest() throws Exception{
		HttpResponseException exception = new HttpResponseException(new URL("http://localhost"), 500, "MESSAGE");
		
		Assert.assertEquals(new URL("http://localhost"), exception.getUrl());
		Assert.assertEquals(500, exception.getCode());
		Assert.assertEquals("HTTP response code 500 with message \"MESSAGE\" for url http://localhost", exception.getMessage());
	}
}
