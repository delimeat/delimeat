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

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.delimeat.guide.GuideService;
import io.delimeat.show.domain.Episode;
import io.delimeat.show.domain.EpisodeStatus;
import io.delimeat.show.domain.Show;
import lombok.Getter;
import lombok.Setter;

@Service
@Getter
@Setter
public class ShowService_Impl implements ShowService {

	private static final String TITLE_REGEX = "(\\(\\d{4}\\))$|[^A-Za-z\\d\\s]";
	
	@Autowired
	private ShowRepository showRepository;
	
	@Autowired
	private EpisodeService episodeService;
	
	@Autowired
	private GuideService guideService;

	/* (non-Javadoc)
	 * @see io.delimeat.common.show.ShowService#create(io.delimeat.common.show.model.Show)
	 */
	@Override
	@Transactional
	public void create(Show show) throws Exception {

		getShowRepository().save(cleanTitle(show));
		final String guideId = show.getGuideId();

		if (guideId != null && guideId.length() > 0) {
			final List<Episode> episodes = guideService.readEpisodes(guideId).stream()
					.map(ShowUtils::fromGuideEpisode)
					.collect(Collectors.toList());
			
			Instant now = Instant.now();
			for(Episode episode: episodes){
				episode.setShow(show);
				
				Instant airDateTime = ShowUtils.determineAirTime(episode.getAirDate(), show.getAirTime(), show.getTimezone());
				if(airDateTime.isBefore(now)){
					episode.setStatus(EpisodeStatus.SKIPPED);
				}
				
				episodeService.save(episode);
			}

		}
	}

	/* (non-Javadoc)
	 * @see io.delimeat.common.show.ShowService#read(java.lang.Long)
	 */
	@Override
	@Transactional
	public Show read(Long id)  throws Exception  {
		return getShowRepository().findOne(id);
	}

	/* (non-Javadoc)
	 * @see io.delimeat.common.show.ShowService#readAll()
	 */
	@Override
	@Transactional
	public List<Show> readAll() throws Exception {
		return getShowRepository().findAll();
	}

	/* (non-Javadoc)
	 * @see io.delimeat.common.show.ShowService#update(io.delimeat.common.show.model.Show)
	 */
	@Override
	@Transactional
	public Show update(Show show)  throws Exception {
		return getShowRepository().save(show);
	}

	/* (non-Javadoc)
	 * @see io.delimeat.common.show.ShowService#delete(java.lang.Long)
	 */
	@Override
	@Transactional
	public void delete(Long id) throws Exception {
		getShowRepository().delete(id);
	}

	/* (non-Javadoc)
	 * @see io.delimeat.common.show.ShowService#readAllEpisodes(java.lang.Long)
	 */
	@Override
	@Transactional
	public List<Episode> readAllEpisodes(Long id) throws Exception {
		return getEpisodeService().findByShow(read(id));
	}
	
	
	/**
	 * Clean up a title remove any unwanted characters
	 * 
	 * @param show
	 * @return show
	 */
	public Show cleanTitle(Show show){
		final String originalTitle = show.getTitle() != null ? show.getTitle() : "";
		String cleanedTitle = originalTitle.replaceAll(TITLE_REGEX, "").trim();
		show.setTitle(cleanedTitle);
		return show;
	}

}
