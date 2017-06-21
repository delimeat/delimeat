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
package io.delimeat.feed.jaxrs;

import java.io.IOException;
import java.io.InputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.domain.FeedSearch;

public class PirateBayJSoupContext implements JSoupContext{

	public FeedSearch process(InputStream entityStream, String encoding) throws IOException{
		FeedSearch feedSearch = new FeedSearch();
		Document document = Jsoup.parse(entityStream, encoding, "http://test.com");
		Elements elements = document.select(".items");
		elements.forEach(el -> {
			FeedResult result = new FeedResult();
			result.setTorrentURL(getText(el, ".url"));
			result.setTitle(getText(el, ".title"));
			result.setSeeders(getLong(el, ".seeders"));
			result.setLeechers(getLong(el, ".leechers"));
			result.setContentLength(getLong(el, ".length"));
			feedSearch.getResults().add(result);
		});
		return feedSearch;
	}
	
}
