package io.delimeat.processor.validation;

import java.util.Optional;

import io.delimeat.config.domain.Config;
import io.delimeat.feed.domain.FeedResultRejection;
import io.delimeat.show.domain.Show;
import io.delimeat.torrent.domain.Torrent;

public interface TorrentValidator {

	/**
	 * Validate a torrent
	 * 
	 * @param torrent
	 * @param show
	 * @param config
	 * @return optional feed result rejection
	 * @throws ValidationException
	 */
	public Optional<FeedResultRejection> validate(Torrent torrent, Show show, Config config) throws ValidationException;

}
