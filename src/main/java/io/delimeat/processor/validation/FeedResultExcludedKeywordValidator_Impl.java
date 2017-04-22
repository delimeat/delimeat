package io.delimeat.processor.validation;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import io.delimeat.config.domain.Config;
import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.domain.FeedResultRejection;
import io.delimeat.show.domain.Episode;

public class FeedResultExcludedKeywordValidator_Impl extends AbstractFeedResultValidator implements FeedResultValidator {

	public FeedResultExcludedKeywordValidator_Impl(){
		super(FeedResultRejection.EXCLUDED_KEYWORD);
	}
	/* (non-Javadoc)
	 * @see io.delimeat.server.processor.validation.FeedResultValidator#validate(java.util.List, io.delimeat.common.show.model.Episode, io.delimeat.common.config.model.Config)
	 */
	@Override
	public void validate(List<FeedResult> results, Episode episode, Config config) throws ValidationException {
		if (config.getExcludedKeywords() == null || config.getExcludedKeywords().isEmpty()) {
			return;
		}

		String regex = config.getExcludedKeywords()
								.stream()
								.collect(Collectors.joining("|", "(", ")"));

		final Pattern pattern = Pattern.compile(regex.toLowerCase());
		String title;
		for (final FeedResult result : results) {
			title = Optional.ofNullable(result.getTitle())
								.map(i->i.toLowerCase())
								.orElse("");
			
			if (pattern.matcher(title).find()) {
				addRejection(result);
			}
		}

	}
}
