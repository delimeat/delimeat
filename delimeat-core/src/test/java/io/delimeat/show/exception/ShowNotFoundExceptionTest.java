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
package io.delimeat.show.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.delimeat.show.exception.ShowNotFoundException;

public class ShowNotFoundExceptionTest {

	@Test
	public void constructorTest(){
		ShowNotFoundException ex = new ShowNotFoundException(Long.MAX_VALUE);
		Assertions.assertEquals(Long.MAX_VALUE, ex.getShowId().longValue());
	}
}
