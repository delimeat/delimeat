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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.delimeat.guide.domain.GuideEpisode;
import io.delimeat.guide.domain.GuideInfo;
import io.delimeat.guide.domain.GuideSearchResult;

@RestController
@RequestMapping(path = "/api")
public class GuideController {
	
	@Autowired
	private GuideService service;

	@RequestMapping(value = "/guide/search/{title}", method = RequestMethod.GET, produces = "application/json")
	public List<GuideSearchResult> get(@PathVariable(value="title", required=true) String title) throws Exception {
		return service.readLike(title);
	}

	@RequestMapping(value = "/guide/info/{id}", method = RequestMethod.GET, produces = "application/json")
	public GuideInfo getInfo(@PathVariable(value="id", required=true) String guideId) throws Exception {
		return service.read(guideId);
	}

	@RequestMapping(value = "/guide/info/{id}/episodes", method = RequestMethod.GET, produces = "application/json")
	public List<GuideEpisode> getEpisodes(@PathVariable(value="id", required=true) String guideId) throws Exception{
		return service.readEpisodes(guideId);
	}

}
