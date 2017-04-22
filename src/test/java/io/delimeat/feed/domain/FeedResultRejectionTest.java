package io.delimeat.feed.domain;

import org.junit.Assert;
import org.junit.Test;

import io.delimeat.feed.domain.FeedResultRejection;

public class FeedResultRejectionTest {

	@Test
	public void incorrectTitleTest() {
		Assert.assertEquals(0, FeedResultRejection.INCORRECT_TITLE.getValue());
	}

	@Test
	public void invalidSeasonResultTest() {
		Assert.assertEquals(1, FeedResultRejection.INVALID_SEASON_RESULT.getValue());
	}

	@Test
	public void invalidMiniSeriesResultTest() {
		Assert.assertEquals(2, FeedResultRejection.INVALID_MINI_SERIES_RESULT.getValue());
	}

	@Test
	public void invalidDailyResultTest() {
		Assert.assertEquals(3, FeedResultRejection.INVALID_DAILY_RESULT.getValue());
	}

	@Test
	public void containsFolersTest() {
		Assert.assertEquals(4, FeedResultRejection.CONTAINS_FOLDERS.getValue());
	}

	@Test
	public void containsCompressedTest() {
		Assert.assertEquals(5, FeedResultRejection.CONTAINS_COMPRESSED.getValue());
	}

	@Test
	public void containsExcludedFileTypesTest() {
		Assert.assertEquals(6, FeedResultRejection.CONTAINS_EXCLUDED_FILE_TYPES.getValue());
	}

	@Test
	public void fileSizeIncorrectTest() {
		Assert.assertEquals(7, FeedResultRejection.FILE_SIZE_INCORRECT.getValue());
	}

	@Test
	public void insufficientSeedersTest() {
		Assert.assertEquals(8, FeedResultRejection.INSUFFICENT_SEEDERS.getValue());
	}

	@Test
	public void unableToGetTorrentTest() {
		Assert.assertEquals(9, FeedResultRejection.UNNABLE_TO_GET_TORRENT.getValue());
	}
  

	@Test
	public void excludedKeywordTest() {
		Assert.assertEquals(10, FeedResultRejection.EXCLUDED_KEYWORD.getValue());
	}

}
