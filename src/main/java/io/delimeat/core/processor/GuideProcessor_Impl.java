package io.delimeat.core.processor;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import io.delimeat.core.guide.GuideEpisode;
import io.delimeat.core.guide.GuideException;
import io.delimeat.core.guide.GuideInfo;
import io.delimeat.core.guide.GuideInfoDao;
import io.delimeat.core.show.Episode;
import io.delimeat.core.show.Show;
import io.delimeat.core.show.ShowDao;
import io.delimeat.core.show.ShowException;
import io.delimeat.util.DelimeatUtils;

public class GuideProcessor_Impl implements GuideProcessor{

	private boolean active = false;
  	private Show show;
	private ShowDao showDao;
  	private GuideInfoDao guideDao;
  	private Exception exception;

	public void setShow(Show show) {
		this.show = show;
	}

	public Show getShow() {
		return show;
	}

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
    public boolean abort() {
      if(active == true){
        active = false;
        return true;
      }
      return false;
    }

    @Override
    public Exception getException() {
        return exception;
    }

    @Override
    public void process() throws ShowException, GuideException {
        if(active == false){
          active = true;
          Show lockedShow = showDao.readAndLock(show.getShowId());
          
          String guideId = DelimeatUtils.findGuideId(lockedShow.getGuideSources(), guideDao.getGuideSource()) ;
          
          if(DelimeatUtils.isNotEmpty(guideId) == true){
            // get the info
            final GuideInfo info = guideDao.info(guideId);
            // only update if the info is newer than the last update we did
            if(info.getLastUpdated().after(show.getLastGuideUpdate()) == true){
              
              // update the airing status
              show.setAiring(info.isAiring());
              
              // get the episodes and refresh them
              final List<GuideEpisode> foundGuideEps = guideDao.episodes(guideId);
              final List<GuideEpisode> guideEps = DelimeatUtils.cleanEpisodes(foundGuideEps);
              Collections.sort(guideEps);
              if(guideEps.isEmpty() == false){
                
                // set the next episode
                if(show.getNextEpisode() == null){
                  GuideEpisode guideEp = null;
                  if(show.getPreviousEpisode() != null){
                    // loop through the guide episodes to find the first ep after the prev ep
                    Episode prevEp = show.getPreviousEpisode();
                    for(GuideEpisode ep: guideEps){
                      if(DelimeatUtils.compare(prevEp, ep) > 0){
                        guideEp = ep;
                        break;
                      }
                    }
                  }else{
                    // if there is no previous ep the next ep is the first one
                    guideEp = guideEps.get(0); 
                  }
                  
                  if(guideEp != null ){
                    Episode nextEp = DelimeatUtils.toEpisode(guideEp);
                    nextEp.setShow(lockedShow);
                    lockedShow.setNextEpisode(nextEp);
                    showDao.createOrUpdate(lockedShow);
                  }  
                }
                
                // update/add the episodes we dont have
                final List<Episode> showEps = showDao.readAllEpisodes(lockedShow.getShowId());
                // sort the existing episodes newest to oldest
                Collections.reverse(showEps);
                // sort the guide episodes newest to oldest
                Collections.reverse(guideEps);
                
                Iterator<Episode> showEpIt = showEps.iterator();                
                while(showEpIt.hasNext()){
                  Episode showEp = showEpIt.next();
                  // stop updating once we reach the next episode
                  if(showEp == lockedShow.getNextEpisode()){
                    break;
                  }
                  // loop through the remaining guide episodes
                  Iterator<GuideEpisode> guideEpIt = guideEps.iterator();
                  while(guideEpIt.hasNext()){
                      GuideEpisode guideEp = guideEpIt.next();
                    	 int compare = DelimeatUtils.compare(showEp, guideEp);
                    	 // if the guide ep is before the current show ep we need to add it
                      if(compare > 0 ){
                        guideEpIt.remove();
                        Episode newEp = DelimeatUtils.toEpisode(guideEp);
                        newEp.setShow(lockedShow);
                        showDao.createOrUpdateEpisode(newEp);
                      }
                      else if(compare == 0){
                        // if the guide ep is the current show ep and the title or air date have changed update it
                        if(showEp.getTitle() != guideEp.getTitle() || showEp.getAirDate().equals(guideEp.getAirDate()) == false){
                          guideEpIt.remove();
                          showEp.setTitle(guideEp.getTitle());
                          showEp.setAirDate(guideEp.getAirDate());
                          showDao.createOrUpdateEpisode(showEp);
                        }
                      }else{
                        // if the guide ep is after the current show ep time to move onto the next show ep
                        break;
                      }
                  }
                }
                
              }
            }           
          }
        }   
    }
}
