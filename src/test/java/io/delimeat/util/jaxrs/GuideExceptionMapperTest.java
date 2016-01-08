package io.delimeat.util.jaxrs;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.core.guide.GuideException;
import io.delimeat.core.guide.GuideNotAuthorisedException;
import io.delimeat.core.guide.GuideNotFoundException;

import javax.ws.rs.core.Response;

public class GuideExceptionMapperTest {
  
  private GuideExceptionMapper mapper;
  
  @Before
  public void setUp(){
    mapper = new GuideExceptionMapper();
  }
  
  @Test
  public void guideExceptionTest(){
    Response response = mapper.toResponse(new GuideException("THIS IS A TEST"));
    Assert.assertNotNull(response);
    Assert.assertEquals(500,response.getStatus());
    Assert.assertEquals(JaxrsError.class, response.getEntity().getClass());
    JaxrsError error = (JaxrsError)response.getEntity();
    Assert.assertEquals(500,error.getStatus());
    Assert.assertEquals("THIS IS A TEST",error.getError());
  }
  
  @Test
  public void guideNotAuthorisedExceptionTest(){
    Response response = mapper.toResponse(new GuideNotAuthorisedException("THIS IS A TEST"));
    Assert.assertNotNull(response);
    Assert.assertEquals(500,response.getStatus());
    Assert.assertEquals(JaxrsError.class, response.getEntity().getClass());
    JaxrsError error = (JaxrsError)response.getEntity();
    Assert.assertEquals(500,error.getStatus());
    Assert.assertEquals("THIS IS A TEST",error.getError());
  }
  
  @Test
  public void guideNotFoundExceptionTest(){
    Response response = mapper.toResponse(new GuideNotFoundException("THIS IS A TEST"));
    Assert.assertNotNull(response);
    Assert.assertEquals(404,response.getStatus());
    Assert.assertEquals(JaxrsError.class, response.getEntity().getClass());
    JaxrsError error = (JaxrsError)response.getEntity();
    Assert.assertEquals(404,error.getStatus());
    Assert.assertEquals("THIS IS A TEST",error.getError());
  }
  
}
