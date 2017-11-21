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
package io.delimeat.rest.util;

import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.delimeat.api.util.ApiError;
import io.delimeat.api.util.JsonUtils;

public class JsonUtilsTest {

	@Test
	public void toJsonNullTest() throws JAXBException{
		Assertions.assertNull(JsonUtils.toJson(null));
	}
	
	@Test
	public void toJsonEmptyListTest() throws JAXBException{
		Assertions.assertEquals("[]",JsonUtils.toJson(new ArrayList<>()));
	}
	
	@Test
	public void toJsonTest() throws JAXBException{
		ApiError apiError = new ApiError("test",500);
		Assertions.assertEquals("{\"error\":\"test\",\"status\":500}", JsonUtils.toJson(apiError));
	}
	
	@Test
	public void fromJsonTest() throws JAXBException{
		ApiError apiError = JsonUtils.fromJson("{\"error\":\"test\",\"status\":500}".getBytes(), ApiError.class);
		Assertions.assertEquals(new ApiError("test",500), apiError);
	}
}
