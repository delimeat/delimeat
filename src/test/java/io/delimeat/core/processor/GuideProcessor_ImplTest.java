package io.delimeat.core.processor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import io.delimeat.core.guide.GuideEpisode;
import io.delimeat.core.guide.GuideException;
import io.delimeat.core.guide.GuideInfo;
import io.delimeat.core.guide.GuideInfoDao;
import io.delimeat.core.guide.GuideSource;
import io.delimeat.core.show.Episode;
import io.delimeat.core.show.Show;
import io.delimeat.core.show.ShowDao;
import io.delimeat.core.show.ShowException;
import io.delimeat.core.show.ShowGuideSource;
import io.delimeat.core.show.ShowGuideSourcePK;

public class GuideProcessor_ImplTest {
  
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

	private GuideProcessor_Impl processor;

	@Before
	public void setUp() throws Exception {
		processor = new GuideProcessor_Impl();
	}

	@Test
	public void showTest() {
		Assert.assertNull(processor.getShow());
		Show show = new Show();
		processor.setShow(show);
		Assert.assertEquals(show, processor.getShow());
	}

	@Test
	public void showDaoTest() {
		Assert.assertNull(processor.getShowDao());
		ShowDao mockedDao = Mockito.mock(ShowDao.class);
		processor.setShowDao(mockedDao);
		Assert.assertEquals(mockedDao, processor.getShowDao());
	}  

	@Test
	public void guideDaoTest() {
		Assert.assertNull(processor.getGuideDao());
		GuideInfoDao mockedDao = Mockito.mock(GuideInfoDao.class);
		processor.setGuideDao(mockedDao);
		Assert.assertEquals(mockedDao, processor.getGuideDao());
	}

  	@Test
	public void abortTest() {
		Assert.assertFalse(processor.isActive());
		processor.setActive(true);
		Assert.assertTrue(processor.isActive());
		processor.abort();
		Assert.assertFalse(processor.isActive());
	}
  
    @Test
    public void listenerTest(){
        Assert.assertNotNull(processor.getListeners());
        Assert.assertTrue(processor.getListeners().isEmpty());
        ProcessorListener listener = Mockito.mock(ProcessorListener.class);
        processor.addListener(listener);
        Assert.assertEquals(1, processor.getListeners().size());
        Assert.assertEquals(listener, processor.getListeners().get(0));
        processor.removeListener(listener);
        Assert.assertTrue(processor.getListeners().isEmpty());    
    }
  
  	@Test
  	public void processAlreadyActiveTest() throws Exception{
     ProcessorListener listener = Mockito.mock(ProcessorListener.class);
     processor.addListener(listener);

     ShowDao showDao = Mockito.mock(ShowDao.class);
     processor.setShowDao(showDao);

     GuideInfoDao guideDao = Mockito.mock(GuideInfoDao.class);
     processor.setGuideDao(guideDao);
     
     Show show = new Show();
     processor.setShow(show);
     
     processor.setActive(true);
     
     processor.process();
     
     Assert.assertTrue(processor.isActive());
     
     Mockito.verify(listener, Mockito.times(0)).alertComplete(Mockito.any(Processor.class));
    
     Mockito.verify(showDao, Mockito.times(0)).readAndLock(Mockito.anyLong());
     Mockito.verify(showDao, Mockito.times(0)).createOrUpdate(Mockito.any(Show.class));
     Mockito.verify(showDao, Mockito.times(0)).readAllEpisodes(Mockito.anyLong());
     Mockito.verify(showDao, Mockito.times(0)).createOrUpdateEpisode(Mockito.any(Episode.class));
     
     Mockito.verify(guideDao, Mockito.times(0)).info(Mockito.anyString());
     Mockito.verify(guideDao, Mockito.times(0)).episodes(Mockito.anyString());     
   }

  	@Test
  	public void processReadAndLockExceptionTest() throws Exception{
     ProcessorListener listener = Mockito.mock(ProcessorListener.class);
     processor.addListener(listener);

     ShowDao showDao = Mockito.mock(ShowDao.class);
     Mockito.when(showDao.readAndLock(Mockito.anyLong())).thenThrow(ShowException.class);
     processor.setShowDao(showDao);

     GuideInfoDao guideDao = Mockito.mock(GuideInfoDao.class);
     processor.setGuideDao(guideDao);
     
     Show show = new Show();
     show.setShowId(Long.MAX_VALUE);
     processor.setShow(show);
     
     try{
       processor.process();
     }catch(ShowException ex){
       //do nothing
     }
     
     Assert.assertFalse(processor.isActive());
     
     Mockito.verify(listener, Mockito.times(1)).alertComplete(Mockito.any(Processor.class));
    
     Mockito.verify(showDao, Mockito.times(1)).readAndLock(Mockito.anyLong());
     Mockito.verify(showDao, Mockito.times(0)).createOrUpdate(Mockito.any(Show.class));
     Mockito.verify(showDao, Mockito.times(0)).readAllEpisodes(Mockito.anyLong());
     Mockito.verify(showDao, Mockito.times(0)).createOrUpdateEpisode(Mockito.any(Episode.class));
     
     Mockito.verify(guideDao, Mockito.times(0)).info(Mockito.anyString());
     Mockito.verify(guideDao, Mockito.times(0)).episodes(Mockito.anyString());     
   }
  
