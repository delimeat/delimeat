package io.delimeat.core.service;

import io.delimeat.core.guide.GuideException;
import io.delimeat.core.show.Episode;
import io.delimeat.core.show.Show;
import io.delimeat.core.show.ShowConcurrencyException;
import io.delimeat.core.show.ShowException;
import io.delimeat.core.show.ShowNotFoundException;

import java.util.List;

public interface ShowService {

	public Show create(Show show) throws GuideException, ShowConcurrencyException, ShowException;

	public Show read(Long id) throws ShowNotFoundException, ShowConcurrencyException, ShowException;

	public List<Show> readAll() throws ShowException;

	public Show update(Show show) throws ShowConcurrencyException, ShowException;

	public void delete(Long id) throws ShowNotFoundException, ShowException;
	
	public List<Episode> readAllEpisodes(Long id)  throws ShowNotFoundException, ShowException;
	
}
