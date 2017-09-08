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

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.delimeat.config.domain.Config;
import io.delimeat.processor.domain.FeedProcessUnitRejection;
import io.delimeat.show.domain.Show;
import io.delimeat.torrent.domain.Torrent;

public abstract class AbstractTorrentValidator implements TorrentValidator {

  	protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	private final FeedProcessUnitRejection rejection;
	
	public AbstractTorrentValidator(FeedProcessUnitRejection rejection){
		this.rejection = rejection;
	}
	
	/* (non-Javadoc)
	 * @see io.delimeat.processor.validation.TorrentValidator#getRejection()
	 */
	@Override
	public FeedProcessUnitRejection getRejection() {
		return rejection;
	}

	/* (non-Javadoc)
	 * @see io.delimeat.processor.validation.TorrentValidator#validate(io.delimeat.torrent.domain.Torrent, io.delimeat.show.domain.Show, io.delimeat.config.domain.Config)
	 */
	@Override
	public boolean validate(Torrent torrent, Show show, Config config) {
		Objects.requireNonNull(torrent, "Torrent must not be null");
		Objects.requireNonNull(show, "Show must not be null");
		Objects.requireNonNull(config, "Config must not be null");
		
		LOGGER.trace("Start validate\n{} \n{}\n{}", torrent, show, config);
		
		if(doValidate(torrent,show,config) == false){
			LOGGER.trace("Rejected {}", rejection);
			return false;
		}else{
			LOGGER.trace("Accepted {}", torrent);
			return true;
		}
	}
	
	/**
	 * Validate torrent
	 * 
	 * @param torrent
	 * @param show
	 * @param config
	 * @return
	 */
	abstract boolean doValidate(Torrent torrent, Show show, Config config);

}
