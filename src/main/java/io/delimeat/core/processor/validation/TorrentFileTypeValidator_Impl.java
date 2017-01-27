package io.delimeat.core.processor.validation;

import io.delimeat.core.config.Config;
import io.delimeat.core.feed.FeedResultRejection;
import io.delimeat.core.show.Show;
import io.delimeat.core.torrent.Torrent;
import io.delimeat.core.torrent.TorrentFile;
import io.delimeat.core.torrent.TorrentInfo;
import io.delimeat.util.DelimeatUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TorrentFileTypeValidator_Impl implements TorrentValidator {

    @Override
    public boolean validate(Torrent torrent, Show show, Config config) throws ValidationException {
        if (DelimeatUtils.isEmpty(config.getIgnoredFileTypes()) == true) {
            return true;
        }

        String regex = "(";
        Iterator<String> it = config.getIgnoredFileTypes().iterator();
        while (it.hasNext()) {
            regex += it.next();
            if (it.hasNext()) {
                regex += "|";
            }
        }
        regex += ")$";

        final Pattern pattern = Pattern.compile(regex.toLowerCase());
        Matcher matcher;
        final TorrentInfo info = torrent.getInfo();
        final List<String> files = new ArrayList<String>();
        if (info.getFiles() != null && !info.getFiles().isEmpty()) {
            for (TorrentFile file : info.getFiles()) {
                files.add(file.getName());
            }
        } else {
            files.add(info.getName());
        }

        for (final String file : files) {
            matcher = pattern.matcher(file.toLowerCase());
            if (matcher.find()) {
                return false;
            }
        }

        return true;
    }


    @Override
    public FeedResultRejection getRejection() {
        return FeedResultRejection.CONTAINS_EXCLUDED_FILE_TYPES;
    }

}
