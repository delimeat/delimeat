package io.delimeat.client.util.jaxrs;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import io.delimeat.client.domain.ErrorDto;
import io.delimeat.common.util.exception.EntityConcurrencyException;
import io.delimeat.common.util.exception.EntityException;
import io.delimeat.common.util.exception.EntityNotFoundException;

public class EntityExceptionMapper implements ExceptionMapper<EntityException>{

    /* (non-Javadoc)
     * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
     */
    @Override
    public Response toResponse(EntityException exception) {
      int status = 500;
      if(exception instanceof EntityNotFoundException){
			status = 404;
      }else if(exception instanceof EntityConcurrencyException){
    	  status = 409;
      }
      
      return Response.status(status)
        					.entity(new ErrorDto(status,exception.getMessage()))
        					.type(MediaType.APPLICATION_JSON_TYPE)
        					.build();
    }

}
