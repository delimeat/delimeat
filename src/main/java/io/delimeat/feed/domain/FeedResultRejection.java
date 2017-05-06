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
package io.delimeat.feed.domain;

public enum FeedResultRejection {

	INCORRECT_TITLE(0),
	INVALID_SEASON_RESULT(1),
	INVALID_MINI_SERIES_RESULT(2),
	INVALID_DAILY_RESULT(3),
	CONTAINS_FOLDERS(4),
	CONTAINS_COMPRESSED(5),
	CONTAINS_EXCLUDED_FILE_TYPES(6),
	FILE_SIZE_INCORRECT(7),
	INSUFFICENT_SEEDERS(8),
	UNNABLE_TO_GET_TORRENT(9),
  	EXCLUDED_KEYWORD(10);
  
  	private final int value;
  
  	private FeedResultRejection(int value){
     this.value = value;
   }
  
  	public int getValue(){
     	return value;
   }
 
}
