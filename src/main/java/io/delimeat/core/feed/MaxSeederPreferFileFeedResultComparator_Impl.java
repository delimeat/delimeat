package io.delimeat.core.feed;

import java.util.Comparator;

public class MaxSeederPreferFileFeedResultComparator_Impl implements Comparator<FeedResult> {

	@Override
	public int compare(FeedResult result1, FeedResult result2) {
		if(result1.getTorrent().getInfo().getFiles().size() == 0 && result2.getTorrent().getInfo().getFiles().size()>0){
			return -1;
		}else if(result1.getTorrent().getInfo().getFiles().size() > 0 && result2.getTorrent().getInfo().getFiles().size()==0){
			return 1;
		}else{
			return Long.compare(result2.getSeeders(), result1.getSeeders());
		}
	}

}
