package io.delimeat.rest.util.jaxrs;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import com.google.common.base.Strings;

@ETag
@Priority(Priorities.HEADER_DECORATOR)
public class AddETagResponseFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) throws IOException {
      if (responseContext.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
        return;
      }

      final String etagStr = responseContext.getHeaderString(HttpHeaders.ETAG);
      final Object entity = responseContext.getEntity();
      if( entity != null && Strings.isNullOrEmpty(etagStr) == true){
        EntityTag eTag = new EntityTag(Integer.toString(entity.hashCode()));
        responseContext.getHeaders().add(HttpHeaders.ETAG, eTag);  
      } 

    }
}
