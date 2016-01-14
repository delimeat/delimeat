package io.delimeat.core.feed.validation;

import io.delimeat.core.feed.FeedResult;
import io.delimeat.core.feed.TorrentRejection;
import io.delimeat.core.show.Show;
import io.delimeat.core.torrent.Torrent;
import io.delimeat.core.torrent.TorrentFile;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TorrentFileTypeValidator_Impl implements TorrentValidator {

	private List<String> fileTypes;
	
	@Override
	public void validate(FeedResult result, Show show)
			throws TorrentValidatorException {
		final List<String> runFileTypes = fileTypes;
		String regex = "";
		for(int index = 0; index < runFileTypes.size();index++){
			regex += ((index > 0) ? "|"+runFileTypes.get(index): runFileTypes.get(index));
		}
		if(regex.length()>0){ //only do it if there is any file types excluded
			Pattern pattern = Pattern.compile(regex.toLowerCase());
			Matcher matcher;
			Torrent torrent = result.getTorrent();
			if(torrent.getInfo().getFiles()!=null && torrent.getInfo().getFiles().size()>0){
				for(TorrentFile file: torrent.getInfo().getFiles()){
					matcher = pattern.matcher(file.getName().toLowerCase());
					if(matcher.find()){
						result.getTorrentRejections().add(TorrentRejection.CONTAINS_EXCLUDED_FILE_TYPES);
						break;
					}
				}
			}else{
				matcher = pattern.matcher(torrent.getInfo().getName().toLowerCase());
				if(matcher.find()){
					result.getTorrentRejections().add(TorrentRejection.CONTAINS_EXCLUDED_FILE_TYPES);
				}
			}
		}
	}

}
