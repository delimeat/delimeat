package io.delimeat.core.processor.validation;

import io.delimeat.core.feed.FeedResult;
import io.delimeat.core.feed.FeedResultRejection;
import io.delimeat.core.show.Show;
import io.delimeat.util.DelimeatUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeedResultMiniSeriesValidator_Impl implements FeedResultValidator {

	private static final String MINI_SERIES_REGEX = "\\d{2}(?=[Oo][Ff]\\d{2})";
	
	@Override
	public void validate(List<FeedResult> results, Show show) throws ValidationException {
		final int episodeNum;
		if(show.getNextEpisode()!=null){
			episodeNum = show.getNextEpisode().getEpisodeNum();
		}else{
			episodeNum = 0;
		}
		final Pattern miniSeriesPattern = Pattern.compile(MINI_SERIES_REGEX);
		Matcher miniSeriesMatcher;
		String title;
		for(FeedResult result: results){
			title = result.getTitle();
			if( DelimeatUtils.isNotEmpty(title) && episodeNum > 0 ){
				miniSeriesMatcher = miniSeriesPattern.matcher(title);
				if( miniSeriesMatcher.find() ){
					int resultEpisodeNum = Integer.parseInt(miniSeriesMatcher.group());
					if( resultEpisodeNum == episodeNum){
						continue;
					}
				}
			}
			result.getFeedResultRejections().add(FeedResultRejection.INVALID_MINI_SERIES_RESULT);
		}

	}

}
