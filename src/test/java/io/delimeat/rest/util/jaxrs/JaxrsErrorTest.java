package io.delimeat.rest.util.jaxrs;

import org.junit.Assert;
import org.junit.Test;

public class JaxrsErrorTest {

    @Test
    public void nullConstructorTest() {
        JaxrsError error = new JaxrsError();
        Assert.assertNull(error.getError());
        Assert.assertEquals(0, error.getStatus());
        error.setError("THIS IS AN ERROR");
        error.setStatus(500);
        Assert.assertEquals(500, error.getStatus());
        Assert.assertEquals("THIS IS AN ERROR", error.getError());

    }

    @Test
    public void constructorTest() {
        JaxrsError error = new JaxrsError(500, "THIS IS AN ERROR");
        Assert.assertEquals(500, error.getStatus());
        Assert.assertEquals("THIS IS AN ERROR", error.getError());
    }

    @Test
    public void toStringTest() {
        JaxrsError error = new JaxrsError(500, "THIS IS AN ERROR");
        Assert.assertEquals("Status: 500 Error: THIS IS AN ERROR", error.toString());

    }
}
