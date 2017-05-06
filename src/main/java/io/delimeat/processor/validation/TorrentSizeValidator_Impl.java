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
@Order(1)
public class TorrentSizeValidator_Impl implements TorrentValidator {

	@Override
	public Optional<FeedResultRejection> validate(Torrent torrent, Show show, Config config)
			throws ValidationException {
		final TorrentInfo info = torrent.getInfo();
		
		long torrentSize = Optional.ofNullable(info.getFiles())
									.filter(files->!files.isEmpty())
									.map(files->files.stream()
														.mapToLong(file->file.getLength())
														.sum()
										)
									.orElse(info.getLength());
		
		int torrentSizeInMB = ((int)(torrentSize/1024)/1024);
		
		if (torrentSizeInMB < show.getMinSize() || torrentSizeInMB > show.getMaxSize()){
			return Optional.of(FeedResultRejection.FILE_SIZE_INCORRECT);
		}
		
		return Optional.empty();
	}

}
