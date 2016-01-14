package io.delimeat.core.feed.validation;

import io.delimeat.core.feed.FeedResult;
import io.delimeat.core.feed.TorrentRejection;
import io.delimeat.core.show.Show;
import io.delimeat.core.torrent.Torrent;

public class TorrentFolderValidator_Impl implements TorrentValidator{

	@Override
	public void validate(FeedResult result, Show show) throws TorrentValidatorException {
		Torrent torrent = result.getTorrent();
		if(torrent.getInfo().getFiles()!=null && torrent.getInfo().getFiles().size()>0){
			result.getTorrentRejections().add(TorrentRejection.CONTAINS_FOLDERS);
		}
	}

	
}
