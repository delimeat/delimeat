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
package io.delimeat.client.domain;

import org.junit.Assert;
import org.junit.Test;

import io.delimeat.client.domain.ErrorDto;

public class ErrorDTOTest {

    @Test
    public void nullConstructorTest() {
        ErrorDto error = new ErrorDto();
        Assert.assertNull(error.getError());
        Assert.assertEquals(0, error.getStatus());
        error.setError("THIS IS AN ERROR");
        error.setStatus(500);
        Assert.assertEquals(500, error.getStatus());
        Assert.assertEquals("THIS IS AN ERROR", error.getError());

    }

    @Test
    public void constructorTest() {
        ErrorDto error = new ErrorDto(500, "THIS IS AN ERROR");
        Assert.assertEquals(500, error.getStatus());
        Assert.assertEquals("THIS IS AN ERROR", error.getError());
    }

    @Test
    public void toStringTest() {
        ErrorDto error = new ErrorDto(500, "THIS IS AN ERROR");
        Assert.assertEquals("Status: 500 Error: THIS IS AN ERROR", error.toString());

    }
}
