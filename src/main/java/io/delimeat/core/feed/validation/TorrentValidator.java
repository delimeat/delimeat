package io.delimeat.core.feed.validation;

import io.delimeat.core.config.Config;
import io.delimeat.core.feed.FeedResultRejection;
import io.delimeat.core.show.Show;
import io.delimeat.core.torrent.Torrent;

public interface TorrentValidator {

	public boolean validate(Torrent torrent, Show show, Config config) throws FeedValidationException;
   public FeedResultRejection getRejection();
}
