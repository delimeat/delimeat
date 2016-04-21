package io.delimeat.core.processor.validation;

import io.delimeat.core.config.Config;
import io.delimeat.core.feed.FeedResult;
import io.delimeat.core.feed.FeedResultRejection;
import io.delimeat.core.show.Show;
import io.delimeat.util.DelimeatUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeedResultSeasonValidator_Impl implements FeedResultValidator {

	private static final String SEASON_REGEX = "(?<=[Ss]?)\\d{1,2}(?=[xXeE]\\d{1,2})";
	private static final String EPISODE_REGEX = "(?<=[Ss]?\\d{1,2}[eExX])\\d{1,2}";

	@Override
	public void validate(List<FeedResult> results, Show show, Config config) throws ValidationException {
		final int seasonNum;
		final int episodeNum;
		if(show.getNextEpisode() != null){
			seasonNum = show.getNextEpisode().getSeasonNum();
			episodeNum = show.getNextEpisode().getEpisodeNum();
		}else{
			seasonNum = 0;
			episodeNum = 0;
		}
		final Pattern seasonSelectPattern = Pattern.compile(SEASON_REGEX);
		final Pattern episodeSelectPattern = Pattern.compile(EPISODE_REGEX);	
		Matcher seasonMatcher;
		Matcher episodeMatcher;
		String title;
		for(FeedResult result: results){
			title = result.getTitle();
			if( DelimeatUtils.isNotEmpty(title) && seasonNum > 0  && episodeNum > 0){
				seasonMatcher = seasonSelectPattern.matcher(title);
				episodeMatcher = episodeSelectPattern.matcher(title);
				
				if( seasonMatcher.find() && episodeMatcher.find() ){
					int resultSeasonNum = Integer.parseInt(seasonMatcher.group().trim());
					int resultEpisodeNum = Integer.parseInt(episodeMatcher.group().trim());
					if(resultSeasonNum == seasonNum && resultEpisodeNum == episodeNum){
						continue;
					}
				}
			}
			result.getFeedResultRejections().add(FeedResultRejection.INVALID_SEASON_RESULT);
		}

	}

}
