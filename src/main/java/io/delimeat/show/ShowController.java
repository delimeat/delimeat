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
