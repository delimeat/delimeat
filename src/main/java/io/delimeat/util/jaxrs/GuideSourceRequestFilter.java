package io.delimeat.util.jaxrs;

import io.delimeat.core.guide.GuideSource;
import io.delimeat.rest.DelimeatRestError;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;


public class GuideSourceRequestFilter implements ContainerRequestFilter {

	private static final String KEY = "source";
	
	@Override
	public void filter(ContainerRequestContext context) throws IOException {
		if(context.getUriInfo().getPathParameters().containsKey(KEY)){
			String source = context.getUriInfo().getPathParameters().get(KEY).get(0).toUpperCase();
			try{
				GuideSource.valueOf(source);
			}catch(IllegalArgumentException e){
				DelimeatRestError error = new DelimeatRestError();
				//TODO i18n
				error.setMessage("Invalid source " + source);
				context.abortWith(Response.status(404).entity(error).build());
			}
			context.getUriInfo().getPathParameters().get(KEY).remove(0);
			context.getUriInfo().getPathParameters().get(KEY).add(source);
		}
		
	}

}
