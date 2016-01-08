package io.delimeat.util.jaxrs;

import org.junit.Assert;
import org.junit.Test;

public class JaxrsErrorTest {
  
  @Test
  public void constructorTest(){
    JaxrsError error = new JaxrsError(500,"THIS IS AN ERROR");
    Assert.assertEquals(500, error.getStatus());
    Assert.assertEquals("THIS IS AN ERROR", error.getError());
  }
  
  @Test
  public void toStringTest(){
    JaxrsError error = new JaxrsError(500,"THIS IS AN ERROR");
    Assert.assertEquals("Status: 500 Error: THIS IS AN ERROR", error.toString());

  }
}
