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
package io.delimeat.processor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import io.delimeat.show.ShowService;
import io.delimeat.show.entity.Show;


@Component
@Scope("prototype")
public class GuideItemReader_Impl implements ItemReader<Show> {

	@Autowired
	private ShowService showService;
	
	private List<Show> shows = null;
	
	/**
	 * @return the showService
	 */
	public ShowService getShowService() {
		return showService;
	}


	/**
	 * @param showService the showService to set
	 */
	public void setShowService(ShowService showService) {
		this.showService = showService;
	}


	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemReader#read()
	 */
	@Override
	public Show read() throws Exception {
		if(shows == null){
			shows = showService.readAll()
							.stream()
							.filter(show->show.isEnabled())
							.collect(Collectors.toList());
		}
		
		try{
			return shows.remove(0);
		}catch(IndexOutOfBoundsException ex){
			return null;
		}
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GuideItemReader_Impl [" + (showService != null ? "showService=" + showService + ", " : "")
				+ (shows != null ? "shows=" + shows : "") + "]";
	}

}
