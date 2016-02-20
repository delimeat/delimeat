package io.delimeat.core.guide;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TvdbTokenTest {

	private TvdbToken token;

	@Before
	public void setUp() {
		token = new TvdbToken();
	}

	@Test
	public void valueTest() {
		Assert.assertNull(token.getValue());
		Assert.assertNotEquals(0, token.getTime());
		token.setValue("VALUE");
		Assert.assertEquals("VALUE", token.getValue());
		Assert.assertNotEquals(0, token.getTime());
	}

   @Ignore("test without time?")
  	@Test
  	public void hashCodeTest(){
     	token.setValue("VALUE");
     	Assert.assertEquals(-1906294749, token.hashCode());
   }
   
  	@Ignore("test without time?")
  	@Test
  	public void toStringTest(){
     	token.setValue("VALUE");
     	Assert.assertEquals("TvdbToken{value=VALUE, time=1455858100917}", token.toString());
   }
}
