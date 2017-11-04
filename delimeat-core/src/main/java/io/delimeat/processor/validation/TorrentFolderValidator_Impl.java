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

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import io.delimeat.config.entity.Config;
import io.delimeat.processor.entity.FeedProcessUnitRejection;
import io.delimeat.show.entity.Show;
import io.delimeat.torrent.entity.Torrent;
import io.delimeat.torrent.entity.TorrentInfo;

@Component
@Order(4)
public class TorrentFolderValidator_Impl extends AbstractTorrentValidator implements TorrentValidator{

	public TorrentFolderValidator_Impl(){
		super(FeedProcessUnitRejection.CONTAINS_FOLDERS);
	}

	/* (non-Javadoc)
	 * @see io.delimeat.processor.validation.AbstractTorrentValidator#doValidate(io.delimeat.processor.torrent.entity.Torrent, io.delimeat.show.entity.Show, io.delimeat.config.entity.Config)
	 */
	@Override
	boolean doValidate(Torrent torrent, Show show, Config config) {
		if(config.isIgnoreFolders() == true){
			final TorrentInfo info = torrent.getInfo();
			
			if(info.getFiles() != null && info.getFiles().isEmpty() == false){
				return false;
			}
		}
		return true;
	}

	
}
