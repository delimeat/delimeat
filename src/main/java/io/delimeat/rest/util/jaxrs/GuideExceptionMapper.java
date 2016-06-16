package io.delimeat.rest.util.jaxrs;

import io.delimeat.core.guide.GuideException;
import io.delimeat.core.guide.GuideNotFoundException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GuideExceptionMapper implements ExceptionMapper<GuideException> {
	
  	 private static final MediaType TYPE = MediaType.APPLICATION_JSON_TYPE;
  
    @Override
    public Response toResponse(GuideException exception) {
      int status = 500;
      if(exception instanceof GuideNotFoundException){
			status = 404;
      }
      return Response.status(status)
        					.entity(new JaxrsError(status,exception.getMessage()))
        					.type(TYPE)
        					.build();
    }
}
