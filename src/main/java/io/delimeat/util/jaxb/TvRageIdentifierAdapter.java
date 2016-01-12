package io.delimeat.util.jaxb;

import io.delimeat.core.guide.GuideSource;

public class TvRageIdentifierAdapter extends AbstractIdentifierAdapter {
  
    @Override
    public GuideSource getSource() {
        return GuideSource.TVRAGE;
    }


}