  	@Test
  	public void processNoGuideIdFoundTest() throws Exception{
     ProcessorListener listener = Mockito.mock(ProcessorListener.class);
     processor.addListener(listener);
       
     Show show = new Show();
     show.setShowId(Long.MAX_VALUE);
     processor.setShow(show);
     
     ShowDao showDao = Mockito.mock(ShowDao.class);
     Mockito.when(showDao.readAndLock(Mockito.anyLong())).thenReturn(show);
     processor.setShowDao(showDao);

     GuideInfoDao guideDao = Mockito.mock(GuideInfoDao.class);
     Mockito.when(guideDao.getGuideSource()).thenReturn(GuideSource.TVDB);
     processor.setGuideDao(guideDao);
     
     processor.process();
     
     Assert.assertFalse(processor.isActive());
     
     Mockito.verify(listener, Mockito.times(1)).alertComplete(Mockito.any(Processor.class));
    
     Mockito.verify(showDao, Mockito.times(1)).readAndLock(Mockito.anyLong());
     Mockito.verify(showDao, Mockito.times(0)).createOrUpdate(Mockito.any(Show.class));
     Mockito.verify(showDao, Mockito.times(0)).readAllEpisodes(Mockito.anyLong());
     Mockito.verify(showDao, Mockito.times(0)).createOrUpdateEpisode(Mockito.any(Episode.class));
     
     Mockito.verify(guideDao, Mockito.times(0)).info(Mockito.anyString());
     Mockito.verify(guideDao, Mockito.times(0)).episodes(Mockito.anyString());     
   }
  
	@Test
  	public void processGuideInfoExceptionTest() throws Exception{
     ProcessorListener listener = Mockito.mock(ProcessorListener.class);
     processor.addListener(listener);
       
     Show show = new Show();
     show.setShowId(Long.MAX_VALUE);
     ShowGuideSource sgs1 = new ShowGuideSource();
     sgs1.setGuideId("GUIDEID");
     ShowGuideSourcePK sgs1pk = new ShowGuideSourcePK();
     sgs1pk.setGuideSource(GuideSource.TVDB);
     sgs1.setId(sgs1pk);
     show.getGuideSources().add(sgs1);
     processor.setShow(show);
     
     ShowDao showDao = Mockito.mock(ShowDao.class);
     Mockito.when(showDao.readAndLock(Mockito.anyLong())).thenReturn(show);
     processor.setShowDao(showDao);

     GuideInfoDao guideDao = Mockito.mock(GuideInfoDao.class);
     Mockito.when(guideDao.getGuideSource()).thenReturn(GuideSource.TVDB);
     Mockito.when(guideDao.info("GUIDEID")).thenThrow(GuideException.class);
     processor.setGuideDao(guideDao);
     
     try{
     	processor.process();
     }catch(GuideException ex){
       //do nothing
     }
     
     Assert.assertFalse(processor.isActive());
     
     Mockito.verify(listener, Mockito.times(1)).alertComplete(Mockito.any(Processor.class));
    
     Mockito.verify(showDao, Mockito.times(1)).readAndLock(Mockito.anyLong());
     Mockito.verify(showDao, Mockito.times(0)).createOrUpdate(Mockito.any(Show.class));
     Mockito.verify(showDao, Mockito.times(0)).readAllEpisodes(Mockito.anyLong());
     Mockito.verify(showDao, Mockito.times(0)).createOrUpdateEpisode(Mockito.any(Episode.class));
     
     Mockito.verify(guideDao, Mockito.times(1)).info(Mockito.anyString());
     Mockito.verify(guideDao, Mockito.times(0)).episodes(Mockito.anyString());     
   }
  
	@Test
  	public void processGuideInfoBeforeTest() throws Exception{
     ProcessorListener listener = Mockito.mock(ProcessorListener.class);
     processor.addListener(listener);
       
     Show show = new Show();
     show.setShowId(Long.MAX_VALUE);
     ShowGuideSource sgs1 = new ShowGuideSource();
     sgs1.setGuideId("GUIDEID");
     ShowGuideSourcePK sgs1pk = new ShowGuideSourcePK();
     sgs1pk.setGuideSource(GuideSource.TVDB);
     sgs1.setId(sgs1pk);
     show.getGuideSources().add(sgs1);
     show.setLastGuideUpdate(SDF.parse("2016-01-29"));
     processor.setShow(show);
     
     ShowDao showDao = Mockito.mock(ShowDao.class);
     Mockito.when(showDao.readAndLock(Mockito.anyLong())).thenReturn(show);
     processor.setShowDao(showDao);

     GuideInfoDao guideDao = Mockito.mock(GuideInfoDao.class);
     Mockito.when(guideDao.getGuideSource()).thenReturn(GuideSource.TVDB);
     GuideInfo info = new GuideInfo();
 	  info.setLastUpdated(SDF.parse("2000-01-01"));
     Mockito.when(guideDao.info("GUIDEID")).thenReturn(info);
     processor.setGuideDao(guideDao);
     
     processor.process();
     
     Assert.assertFalse(processor.isActive());
     
     Mockito.verify(listener, Mockito.times(1)).alertComplete(Mockito.any(Processor.class));
    
     Mockito.verify(showDao, Mockito.times(1)).readAndLock(Mockito.anyLong());
     Mockito.verify(showDao, Mockito.times(0)).createOrUpdate(Mockito.any(Show.class));
     Mockito.verify(showDao, Mockito.times(0)).readAllEpisodes(Mockito.anyLong());
     Mockito.verify(showDao, Mockito.times(0)).createOrUpdateEpisode(Mockito.any(Episode.class));
     
     Mockito.verify(guideDao, Mockito.times(1)).info(Mockito.anyString());
     Mockito.verify(guideDao, Mockito.times(0)).episodes(Mockito.anyString());     
   }

