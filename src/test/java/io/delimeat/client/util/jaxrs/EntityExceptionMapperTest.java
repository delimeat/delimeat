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
package io.delimeat.client.util.jaxrs;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.client.domain.ErrorDto;
import io.delimeat.guide.exception.GuideConcurrencyException;
import io.delimeat.guide.exception.GuideException;
import io.delimeat.guide.exception.GuideNotFoundException;

public class EntityExceptionMapperTest {
  
  private EntityExceptionMapper mapper;
  
  @Before
  public void setUp(){
    mapper = new EntityExceptionMapper();
  }
  
  @Test
  public void showExceptionTest(){
    Response response = mapper.toResponse(new GuideException("THIS IS A TEST"));
    Assert.assertNotNull(response);
    Assert.assertEquals(500,response.getStatus());
    Assert.assertEquals(ErrorDto.class, response.getEntity().getClass());
    ErrorDto error = (ErrorDto)response.getEntity();
    Assert.assertEquals(500,error.getStatus());
    Assert.assertEquals("THIS IS A TEST",error.getError());
  }
  
  @Test
  public void showNotFoundExceptionTest(){
    Response response = mapper.toResponse(new GuideNotFoundException("THIS IS A TEST"));
    Assert.assertNotNull(response);
    Assert.assertEquals(404,response.getStatus());
    Assert.assertEquals(ErrorDto.class, response.getEntity().getClass());
    ErrorDto error = (ErrorDto)response.getEntity();
    Assert.assertEquals(404,error.getStatus());
    Assert.assertEquals("THIS IS A TEST",error.getError());
  }
  
  @Test
  public void showConcurrencyExceptionTest(){
    Response response = mapper.toResponse(new GuideConcurrencyException("THIS IS A TEST"));
    Assert.assertNotNull(response);
    Assert.assertEquals(409,response.getStatus());
    Assert.assertEquals(ErrorDto.class, response.getEntity().getClass());
    ErrorDto error = (ErrorDto)response.getEntity();
    Assert.assertEquals(409,error.getStatus());
    Assert.assertEquals("THIS IS A TEST",error.getError());
  }
  
}
