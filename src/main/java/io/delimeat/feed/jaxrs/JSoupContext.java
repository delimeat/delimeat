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

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.delimeat.feed.domain.FeedSearch;

public interface JSoupContext {

	public FeedSearch process(InputStream entityStream, String encoding) throws IOException;
	
	public default String getText(Element element, String selector){
		Elements elements = element.select(selector);
		if(elements.size()>0){
			return elements.first().text();
		}
		return "";
	}
	
	public default long getLong(Element element, String selector){
		try{
			return Long.valueOf(getText(element,selector));
		}catch(NumberFormatException ex){
			return 0;
		}
	}
}