	@Test
  	public void processAiringUpdateTest() throws Exception{
     ProcessorListener listener = Mockito.mock(ProcessorListener.class);
     processor.addListener(listener);
       
     Show show = new Show();
     show.setShowId(Long.MAX_VALUE);
     ShowGuideSource sgs1 = new ShowGuideSource();
     sgs1.setGuideId("GUIDEID");
     ShowGuideSourcePK sgs1pk = new ShowGuideSourcePK();
     sgs1pk.setGuideSource(GuideSource.TVDB);
     sgs1.setId(sgs1pk);
     show.getGuideSources().add(sgs1);
     show.setLastGuideUpdate(SDF.parse("2000-01-01"));
     show.setAiring(true);
     processor.setShow(show);
     
     ShowDao showDao = Mockito.mock(ShowDao.class);
     Mockito.when(showDao.readAndLock(Mockito.anyLong())).thenReturn(show);
     processor.setShowDao(showDao);

     GuideInfoDao guideDao = Mockito.mock(GuideInfoDao.class);
     Mockito.when(guideDao.getGuideSource()).thenReturn(GuideSource.TVDB);
     GuideInfo info = new GuideInfo();
 	  info.setLastUpdated(SDF.parse("2016-01-29"));
     info.setAiring(false);
     Mockito.when(guideDao.info("GUIDEID")).thenReturn(info);
     processor.setGuideDao(guideDao);
     
     processor.process();
     
     Assert.assertFalse(processor.isActive());
     Assert.assertFalse(show.isAiring());
     
     Mockito.verify(listener, Mockito.times(1)).alertComplete(Mockito.any(Processor.class));
    
     Mockito.verify(showDao, Mockito.times(1)).readAndLock(Mockito.anyLong());
     Mockito.verify(showDao, Mockito.times(1)).createOrUpdate(Mockito.any(Show.class));
     Mockito.verify(showDao, Mockito.times(0)).readAllEpisodes(Mockito.anyLong());
     Mockito.verify(showDao, Mockito.times(0)).createOrUpdateEpisode(Mockito.any(Episode.class));
     
     Mockito.verify(guideDao, Mockito.times(1)).info(Mockito.anyString());
     Mockito.verify(guideDao, Mockito.times(1)).episodes(Mockito.anyString());     
   }
  
	@Test
  	public void processAiringNoUpdateNoResultsTest() throws Exception{
     ProcessorListener listener = Mockito.mock(ProcessorListener.class);
     processor.addListener(listener);
       
     Show show = new Show();
     show.setShowId(Long.MAX_VALUE);
     ShowGuideSource sgs1 = new ShowGuideSource();
     sgs1.setGuideId("GUIDEID");
     ShowGuideSourcePK sgs1pk = new ShowGuideSourcePK();
     sgs1pk.setGuideSource(GuideSource.TVDB);
     sgs1.setId(sgs1pk);
     show.getGuideSources().add(sgs1);
     show.setLastGuideUpdate(SDF.parse("2000-01-01"));
     show.setAiring(true);
     processor.setShow(show);
     
     ShowDao showDao = Mockito.mock(ShowDao.class);
     Mockito.when(showDao.readAndLock(Mockito.anyLong())).thenReturn(show);
     processor.setShowDao(showDao);

     GuideInfoDao guideDao = Mockito.mock(GuideInfoDao.class);
     Mockito.when(guideDao.getGuideSource()).thenReturn(GuideSource.TVDB);
     GuideInfo info = new GuideInfo();
 	  info.setLastUpdated(SDF.parse("2016-01-29"));
     info.setAiring(true);
     Mockito.when(guideDao.info("GUIDEID")).thenReturn(info);
     processor.setGuideDao(guideDao);
     
     processor.process();
     
     Assert.assertFalse(processor.isActive());
     Assert.assertTrue(show.isAiring());
     Assert.assertTrue(show.getLastGuideUpdate().after(SDF.parse("2000-01-01")));

     
     Mockito.verify(listener, Mockito.times(1)).alertComplete(Mockito.any(Processor.class));
    
     Mockito.verify(showDao, Mockito.times(1)).readAndLock(Mockito.anyLong());
     Mockito.verify(showDao, Mockito.times(1)).createOrUpdate(Mockito.any(Show.class));
     Mockito.verify(showDao, Mockito.times(0)).readAllEpisodes(Mockito.anyLong());
     Mockito.verify(showDao, Mockito.times(0)).createOrUpdateEpisode(Mockito.any(Episode.class));
     
     Mockito.verify(guideDao, Mockito.times(1)).info(Mockito.anyString());
     Mockito.verify(guideDao, Mockito.times(1)).episodes(Mockito.anyString());     
   }
  
