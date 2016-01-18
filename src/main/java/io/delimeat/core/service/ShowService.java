package io.delimeat.core.service;

import io.delimeat.core.show.Show;
import io.delimeat.core.show.ShowConcurrencyException;
import io.delimeat.core.show.ShowException;
import io.delimeat.core.show.ShowNotFoundException;

import java.util.List;

import javax.transaction.Transactional;

public interface ShowService {

   @Transactional
	public Show create(Show show) throws ShowConcurrencyException, ShowException;

   @Transactional
	public Show read(Long id) throws ShowNotFoundException, ShowConcurrencyException, ShowException;

   @Transactional
	public List<Show> readAll() throws ShowException;

   @Transactional
	public Show update(Show show) throws ShowConcurrencyException, ShowException;

  	@Transactional
	public void delete(Long id) throws ShowNotFoundException, ShowException;
	
}
