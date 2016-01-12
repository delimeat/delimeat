package io.delimeat.util.jaxb;

import io.delimeat.core.guide.GuideSource;

public class TvMazeIdentifierAdapter extends AbstractIdentifierAdapter {

    @Override
    public GuideSource getSource() {
        return GuideSource.TVMAZE;
    }


}
