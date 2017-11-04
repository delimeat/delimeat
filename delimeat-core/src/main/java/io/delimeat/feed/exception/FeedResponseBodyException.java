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
package io.delimeat.feed.exception;

import java.net.URL;

public class FeedResponseBodyException extends FeedException {

	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 */
	public FeedResponseBodyException(URL url, String body, Throwable throwable) {
		super(String.format("Unable to parse response for url %s \n%s", url, body), throwable);
	}

}
