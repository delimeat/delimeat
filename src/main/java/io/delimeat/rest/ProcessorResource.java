package io.delimeat.rest;

import io.delimeat.core.config.ConfigException;
import io.delimeat.core.service.ProcessorService;
import io.delimeat.core.show.ShowException;

import javax.inject.Inject;
import javax.ws.rs.HEAD;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("process")
@Produces(MediaType.APPLICATION_JSON)
public class ProcessorResource {

	@Inject
	private ProcessorService processorService;

	@Path("guide")
	@HEAD
	public void processAllGuide() throws ConfigException, ShowException {
		processorService.processAllGuideUpdates();
	}

	@Path("feed")
	@HEAD
	public void processAllFeed() throws ConfigException, ShowException {
		processorService.processAllFeedUpdates();
	}

}
