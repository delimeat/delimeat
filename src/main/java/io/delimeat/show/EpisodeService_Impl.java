package io.delimeat.show;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.delimeat.show.domain.Episode;
import io.delimeat.show.domain.EpisodeStatus;
import io.delimeat.show.domain.Show;

@Service("episodeServiceId")
public class EpisodeService_Impl implements EpisodeService {

	@Autowired
	private EpisodeRepository episodeRepository;
	
	/**
	 * @return the episodeRepository
	 */
	public EpisodeRepository getEpisodeRepository() {
		return episodeRepository;
	}

	/**
	 * @param episodeRepository the episodeRepository to set
	 */
	public void setEpisodeRepository(EpisodeRepository episodeRepository) {
		this.episodeRepository = episodeRepository;
	}

	/* (non-Javadoc)
	 * @see io.delimeat.show.EpisodeService#read(java.lang.Long)
	 */
	@Override
	public Episode read(Long episodeId) {
		return episodeRepository.findOne(episodeId);
	}

	/* (non-Javadoc)
	 * @see io.delimeat.show.EpisodeService#save(io.delimeat.show.domain.Episode)
	 */
	@Override
	public Episode save(Episode episode) {
		return episodeRepository.save(episode);
	}

	/* (non-Javadoc)
	 * @see io.delimeat.show.EpisodeService#delete(java.lang.Long)
	 */
	@Override
	public void delete(Long episodeId) {
		episodeRepository.delete(episodeId);	
	}

	/* (non-Javadoc)
	 * @see io.delimeat.show.EpisodeService#findAllPending()
	 */
	@Override
	public List<Episode> findAllPending() {
		return episodeRepository.findByStatusIn(Arrays.asList(EpisodeStatus.PENDING));
	}

	@Override
	public List<Episode> findByShow(Show show) {
		return episodeRepository.findByShow(show);
	}



}
