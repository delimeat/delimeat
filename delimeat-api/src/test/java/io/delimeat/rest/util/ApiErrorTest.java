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

import org.junit.Assert;
import org.junit.Test;

import io.delimeat.api.util.ApiError;

public class ApiErrorTest {

    @Test
    public void nullConstructorTest() {
        ApiError error = new ApiError();
        Assert.assertNull(error.getError());
        Assert.assertEquals(0, error.getStatus());
        error.setError("THIS IS AN ERROR");
        error.setStatus(500);
        Assert.assertEquals(500, error.getStatus());
        Assert.assertEquals("THIS IS AN ERROR", error.getError());
    }

    @Test
    public void constructorTest() {
        ApiError error = new ApiError("THIS IS AN ERROR",500);
        Assert.assertEquals(500, error.getStatus());
        Assert.assertEquals("THIS IS AN ERROR", error.getError());
    }

    @Test
    public void toStringTest() {
        ApiError error = new ApiError("THIS IS AN ERROR",500);
        Assert.assertEquals("ApiError [error=THIS IS AN ERROR, status=500]", error.toString());

    }
    
  	@Test
  	public void hashCodeTest(){
        ApiError error = new ApiError();
        error.setError("THIS IS AN ERROR");
        error.setStatus(500);
     	Assert.assertEquals(-138041620, error.hashCode());
   }
  	

	@Test
	public void equalsTest(){
        ApiError error = new ApiError("THIS IS AN ERROR",500);
        ApiError other = new ApiError("THIS IS AN ERROR",500);
		Assert.assertTrue(error.equals(other));
	}
	
	@Test
	public void equalsSelfTest(){
        ApiError error = new ApiError("THIS IS AN ERROR",500);
		Assert.assertTrue(error.equals(error));
	}
	
	@Test
	public void equalsNullTest(){
        ApiError error = new ApiError("THIS IS AN ERROR",500);
		Assert.assertFalse(error.equals(null));
	}
	
	@Test
	public void equalsOtherClassTest(){
        ApiError error = new ApiError("THIS IS AN ERROR",500);
		Assert.assertFalse(error.equals("STRING"));
	}
	
	@Test
	public void equalsNullOtherNullTest(){
        ApiError error = new ApiError();
        ApiError other = new ApiError();
		Assert.assertTrue(error.equals(other));
	}
	
	@Test
	public void equalsNullErrorOtherNotNullErrorTest(){
        ApiError error = new ApiError(null,500);
        ApiError other = new ApiError("THIS IS AN ERROR",500);
		Assert.assertFalse(error.equals(other));
	}
	
	@Test
	public void equalsNotNullErrorOtherNullErrorTest(){
        ApiError error = new ApiError("THIS IS AN ERROR",500);
        ApiError other = new ApiError(null,500);
		Assert.assertFalse(error.equals(other));
	}
	
	@Test
	public void equalsStatusNotEqualTest(){
        ApiError error = new ApiError("THIS IS AN ERROR",500);
        ApiError other = new ApiError("THIS IS AN ERROR",400);
		Assert.assertFalse(error.equals(other));
	}
}
