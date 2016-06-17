package io.delimeat.core.guide;

import io.delimeat.common.util.jaxrs.client.AbstractJaxrsClientHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;

public class TvMazeJaxrsGuideDao_Impl extends AbstractJaxrsClientHelper implements GuideDao {

	@Override
	public GuideSource getGuideSource() {
		return GuideSource.TVMAZE;
	}

	@Override
	public List<GuideSearchResult> search(String title) throws GuideException {
        String encodedTitle;
        try {
            encodedTitle = URLEncoder.encode(title, getEncoding());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
     
        try {
            return getTarget().path("search")
              						.path("shows")
              						.queryParam("q", encodedTitle)
              						.request(getMediaType())
                              .get(new GenericType<List<GuideSearchResult>>() {});
          
        } catch (WebApplicationException ex) {
            throw new GuideException(ex);
        }
	}

	@Override
	public GuideInfo info(String guideId) throws GuideNotFoundException, GuideException {
        String encodedGuideId;
        try {
            encodedGuideId = URLEncoder.encode(guideId, getEncoding());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
     
        try {
            return getTarget().path("shows")
              						.path(encodedGuideId)
              						.request(getMediaType())
              						.get(GuideInfo.class);
          
        } catch (NotFoundException ex) {
            GuideError error = ex.getResponse().readEntity(GuideError.class);
            throw new GuideNotFoundException(error.getMessage(), ex);
        } catch (WebApplicationException ex) {
            throw new GuideException(ex);
        }
	}

	@Override
	public List<GuideEpisode> episodes(String guideId) throws GuideNotFoundException, GuideException {
        String encodedGuideId;
        try {
            encodedGuideId = URLEncoder.encode(guideId, getEncoding());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
     
        try {
          return getTarget()
                .path("shows")
                .path(encodedGuideId)
                .path("episodes")
                .request(getMediaType())
                .get(new GenericType<List<GuideEpisode>>() {});
          
        } catch (NotFoundException ex) {
            GuideError error = ex.getResponse().readEntity(GuideError.class);
            throw new GuideNotFoundException(error.getMessage(), ex);
        } catch (WebApplicationException ex) {
            throw new GuideException(ex);
        }
	}

}
