package io.delimeat.core.feed.validation;

import io.delimeat.core.config.Config;
import io.delimeat.core.feed.FeedResultRejection;
import io.delimeat.core.feed.validation.TorrentValidatorException;
import io.delimeat.core.show.Show;
import io.delimeat.core.torrent.Torrent;
import io.delimeat.core.torrent.TorrentFile;
import io.delimeat.core.torrent.TorrentInfo;

public class TorrentSizeValidator_Impl implements TorrentValidator {

	@Override
	public boolean validate(Torrent torrent, Show show, Config config)
			throws TorrentValidatorException {
		final TorrentInfo info = torrent.getInfo();
		long torrentSize = 0;
		if(info.getFiles() != null && info.getFiles().isEmpty() == false){
			for(TorrentFile file: info.getFiles()){
				torrentSize += file.getLength();
			}
		}else{
			torrentSize = info.getLength();
		}
		
		int torrentSizeInMB = ((int)(torrentSize/1024)/1024);
		return (torrentSizeInMB > show.getMinSize() && torrentSizeInMB < show.getMaxSize());
	}

    @Override
    public FeedResultRejection getRejection() {
        return FeedResultRejection.FILE_SIZE_INCORRECT;
    }

}
