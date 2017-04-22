package io.delimeat.processor.validation;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.delimeat.config.domain.Config;
import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.domain.FeedResultRejection;
import io.delimeat.show.domain.Episode;

public class FeedResultSeasonValidator_Impl extends AbstractFeedResultValidator implements FeedResultValidator {

	private static final String SEASON_REGEX = "(?<=[Ss]?)\\d{1,2}(?=[xXeE]\\d{1,2})";
	private static final String EPISODE_REGEX = "(?<=[Ss]?\\d{1,2}[eExX])\\d{1,2}";

	public FeedResultSeasonValidator_Impl(){
		super(FeedResultRejection.INVALID_SEASON_RESULT);
	}
	
	/* (non-Javadoc)
	 * @see io.delimeat.server.processor.validation.FeedResultValidator#validate(java.util.List, io.delimeat.common.show.model.Episode, io.delimeat.common.config.model.Config)
	 */
	@Override
	public void validate(List<FeedResult> results, Episode episode, Config config) throws ValidationException {
		final int seasonNum = episode.getSeasonNum();
		final int episodeNum = episode.getEpisodeNum();

		final Pattern seasonSelectPattern = Pattern.compile(SEASON_REGEX);
		final Pattern episodeSelectPattern = Pattern.compile(EPISODE_REGEX);
		Matcher seasonMatcher;
		Matcher episodeMatcher;
		String title;
		for (FeedResult result : results) {
			title = result.getTitle();
			if (title != null && seasonNum > 0 && episodeNum > 0) {
				seasonMatcher = seasonSelectPattern.matcher(title);
				episodeMatcher = episodeSelectPattern.matcher(title);

				if (seasonMatcher.find() && episodeMatcher.find()) {
					int resultSeasonNum = Integer.parseInt(seasonMatcher.group().trim());
					int resultEpisodeNum = Integer.parseInt(episodeMatcher.group().trim());
					if (resultSeasonNum == seasonNum && resultEpisodeNum == episodeNum) {
						continue;
					}
				}
			}
			addRejection(result);
		}

	}

}
