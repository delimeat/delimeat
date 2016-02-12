package io.delimeat.core.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.delimeat.core.guide.GuideEpisode;
import io.delimeat.core.guide.GuideException;
import io.delimeat.core.guide.GuideInfo;
import io.delimeat.core.guide.GuideDao;
import io.delimeat.core.show.Episode;
import io.delimeat.core.show.Show;
import io.delimeat.core.show.ShowDao;
import io.delimeat.core.show.ShowException;
import io.delimeat.util.DelimeatUtils;

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

	@Override
	public void process() throws ShowException, GuideException {
		if (active == false) {
			try {
				active = true;
				final Show lockedShow = showDao.readAndLock(show.getShowId());

				final String guideId = DelimeatUtils.findGuideId(lockedShow.getGuideSources(), guideDao.getGuideSource());

				if (active == true && DelimeatUtils.isNotEmpty(guideId) == true) {
					// get the info
					final GuideInfo info = guideDao.info(guideId);
					// only update if the info is newer than the last update
					if (info.getLastUpdated().after(show.getLastGuideUpdate()) == true) {

						// update the airing status
						lockedShow.setAiring(info.isAiring());

						// episodes to create or update
						final List<Episode> createOrUpdateEps = new ArrayList<Episode>();

						// get the episodes and refresh them
						final List<GuideEpisode> foundGuideEps = guideDao.episodes(guideId);

						if (active == true
								&& DelimeatUtils.isCollectionNotEmpty(foundGuideEps) == true) {

							// get the existing episodes
							final List<Episode> showEps = showDao.readAllEpisodes(lockedShow.getShowId());

							// remove any specials or duds
							final List<GuideEpisode> guideEps = DelimeatUtils.cleanEpisodes(foundGuideEps);
							// order them by airing date ascending
							Collections.sort(guideEps);
							// sort the episode from latest to earliest
							Collections.reverse(guideEps);

							// loop through all the guide eps
							GuideEpisode prevGuideEp = null;
							for (GuideEpisode guideEp : guideEps) {
								// see if we already have the episode
								int indexOf = showEps.indexOf(guideEp);
								if (indexOf >= 0) {
									// if we do have the episode check if we
									// need to move the air date or update the
									// title
									Episode showEp = showEps.get(indexOf);
									Date epAirDate = guideEp.getAirDate();
									if (showEp.getTitle() != guideEp.getTitle()
											|| showEp.getAirDate().equals(epAirDate) == false) {
										showEp.setTitle(guideEp.getTitle());
										showEp.setAirDate(epAirDate);
										createOrUpdateEps.add(showEp);
									}
								} else {
									// if we dont have the episode add it
									Episode newEp = new Episode(guideEp);
									newEp.setShow(lockedShow);
									createOrUpdateEps.add(newEp);
								}

								// stop when we reach the previous episode
								if (lockedShow.getPreviousEpisode() != null
										&& lockedShow.getPreviousEpisode().equals(guideEp)) {
									break;
								}
								prevGuideEp = guideEp;
							}

							// if the show has no next episode and we have found
							// one use that
							if (lockedShow.getNextEpisode() == null
									&& prevGuideEp != null) {
								
								Episode nextEp = null;
								if (createOrUpdateEps.indexOf(prevGuideEp) != -1) {
									
									int indexOf = createOrUpdateEps.indexOf(prevGuideEp);
									nextEp = createOrUpdateEps.get(indexOf);
								} else if (showEps.indexOf(prevGuideEp) != -1) {
									
									// this should never happen but just in case
									int indexOf = showEps.indexOf(prevGuideEp);
									nextEp = showEps.get(indexOf);
								}
								
								if (nextEp != null) {
									lockedShow.setNextEpisode(nextEp);
								}
							}

						}

						// if we're still active update the show
						if (active == true) {
							// create/update eps if any
							if (DelimeatUtils.isCollectionNotEmpty(createOrUpdateEps) == true) {
								showDao.createOrUpdateEpisodes(createOrUpdateEps);
							}
							lockedShow.setLastGuideUpdate(new Date());
							showDao.createOrUpdate(lockedShow);
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