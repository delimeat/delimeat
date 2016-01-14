package io.delimeat.core.feed.validation;

import io.delimeat.core.feed.FeedResult;
import io.delimeat.core.feed.TorrentRejection;
import io.delimeat.core.show.Show;
import io.delimeat.core.torrent.Torrent;
import io.delimeat.core.torrent.TorrentFile;

public class TorrentSizeValidator_Impl implements TorrentValidator {

	private static final int MINSIZE = 100;
	private static final int MAXSIZE = 850;
	
	@Override
	public void validate(FeedResult result, Show show)
			throws TorrentValidatorException {
		
		long torrentSize = 0;
		Torrent torrent = result.getTorrent();
		if(torrent.getInfo().getFiles()!=null && torrent.getInfo().getFiles().size()>0){
			for(TorrentFile file: torrent.getInfo().getFiles()){
				torrentSize += file.getLength();
			}
		}else{
			torrentSize = torrent.getInfo().getLength();
		}
		
		int torrentSizeInMB = ((int)(torrentSize/1024)/1024);
		if(torrentSizeInMB< MINSIZE || torrentSizeInMB> MAXSIZE){
			result.getTorrentRejections().add(TorrentRejection.FILE_SIZE_INCORRECT);
		}
	}

}
