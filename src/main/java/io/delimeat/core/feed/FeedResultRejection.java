package io.delimeat.core.feed;

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
	UNNABLE_TO_GET_TORRENT(9);
  
  	private final int value;
  
  	private FeedResultRejection(int value){
     this.value = value;
   }
  
  	public int getValue(){
     	return value;
   }
 
}
