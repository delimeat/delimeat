package io.delimeat.core.feed.validation;

import io.delimeat.core.feed.FeedResult;
import io.delimeat.core.show.Show;

public interface TorrentValidator {

	public void validate(FeedResult result, Show show) throws TorrentValidatorException;
}
