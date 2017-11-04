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
package io.delimeat.processor.validation;

import io.delimeat.config.entity.Config;
import io.delimeat.processor.entity.FeedProcessUnitRejection;
import io.delimeat.torrent.entity.Torrent;
import io.delimeat.show.entity.Show;

public interface TorrentValidator {

	/**
	 * Get Rejection Reason for Validator
	 * @return rejection reason
	 */
	public FeedProcessUnitRejection getRejection();
	
	/**
	 * Validate a torrent 
	 * @param torrent
	 * @param show
	 * @param config
	 * @return if the result is valid or not
	 */
	public boolean validate(Torrent torrent, Show show, Config config);

}
