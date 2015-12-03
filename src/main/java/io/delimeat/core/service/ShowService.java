package io.delimeat.core.service;

import io.delimeat.core.show.Show;

import java.util.List;

public interface ShowService {

	public Show create(Show show) throws Exception;

	public Show read(Long id) throws Exception;

	public List<Show> readAll() throws Exception;

	public Show update(Show show) throws Exception;

	public void delete(Long id) throws Exception;

}
