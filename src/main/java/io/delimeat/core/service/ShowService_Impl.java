package io.delimeat.core.service;

import io.delimeat.core.guide.GuideEpisode;
import io.delimeat.core.guide.GuideException;
import io.delimeat.core.guide.GuideInfoDao;
import io.delimeat.core.show.Episode;
import io.delimeat.core.show.Show;
import io.delimeat.core.show.ShowConcurrencyException;
import io.delimeat.core.show.ShowDao;
import io.delimeat.core.show.ShowException;
import io.delimeat.core.show.ShowGuideSource;
import io.delimeat.core.show.ShowNotFoundException;
import io.delimeat.util.DelimeatUtils;

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import javax.transaction.Transactional;

public class ShowService_Impl implements ShowService {

	private ShowDao showDao;
   private GuideInfoDao guideDao;

	public ShowDao getShowDao() {
		return showDao;
	}

	public void setShowDao(ShowDao showDao) {
		this.showDao = showDao;
	}
  
	public GuideInfoDao getGuideDao() {
		return guideDao;
	}

	public void setGuideDao(GuideInfoDao guideDao) {
		this.guideDao = guideDao;
	}

	@Override
	@Transactional
	public Show create(Show show) throws GuideException, ShowConcurrencyException, ShowException {
     
      final Show createdShow = getShowDao().createOrUpdate(prepareShow(show));
      
      final String guideId = DelimeatUtils.findGuideId(createdShow.getGuideSources(), guideDao.getGuideSource());
     
      if(DelimeatUtils.isNotEmpty(guideId) == true){
      	final List<GuideEpisode> foundGuideEps = guideDao.episodes(guideId);
         final List<GuideEpisode> guideEps = DelimeatUtils.cleanEpisodes(foundGuideEps);
         Collections.sort(guideEps);
         ListIterator<GuideEpisode> guideEpIt = guideEps.listIterator();
         while(guideEpIt.hasNext()){
            
            GuideEpisode guideEp = guideEpIt.next();
            
            Episode nextEp = createdShow.getNextEpisode();
            if(nextEp != null && nextEp.getSeasonNum() == guideEp.getSeasonNum() && nextEp.getEpisodeNum() == guideEp.getEpisodeNum() ){
              // do nothing because we alread have it
              //guideEpIt.remove();
              continue;
            }
           
            Episode prevEp = createdShow.getPreviousEpisode();
            if(prevEp != null && prevEp.getSeasonNum() == guideEp.getSeasonNum() && prevEp.getEpisodeNum() == guideEp.getEpisodeNum() ){
              // do nothing because we alread have it
              //guideEpIt.remove();
              continue;
            }
           
            /*
            if(guideEpIt.hasPrevious() && guideEpIt.previous().getAirDate() == guideEp.getAirDate()){
              //merge the double episode
              GuideEpisode prevGuideEp = guideEpIt.previous();
              String newTitle = prevGuideEp.getTitle() + " & " + guideEp.getTitle();
              prevGuideEp.setTitle(newTitle);
              guideEpIt.remove();
            }*/
            Episode ep = new Episode();
            ep.setShow(createdShow);
            ep.setTitle(guideEp.getTitle());
            ep.setSeasonNum(guideEp.getSeasonNum());
            ep.setEpisodeNum(guideEp.getEpisodeNum());
            ep.setAirDate(guideEp.getAirDate());
            showDao.createOrUpdateEpisode(ep);
           
         }
        
         
      }
      
		return createdShow;
	}

	@Override
	@Transactional
	public Show read(Long id) throws ShowNotFoundException, ShowConcurrencyException, ShowException {
		return getShowDao().read(id);
	}

	@Override
	@Transactional
	public List<Show> readAll() throws ShowException {
		return getShowDao().readAll();
	}

	@Override
	@Transactional
	public Show update(Show show) throws ShowConcurrencyException, ShowException {
		return getShowDao().createOrUpdate(prepareShow(show));
	}

	@Override
	@Transactional
	public void delete(Long id) throws ShowNotFoundException, ShowException {
		getShowDao().delete(id);
	}
	
	public Show prepareShow(Show show){
		if (!DelimeatUtils.isCollectionEmpty(show.getGuideSources())) {
			for (ShowGuideSource guideSource : show.getGuideSources()) {
				guideSource.setShow(show);
			}
		}
		
		if (show.getNextEpisode() != null) {
			show.getNextEpisode().setShow(show);
		}
		
		if (show.getPreviousEpisode() != null) {
			show.getPreviousEpisode().setShow(show);
		}
		return show;
	}

}