	@Test
  	public void processNewResultTest() throws Exception{
     ProcessorListener listener = Mockito.mock(ProcessorListener.class);
     processor.addListener(listener);
       
     Show show = new Show();
     show.setShowId(Long.MAX_VALUE);
     show.setTimezone("ETC");
     ShowGuideSource sgs1 = new ShowGuideSource();
     sgs1.setGuideId("GUIDEID");
     ShowGuideSourcePK sgs1pk = new ShowGuideSourcePK();
     sgs1pk.setGuideSource(GuideSource.TVDB);
     sgs1.setId(sgs1pk);
     show.getGuideSources().add(sgs1);
     show.setLastGuideUpdate(SDF.parse("2000-01-01"));
     show.setAiring(true);
     Episode nextEp = new Episode();
     show.setNextEpisode(nextEp);
     processor.setShow(show);
     
     ShowDao showDao = Mockito.mock(ShowDao.class);
     Mockito.when(showDao.readAndLock(Mockito.anyLong())).thenReturn(show);
     processor.setShowDao(showDao);

     GuideInfoDao guideDao = Mockito.mock(GuideInfoDao.class);
     Mockito.when(guideDao.getGuideSource()).thenReturn(GuideSource.TVDB);
     GuideInfo info = new GuideInfo();
 	  info.setLastUpdated(SDF.parse("2016-01-29"));
     info.setAiring(true);
     Mockito.when(guideDao.info("GUIDEID")).thenReturn(info);
     GuideEpisode guideEp1 = new GuideEpisode();
     guideEp1.setSeasonNum(1);
     guideEp1.setEpisodeNum(1);
     guideEp1.setTitle("GUIDE_EP_1");
     guideEp1.setAirDate(SDF.parse("2016-01-29"));
     Mockito.when(guideDao.episodes("GUIDEID")).thenReturn(Arrays.asList(guideEp1));
     processor.setGuideDao(guideDao);
     
     processor.process();
     
     Assert.assertFalse(processor.isActive());
     Assert.assertTrue(show.getLastGuideUpdate().after(SDF.parse("2000-01-01")));

     
     Mockito.verify(listener, Mockito.times(1)).alertComplete(Mockito.any(Processor.class));
    
     Mockito.verify(showDao, Mockito.times(1)).readAndLock(Mockito.anyLong());
     Mockito.verify(showDao, Mockito.times(1)).createOrUpdate(Mockito.any(Show.class));
     Mockito.verify(showDao, Mockito.times(1)).readAllEpisodes(Mockito.anyLong());
     Mockito.verify(showDao, Mockito.times(1)).createOrUpdateEpisode(Mockito.any(Episode.class));
     
     Mockito.verify(guideDao, Mockito.times(1)).info(Mockito.anyString());
     Mockito.verify(guideDao, Mockito.times(1)).episodes(Mockito.anyString());     
   }
  
	@Test
  	public void processUpdateTitleResultTest() throws Exception{
     ProcessorListener listener = Mockito.mock(ProcessorListener.class);
     processor.addListener(listener);
       
     Show show = new Show();
     show.setShowId(Long.MAX_VALUE);
     show.setTimezone("ETC");
     ShowGuideSource sgs1 = new ShowGuideSource();
     sgs1.setGuideId("GUIDEID");
     ShowGuideSourcePK sgs1pk = new ShowGuideSourcePK();
     sgs1pk.setGuideSource(GuideSource.TVDB);
     sgs1.setId(sgs1pk);
     show.getGuideSources().add(sgs1);
     show.setLastGuideUpdate(SDF.parse("2000-01-01"));
     show.setAiring(true);
     Episode nextEp = new Episode();
     nextEp.setSeasonNum(1);
     nextEp.setEpisodeNum(1);
     nextEp.setTitle("NEXT_EP");
     nextEp.setAirDate(SDF.parse("2016-01-29"));
     show.setNextEpisode(nextEp);
     processor.setShow(show);
     
     ShowDao showDao = Mockito.mock(ShowDao.class);
     Mockito.when(showDao.readAndLock(Mockito.anyLong())).thenReturn(show);
     Mockito.when(showDao.readAllEpisodes(Mockito.anyLong())).thenReturn(Arrays.asList(nextEp));
     processor.setShowDao(showDao);

     GuideInfoDao guideDao = Mockito.mock(GuideInfoDao.class);
     Mockito.when(guideDao.getGuideSource()).thenReturn(GuideSource.TVDB);
     GuideInfo info = new GuideInfo();
 	  info.setLastUpdated(SDF.parse("2016-01-29"));
     info.setAiring(true);
     Mockito.when(guideDao.info("GUIDEID")).thenReturn(info);
     GuideEpisode guideEp1 = new GuideEpisode();
     guideEp1.setSeasonNum(1);
     guideEp1.setEpisodeNum(1);
     guideEp1.setTitle("GUIDE_EP_1");
     guideEp1.setAirDate(SDF.parse("2016-01-29"));
     Mockito.when(guideDao.episodes("GUIDEID")).thenReturn(Arrays.asList(guideEp1));
     processor.setGuideDao(guideDao);
     
     processor.process();
     
     Assert.assertFalse(processor.isActive());
     Assert.assertTrue(show.getLastGuideUpdate().after(SDF.parse("2000-01-01")));
     
     Mockito.verify(listener, Mockito.times(1)).alertComplete(Mockito.any(Processor.class));
    
     Mockito.verify(showDao, Mockito.times(1)).readAndLock(Mockito.anyLong());
     Mockito.verify(showDao, Mockito.times(1)).createOrUpdate(Mockito.any(Show.class));
     Mockito.verify(showDao, Mockito.times(1)).readAllEpisodes(Mockito.anyLong());
     Mockito.verify(showDao, Mockito.times(1)).createOrUpdateEpisode(nextEp);
     
     Mockito.verify(guideDao, Mockito.times(1)).info(Mockito.anyString());
     Mockito.verify(guideDao, Mockito.times(1)).episodes(Mockito.anyString());     
   }

