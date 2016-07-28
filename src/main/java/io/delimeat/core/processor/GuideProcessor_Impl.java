package io.delimeat.core.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

import javax.transaction.Transactional;

import com.google.common.collect.ComparisonChain;

import io.delimeat.core.guide.GuideEpisode;
import io.delimeat.core.guide.GuideException;
import io.delimeat.core.guide.GuideInfo;
import io.delimeat.core.guide.GuideDao;
import io.delimeat.core.show.Episode;
import io.delimeat.core.show.Show;
import io.delimeat.core.show.ShowDao;
import io.delimeat.core.show.ShowException;
import io.delimeat.common.util.DelimeatUtils;

public class GuideProcessor_Impl extends AbstractProcessor implements Processor {
  	
	private ShowDao showDao;
	private GuideDao guideDao;


	public ShowDao getShowDao() {
		return showDao;
	}

	public void setShowDao(ShowDao showDao) {
		this.showDao = showDao;
	}

	public GuideDao getGuideDao() {
		return guideDao;
	}

	public void setGuideDao(GuideDao guideDao) {
		this.guideDao = guideDao;
	}

   @Transactional
	@Override
	public void process() throws ShowException, GuideException {
		if (active == false) {
			try {
				active = true;
				final long showId = show.getShowId();
				final Show lockedShow = showDao.readAndLock(showId);
				final String guideId = lockedShow.getGuideId();
				boolean updated = false;
           	boolean removeNextEps = false;
				// episodes to create or update
				final List<Episode> createOrUpdateEps = new ArrayList<Episode>();

				if (active == true && DelimeatUtils.isNotEmpty(guideId) == true) {
					// get the info
					final GuideInfo info = guideDao.info(guideId);
					// only update if the info is newer than the last update
					Date infoLastUpdated = new Date(0);
					if (info.getLastUpdated() != null) {
						infoLastUpdated = info.getLastUpdated();
					}
					Date showLastUpdated = new Date(0);
					if (show.getLastGuideUpdate() != null) {
						showLastUpdated = show.getLastGuideUpdate();
					}
					if (infoLastUpdated.after(showLastUpdated) == true) {

						// update the airing status
						if (lockedShow.isAiring() != info.isAiring()) {
							lockedShow.setAiring(info.isAiring());
							updated = true;
						}

						// get the episodes and refresh them
						final List<GuideEpisode> foundGuideEps = guideDao.episodes(guideId);
						if (active == true && DelimeatUtils.isNotEmpty(foundGuideEps)) {

							// get the existing episodes
							final List<Episode> showEps = showDao.readAllEpisodes(showId);

							// remove any specials or duds
							final List<GuideEpisode> guideEps = DelimeatUtils.cleanEpisodes(foundGuideEps);
							// order them by airing date ascending
							Collections.sort(guideEps);
							// sort the episode from latest to earliest
							Collections.reverse(guideEps);

							// loop through all the guide eps
							ListIterator<GuideEpisode> guideEpIt = guideEps.listIterator();
							GuideEpisode prevGuideEp = null;
							while (guideEpIt.hasNext()) {

								GuideEpisode guideEp = guideEpIt.next();

								// stop when we reach the previous episode
								final Episode showPrevEp = lockedShow.getPreviousEpisode();
								if (showPrevEp != null) {

									int compare = ComparisonChain.start()
															.compare(guideEp.getSeasonNum(), (Integer)showPrevEp.getSeasonNum())
															.compare(guideEp.getEpisodeNum(),(Integer)showPrevEp.getEpisodeNum())
                                             .result();
									if (compare <= 0) {
                             	//reached the previous episode (or further) time to stop
										continue;
									}
								}

								// see if we already have the episode
								int indexOf = showEps.indexOf(guideEp);
								if (indexOf >= 0) {
									boolean updateThisEp = false;
									// if we do have the episode check if we
									// need to move the air date or update the
									// title
									Episode showEp = showEps.get(indexOf);
									// update the air date if needed
									if (Objects.equals(showEp.getAirDate(), guideEp.getAirDate()) == false) {
										showEp.setAirDate(guideEp.getAirDate());
										updateThisEp = true;
									}

									// update the title if needed
									if (Objects.equals(showEp.getTitle(), guideEp.getTitle()) == false) {
										showEp.setTitle(guideEp.getTitle());
										updateThisEp = true;
									}
									if(updateThisEp==true){
										createOrUpdateEps.add(showEp);
										updated = true;
									}

								} else {

									// if we dont have the episode add it
									Episode currentEp = new Episode(guideEp);
									currentEp.setShow(lockedShow);
									createOrUpdateEps.add(currentEp);
									updated = true;
								}
								prevGuideEp = guideEp;
							}
							
							// if the show has no next episode and we have found
							// one use that
							if (lockedShow.getNextEpisode() == null
									&& prevGuideEp != null) {

								Episode nextEp = null;
								if (createOrUpdateEps.contains(prevGuideEp)) {

									int indexOf = createOrUpdateEps.indexOf(prevGuideEp);
									nextEp = createOrUpdateEps.get(indexOf);
								} else if (showEps.contains(prevGuideEp)) {

									// this should never happen but just in case
									int indexOf = showEps.indexOf(prevGuideEp);
									nextEp = showEps.get(indexOf);
								}

								if (nextEp != null) {
									lockedShow.setNextEpisode(nextEp);
									updated = true;
								}
							}  //if the show as a next episode but the guides doesnt remove the shows future episodes
                    	else if(prevGuideEp == null && lockedShow.getNextEpisode() != null){
                        removeNextEps = true;
                        lockedShow.setNextEpisode(null);
                      }
						}
                 	else if(active == true && lockedShow.getNextEpisode() != null){
                        removeNextEps = true;
                        lockedShow.setNextEpisode(null);
                  }
					}
				}
				// once everything is done update the show
				if (active == true) {
					final Date now = new Date();
					lockedShow.setLastGuideCheck(now);
					if (updated == true) {
						lockedShow.setLastGuideUpdate(now);
						// create/update eps if any
						if (DelimeatUtils.isNotEmpty(createOrUpdateEps) == true) {
							showDao.createOrUpdateEpisodes(createOrUpdateEps);
						}
					}
					showDao.createOrUpdate(lockedShow);
               
              	// if there are episodes 
               if(removeNextEps == true){
                 	if(show.getPreviousEpisode() != null){
                    showDao.deleteEpisodesAfter(show.getPreviousEpisode());
                  }else{
                    showDao.deleteEpisodes(lockedShow);
                  }
               }
				}
			} finally {
				alertListenersComplete();
			}
		}
	}

	@Override
	public String toString() {
		return "GuideProcessor_Impl [show=" + show + ", config=" + config
				+ ", active=" + active + ", listeners=" + listeners
				+ ", showDao=" + showDao + ", guideDao=" + guideDao + "]";
	}


}