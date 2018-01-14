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

import io.delimeat.config.entity.Config;
import io.delimeat.processor.entity.FeedProcessUnitRejection;
import io.delimeat.torrent.entity.Torrent;
import io.delimeat.torrent.entity.TorrentInfo;
import io.delimeat.show.entity.Show;

public class TorrentCompressedValidator_Impl extends AbstractTorrentValidator implements TorrentValidator {

	public TorrentCompressedValidator_Impl(){
		super(FeedProcessUnitRejection.CONTAINS_COMPRESSED);
	}

	/* (non-Javadoc)
	 * @see io.delimeat.processor.validation.AbstractTorrentValidator#doValidate(io.delimeat.torrent.entity.Torrent, io.delimeat.show.entity.Show, io.delimeat.config.entity.Config)
	 */
	@Override
	boolean doValidate(Torrent torrent, Show show, Config config) {
		final TorrentInfo info = torrent.getInfo();

		final List<String> files = Optional.ofNullable(info.getFiles())
											.orElse(Collections.emptyList())
											.stream()
											.map(file -> file.getName())
											.collect(Collectors.toList());

		Optional.ofNullable(info.getName())
				.ifPresent(fileName -> files.add(fileName));

		final Pattern pattern = Pattern.compile("(zip|rar|tar)$");
		for (final String file : files) {
			if (pattern.matcher(file.toLowerCase()).find()) {
				return false;
			}
		}

		return true;
	}
}
