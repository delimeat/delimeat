package io.delimeat.core.guide;

import io.delimeat.util.jaxrs.AbstractJaxrsClientHelper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import javax.xml.bind.JAXBException;

public class TvdbJaxrsGuideDao_Impl extends AbstractJaxrsClientHelper implements GuideDao {

    private String apiKey;
    private TvdbToken token;
    private int validPeriodInMs = 0;

    @Override
    public GuideSource getGuideSource() {
        return GuideSource.TVDB;
    }

    public TvdbToken login(String apiKey) throws GuideNotAuthorisedException, GuideException {
        TvdbApiKey key = new TvdbApiKey();
        key.setValue(apiKey);
        Entity<TvdbApiKey> entity = Entity.entity(key, getMediaType());
        try {
            return getTarget().path("login")
              						.request(getMediaType())
              						.post(entity, TvdbToken.class);
          
        } catch (NotAuthorizedException ex) {
            GuideError error = ex.getResponse().readEntity(GuideError.class);
            throw new GuideNotAuthorisedException(error.getMessage(), ex);
        } catch (WebApplicationException ex) {
            throw new GuideException(ex);
        }
    }

    public TvdbToken refreshToken(TvdbToken token) throws GuideNotAuthorisedException, GuideException {
        try {
            return getTarget().path("refresh_token")
              						.request(getMediaType())
              						.header("Authorization", "Bearer " + token.getValue())
                              .get(TvdbToken.class);
          
        } catch (NotAuthorizedException ex) {
            GuideError error = ex.getResponse().readEntity(GuideError.class);
            throw new GuideNotAuthorisedException(error.getMessage(), ex);
        } catch (WebApplicationException ex) {
            throw new GuideException(ex);
        }

    }

    @Override
    public List<GuideSearchResult> search(String title) throws GuideNotFoundException, GuideNotAuthorisedException, GuideException {
        String encodedTitle;
        try {
            encodedTitle = URLEncoder.encode(title, ENCODING);
        } catch (UnsupportedEncodingException ex) {
            encodedTitle = title;
        }
      
        try {
            return getTarget().path("search")
              						.path("series")
              						.queryParam("name", encodedTitle)
              						.request(getMediaType())
                              .header("Authorization", "Bearer " + getToken().getValue())
              						.get(GuideSearch.class)
              						.getResults();
          
        } catch (NotAuthorizedException ex) {
            GuideError error = ex.getResponse().readEntity(GuideError.class);
            throw new GuideNotAuthorisedException(error.getMessage(), ex);
        } catch (NotFoundException ex) {
            GuideError error = ex.getResponse().readEntity(GuideError.class);
            throw new GuideNotFoundException(error.getMessage(), ex);
        } catch (WebApplicationException ex) {
            throw new GuideException(ex);
        }
    }

    @Override
    public GuideInfo info(String guideId) throws GuideNotFoundException, GuideNotAuthorisedException, GuideException {
        String encodedGuideId;
        try {
            encodedGuideId = URLEncoder.encode(guideId, ENCODING);
        } catch (UnsupportedEncodingException ex) {
            encodedGuideId = guideId;
        }

        try {
            return getTarget().path("series")
              						.path(encodedGuideId)
              						.request(getMediaType())
                              .header("Authorization", "Bearer " + getToken().getValue())
              						.get(GuideInfo.class);
          
        } catch (NotAuthorizedException ex) {
            GuideError error = ex.getResponse().readEntity(GuideError.class);
            throw new GuideNotAuthorisedException(error.getMessage(), ex);
        } catch (NotFoundException ex) {
            GuideError error = ex.getResponse().readEntity(GuideError.class);
            throw new GuideNotFoundException(error.getMessage(), ex);
        } catch (WebApplicationException ex) {
            throw new GuideException(ex);
        }
    }

    @Override
    public List<GuideEpisode> episodes(String guideId) throws GuideNotFoundException, GuideNotAuthorisedException, GuideException {
        List<GuideEpisode> episodes = new ArrayList<GuideEpisode>();
        Integer page = 1;
        do {
            TvdbEpisodes result = episodes(guideId, page);
            episodes.addAll(result.getEpisodes());
            page = result.getNext();
        } while (page != null && page > 0);

        return episodes;
    }

    public TvdbEpisodes episodes(String guideId, int page) throws GuideNotFoundException, GuideNotAuthorisedException, GuideException {
        String encodedGuideId;
        try {
            encodedGuideId = URLEncoder.encode(guideId, ENCODING);
        } catch (UnsupportedEncodingException ex) {
            encodedGuideId = guideId;
        }
      
        try {
            return getTarget().path("series")
              						.path(encodedGuideId)
              						.path("episodes")
              						.queryParam("page", page)
              						.request(getMediaType())
                              .header("Authorization", "Bearer " + getToken().getValue())
              						.get(TvdbEpisodes.class);
          
        } catch (NotAuthorizedException ex) {
            GuideError error = ex.getResponse().readEntity(GuideError.class);
            throw new GuideNotAuthorisedException(error.getMessage(), ex);
        } catch (NotFoundException ex) {
            GuideError error = ex.getResponse().readEntity(GuideError.class);
            throw new GuideNotFoundException(error.getMessage(), ex);
        } catch (WebApplicationException ex) {
            throw new GuideException(ex);
        }
    }

    /**
     * @return the apiKey
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * @param apiKey
     *            the apiKey to set
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * @return the token
     * @throws IOException
     * @throws JAXBException
     */
    public TvdbToken getToken() throws GuideNotAuthorisedException, GuideException {
        long now = System.currentTimeMillis();
        if (token == null || now >= token.getTime() + validPeriodInMs) {
            token = login(apiKey);
        } else if (now >= token.getTime() + validPeriodInMs - 10 * 60 * 1000) {
            token = refreshToken(token);
        }
        return token;
    }

    /**
     * @param token
     *            the token to set
     */
    public void setToken(TvdbToken token) {
        this.token = token;
    }

    /**
     * @return the validPeriodInMs
     */
    public int getValidPeriodInMs() {
        return validPeriodInMs;
    }

    /**
     * @param validPeriodInMs
     *            the validPeriodInMs to set
     */
    public void setValidPeriodInMs(int validPeriodInMs) {
        this.validPeriodInMs = validPeriodInMs;
    }
}
