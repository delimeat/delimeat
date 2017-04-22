package io.delimeat.show;

import java.util.List;

import io.delimeat.show.domain.Episode;
import io.delimeat.show.domain.Show;

public interface EpisodeService {

	public Episode read(Long episodeId);

	public Episode save(Episode episode);

	public void delete(Long episodeId);
	
	public List<Episode> findAllPending();
	
	public List<Episode> findByShow(Show show);


}
