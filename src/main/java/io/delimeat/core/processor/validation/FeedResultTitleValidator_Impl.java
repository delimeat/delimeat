package io.delimeat.core.processor.validation;

import io.delimeat.core.config.Config;
import io.delimeat.core.feed.FeedResult;
import io.delimeat.core.feed.FeedResultRejection;
import io.delimeat.core.show.Show;
import io.delimeat.common.util.DelimeatUtils;

import java.util.List;

public class FeedResultTitleValidator_Impl implements FeedResultValidator {
	
	@Override
	public void validate(List<FeedResult> results, Show show, Config config) throws ValidationException {
		final String showTitle;
		if( DelimeatUtils.isNotEmpty(show.getTitle()) ){
			showTitle = show.getTitle().toLowerCase().replace(".", " ");
		}else{
			showTitle = "";
		}
		String title;
		for(FeedResult result : results){
			title = result.getTitle();
			if(DelimeatUtils.isEmpty(title) || title.toLowerCase().replace(".", " ").contains(showTitle) == false){
				result.getFeedResultRejections().add(FeedResultRejection.INCORRECT_TITLE);
			}
		}

	}

}
