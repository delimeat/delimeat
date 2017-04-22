package io.delimeat.guide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.delimeat.common.util.exception.EntityAuthorizationException;
import io.delimeat.common.util.exception.EntityException;
import io.delimeat.common.util.exception.EntityNotFoundException;
import io.delimeat.guide.domain.GuideEpisode;
import io.delimeat.guide.domain.GuideInfo;
import io.delimeat.guide.domain.GuideSearch;
import io.delimeat.guide.domain.GuideSearchResult;
import io.delimeat.guide.domain.GuideSource;
import io.delimeat.guide.domain.TvdbApiKey;
import io.delimeat.guide.domain.TvdbEpisodes;
import io.delimeat.guide.domain.TvdbToken;

@Component
public class TvdbJaxrsGuideDao_Impl extends AbstractGuideDao implements GuideDao {

	@Value("${io.delimeat.guide.tvdb.apikey}")
	private String apiKey;
    
	@Value("${io.delimeat.guide.tvdb.validPeriod}")
    private int validPeriodInMs = 0;

    private TvdbToken token;
    
    public TvdbJaxrsGuideDao_Impl() {
		super(GuideSource.TVDB);
	}


	/* (non-Javadoc)
	 * @see io.delimeat.common.guide.GuideDao#info(java.lang.String)
	 */
	@Override
	public GuideInfo info(String guideId) throws EntityNotFoundException, EntityAuthorizationException, EntityException {
		return get(getTarget()
        		.path("series")
          		.path(urlEncodeString(guideId))
          		.request(getMediaType())
          		.header("Authorization", getAuthorization()), new GenericType<GuideInfo>(){});
	}

	/* (non-Javadoc)
	 * @see io.delimeat.common.guide.GuideDao#episodes(java.lang.String)
	 */
	@Override
	public List<GuideEpisode> episodes(String guideId)
			throws EntityNotFoundException, EntityAuthorizationException, EntityException {
        List<GuideEpisode> episodes = new ArrayList<GuideEpisode>();
        Integer page = 1;
        do {
            TvdbEpisodes result = episodes(urlEncodeString(guideId), page);
            episodes.addAll(result.getEpisodes());
            page = result.getNext();
        } while (page != null && page > 0);

        return episodes;
	}
	
    /**
     * @param guideId
     * @param page
     * @return
     * @throws EntityNotFoundException
     * @throws EntityAuthorizationException
     * @throws EntityException
     */
    public TvdbEpisodes episodes(String guideId, int page) throws EntityNotFoundException, EntityAuthorizationException, EntityException {
		return get(getTarget()
				.path("series")
				.path(guideId)
				.path("episodes")
				.queryParam("page", page)
				.request(getMediaType())
          		.header("Authorization", getAuthorization()), new GenericType<TvdbEpisodes>(){});
    }

	/* (non-Javadoc)
	 * @see io.delimeat.common.guide.GuideDao#search(java.lang.String)
	 */
	@Override
	public List<GuideSearchResult> search(String title)
			throws EntityNotFoundException, EntityAuthorizationException, EntityException {
		
		return get(getTarget()
				.path("search")
				.path("series")
				.queryParam("name", title)
				.request(getMediaType())
				.header("Authorization", getAuthorization()), new GenericType<GuideSearch>(){}).getResults();
	}
	
    public TvdbToken login(String apiKey) throws EntityAuthorizationException, EntityException {
    	 TvdbApiKey key = new TvdbApiKey();
         key.setValue(apiKey);
         Entity<TvdbApiKey> entity = Entity.entity(key, getMediaType());
         return put(getTarget()
        		 .path("login")
              	.request(getMediaType()), entity, new GenericType<TvdbToken>(){});
    }

    public TvdbToken refreshToken(TvdbToken token) throws EntityAuthorizationException, EntityException {
    	return get(getTarget()
				.path("refresh_token")
				.request(getMediaType())
				.header("Authorization", "Bearer "+ token.getValue()), new GenericType<TvdbToken>(){});
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
    public TvdbToken getToken() throws EntityAuthorizationException, EntityException {
        long now = System.currentTimeMillis();
        if (token == null || now >= token.getTime() + validPeriodInMs) {
            token = login(apiKey);
        } else if (now >= token.getTime() + validPeriodInMs - 10 * 60 * 1000) {
            token = refreshToken(token);
        }
        return token;
    }
    
    /**
     * @return the authorisation
     * @throws EntityAuthorizationException
     * @throws EntityException
     */
    public String getAuthorization() throws EntityAuthorizationException, EntityException{
    	return String.format("Bearer %s", getToken().getValue());
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