	@Test
  	public void processUpdateAirDateResultTest() throws Exception{
     ProcessorListener listener = Mockito.mock(ProcessorListener.class);
     processor.addListener(listener);
       
     Show show = new Show();
     show.setShowId(Long.MAX_VALUE);
     show.setTimezone("ETC");
     ShowGuideSource sgs1 = new ShowGuideSource();
     sgs1.setGuideId("GUIDEID");
     ShowGuideSourcePK sgs1pk = new ShowGuideSourcePK();
     sgs1pk.setGuideSource(GuideSource.TVDB);
     sgs1.setId(sgs1pk);
     show.getGuideSources().add(sgs1);
     show.setLastGuideUpdate(SDF.parse("2000-01-01"));
     show.setAiring(true);
     Episode nextEp = new Episode();
     nextEp.setSeasonNum(1);
     nextEp.setEpisodeNum(1);
     nextEp.setTitle("NEXT_EP");
     nextEp.setAirDate(SDF.parse("2016-01-29"));
     show.setNextEpisode(nextEp);
     processor.setShow(show);
     
     ShowDao showDao = Mockito.mock(ShowDao.class);
     Mockito.when(showDao.readAndLock(Mockito.anyLong())).thenReturn(show);
     Mockito.when(showDao.readAllEpisodes(Mockito.anyLong())).thenReturn(Arrays.asList(nextEp));
     processor.setShowDao(showDao);

     GuideInfoDao guideDao = Mockito.mock(GuideInfoDao.class);
     Mockito.when(guideDao.getGuideSource()).thenReturn(GuideSource.TVDB);
     GuideInfo info = new GuideInfo();
 	  info.setLastUpdated(SDF.parse("2016-01-29"));
     info.setAiring(true);
     Mockito.when(guideDao.info("GUIDEID")).thenReturn(info);
     GuideEpisode guideEp1 = new GuideEpisode();
     guideEp1.setSeasonNum(1);
     guideEp1.setEpisodeNum(1);
     guideEp1.setTitle("NEXT_EP");
     guideEp1.setAirDate(SDF.parse("2000-01-01"));
     Mockito.when(guideDao.episodes("GUIDEID")).thenReturn(Arrays.asList(guideEp1));
     processor.setGuideDao(guideDao);
     
     processor.process();
     
     Assert.assertFalse(processor.isActive());
     Assert.assertTrue(show.getLastGuideUpdate().after(SDF.parse("2000-01-01")));
     
     Mockito.verify(listener, Mockito.times(1)).alertComplete(Mockito.any(Processor.class));
    
     Mockito.verify(showDao, Mockito.times(1)).readAndLock(Mockito.anyLong());
     Mockito.verify(showDao, Mockito.times(1)).createOrUpdate(Mockito.any(Show.class));
     Mockito.verify(showDao, Mockito.times(1)).readAllEpisodes(Mockito.anyLong());
     Mockito.verify(showDao, Mockito.times(1)).createOrUpdateEpisode(nextEp);
     
     Mockito.verify(guideDao, Mockito.times(1)).info(Mockito.anyString());
     Mockito.verify(guideDao, Mockito.times(1)).episodes(Mockito.anyString());     
   }
  
	@Test
  	public void processNoUpdateResultTest() throws Exception{
     ProcessorListener listener = Mockito.mock(ProcessorListener.class);
     processor.addListener(listener);
       
     Show show = new Show();
     show.setShowId(Long.MAX_VALUE);
     show.setTimezone("ETC");
     ShowGuideSource sgs1 = new ShowGuideSource();
     sgs1.setGuideId("GUIDEID");
     ShowGuideSourcePK sgs1pk = new ShowGuideSourcePK();
     sgs1pk.setGuideSource(GuideSource.TVDB);
     sgs1.setId(sgs1pk);
     show.getGuideSources().add(sgs1);
     show.setLastGuideUpdate(SDF.parse("2000-01-01"));
     show.setAiring(true);
     Episode nextEp = new Episode();
     nextEp.setSeasonNum(1);
     nextEp.setEpisodeNum(1);
     nextEp.setTitle("NEXT_EP");
     nextEp.setAirDate(SDF.parse("2016-01-29"));
     show.setNextEpisode(nextEp);
     processor.setShow(show);
     
     ShowDao showDao = Mockito.mock(ShowDao.class);
     Mockito.when(showDao.readAndLock(Mockito.anyLong())).thenReturn(show);
     Mockito.when(showDao.readAllEpisodes(Mockito.anyLong())).thenReturn(Arrays.asList(nextEp));
     processor.setShowDao(showDao);

     GuideInfoDao guideDao = Mockito.mock(GuideInfoDao.class);
     Mockito.when(guideDao.getGuideSource()).thenReturn(GuideSource.TVDB);
     GuideInfo info = new GuideInfo();
 	  info.setLastUpdated(SDF.parse("2016-01-29"));
     info.setAiring(true);
     Mockito.when(guideDao.info("GUIDEID")).thenReturn(info);
     GuideEpisode guideEp1 = new GuideEpisode();
     guideEp1.setSeasonNum(1);
     guideEp1.setEpisodeNum(1);
     guideEp1.setTitle("NEXT_EP");
     guideEp1.setAirDate(SDF.parse("2016-01-29"));
     Mockito.when(guideDao.episodes("GUIDEID")).thenReturn(Arrays.asList(guideEp1));
     processor.setGuideDao(guideDao);
     
     processor.process();
     
     Assert.assertFalse(processor.isActive());
     Assert.assertTrue(show.getLastGuideUpdate().after(SDF.parse("2000-01-01")));

     
     Mockito.verify(listener, Mockito.times(1)).alertComplete(Mockito.any(Processor.class));
    
     Mockito.verify(showDao, Mockito.times(1)).readAndLock(Mockito.anyLong());
     Mockito.verify(showDao, Mockito.times(1)).createOrUpdate(Mockito.any(Show.class));
     Mockito.verify(showDao, Mockito.times(1)).readAllEpisodes(Mockito.anyLong());
     Mockito.verify(showDao, Mockito.times(0)).createOrUpdateEpisode(nextEp);
     
     Mockito.verify(guideDao, Mockito.times(1)).info(Mockito.anyString());
     Mockito.verify(guideDao, Mockito.times(1)).episodes(Mockito.anyString());     
   }
  
