package io.delimeat.util.jaxrs;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable>{
	
  	 private static final int STATUS = 500;
  	 private static final MediaType TYPE = MediaType.APPLICATION_JSON_TYPE;
   
  	 @Override
    public Response toResponse(Throwable exception) {
    	 return Response.status(STATUS)
         					.entity(new JaxrsError(STATUS,exception.getMessage()))
         					.type(TYPE)
         					.build();
    }
  
}
