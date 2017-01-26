package io.delimeat.core.processor.validation;

import java.util.List;

import io.delimeat.core.config.Config;
import io.delimeat.core.feed.FeedResult;
import io.delimeat.core.feed.FeedResultRejection;
import io.delimeat.core.show.Show;

public class FeedResultTitleValidator_Impl implements FeedResultValidator {
	
	@Override
	public void validate(List<FeedResult> results, Show show, Config config) throws ValidationException {
		final String showTitle;
		if( show.getTitle() != null ){
			showTitle = show.getTitle().toLowerCase().replace(".", " ");
		}else{
			showTitle = "";
		}
		String title;
		for(FeedResult result : results){
			title = result.getTitle();
			if(title == null || title.toLowerCase().replace(".", " ").contains(showTitle) == false){
				result.getFeedResultRejections().add(FeedResultRejection.INCORRECT_TITLE);
			}
		}

	}

}
