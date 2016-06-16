package io.delimeat.rest.util.jaxrs;

import io.delimeat.core.show.ShowConcurrencyException;
import io.delimeat.core.show.ShowException;
import io.delimeat.core.show.ShowNotFoundException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class ShowExceptionMapper implements ExceptionMapper<ShowException> {
  
	private static final MediaType TYPE = MediaType.APPLICATION_JSON_TYPE;

    @Override
    public Response toResponse(ShowException exception) {
      int status = 500;
      if(exception instanceof ShowNotFoundException){
			status = 404;
      }
      else if(exception instanceof ShowConcurrencyException){
    	  status = 409;
      }
      return Response.status(status)
        					.entity(new JaxrsError(status,exception.getMessage()))
        					.type(TYPE)
        					.build();
    }

}
