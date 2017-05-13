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

import io.delimeat.guide.domain.GuideEpisode;
import io.delimeat.guide.domain.GuideInfo;
import io.delimeat.guide.domain.GuideSearchResult;

@Service
public class GuideService_Impl implements GuideService {

	@Autowired
	private GuideDataSource guideDataSource;

	public GuideDataSource getGuideDataSource() {
		return guideDataSource;
	}

	public void setGuideDataSource(GuideDataSource guideDataSource) {
		this.guideDataSource = guideDataSource;
	}

	@Override
	public List<GuideSearchResult> readLike(final String title) throws Exception {
		return guideDataSource.search(title);
	}

	@Override
	public GuideInfo read(final String guideId) throws Exception {
		return guideDataSource.info(guideId);
	}

	@Override
	public List<GuideEpisode> readEpisodes(final String guideId) throws Exception {
		return guideDataSource.episodes(guideId).stream()
				.filter(p -> (p.getSeasonNum() != null && p.getSeasonNum() != 0 && p.getAirDate() != null))
				.sorted()
				.collect(Collectors.toList());
	}


}
