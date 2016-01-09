package io.delimeat.util.jaxrs;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;
import javax.persistence.EntityNotFoundException;

@Provider
public class PersistenceExceptionMapper implements ExceptionMapper<PersistenceException> {

    private static final MediaType TYPE = MediaType.APPLICATION_JSON_TYPE;
  
    @Override
    public Response toResponse(PersistenceException exception) {
      int status = 500;
      if(exception instanceof EntityNotFoundException){
			status = 404;
      }
      else if(exception instanceof OptimisticLockException){
    	  status = 409;
      }
      return Response.status(status)
        					.entity(new JaxrsError(status,exception.getMessage()))
        					.type(TYPE)
        					.build();
    }
}
