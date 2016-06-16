package io.delimeat.rest.util.jaxrs;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

public class GenericExceptionMapperTest {

  private GenericExceptionMapper mapper;
  
  @Before
  public void setUp(){
    mapper = new GenericExceptionMapper();
  }
  
  @Test
  public void exceptionTest(){
    Response response = mapper.toResponse(new Exception("THIS IS A TEST"));
    Assert.assertNotNull(response);
    Assert.assertEquals(500,response.getStatus());
    Assert.assertEquals(JaxrsError.class, response.getEntity().getClass());
    JaxrsError error = (JaxrsError)response.getEntity();
    Assert.assertEquals(500,error.getStatus());
    Assert.assertEquals("THIS IS A TEST",error.getError());
  }
  
}
