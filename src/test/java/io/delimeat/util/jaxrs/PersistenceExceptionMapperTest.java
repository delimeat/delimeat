package io.delimeat.util.jaxrs;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;

import javax.ws.rs.core.Response;

public class PersistenceExceptionMapperTest {
  
  private PersistenceExceptionMapper mapper;
  
  @Before
  public void setUp(){
    mapper = new PersistenceExceptionMapper();
  }
  
  @Test
  public void persistenceExceptionTest(){
    Response response = mapper.toResponse(new PersistenceException("THIS IS A TEST"));
    Assert.assertNotNull(response);
    Assert.assertEquals(500,response.getStatus());
    Assert.assertEquals(JaxrsError.class, response.getEntity().getClass());
    JaxrsError error = (JaxrsError)response.getEntity();
    Assert.assertEquals(500,error.getStatus());
    Assert.assertEquals("THIS IS A TEST",error.getError());
  }
  
  @Test
  public void entityNotFoundExceptionTest(){
    Response response = mapper.toResponse(new EntityNotFoundException("THIS IS A TEST"));
    Assert.assertNotNull(response);
    Assert.assertEquals(404,response.getStatus());
    Assert.assertEquals(JaxrsError.class, response.getEntity().getClass());
    JaxrsError error = (JaxrsError)response.getEntity();
    Assert.assertEquals(404,error.getStatus());
    Assert.assertEquals("THIS IS A TEST",error.getError());
  }
  
  
  
}
