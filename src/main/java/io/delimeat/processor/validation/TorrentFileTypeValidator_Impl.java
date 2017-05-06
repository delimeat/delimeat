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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import io.delimeat.config.domain.Config;
import io.delimeat.feed.domain.FeedResultRejection;
import io.delimeat.show.domain.Show;
import io.delimeat.torrent.domain.Torrent;
import io.delimeat.torrent.domain.TorrentInfo;

@Component
@Order(3)
public class TorrentFileTypeValidator_Impl implements TorrentValidator {

	/* (non-Javadoc)
	 * @see io.delimeat.server.processor.validation.TorrentValidator#validate(io.delimeat.server.torrent.model.Torrent, io.delimeat.common.show.model.Show, io.delimeat.common.config.model.Config)
	 */
	@Override
	public Optional<FeedResultRejection> validate(Torrent torrent, Show show, Config config) throws ValidationException {
		final TorrentInfo info = torrent.getInfo();
		
		if(config.getIgnoredFileTypes() == null || config.getIgnoredFileTypes().isEmpty() == true){
			return Optional.empty();
		}
		
		String regex = config.getIgnoredFileTypes()
								.stream()
								.collect(Collectors.joining("|", "(", ")$"));

		final Pattern pattern = Pattern.compile(regex.toLowerCase());

		final List<String> files = Optional.ofNullable(info.getFiles())
											.orElse(Collections.emptyList())
											.stream()
											.map(file->file.getName())
											.collect(Collectors.toList());
		
		Optional.ofNullable(info.getName())
					.ifPresent(fileName->files.add(fileName));
		
		for (String file : files) {
			if(pattern.matcher(file.toLowerCase()).find()){
				return Optional.of(FeedResultRejection.CONTAINS_EXCLUDED_FILE_TYPES);
			}
		}
		
		return Optional.empty();
	}

}
