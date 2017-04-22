package io.delimeat.processor.validation;

import java.util.Optional;

import io.delimeat.config.domain.Config;
import io.delimeat.feed.domain.FeedResultRejection;
import io.delimeat.show.domain.Show;
import io.delimeat.torrent.domain.Torrent;
import io.delimeat.torrent.domain.TorrentInfo;

public class TorrentSizeValidator_Impl implements TorrentValidator {

	@Override
	public Optional<FeedResultRejection> validate(Torrent torrent, Show show, Config config)
			throws ValidationException {
		final TorrentInfo info = torrent.getInfo();
		
		long torrentSize = Optional.ofNullable(info.getFiles())
									.filter(files->!files.isEmpty())
									.map(files->files.stream()
														.mapToLong(file->file.getLength())
														.sum()
										)
									.orElse(info.getLength());
		
		int torrentSizeInMB = ((int)(torrentSize/1024)/1024);
		
		if (torrentSizeInMB < show.getMinSize() || torrentSizeInMB > show.getMaxSize()){
			return Optional.of(FeedResultRejection.FILE_SIZE_INCORRECT);
		}
		
		return Optional.empty();
	}

}
