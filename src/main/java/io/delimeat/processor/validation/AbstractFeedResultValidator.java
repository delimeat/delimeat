package io.delimeat.processor.validation;

import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.domain.FeedResultRejection;

public abstract class AbstractFeedResultValidator implements FeedResultValidator {

	protected final FeedResultRejection rejection;
	
	/**
	 * Constructor
	 * @param rejection
	 */
	AbstractFeedResultValidator(FeedResultRejection rejection){
		this.rejection = rejection;
	}
	
	/**
	 * @return rejection
	 */
	public FeedResultRejection getRejection(){
		return rejection;
	}
	
	/**
	 * Add rejection to a result
	 * 
	 * @param result
	 */
	public void addRejection(FeedResult result){
		result.getFeedResultRejections().add(rejection);
	}

}
