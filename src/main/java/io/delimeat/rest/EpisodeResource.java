package io.delimeat.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import io.delimeat.core.service.EpisodeService;
import io.delimeat.rest.domain.EpisodeDTO;
import io.delimeat.rest.util.jaxrs.ETag;
import io.delimeat.util.EntityException;

@Path("episodes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EpisodeResource {

	@Inject
	private EpisodeService service;

	/**
	 * @return the service
	 */
	public EpisodeService getService() {
		return service;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(EpisodeService service) {
		this.service = service;
	}

	/**
	 * Get all episodes
	 * 
	 * @param page
	 * @param pageSize
	 * @param includeAll
	 * @return episodes
	 * @throws EntityException 
	 */
	@GET
	@ETag
	@Transactional
	public List<EpisodeDTO> getAll(@QueryParam("page") @DefaultValue("1") int page,@QueryParam("size") @DefaultValue("50") int pageSize, @QueryParam("all") @DefaultValue("false") boolean includeAll) throws EntityException{
		return service.readAll(page, pageSize, includeAll)
				.getResults()
				.stream()
				.map(EpisodeDTO::new)
				.collect(Collectors.toList());
	}
	
}
