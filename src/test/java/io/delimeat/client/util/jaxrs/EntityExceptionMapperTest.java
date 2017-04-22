package io.delimeat.client.util.jaxrs;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.client.domain.ErrorDto;
import io.delimeat.common.util.exception.EntityConcurrencyException;
import io.delimeat.common.util.exception.EntityException;
import io.delimeat.common.util.exception.EntityNotFoundException;

public class EntityExceptionMapperTest {
  
  private EntityExceptionMapper mapper;
  
  @Before
  public void setUp(){
    mapper = new EntityExceptionMapper();
  }
  
  @Test
  public void showExceptionTest(){
    Response response = mapper.toResponse(new EntityException("THIS IS A TEST"));
    Assert.assertNotNull(response);
    Assert.assertEquals(500,response.getStatus());
    Assert.assertEquals(ErrorDto.class, response.getEntity().getClass());
    ErrorDto error = (ErrorDto)response.getEntity();
    Assert.assertEquals(500,error.getStatus());
    Assert.assertEquals("THIS IS A TEST",error.getError());
  }
  
  @Test
  public void showNotFoundExceptionTest(){
    Response response = mapper.toResponse(new EntityNotFoundException("THIS IS A TEST"));
    Assert.assertNotNull(response);
    Assert.assertEquals(404,response.getStatus());
    Assert.assertEquals(ErrorDto.class, response.getEntity().getClass());
    ErrorDto error = (ErrorDto)response.getEntity();
    Assert.assertEquals(404,error.getStatus());
    Assert.assertEquals("THIS IS A TEST",error.getError());
  }
  
  @Test
  public void showConcurrencyExceptionTest(){
    Response response = mapper.toResponse(new EntityConcurrencyException("THIS IS A TEST"));
    Assert.assertNotNull(response);
    Assert.assertEquals(409,response.getStatus());
    Assert.assertEquals(ErrorDto.class, response.getEntity().getClass());
    ErrorDto error = (ErrorDto)response.getEntity();
    Assert.assertEquals(409,error.getStatus());
    Assert.assertEquals("THIS IS A TEST",error.getError());
  }
  
}
