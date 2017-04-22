package io.delimeat.guide.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.guide.domain.TvdbApiKey;

public class TvdbApiKeyTest {

	private TvdbApiKey apiKey;

	@Before
	public void setUp() {
		apiKey = new TvdbApiKey();
	}

	@Test
	public void valueTest() {
		Assert.assertNull(apiKey.getValue());
		apiKey.setValue("VALUE");
		Assert.assertEquals("VALUE", apiKey.getValue());
	}
  
  	@Test
  	public void hashCodeTest(){
     	apiKey.setValue("VALUE");
     	Assert.assertEquals(81434992, apiKey.hashCode());
   }
   
  	@Test
  	public void toStringTest(){
     	apiKey.setValue("VALUE");
     	Assert.assertEquals("TvdbApiKey{value=VALUE}", apiKey.toString());
   }
}