	@Test
  	public void processStopAtPrevEpTest() throws Exception{
     ProcessorListener listener = Mockito.mock(ProcessorListener.class);
     processor.addListener(listener);
       
     Show show = new Show();
     show.setShowId(Long.MAX_VALUE);
     show.setTimezone("ETC");
     ShowGuideSource sgs1 = new ShowGuideSource();
     sgs1.setGuideId("GUIDEID");
     ShowGuideSourcePK sgs1pk = new ShowGuideSourcePK();
     sgs1pk.setGuideSource(GuideSource.TVDB);
     sgs1.setId(sgs1pk);
     show.getGuideSources().add(sgs1);
     show.setLastGuideUpdate(SDF.parse("2000-01-01"));
     show.setAiring(true);
     Episode ep1 = new Episode();
     ep1.setSeasonNum(1);
     ep1.setEpisodeNum(1);
     ep1.setTitle("EP1");
     ep1.setAirDate(SDF.parse("2015-01-01"));
     //show.setNextEpisode(ep1);
     Episode ep2 = new Episode();
     ep2.setSeasonNum(1);
     ep2.setEpisodeNum(2);
     ep2.setTitle("EP2");
     ep2.setAirDate(SDF.parse("2016-01-29"));
     show.setPreviousEpisode(ep2);
     processor.setShow(show);
     
     ShowDao showDao = Mockito.mock(ShowDao.class);
     Mockito.when(showDao.readAndLock(Mockito.anyLong())).thenReturn(show);
     Mockito.when(showDao.readAllEpisodes(Mockito.anyLong())).thenReturn(Arrays.asList(ep1, ep2));
     processor.setShowDao(showDao);

     GuideInfoDao guideDao = Mockito.mock(GuideInfoDao.class);
     Mockito.when(guideDao.getGuideSource()).thenReturn(GuideSource.TVDB);
     GuideInfo info = new GuideInfo();
 	  info.setLastUpdated(SDF.parse("2016-01-29"));
     info.setAiring(true);
     Mockito.when(guideDao.info("GUIDEID")).thenReturn(info);
     GuideEpisode guideEp1 = new GuideEpisode();
     guideEp1.setSeasonNum(1);
     guideEp1.setEpisodeNum(1);
     guideEp1.setTitle("EP1");
     guideEp1.setAirDate(SDF.parse("2016-01-01"));
     GuideEpisode guideEp2 = new GuideEpisode();
     guideEp2.setSeasonNum(1);
     guideEp2.setEpisodeNum(2);
     guideEp2.setTitle("EP2");
     guideEp2.setAirDate(SDF.parse("2016-01-29"));
     Mockito.when(guideDao.episodes("GUIDEID")).thenReturn(Arrays.asList(guideEp2,guideEp1));
     processor.setGuideDao(guideDao);
     
     processor.process();
     
     Assert.assertFalse(processor.isActive());
     Assert.assertTrue(show.getLastGuideUpdate().after(SDF.parse("2000-01-01")));
     
     Mockito.verify(listener, Mockito.times(1)).alertComplete(Mockito.any(Processor.class));
    
     Mockito.verify(showDao, Mockito.times(1)).readAndLock(Mockito.anyLong());
     Mockito.verify(showDao, Mockito.times(1)).createOrUpdate(Mockito.any(Show.class));
     Mockito.verify(showDao, Mockito.times(1)).readAllEpisodes(Mockito.anyLong());
     Mockito.verify(showDao, Mockito.times(0)).createOrUpdateEpisode(Mockito.any(Episode.class));
     
     Mockito.verify(guideDao, Mockito.times(1)).info(Mockito.anyString());
     Mockito.verify(guideDao, Mockito.times(1)).episodes(Mockito.anyString());     
   }

