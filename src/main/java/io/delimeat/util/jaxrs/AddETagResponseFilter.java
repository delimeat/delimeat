package io.delimeat.util.jaxrs;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;

@ETag
@Priority(Priorities.HEADER_DECORATOR)
public class AddETagResponseFilter implements ContainerResponseFilter {

    private static final String ENTITY_TAG = "ETag";

    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) throws IOException {
      if (responseContext.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
        return;
      }

      final Object entity = responseContext.getEntity();
      if(entity != null){
        String eTag = "\"" + Integer.toString(entity.hashCode()) + "\"";
        responseContext.getHeaders().add(ENTITY_TAG, eTag);  
      } 

    }
}
