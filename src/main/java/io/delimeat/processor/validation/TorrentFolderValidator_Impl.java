package io.delimeat.processor.validation;

import java.util.Optional;

import io.delimeat.config.domain.Config;
import io.delimeat.feed.domain.FeedResultRejection;
import io.delimeat.show.domain.Show;
import io.delimeat.torrent.domain.Torrent;
import io.delimeat.torrent.domain.TorrentInfo;

public class TorrentFolderValidator_Impl implements TorrentValidator{

	/* (non-Javadoc)
	 * @see io.delimeat.server.processor.validation.TorrentValidator#validate(io.delimeat.server.torrent.model.Torrent, io.delimeat.common.show.model.Show, io.delimeat.common.config.model.Config)
	 */
	@Override
	public Optional<FeedResultRejection> validate(Torrent torrent, Show show, Config config) throws ValidationException {
		final TorrentInfo info = torrent.getInfo();
		
		if(info.getFiles() != null && info.getFiles().isEmpty() == false){
			return Optional.of(FeedResultRejection.CONTAINS_FOLDERS);
		}
		
		return Optional.empty();

	}

	
}
