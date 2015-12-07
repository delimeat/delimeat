package io.delimeat.core.service.exception;

import io.delimeat.core.service.exception.ServiceException;

import org.junit.Assert;
import org.junit.Test;

public class ServiceExceptionTest {

	private ServiceException exception;
	
	@Test
	public void constructorTest(){
		exception = new ServiceException("MESSAGE");
		Assert.assertEquals("MESSAGE", exception.getLocalizedMessage());
	}

}
