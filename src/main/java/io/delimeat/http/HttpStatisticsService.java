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
package io.delimeat.http;

import java.net.URL;
import java.util.List;

import io.delimeat.http.domain.HttpStatistics;;

public interface HttpStatisticsService {

	/**
	 * Add a response
	 * @param url
	 * @param status
	 */
	public void addResponse(URL url, int statusCode);
	
	/**
	 * @return the statistics
	 */
	public List<HttpStatistics> getStatistics();
	
}