	@Test
  	public void processSetNextEpAlreadyExistsTest() throws Exception{
     ProcessorListener listener = Mockito.mock(ProcessorListener.class);
     processor.addListener(listener);
       
     Show show = new Show();
     show.setShowId(Long.MAX_VALUE);
     show.setTimezone("ETC");
     ShowGuideSource sgs1 = new ShowGuideSource();
     sgs1.setGuideId("GUIDEID");
     ShowGuideSourcePK sgs1pk = new ShowGuideSourcePK();
     sgs1pk.setGuideSource(GuideSource.TVDB);
     sgs1.setId(sgs1pk);
     show.getGuideSources().add(sgs1);
     show.setLastGuideUpdate(SDF.parse("2000-01-01"));
     show.setAiring(true);
     Episode ep1 = new Episode();
     ep1.setSeasonNum(1);
     ep1.setEpisodeNum(1);
     ep1.setTitle("EP1");
     ep1.setAirDate(SDF.parse("2015-01-01"));
     //show.setNextEpisode(ep1);
     Episode ep2 = new Episode();
     ep2.setSeasonNum(1);
     ep2.setEpisodeNum(2);
     ep2.setTitle("EP2");
     ep2.setAirDate(SDF.parse("2016-01-29"));
     //show.setPreviousEpisode(ep2);
     processor.setShow(show);
     
     ShowDao showDao = Mockito.mock(ShowDao.class);
     Mockito.when(showDao.readAndLock(Mockito.anyLong())).thenReturn(show);
     Mockito.when(showDao.readAllEpisodes(Mockito.anyLong())).thenReturn(Arrays.asList(ep1, ep2));
     processor.setShowDao(showDao);

     GuideInfoDao guideDao = Mockito.mock(GuideInfoDao.class);
     Mockito.when(guideDao.getGuideSource()).thenReturn(GuideSource.TVDB);
     GuideInfo info = new GuideInfo();
 	  info.setLastUpdated(SDF.parse("2016-01-29"));
     info.setAiring(true);
     Mockito.when(guideDao.info("GUIDEID")).thenReturn(info);
     GuideEpisode guideEp1 = new GuideEpisode();
     guideEp1.setSeasonNum(1);
     guideEp1.setEpisodeNum(1);
     guideEp1.setTitle("EP1");
     guideEp1.setAirDate(SDF.parse("2015-01-01"));
     GuideEpisode guideEp2 = new GuideEpisode();
     guideEp2.setSeasonNum(1);
     guideEp2.setEpisodeNum(2);
     guideEp2.setTitle("EP2");
     guideEp2.setAirDate(SDF.parse("2016-01-29"));
     Mockito.when(guideDao.episodes("GUIDEID")).thenReturn(Arrays.asList(guideEp2,guideEp1));
     processor.setGuideDao(guideDao);
     
     processor.process();
     
     Assert.assertFalse(processor.isActive());
     
     Assert.assertNotNull(show.getNextEpisode());
     Assert.assertEquals(ep1, show.getNextEpisode());
     
     Mockito.verify(listener, Mockito.times(1)).alertComplete(Mockito.any(Processor.class));
    
     Mockito.verify(showDao, Mockito.times(1)).readAndLock(Mockito.anyLong());
     Mockito.verify(showDao, Mockito.times(1)).createOrUpdate(Mockito.any(Show.class));
     Mockito.verify(showDao, Mockito.times(1)).readAllEpisodes(Mockito.anyLong());
     Mockito.verify(showDao, Mockito.times(0)).createOrUpdateEpisode(Mockito.any(Episode.class));
     
     Mockito.verify(guideDao, Mockito.times(1)).info(Mockito.anyString());
     Mockito.verify(guideDao, Mockito.times(1)).episodes(Mockito.anyString());     
   }
  
	@Test
  	public void processSetNextEpCreateTest() throws Exception{
     ProcessorListener listener = Mockito.mock(ProcessorListener.class);
     processor.addListener(listener);
       
     Show show = new Show();
     show.setShowId(Long.MAX_VALUE);
     show.setTimezone("ETC");
     ShowGuideSource sgs1 = new ShowGuideSource();
     sgs1.setGuideId("GUIDEID");
     ShowGuideSourcePK sgs1pk = new ShowGuideSourcePK();
     sgs1pk.setGuideSource(GuideSource.TVDB);
     sgs1.setId(sgs1pk);
     show.getGuideSources().add(sgs1);
     show.setLastGuideUpdate(SDF.parse("2000-01-01"));
     show.setAiring(true);
     Episode ep1 = new Episode();
     ep1.setSeasonNum(1);
     ep1.setEpisodeNum(1);
     ep1.setTitle("EP1");
     ep1.setAirDate(SDF.parse("2015-01-01"));
     //show.setNextEpisode(ep1);
     Episode ep2 = new Episode();
     ep2.setSeasonNum(1);
     ep2.setEpisodeNum(2);
     ep2.setTitle("EP2");
     ep2.setAirDate(SDF.parse("2016-01-29"));
     //show.setPreviousEpisode(ep2);
     processor.setShow(show);
     
     ShowDao showDao = Mockito.mock(ShowDao.class);
     Mockito.when(showDao.readAndLock(Mockito.anyLong())).thenReturn(show);
     Mockito.when(showDao.readAllEpisodes(Mockito.anyLong())).thenReturn(Arrays.asList(ep2));
     processor.setShowDao(showDao);

     GuideInfoDao guideDao = Mockito.mock(GuideInfoDao.class);
     Mockito.when(guideDao.getGuideSource()).thenReturn(GuideSource.TVDB);
     GuideInfo info = new GuideInfo();
 	  info.setLastUpdated(SDF.parse("2016-01-29"));
     info.setAiring(true);
     Mockito.when(guideDao.info("GUIDEID")).thenReturn(info);
     GuideEpisode guideEp1 = new GuideEpisode();
     guideEp1.setSeasonNum(1);
     guideEp1.setEpisodeNum(1);
     guideEp1.setTitle("EP1");
     guideEp1.setAirDate(SDF.parse("2015-01-01"));
     GuideEpisode guideEp2 = new GuideEpisode();
     guideEp2.setSeasonNum(1);
     guideEp2.setEpisodeNum(2);
     guideEp2.setTitle("EP2");
     guideEp2.setAirDate(SDF.parse("2016-01-29"));
     Mockito.when(guideDao.episodes("GUIDEID")).thenReturn(Arrays.asList(guideEp2,guideEp1));
     processor.setGuideDao(guideDao);
     
     processor.process();
     
     Assert.assertFalse(processor.isActive());
     Assert.assertNotNull(show.getNextEpisode());
     Assert.assertEquals(1, show.getNextEpisode().getSeasonNum());
     Assert.assertEquals(1, show.getNextEpisode().getEpisodeNum());
     Assert.assertEquals("EP1", show.getNextEpisode().getTitle());
     Assert.assertEquals(SDF.parse("2015-01-01"), show.getNextEpisode().getAirDate());
     
     Assert.assertNull(show.getPreviousEpisode());
     
     Mockito.verify(listener, Mockito.times(1)).alertComplete(Mockito.any(Processor.class));
    
     Mockito.verify(showDao, Mockito.times(1)).readAndLock(Mockito.anyLong());
     Mockito.verify(showDao, Mockito.times(1)).createOrUpdate(Mockito.any(Show.class));
     Mockito.verify(showDao, Mockito.times(1)).readAllEpisodes(Mockito.anyLong());
     Mockito.verify(showDao, Mockito.times(1)).createOrUpdateEpisode(Mockito.any(Episode.class));
     
     Mockito.verify(guideDao, Mockito.times(1)).info(Mockito.anyString());
     Mockito.verify(guideDao, Mockito.times(1)).episodes(Mockito.anyString());     
   }
  
