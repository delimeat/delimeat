package io.delimeat.processor.validation;

import java.util.List;

import io.delimeat.config.domain.Config;
import io.delimeat.feed.domain.FeedResult;
import io.delimeat.show.domain.Episode;

public interface FeedResultValidator {

	/**
	 * Validate feed results
	 * 
	 * @param results
	 * @param episode
	 * @param config
	 * @throws ValidationException
	 */
	public void validate(List<FeedResult> results, Episode episode, Config config) throws ValidationException;
	
	
}
