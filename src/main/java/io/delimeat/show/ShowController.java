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
package io.delimeat.show;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.delimeat.show.domain.Episode;
import io.delimeat.show.domain.Show;

@RestController
@RequestMapping(path = "/api")
public class ShowController {

	@Autowired
	private ShowService showService;
	
	@Autowired
	private EpisodeService episodeService;
	
	@RequestMapping(value = "/show", method = RequestMethod.GET, produces = "application/json")
	public List<Show> getAll() throws Exception {
		return showService.readAll();
	}

	@RequestMapping(value = "/show/{id}", method = RequestMethod.GET, produces = "application/json")
	public Show read(@PathVariable(value="id", required=true) Long id) throws Exception {
		return showService.read(id);
	}

	@RequestMapping(value = "/show/{id}", method = RequestMethod.PUT, produces = "application/json", consumes="application/json")
	public Show update(@PathVariable(value="id", required=true) Long id, @RequestBody Show show) throws Exception {
		return showService.update(show);
	}

	@RequestMapping(value = "/show/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable(value="id", required=true) Long id) throws Exception {
		showService.delete(id);
	}

	@RequestMapping(value = "/show", method = RequestMethod.POST, produces = "application/json", consumes="application/json")
	public Show create(@RequestBody Show show) throws Exception {
		showService.create(show);
		return show;
	}

	@RequestMapping(value = "/show/{id}/episodes", method = RequestMethod.GET, produces = "application/json")
	public List<Episode> getAllEpisodes(@PathVariable(value="id", required=true) Long id) throws Exception {
		return episodeService.findByShow(showService.read(id))
								.stream()
								.map(p->{p.setShow(null); return p;})
								.collect(Collectors.toList());
	}
	
}
