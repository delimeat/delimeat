package io.delimeat.core.service.exception;

import io.delimeat.core.guide.GuideSource;

import org.junit.Assert;
import org.junit.Test;

public class GuideNotFoundExceptionTest {

	
	@Test
	public void constructorTest(){
		GuideNotFoundException exception = new GuideNotFoundException(GuideSource.IMDB);
		Assert.assertEquals("GuideSource IMDB not found", exception.getLocalizedMessage());
	}

}
