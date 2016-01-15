package io.delimeat.core.feed;

public enum FeedResultRejection {

	INCORRECT_TITLE,
	NOT_A_SEASON_EPISODE,
	INCORRECT_SEASON,
	INCORRECT_EPISODE,
	NOT_A_MINI_SERIES_EPISODE,
	NOT_A_DAILY_EPISODE,
	INCORRECT_DAILY_EPISODE,
	CONTAINS_FOLDERS,
	CONTAINS_COMPRESSED,
	CONTAINS_EXCLUDED_FILE_TYPES,
	FILE_SIZE_INCORRECT,
	UNNABLE_TO_GET_TORRENT,
	INSUFFICENT_SEEDERS
}
