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
package io.delimeat.guide.domain;

import com.google.common.base.MoreObjects;

public class GuideError {

	private String message;

	/**
	 * Set the error message
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Get the error message
	 * 
	 * @return error message
	 */
	public String getMessage() {
		return message;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
	      return MoreObjects.toStringHelper(this)
	    	        .add("message", message)
	    	        .omitNullValues()
	    	        .toString();
	}

}
