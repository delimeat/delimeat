package io.delimeat.core.feed.validation;

import io.delimeat.core.config.Config;
import io.delimeat.core.feed.FeedResultRejection;
import io.delimeat.core.show.Show;
import io.delimeat.core.torrent.Torrent;
import io.delimeat.core.torrent.TorrentFile;
import io.delimeat.core.torrent.TorrentInfo;
import io.delimeat.util.DelimeatUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TorrentCompressedValidator_Impl implements TorrentValidator {

	private static final String REGEX = "(zip|rar|tar)$";
  
	@Override
	public boolean validate(Torrent torrent, Show show, Config config) throws FeedValidationException {
		final Pattern fileTypePattern = Pattern.compile(REGEX);
      final TorrentInfo info = torrent.getInfo();
      Matcher fileTypeMatcher;
		if(info.getFiles() != null && info.getFiles().isEmpty() == false ){
			for(TorrentFile file: info.getFiles()){
				fileTypeMatcher = fileTypePattern.matcher(file.getName().toLowerCase());
				if(fileTypeMatcher.find()){
					return false;
				}
			}
		}else if( DelimeatUtils.isNotEmpty(info.getName()) ){
		  fileTypeMatcher = fileTypePattern.matcher(info.getName().toLowerCase());
        return fileTypeMatcher.find() == false;
      }
      return true;
	}

    @Override
    public FeedResultRejection getRejection() {
        return FeedResultRejection.CONTAINS_COMPRESSED;
    }

}
