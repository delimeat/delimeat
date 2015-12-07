package io.delimeat.util.jaxrs;

import io.delimeat.core.service.exception.GuideNotFoundException;
import io.delimeat.rest.DelimeatRestError;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class GuideNotFoundExceptionMapperTest {

	private GuideNotFoundExceptionMapper mapper;
	
	@Before
	public void setUp() throws Exception {
		mapper = new GuideNotFoundExceptionMapper();
	}
	
	@Test
	public void toResponseTest(){
		GuideNotFoundException mockedException = Mockito.mock(GuideNotFoundException.class);
		Mockito.when(mockedException.getLocalizedMessage()).thenReturn("MESSAGE");
		
		Response actualResponse = mapper.toResponse(mockedException);
		Assert.assertEquals(404, actualResponse.getStatus());
		Assert.assertEquals(DelimeatRestError.class, actualResponse.getEntity().getClass());
		Assert.assertEquals("MESSAGE", ((DelimeatRestError)actualResponse.getEntity()).getMessage());
	}

}