	@Test
  	public void processEpisodesExceptionTest() throws Exception{
     ProcessorListener listener = Mockito.mock(ProcessorListener.class);
     processor.addListener(listener);
       
     Show show = new Show();
     show.setShowId(Long.MAX_VALUE);
     ShowGuideSource sgs1 = new ShowGuideSource();
     sgs1.setGuideId("GUIDEID");
     ShowGuideSourcePK sgs1pk = new ShowGuideSourcePK();
     sgs1pk.setGuideSource(GuideSource.TVDB);
     sgs1.setId(sgs1pk);
     show.getGuideSources().add(sgs1);
     show.setLastGuideUpdate(SDF.parse("2000-01-01"));
     show.setAiring(true);
     processor.setShow(show);
     
     ShowDao showDao = Mockito.mock(ShowDao.class);
     Mockito.when(showDao.readAndLock(Mockito.anyLong())).thenReturn(show);
     processor.setShowDao(showDao);

     GuideInfoDao guideDao = Mockito.mock(GuideInfoDao.class);
     Mockito.when(guideDao.getGuideSource()).thenReturn(GuideSource.TVDB);
     GuideInfo info = new GuideInfo();
 	  info.setLastUpdated(SDF.parse("2016-01-29"));
     info.setAiring(true);
     Mockito.when(guideDao.info("GUIDEID")).thenReturn(info);
     Mockito.when(guideDao.episodes("GUIDEID")).thenThrow(GuideException.class);
     processor.setGuideDao(guideDao);
     
     try{
     	processor.process();
     }catch(GuideException ex){
      //do nothing 
     }
     
     Assert.assertFalse(processor.isActive());
     
     Mockito.verify(listener, Mockito.times(1)).alertComplete(Mockito.any(Processor.class));
    
     Mockito.verify(showDao, Mockito.times(1)).readAndLock(Mockito.anyLong());
     Mockito.verify(showDao, Mockito.times(0)).createOrUpdate(Mockito.any(Show.class));
     Mockito.verify(showDao, Mockito.times(0)).readAllEpisodes(Mockito.anyLong());
     Mockito.verify(showDao, Mockito.times(0)).createOrUpdateEpisode(Mockito.any(Episode.class));
     
     Mockito.verify(guideDao, Mockito.times(1)).info(Mockito.anyString());
     Mockito.verify(guideDao, Mockito.times(1)).episodes(Mockito.anyString());     
   }
  
	@Test
  	public void processReadAllEpisodesExceptionTest() throws Exception{
     ProcessorListener listener = Mockito.mock(ProcessorListener.class);
     processor.addListener(listener);
       
     Show show = new Show();
     show.setShowId(Long.MAX_VALUE);
     ShowGuideSource sgs1 = new ShowGuideSource();
     sgs1.setGuideId("GUIDEID");
     ShowGuideSourcePK sgs1pk = new ShowGuideSourcePK();
     sgs1pk.setGuideSource(GuideSource.TVDB);
     sgs1.setId(sgs1pk);
     show.getGuideSources().add(sgs1);
     show.setLastGuideUpdate(SDF.parse("2000-01-01"));
     show.setAiring(true);
     processor.setShow(show);
     
     ShowDao showDao = Mockito.mock(ShowDao.class);
     Mockito.when(showDao.readAndLock(Mockito.anyLong())).thenReturn(show);
     Mockito.when(showDao.readAllEpisodes(Mockito.anyLong())).thenThrow(ShowException.class);
     processor.setShowDao(showDao);

     GuideInfoDao guideDao = Mockito.mock(GuideInfoDao.class);
     Mockito.when(guideDao.getGuideSource()).thenReturn(GuideSource.TVDB);
     GuideInfo info = new GuideInfo();
 	  info.setLastUpdated(SDF.parse("2016-01-29"));
     info.setAiring(true);
     Mockito.when(guideDao.info("GUIDEID")).thenReturn(info);
     GuideEpisode guideEp1 = new GuideEpisode();
     Mockito.when(guideDao.episodes("GUIDEID")).thenReturn(Arrays.asList(guideEp1));
     processor.setGuideDao(guideDao);
     
     try{
     	processor.process();
     }catch(ShowException ex){
      //do nothing 
     }
     
     Assert.assertFalse(processor.isActive());
     
     Mockito.verify(listener, Mockito.times(1)).alertComplete(Mockito.any(Processor.class));
    
     Mockito.verify(showDao, Mockito.times(1)).readAndLock(Mockito.anyLong());
     Mockito.verify(showDao, Mockito.times(0)).createOrUpdate(Mockito.any(Show.class));
     Mockito.verify(showDao, Mockito.times(1)).readAllEpisodes(Mockito.anyLong());
     Mockito.verify(showDao, Mockito.times(0)).createOrUpdateEpisode(Mockito.any(Episode.class));
     
     Mockito.verify(guideDao, Mockito.times(1)).info(Mockito.anyString());
     Mockito.verify(guideDao, Mockito.times(1)).episodes(Mockito.anyString());     
   }
}
