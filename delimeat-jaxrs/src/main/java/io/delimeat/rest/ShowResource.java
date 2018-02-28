/*
 * Copyright 2013-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.delimeat.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import io.delimeat.show.ShowService;
import io.delimeat.show.entity.Episode;
import io.delimeat.show.entity.Show;
import io.delimeat.show.exception.ShowException;
import io.delimeat.show.exception.ShowNotFoundException;

@Path("/show")
@Produces("application/json")
@Consumes("application/json")
public class ShowResource {

	@Inject ShowService service;
	
	@GET
	public List<Show> readAll() throws ShowException{
		return service.readAll();
	}
	
	@POST
	public Show create(Show show) throws ShowException {
		service.create(show);
		return show;
	}
	
	@GET
	@Path("/{id}")
	public Show read(@PathParam("id") Long id) throws ShowNotFoundException, ShowException {
		return service.read(id);
	}
	
	@PUT
	@Path("/{id}")
	public Show update(@PathParam("id") Long id, Show show) throws ShowNotFoundException, ShowException {
		return service.update(show);
	}
	
	@DELETE
	@Path("/{id}")
	public void delete(@PathParam("id") Long id) throws ShowException {
		service.delete(id);
	}
	
	@GET
	@Path("/{id}/episode")
	public List<Episode> episodes(@PathParam("id") Long id) throws ShowNotFoundException, ShowException{
		return service.readAllEpisodes(id);
	}	
}
