package io.delimeat.core.feed.validation;

import io.delimeat.core.feed.FeedResult;
import io.delimeat.core.feed.TorrentRejection;
import io.delimeat.core.show.Show;
import io.delimeat.core.torrent.Torrent;
import io.delimeat.core.torrent.TorrentFile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TorrentCompressedValidator_Impl implements TorrentValidator {

	private String regex;
	
	public void setRegex(String regex){
		this.regex = regex;
	}
	public String getRegex(){
		return regex;
	}
	@Override
	public void validate(FeedResult result, Show show) throws TorrentValidatorException {
		Pattern fileTypePattern = Pattern.compile(getRegex());
		Matcher fileTypeMatcher;
		Torrent torrent  = result.getTorrent();
		if(torrent.getInfo().getFiles()!=null && torrent.getInfo().getFiles().size()>0){
			for(TorrentFile file: torrent.getInfo().getFiles()){
				fileTypeMatcher = fileTypePattern.matcher(file.getName().toLowerCase());
				if(fileTypeMatcher.find()){
					result.getTorrentRejections().add(TorrentRejection.CONTAINS_COMPRESSED);
					break;
				}
			}	
		}
		else{
			fileTypeMatcher = fileTypePattern.matcher(torrent.getInfo().getName().toLowerCase());
			if(fileTypeMatcher.find()){
				result.getTorrentRejections().add(TorrentRejection.CONTAINS_COMPRESSED);
			}
		}
	}

}
