package io.delimeat.processor.validation;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.delimeat.config.domain.Config;
import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.domain.FeedResultRejection;
import io.delimeat.show.domain.Episode;

public class FeedResultDailyValidator_Impl extends AbstractFeedResultValidator implements FeedResultValidator {

	private static final String YEAR_REGEX = "\\d{4}(?=[\\s\\.]\\d{2}[\\s\\.]\\d{2})";
	private static final String MONTH_REGEX = "(?<=\\d{4}[\\s\\.])\\d{2}(?=[\\s\\.]\\d{2})";
	private static final String DAY_REGEX = "(?<=\\d{4}[\\s\\.]\\d{2}[\\s\\.])\\d{2}";

	public FeedResultDailyValidator_Impl(){
		super(FeedResultRejection.INVALID_DAILY_RESULT);
	}
	
	/* (non-Javadoc)
	 * @see io.delimeat.server.processor.validation.FeedResultValidator#validate(java.util.List, io.delimeat.common.show.model.Episode, io.delimeat.common.config.model.Config)
	 */
	@Override
	public void validate(List<FeedResult> results, Episode episode, Config config) throws ValidationException {
		final LocalDate airDate = episode.getAirDate();

		final Pattern yearSelectPattern = Pattern.compile(YEAR_REGEX);
		final Pattern monthSelectPattern = Pattern.compile(MONTH_REGEX);
		final Pattern daySelectPattern = Pattern.compile(DAY_REGEX);
		Matcher yearMatcher;
		Matcher monthMatcher;
		Matcher dayMatcher;
		String title;
		for (FeedResult result : results) {
			title = result.getTitle();
			if (title != null && airDate != null) {
				yearMatcher = yearSelectPattern.matcher(title);
				monthMatcher = monthSelectPattern.matcher(title);
				dayMatcher = daySelectPattern.matcher(title);
				if (yearMatcher.find() && monthMatcher.find() && dayMatcher.find()) {
					int year = Integer.valueOf(yearMatcher.group());
					int month = Integer.valueOf(monthMatcher.group());
					int day = Integer.valueOf(dayMatcher.group());
					try {
						LocalDate resultAirDate = LocalDate.of(year, month, day);
						if (airDate.equals(resultAirDate)) {
							continue;
						}
					} catch (DateTimeException ex) {
						// do nothing
					}
				}
			}
			addRejection(result);
		}

	}

}
