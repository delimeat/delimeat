package io.delimeat.util.jaxrs;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.core.show.ShowConcurrencyException;
import io.delimeat.core.show.ShowException;
import io.delimeat.core.show.ShowNotFoundException;

import javax.ws.rs.core.Response;

public class ShowExceptionMapperTest {
  
  private ShowExceptionMapper mapper;
  
  @Before
  public void setUp(){
    mapper = new ShowExceptionMapper();
  }
  
  @Test
  public void showExceptionTest(){
    Response response = mapper.toResponse(new ShowException("THIS IS A TEST"));
    Assert.assertNotNull(response);
    Assert.assertEquals(500,response.getStatus());
    Assert.assertEquals(JaxrsError.class, response.getEntity().getClass());
    JaxrsError error = (JaxrsError)response.getEntity();
    Assert.assertEquals(500,error.getStatus());
    Assert.assertEquals("THIS IS A TEST",error.getError());
  }
  
  @Test
  public void showNotFoundExceptionTest(){
    Response response = mapper.toResponse(new ShowNotFoundException("THIS IS A TEST"));
    Assert.assertNotNull(response);
    Assert.assertEquals(404,response.getStatus());
    Assert.assertEquals(JaxrsError.class, response.getEntity().getClass());
    JaxrsError error = (JaxrsError)response.getEntity();
    Assert.assertEquals(404,error.getStatus());
    Assert.assertEquals("THIS IS A TEST",error.getError());
  }
  
  @Test
  public void showConcurrencyExceptionTest(){
    Response response = mapper.toResponse(new ShowConcurrencyException("THIS IS A TEST"));
    Assert.assertNotNull(response);
    Assert.assertEquals(409,response.getStatus());
    Assert.assertEquals(JaxrsError.class, response.getEntity().getClass());
    JaxrsError error = (JaxrsError)response.getEntity();
    Assert.assertEquals(409,error.getStatus());
    Assert.assertEquals("THIS IS A TEST",error.getError());
  }
  
}
