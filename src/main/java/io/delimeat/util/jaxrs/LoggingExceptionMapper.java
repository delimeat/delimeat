package io.delimeat.util.jaxrs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

@Provider
public class LoggingExceptionMapper implements ExceptionMapper<Exception> {
	
  private static final String MESSAGE = "JAX-RS encountered an exception";
  private static final Log LOG = LogFactory.getLog(LoggingExceptionMapper.class);

  @Override
  public Response toResponse(Exception exception) {
    LOG.error(MESSAGE, exception);
    return Response.status(Status.INTERNAL_SERVER_ERROR).build();
  }

}
