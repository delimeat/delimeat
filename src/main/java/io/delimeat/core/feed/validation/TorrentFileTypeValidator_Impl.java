package io.delimeat.core.feed.validation;

import io.delimeat.core.config.Config;
import io.delimeat.core.feed.FeedResultRejection;
import io.delimeat.core.show.Show;
import io.delimeat.core.torrent.Torrent;
import io.delimeat.core.torrent.TorrentFile;
import io.delimeat.core.torrent.TorrentInfo;
import io.delimeat.util.DelimeatUtils;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TorrentFileTypeValidator_Impl implements TorrentValidator {
	
	@Override
	public boolean validate(Torrent torrent, Show show, Config config)
			throws TorrentValidatorException {
		String regex = "";
      Iterator<String> it = config.getIgnoredFileTypes().iterator();
		while(it.hasNext()){
        regex += it.next();
        if(it.hasNext()){
          regex += "|";
        }
      }

		if(!DelimeatUtils.isEmpty(regex)){ //only do it if there is any file types excluded
			final Pattern pattern = Pattern.compile(regex.toLowerCase());
			Matcher matcher;
         final TorrentInfo info = torrent.getInfo();
			if(info.getFiles() != null && info.getFiles().isEmpty() == false){
				for(TorrentFile file: info.getFiles()){
					matcher = pattern.matcher(file.getName().toLowerCase());
					if(matcher.find()){
						return false;
					}
				}
			}else if( DelimeatUtils.isNotEmpty(info.getName()) ){
         	matcher = pattern.matcher(info.getName().toLowerCase());
         	return (matcher.find() == false);
         }
		}
      return true;
	}
  
  
    @Override
    public FeedResultRejection getRejection() {
        return FeedResultRejection.CONTAINS_EXCLUDED_FILE_TYPES;
    }

}
