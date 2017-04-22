package io.delimeat.show;

import java.util.List;

import org.springframework.data.repository.Repository;

import io.delimeat.show.domain.Episode;
import io.delimeat.show.domain.EpisodeStatus;
import io.delimeat.show.domain.Show;

public interface EpisodeRepository extends Repository<Episode, Long> {

	public Episode findOne(Long id);
	
	public Episode save(Episode episode);
	
	public void delete(Long id);
	
	public List<Episode> findByStatusIn(List<EpisodeStatus> statuses);
	
	public List<Episode> findByShow(Show show);
}
