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

import java.util.Optional;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import io.delimeat.config.domain.Config;
import io.delimeat.feed.domain.FeedResultRejection;
import io.delimeat.show.domain.Show;
import io.delimeat.torrent.domain.Torrent;
import io.delimeat.torrent.domain.TorrentInfo;

@Component
@Order(4)
public class TorrentFolderValidator_Impl implements TorrentValidator{

	/* (non-Javadoc)
	 * @see io.delimeat.server.processor.validation.TorrentValidator#validate(io.delimeat.server.torrent.model.Torrent, io.delimeat.common.show.model.Show, io.delimeat.common.config.model.Config)
	 */
	@Override
	public Optional<FeedResultRejection> validate(Torrent torrent, Show show, Config config) throws ValidationException {
		
		if(config.isIgnoreFolders() == true){
			final TorrentInfo info = torrent.getInfo();
			
			if(info.getFiles() != null && info.getFiles().isEmpty() == false){
				return Optional.of(FeedResultRejection.CONTAINS_FOLDERS);
			}
		}
		return Optional.empty();

	}

	
}
