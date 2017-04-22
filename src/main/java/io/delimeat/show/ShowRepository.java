package io.delimeat.show;

import java.util.List;

import org.springframework.data.repository.Repository;

import io.delimeat.show.domain.Show;


public interface ShowRepository extends Repository<Show, Long> {

	public Show findOne(Long id);
	
	public Show save(Show show);
	
	public void delete(Long id);
	
	public List<Show> findAll();
}
