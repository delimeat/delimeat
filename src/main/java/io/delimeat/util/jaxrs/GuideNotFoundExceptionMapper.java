package io.delimeat.util.jaxrs;

import io.delimeat.core.service.exception.GuideNotFoundException;
import io.delimeat.rest.DelimeatRestError;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GuideNotFoundExceptionMapper implements
		ExceptionMapper<GuideNotFoundException> {

	@Override
	public Response toResponse(GuideNotFoundException exception) {
		DelimeatRestError error = new DelimeatRestError();
		error.setMessage(exception.getLocalizedMessage());
		return Response.status(Status.NOT_FOUND).entity(error).build();
	}

}
