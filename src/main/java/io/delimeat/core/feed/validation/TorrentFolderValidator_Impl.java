package io.delimeat.core.feed.validation;

import io.delimeat.core.config.Config;
import io.delimeat.core.feed.FeedResultRejection;
import io.delimeat.core.show.Show;
import io.delimeat.core.torrent.Torrent;
import io.delimeat.core.torrent.TorrentInfo;

public class TorrentFolderValidator_Impl implements TorrentValidator{

	@Override
	public boolean validate(Torrent torrent, Show show, Config config) throws FeedValidationException {
     final TorrentInfo info = torrent.getInfo();
     return (info.getFiles() == null || info.getFiles().isEmpty() );
	}
  
    @Override
    public FeedResultRejection getRejection() {
        return FeedResultRejection.CONTAINS_FOLDERS;
    }

	
}
