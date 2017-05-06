/*
 * Copyright 2013-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
