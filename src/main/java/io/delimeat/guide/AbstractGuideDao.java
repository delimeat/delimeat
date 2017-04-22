package io.delimeat.guide;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.GenericType;

import io.delimeat.common.util.exception.EntityAuthorizationException;
import io.delimeat.common.util.exception.EntityException;
import io.delimeat.common.util.exception.EntityNotFoundException;
import io.delimeat.guide.domain.GuideError;
import io.delimeat.guide.domain.GuideSource;
import io.delimeat.util.jaxrs.client.AbstractJaxrsClientHelper;

public abstract class AbstractGuideDao extends AbstractJaxrsClientHelper implements GuideDao {

	protected final GuideSource guideSource;

	AbstractGuideDao(GuideSource guideSource) {
		this.guideSource = guideSource;
	}

	@Override
	public GuideSource getGuideSource() {
		return guideSource;
	}
	
	public <T> T get(Builder builder, GenericType<T> returnType) throws EntityException{
		try {
			
			return builder.get(returnType);

		} catch (NotAuthorizedException ex) {
			GuideError error = ex.getResponse().readEntity(GuideError.class);
			throw new EntityAuthorizationException(error.getMessage(), ex);
		} catch (NotFoundException ex) {
			GuideError error = ex.getResponse().readEntity(GuideError.class);
			throw new EntityNotFoundException(error.getMessage(), ex);
		} catch (WebApplicationException ex) {
			throw new EntityException(ex);
		}
	}
	
	public <T,S> T put(Builder builder, Entity<S> entity, GenericType<T> returnType) throws EntityException{
        try {
            return builder.post(entity, returnType);
          
		} catch (NotAuthorizedException ex) {
			GuideError error = ex.getResponse().readEntity(GuideError.class);
			throw new EntityAuthorizationException(error.getMessage(), ex);
		} catch (NotFoundException ex) {
			GuideError error = ex.getResponse().readEntity(GuideError.class);
			throw new EntityAuthorizationException(error.getMessage(), ex);
		} catch (WebApplicationException ex) {
			throw new EntityException(ex);
		}
	}
}
