package io.delimeat.processor.validation;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.delimeat.config.domain.Config;
import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.domain.FeedResultRejection;
import io.delimeat.show.domain.Episode;

public class FeedResultMiniSeriesValidator_Impl extends AbstractFeedResultValidator implements FeedResultValidator {

	private static final String MINI_SERIES_REGEX = "\\d{2}(?=[Oo][Ff]\\d{2})";
	
	public FeedResultMiniSeriesValidator_Impl() {
		super(FeedResultRejection.INVALID_MINI_SERIES_RESULT);
	}

	/* (non-Javadoc)
	 * @see io.delimeat.server.processor.validation.FeedResultValidator#validate(java.util.List, io.delimeat.common.show.model.Episode, io.delimeat.common.config.model.Config)
	 */
	@Override
	public void validate(List<FeedResult> results, Episode episode, Config config) throws ValidationException {
		final int episodeNum = episode.getEpisodeNum();

		final Pattern pattern = Pattern.compile(MINI_SERIES_REGEX);
		Matcher matcher;
		String title;
		for (FeedResult result : results) {
			title = result.getTitle();
			if (title != null && episodeNum > 0) {
				matcher = pattern.matcher(title);
				if (matcher.find()) {
					int resultEpisodeNum = Integer.parseInt(matcher.group());
					if (resultEpisodeNum == episodeNum) {
						continue;
					}
				}
			}
			addRejection(result);
		}

	}

}
