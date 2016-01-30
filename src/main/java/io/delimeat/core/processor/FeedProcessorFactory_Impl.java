package io.delimeat.core.processor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import io.delimeat.core.config.Config;
import io.delimeat.core.feed.FeedDao;
import io.delimeat.core.feed.FeedResult;
import io.delimeat.core.processor.validation.FeedResultValidator;
import io.delimeat.core.processor.validation.TorrentValidator;
import io.delimeat.core.processor.writer.TorrentWriter;
import io.delimeat.core.show.Show;
import io.delimeat.core.show.ShowDao;
import io.delimeat.core.torrent.TorrentDao;

public class FeedProcessorFactory_Impl implements ProcessorFactory {

	private ShowDao showDao;
	private List<FeedDao> feedDaos = new ArrayList<FeedDao>();
	private TorrentDao torrentDao;
	private List<FeedResultValidator> dailyFeedResultValidators = new ArrayList<FeedResultValidator>();
	private List<FeedResultValidator> seasonFeedResultValidators = new ArrayList<FeedResultValidator>();
	private List<TorrentValidator> torrentValidators = new ArrayList<TorrentValidator>();
	private TorrentValidator folderTorrentValidator;
	private Comparator<FeedResult> preferFilesComparator;
	private Comparator<FeedResult> maxSeedersComparator;
	private TorrentWriter torrentWriter;

	public ShowDao getShowDao() {
		return showDao;
	}

	public void setShowDao(ShowDao showDao) {
		this.showDao = showDao;
	}

	public List<FeedDao> getFeedDaos() {
		return feedDaos;
	}

	public void setFeedDaos(List<FeedDao> feedDaos) {
		this.feedDaos = feedDaos;
	}

	public TorrentDao getTorrentDao() {
		return torrentDao;
	}

	public void setTorrentDao(TorrentDao torrentDao) {
		this.torrentDao = torrentDao;
	}

	public List<FeedResultValidator> getDailyFeedResultValidators() {
		return dailyFeedResultValidators;
	}

	public void setDailyFeedResultValidators(
			List<FeedResultValidator> dailyFeedResultValidators) {
		this.dailyFeedResultValidators = dailyFeedResultValidators;
	}

	public List<FeedResultValidator> getSeasonFeedResultValidators() {
		return seasonFeedResultValidators;
	}

	public void setSeasonFeedResultValidators(
			List<FeedResultValidator> seasonFeedResultValidators) {
		this.seasonFeedResultValidators = seasonFeedResultValidators;
	}

	public List<TorrentValidator> getTorrentValidators() {
		return torrentValidators;
	}

	public void setTorrentValidators(List<TorrentValidator> torrentValidators) {
		this.torrentValidators = torrentValidators;
	}

	public TorrentValidator getFolderTorrentValidator() {
		return folderTorrentValidator;
	}

	public void setFolderTorrentValidator(
			TorrentValidator folderTorrentValidator) {
		this.folderTorrentValidator = folderTorrentValidator;
	}

	public Comparator<FeedResult> getPreferFilesComparator() {
		return preferFilesComparator;
	}

	public void setPreferFilesComparator(
			Comparator<FeedResult> preferFilesComparator) {
		this.preferFilesComparator = preferFilesComparator;
	}

	public Comparator<FeedResult> getMaxSeedersComparator() {
		return maxSeedersComparator;
	}

	public void setMaxSeedersComparator(
			Comparator<FeedResult> maxSeedersComparator) {
		this.maxSeedersComparator = maxSeedersComparator;
	}

	public TorrentWriter getTorrentWriter() {
		return torrentWriter;
	}

	public void setTorrentWriter(TorrentWriter torrentWriter) {
		this.torrentWriter = torrentWriter;
	}

	@Override
	public Processor build(Show show, Config config) {
		final FeedProcessor_Impl processor = new FeedProcessor_Impl();

		processor.setShow(show);
		processor.setConfig(config);

		// set the standard stuff
		processor.setFeedDaos(feedDaos);
		processor.setShowDao(showDao);
		processor.setTorrentDao(torrentDao);
		processor.setTorrentWriter(torrentWriter);

		// set feed results validators
		switch (show.getShowType()) {
		case DAILY:
			processor.setFeedResultValidators(dailyFeedResultValidators);
			break;
		default:
			processor.setFeedResultValidators(seasonFeedResultValidators);
		}

		// set torrent validators
		List<TorrentValidator> validators = new ArrayList<TorrentValidator>();
		validators.addAll(torrentValidators);
		if (config.isIgnoreFolders() == true) {
			validators.add(folderTorrentValidator);
		}
		processor.setTorrentValidators(validators);

		// set file comparator
		if (config.isPreferFiles() == true) {
			processor.setResultComparator(preferFilesComparator);
		} else {
			processor.setResultComparator(maxSeedersComparator);
		}

		return processor;
	}
}
