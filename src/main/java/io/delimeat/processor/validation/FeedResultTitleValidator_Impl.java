package io.delimeat.processor.validation;

import java.util.List;
import java.util.Optional;

import io.delimeat.config.domain.Config;
import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.domain.FeedResultRejection;
import io.delimeat.show.domain.Episode;

public class FeedResultTitleValidator_Impl extends AbstractFeedResultValidator implements FeedResultValidator {

	public FeedResultTitleValidator_Impl() {
		super(FeedResultRejection.INCORRECT_TITLE);
	}

	/* (non-Javadoc)
	 * @see io.delimeat.server.processor.validation.FeedResultValidator#validate(java.util.List, io.delimeat.common.show.model.Episode, io.delimeat.common.config.model.Config)
	 */
	@Override
	public void validate(List<FeedResult> results, Episode episode, Config config) throws ValidationException {
		final String showTitle = episode.getShow()
										.getTitle()
										.toLowerCase()
										.replace(".", " ");

		String title;
		for (FeedResult result : results) {
			title = Optional.ofNullable(result.getTitle())
							.map(i->i.toLowerCase())
							.map(i->i.replace(".", " "))
							.orElse("");
			
			if (title.contains(showTitle) == false) {
				addRejection(result);
			}
		}

	}

}
