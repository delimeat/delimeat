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
package io.delimeat.guide;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.delimeat.guide.comparator.GuideEpisodeComparator;
import io.delimeat.guide.domain.GuideEpisode;
import io.delimeat.guide.domain.GuideInfo;
import io.delimeat.guide.domain.GuideSearchResult;
import io.delimeat.guide.exception.GuideAuthorizationException;
import io.delimeat.guide.exception.GuideException;
import io.delimeat.guide.exception.GuideNotFoundException;

@Service
public class GuideService_Impl implements GuideService {

	@Autowired
	private GuideDataSource guideDataSource;

	/**
	 * @return the guideDataSource
	 */
	public GuideDataSource getGuideDataSource() {
		return guideDataSource;
	}

	/**
	 * @param guideDataSource the guideDataSource to set
	 */
	public void setGuideDataSource(GuideDataSource guideDataSource) {
		this.guideDataSource = guideDataSource;
	}

	/* (non-Javadoc)
	 * @see io.delimeat.guide.GuideService#readLike(java.lang.String)
	 */
	@Override
	public List<GuideSearchResult> readLike(final String title) throws GuideNotFoundException,GuideAuthorizationException, GuideException {
		return guideDataSource.search(title);
	}

	/* (non-Javadoc)
	 * @see io.delimeat.guide.GuideService#read(java.lang.String)
	 */
	@Override
	public GuideInfo read(final String guideId) throws GuideNotFoundException,GuideAuthorizationException, GuideException {
		return guideDataSource.info(guideId);
	}

	/* (non-Javadoc)
	 * @see io.delimeat.guide.GuideService#readEpisodes(java.lang.String)
	 */
	@Override
	public List<GuideEpisode> readEpisodes(final String guideId) throws GuideNotFoundException,GuideAuthorizationException, GuideException {
		return guideDataSource.episodes(guideId).stream()
				.filter(p -> (p.getSeasonNum() != null && p.getSeasonNum() != 0 && p.getAirDate() != null))
				.sorted(new GuideEpisodeComparator())
				.collect(Collectors.toList());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GuideService_Impl [" + (guideDataSource != null ? "guideDataSource=" + guideDataSource : "") + "]";
	}

}
