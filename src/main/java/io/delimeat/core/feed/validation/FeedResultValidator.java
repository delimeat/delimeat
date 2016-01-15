package io.delimeat.core.feed.validation;

import io.delimeat.core.feed.FeedResult;
import io.delimeat.core.show.Show;

import java.util.List;

public interface FeedResultValidator {
	
	public void validate(List<FeedResult> results, Show show) throws FeedValidationException;
}
