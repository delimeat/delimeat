package io.delimeat.client.util.jaxrs;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import io.delimeat.client.domain.ErrorDto;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable>{
   
  	 /* (non-Javadoc)
  	 * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
  	 */
  	@Override
    public Response toResponse(Throwable exception) {
    	 return Response.status(500)
         					.entity(new ErrorDto(500, exception.getMessage()))
         					.type(MediaType.APPLICATION_JSON_TYPE)
         					.build();
    }
  
}
