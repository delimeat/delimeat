package io.delimeat.core.processor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;

import io.delimeat.core.config.Config;
import io.delimeat.core.guide.GuideEpisode;
import io.delimeat.core.guide.GuideException;
import io.delimeat.core.guide.GuideInfo;
import io.delimeat.core.guide.GuideDao;
import io.delimeat.core.guide.GuideSource;
import io.delimeat.core.show.Episode;
import io.delimeat.core.show.Show;
import io.delimeat.core.show.ShowDao;
import io.delimeat.core.show.ShowException;

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
	public void configTest() {
		Assert.assertNull(processor.getConfig());
		Config config = new Config();
		processor.setConfig(config);
		Assert.assertEquals(config, processor.getConfig());
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
		GuideDao mockedDao = Mockito.mock(GuideDao.class);
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
    public void alertListenersCompleteTest(){
      ProcessorListener listener = Mockito.mock(ProcessorListener.class);
      processor.addListener(listener);
      processor.alertListenersComplete();

      Mockito.verify(listener).alertComplete(processor);
    }
  
  	@Test
  	public void processAlreadyActiveTest() throws Exception{
     ProcessorListener listener = Mockito.mock(ProcessorListener.class);
     processor.addListener(listener);

     ShowDao showDao = Mockito.mock(ShowDao.class);
     processor.setShowDao(showDao);

     GuideDao guideDao = Mockito.mock(GuideDao.class);
     processor.setGuideDao(guideDao);
     
     Show show = new Show();
     processor.setShow(show);
     
     processor.setActive(true);
     
     processor.process();
     
     Assert.assertTrue(processor.isActive());
     
     Mockito.verifyZeroInteractions(listener);
    
     Mockito.verifyZeroInteractions(showDao);
     
     Mockito.verifyZeroInteractions(guideDao);    
   }

  	@Test
  	public void processReadAndLockExceptionTest() throws Exception{
     ProcessorListener listener = Mockito.mock(ProcessorListener.class);
     processor.addListener(listener);

     ShowDao showDao = Mockito.mock(ShowDao.class);
     Mockito.when(showDao.readAndLock(Long.MAX_VALUE)).thenThrow(ShowException.class);
     processor.setShowDao(showDao);

     GuideDao guideDao = Mockito.mock(GuideDao.class);
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
     
     Mockito.verify(listener).alertComplete(processor);
     Mockito.verifyNoMoreInteractions(listener);

    
     Mockito.verify(showDao).readAndLock(Long.MAX_VALUE);
     Mockito.verifyNoMoreInteractions(showDao);

     Mockito.verifyZeroInteractions(guideDao);     
   }
  
  	@Test
  	public void processNoGuideIdFoundTest() throws Exception{
     ProcessorListener listener = Mockito.mock(ProcessorListener.class);
     processor.addListener(listener);
       
     Show show = new Show();
     show.setShowId(Long.MAX_VALUE);
     processor.setShow(show);
     
     ShowDao showDao = Mockito.mock(ShowDao.class);
     Mockito.when(showDao.readAndLock(Long.MAX_VALUE)).thenReturn(show);
     processor.setShowDao(showDao);

     GuideDao guideDao = Mockito.mock(GuideDao.class);
     Mockito.when(guideDao.getGuideSource()).thenReturn(GuideSource.TVDB);
     processor.setGuideDao(guideDao);
     
     processor.process();
     
     Assert.assertFalse(processor.isActive());
     
     Mockito.verify(listener).alertComplete(processor);
     Mockito.verifyNoMoreInteractions(guideDao);     

     Mockito.verify(showDao).readAndLock(Long.MAX_VALUE);
     Mockito.verify(showDao).createOrUpdate(show);
     Mockito.verifyNoMoreInteractions(showDao);
     
     Mockito.verifyZeroInteractions(guideDao);     
   }
  
	@Test
  	public void processGuideInfoExceptionTest() throws Exception{
     ProcessorListener listener = Mockito.mock(ProcessorListener.class);
     processor.addListener(listener);
       
     Show show = new Show();
     show.setShowId(Long.MAX_VALUE);
     show.setGuideId("GUIDEID");
     processor.setShow(show);
     
     ShowDao showDao = Mockito.mock(ShowDao.class);
     Mockito.when(showDao.readAndLock(Long.MAX_VALUE)).thenReturn(show);
     processor.setShowDao(showDao);

     GuideDao guideDao = Mockito.mock(GuideDao.class);
     Mockito.when(guideDao.getGuideSource()).thenReturn(GuideSource.TVDB);
     Mockito.when(guideDao.info("GUIDEID")).thenThrow(GuideException.class);
     processor.setGuideDao(guideDao);
     
     try{
     	processor.process();
     }catch(GuideException ex){
       //do nothing
     }
     
     Assert.assertFalse(processor.isActive());
     
     Mockito.verify(listener).alertComplete(processor);
     Mockito.verifyNoMoreInteractions(listener);     

     Mockito.verify(showDao).readAndLock(Long.MAX_VALUE);
     Mockito.verifyNoMoreInteractions(showDao);

     Mockito.verify(guideDao).info("GUIDEID");
     Mockito.verifyNoMoreInteractions(guideDao);
    
   }
  
	@Test
  	public void processGuideInfoBeforeTest() throws Exception{
     ProcessorListener listener = Mockito.mock(ProcessorListener.class);
     processor.addListener(listener);
       
     Show show = new Show();
     show.setShowId(Long.MAX_VALUE);
     show.setGuideId("GUIDEID");
     show.setLastGuideUpdate(SDF.parse("2016-01-29"));
     show.setLastGuideCheck(SDF.parse("2000-01-01"));
     processor.setShow(show);
     
     ShowDao showDao = Mockito.mock(ShowDao.class);
     Mockito.when(showDao.readAndLock(Long.MAX_VALUE)).thenReturn(show);
     processor.setShowDao(showDao);

     GuideDao guideDao = Mockito.mock(GuideDao.class);
     Mockito.when(guideDao.getGuideSource()).thenReturn(GuideSource.TVDB);
     GuideInfo info = new GuideInfo();
 	 info.setLastUpdated(SDF.parse("2000-01-01"));
     Mockito.when(guideDao.info("GUIDEID")).thenReturn(info);
     processor.setGuideDao(guideDao);
     
     processor.process();
     
     Assert.assertFalse(processor.isActive());
     Assert.assertTrue(show.getLastGuideUpdate().after(SDF.parse("2000-01-01")));
     Assert.assertTrue(show.getLastGuideCheck().after(SDF.parse("2000-01-01")));
     
     Mockito.verify(listener).alertComplete(processor);
     Mockito.verifyNoMoreInteractions(listener);     
    
     Mockito.verify(showDao).readAndLock(Long.MAX_VALUE);
     Mockito.verify(showDao).createOrUpdate(show);
     Mockito.verifyNoMoreInteractions(showDao);
     
     Mockito.verify(guideDao).info("GUIDEID");
     Mockito.verifyNoMoreInteractions(guideDao);
    
   }

	@Test
  	public void processAiringUpdateTest() throws Exception{
     ProcessorListener listener = Mockito.mock(ProcessorListener.class);
     processor.addListener(listener);
       
     Show show = new Show();
     show.setShowId(Long.MAX_VALUE);
     show.setGuideId("GUIDEID");
     show.setLastGuideUpdate(SDF.parse("2000-01-01"));
     show.setLastGuideCheck(SDF.parse("2000-01-01"));
     show.setAiring(true);
     processor.setShow(show);
     
     ShowDao showDao = Mockito.mock(ShowDao.class);
     Mockito.when(showDao.readAndLock(Long.MAX_VALUE)).thenReturn(show);
     processor.setShowDao(showDao);

     GuideDao guideDao = Mockito.mock(GuideDao.class);
     Mockito.when(guideDao.getGuideSource()).thenReturn(GuideSource.TVDB);
     GuideInfo info = new GuideInfo();
 	 info.setLastUpdated(SDF.parse("2016-01-29"));
     info.setAiring(false);
     Mockito.when(guideDao.info("GUIDEID")).thenReturn(info);
     processor.setGuideDao(guideDao);
     
     processor.process();
     
     Assert.assertFalse(processor.isActive());
     Assert.assertFalse(show.isAiring());
     
     Mockito.verify(listener).alertComplete(processor);
     Mockito.verifyNoMoreInteractions(listener);     

     Mockito.verify(showDao).readAndLock(Long.MAX_VALUE);
     Mockito.verify(showDao).createOrUpdate(show);
     Mockito.verifyNoMoreInteractions(showDao);     

     Mockito.verify(guideDao).info("GUIDEID");
     Mockito.verify(guideDao).episodes("GUIDEID"); 
     Mockito.verifyNoMoreInteractions(guideDao);
   }
  
	@Test
  	public void processAiringNoUpdateNoResultsTest() throws Exception{
     ProcessorListener listener = Mockito.mock(ProcessorListener.class);
     processor.addListener(listener);
       
     Show show = new Show();
     show.setShowId(Long.MAX_VALUE);
     show.setGuideId("GUIDEID");
     show.setLastGuideUpdate(SDF.parse("2000-01-01"));
     show.setLastGuideCheck(SDF.parse("2000-01-01"));
     show.setAiring(true);
     processor.setShow(show);
     
     ShowDao showDao = Mockito.mock(ShowDao.class);
     Mockito.when(showDao.readAndLock(Long.MAX_VALUE)).thenReturn(show);
     processor.setShowDao(showDao);

     GuideDao guideDao = Mockito.mock(GuideDao.class);
     Mockito.when(guideDao.getGuideSource()).thenReturn(GuideSource.TVDB);
     GuideInfo info = new GuideInfo();
 	 info.setLastUpdated(SDF.parse("2016-01-29"));
     info.setAiring(true);
     Mockito.when(guideDao.info("GUIDEID")).thenReturn(info);
     processor.setGuideDao(guideDao);
     
     processor.process();
     
     Assert.assertFalse(processor.isActive());
     Assert.assertTrue(show.isAiring());
     Assert.assertTrue(show.getLastGuideUpdate().equals(SDF.parse("2000-01-01")));
     Assert.assertTrue(show.getLastGuideCheck().after(SDF.parse("2000-01-01")));
    
     Mockito.verify(listener).alertComplete(processor);
     Mockito.verifyNoMoreInteractions(listener);
    
     Mockito.verify(showDao).readAndLock(Long.MAX_VALUE);
     Mockito.verify(showDao).createOrUpdate(show);
     Mockito.verifyNoMoreInteractions(showDao);
     
     Mockito.verify(guideDao).info("GUIDEID");
     Mockito.verify(guideDao).episodes("GUIDEID"); 
     Mockito.verifyNoMoreInteractions(guideDao);
   }
  
	@Test
  	public void processNewResultTest() throws Exception{
     ProcessorListener listener = Mockito.mock(ProcessorListener.class);
     processor.addListener(listener);
       
     Show show = new Show();
     show.setShowId(Long.MAX_VALUE);
     show.setTimezone("ETC");
     show.setGuideId("GUIDEID");
     show.setLastGuideUpdate(SDF.parse("2000-01-01"));
     show.setLastGuideCheck(SDF.parse("2000-01-01"));
     show.setAiring(true);
     Episode nextEp = new Episode();
     show.setNextEpisode(nextEp);
     processor.setShow(show);
     
     ShowDao showDao = Mockito.mock(ShowDao.class);
     Mockito.when(showDao.readAndLock(Long.MAX_VALUE)).thenReturn(show);
     processor.setShowDao(showDao);

     GuideDao guideDao = Mockito.mock(GuideDao.class);
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
     Assert.assertTrue(show.getLastGuideCheck().after(SDF.parse("2000-01-01")));
     
     Mockito.verify(listener).alertComplete(processor);
     Mockito.verifyNoMoreInteractions(listener);
    
     Mockito.verify(showDao).readAndLock(Long.MAX_VALUE);
     Mockito.verify(showDao).createOrUpdate(show);
     Mockito.verify(showDao).readAllEpisodes(Long.MAX_VALUE);
     Mockito.verify(showDao).createOrUpdateEpisodes(ArgumentMatchers.anyList());
     Mockito.verifyNoMoreInteractions(showDao);
     
     Mockito.verify(guideDao).info("GUIDEID");
     Mockito.verify(guideDao).episodes("GUIDEID"); 
     Mockito.verifyNoMoreInteractions(guideDao);
   }
  
	@Test
  	public void processUpdateTitleResultTest() throws Exception{
     ProcessorListener listener = Mockito.mock(ProcessorListener.class);
     processor.addListener(listener);
       
     Show show = new Show();
     show.setShowId(Long.MAX_VALUE);
     show.setTimezone("ETC");
     show.setGuideId("GUIDEID");
     show.setLastGuideUpdate(SDF.parse("2000-01-01"));
     show.setLastGuideCheck(SDF.parse("2000-01-01"));
     show.setAiring(true);
     Episode nextEp = new Episode();
     nextEp.setSeasonNum(1);
     nextEp.setEpisodeNum(1);
     nextEp.setTitle("NEXT_EP");
     nextEp.setAirDate(SDF.parse("2016-01-29"));
     show.setNextEpisode(nextEp);
     processor.setShow(show);
     
     ShowDao showDao = Mockito.mock(ShowDao.class);
     Mockito.when(showDao.readAndLock(Long.MAX_VALUE)).thenReturn(show);
     Mockito.when(showDao.readAllEpisodes(Long.MAX_VALUE)).thenReturn(Arrays.asList(nextEp));
     processor.setShowDao(showDao);

     GuideDao guideDao = Mockito.mock(GuideDao.class);
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
     Assert.assertTrue(show.getLastGuideCheck().after(SDF.parse("2000-01-01")));
     
     Mockito.verify(listener).alertComplete(processor);
     Mockito.verifyNoMoreInteractions(listener);
    
     Mockito.verify(showDao).readAndLock(Long.MAX_VALUE);
     Mockito.verify(showDao).createOrUpdate(show);
     Mockito.verify(showDao).readAllEpisodes(Long.MAX_VALUE);
     Mockito.verify(showDao).createOrUpdateEpisodes(ArgumentMatchers.anyList());
     Mockito.verifyNoMoreInteractions(showDao);
     
     Mockito.verify(guideDao).info("GUIDEID");
     Mockito.verify(guideDao).episodes("GUIDEID");  
     Mockito.verifyNoMoreInteractions(guideDao);
   }

	@Test
  	public void processUpdateAirDateResultTest() throws Exception{
     ProcessorListener listener = Mockito.mock(ProcessorListener.class);
     processor.addListener(listener);
       
     Show show = new Show();
     show.setShowId(Long.MAX_VALUE);
     show.setTimezone("ETC");
     show.setGuideId("GUIDEID");
     show.setLastGuideUpdate(SDF.parse("2000-01-01"));
     show.setLastGuideCheck(SDF.parse("2000-01-01"));
     show.setAiring(true);
     Episode nextEp = new Episode();
     nextEp.setSeasonNum(1);
     nextEp.setEpisodeNum(1);
     nextEp.setTitle("NEXT_EP");
     nextEp.setAirDate(SDF.parse("2016-01-29"));
     show.setNextEpisode(nextEp);
     processor.setShow(show);
     
     ShowDao showDao = Mockito.mock(ShowDao.class);
     Mockito.when(showDao.readAndLock(Long.MAX_VALUE)).thenReturn(show);
     Mockito.when(showDao.readAllEpisodes(Long.MAX_VALUE)).thenReturn(Arrays.asList(nextEp));
     processor.setShowDao(showDao);

     GuideDao guideDao = Mockito.mock(GuideDao.class);
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
     Assert.assertTrue(show.getLastGuideCheck().after(SDF.parse("2000-01-01")));
     
     Mockito.verify(listener).alertComplete(processor);
     Mockito.verifyNoMoreInteractions(listener);
    
     Mockito.verify(showDao).readAndLock(Long.MAX_VALUE);
     Mockito.verify(showDao).createOrUpdate(show);
     Mockito.verify(showDao).readAllEpisodes(Long.MAX_VALUE);
     Mockito.verify(showDao).createOrUpdateEpisodes(ArgumentMatchers.anyList());
     Mockito.verifyNoMoreInteractions(showDao);
     
     Mockito.verify(guideDao).info("GUIDEID");
     Mockito.verify(guideDao).episodes("GUIDEID");
     Mockito.verifyNoMoreInteractions(guideDao);
   }
  
	@Test
  	public void processNoUpdateResultTest() throws Exception{
     ProcessorListener listener = Mockito.mock(ProcessorListener.class);
     processor.addListener(listener);
       
     Show show = new Show();
     show.setShowId(Long.MAX_VALUE);
     show.setTimezone("ETC");
     show.setGuideId("GUIDEID");
     show.setLastGuideUpdate(SDF.parse("2000-01-01"));
     show.setLastGuideCheck(SDF.parse("2000-01-01"));
     show.setAiring(true);
     Episode nextEp = new Episode();
     nextEp.setSeasonNum(1);
     nextEp.setEpisodeNum(1);
     nextEp.setTitle("NEXT_EP");
     nextEp.setAirDate(SDF.parse("2016-01-29"));
     show.setNextEpisode(nextEp);
     processor.setShow(show);
     
     ShowDao showDao = Mockito.mock(ShowDao.class);
     Mockito.when(showDao.readAndLock(Long.MAX_VALUE)).thenReturn(show);
     Mockito.when(showDao.readAllEpisodes(Long.MAX_VALUE)).thenReturn(Arrays.asList(nextEp));
     processor.setShowDao(showDao);

     GuideDao guideDao = Mockito.mock(GuideDao.class);
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
     Assert.assertTrue(show.getLastGuideUpdate().equals(SDF.parse("2000-01-01")));
     Assert.assertTrue(show.getLastGuideCheck().after(SDF.parse("2000-01-01")));

     Mockito.verify(listener).alertComplete(processor);
     Mockito.verifyNoMoreInteractions(listener);
    
     Mockito.verify(showDao).readAndLock(Long.MAX_VALUE);
     Mockito.verify(showDao).createOrUpdate(show);
     Mockito.verify(showDao).readAllEpisodes(Long.MAX_VALUE);
     Mockito.verifyNoMoreInteractions(showDao);
     
     Mockito.verify(guideDao).info("GUIDEID");
     Mockito.verify(guideDao).episodes("GUIDEID");
     Mockito.verifyNoMoreInteractions(guideDao);
   }
  
	@Test
  	public void processStopAtPrevEpTest() throws Exception{
     ProcessorListener listener = Mockito.mock(ProcessorListener.class);
     processor.addListener(listener);
       
     Show show = new Show();
     show.setShowId(Long.MAX_VALUE);
     show.setTimezone("ETC");
     show.setGuideId("GUIDEID");
     show.setLastGuideUpdate(SDF.parse("2000-01-01"));
     show.setLastGuideCheck(SDF.parse("2000-01-01"));
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
     Mockito.when(showDao.readAndLock(Long.MAX_VALUE)).thenReturn(show);
     Mockito.when(showDao.readAllEpisodes(Long.MAX_VALUE)).thenReturn(Arrays.asList(ep1, ep2));
     processor.setShowDao(showDao);

     GuideDao guideDao = Mockito.mock(GuideDao.class);
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
     Mockito.when(guideDao.episodes("GUIDEID")).thenReturn(Arrays.asList(guideEp1,guideEp2));
     processor.setGuideDao(guideDao);
     
     processor.process();
     
     Assert.assertFalse(processor.isActive());
     Assert.assertTrue(show.getLastGuideUpdate().equals(SDF.parse("2000-01-01")));
     Assert.assertTrue(show.getLastGuideCheck().after(SDF.parse("2000-01-01")));
     
     Mockito.verify(listener).alertComplete(processor);
     Mockito.verifyNoMoreInteractions(listener);
    
     Mockito.verify(showDao).readAndLock(Long.MAX_VALUE);
     Mockito.verify(showDao).readAllEpisodes(Long.MAX_VALUE);
     Mockito.verify(showDao).createOrUpdate(show);
     Mockito.verifyNoMoreInteractions(showDao);
     
     Mockito.verify(guideDao).info("GUIDEID");
     Mockito.verify(guideDao).episodes("GUIDEID");
     Mockito.verifyNoMoreInteractions(guideDao);
   }

	@Test
  	public void processStopBeforePrevEpTest() throws Exception{
     ProcessorListener listener = Mockito.mock(ProcessorListener.class);
     processor.addListener(listener);
       
     Show show = new Show();
     show.setShowId(Long.MAX_VALUE);
     show.setTimezone("ETC");
     show.setGuideId("GUIDEID");
     show.setLastGuideUpdate(SDF.parse("2000-01-01"));
     show.setLastGuideCheck(SDF.parse("2000-01-01"));
     show.setAiring(true);
     Episode ep1 = new Episode();
     ep1.setSeasonNum(2);
     ep1.setEpisodeNum(1);
     ep1.setTitle("EP1");
     ep1.setAirDate(SDF.parse("2015-01-01"));
     //show.setNextEpisode(ep1);
     Episode ep2 = new Episode();
     ep2.setSeasonNum(2);
     ep2.setEpisodeNum(2);
     ep2.setTitle("EP2");
     ep2.setAirDate(SDF.parse("2016-01-29"));
     show.setPreviousEpisode(ep2);
     processor.setShow(show);
     
     ShowDao showDao = Mockito.mock(ShowDao.class);
     Mockito.when(showDao.readAndLock(Long.MAX_VALUE)).thenReturn(show);
     Mockito.when(showDao.readAllEpisodes(Long.MAX_VALUE)).thenReturn(Arrays.asList(ep1, ep2));
     processor.setShowDao(showDao);

     GuideDao guideDao = Mockito.mock(GuideDao.class);
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
     Assert.assertTrue(show.getLastGuideUpdate().equals(SDF.parse("2000-01-01")));
     Assert.assertTrue(show.getLastGuideCheck().after(SDF.parse("2000-01-01")));
     
     Mockito.verify(listener).alertComplete(processor);
     Mockito.verifyNoMoreInteractions(listener);
    
     Mockito.verify(showDao).readAndLock(Long.MAX_VALUE);
     Mockito.verify(showDao).createOrUpdate(show);
     Mockito.verify(showDao).readAllEpisodes(Long.MAX_VALUE);
     Mockito.verifyNoMoreInteractions(showDao);
     
     Mockito.verify(guideDao).info("GUIDEID");
     Mockito.verify(guideDao).episodes("GUIDEID");
     Mockito.verifyNoMoreInteractions(guideDao);
   }
	
	@Test
  	public void processSetNextEpAlreadyExistsTest() throws Exception{
     ProcessorListener listener = Mockito.mock(ProcessorListener.class);
     processor.addListener(listener);
       
     Show show = new Show();
     show.setShowId(Long.MAX_VALUE);
     show.setTimezone("ETC");
     show.setGuideId("GUIDEID");
     show.setLastGuideUpdate(SDF.parse("2000-01-01"));
     show.setLastGuideCheck(SDF.parse("2000-01-01"));
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
     Mockito.when(showDao.readAndLock(Long.MAX_VALUE)).thenReturn(show);
     Mockito.when(showDao.readAllEpisodes(Long.MAX_VALUE)).thenReturn(Arrays.asList(ep1, ep2));
     processor.setShowDao(showDao);

     GuideDao guideDao = Mockito.mock(GuideDao.class);
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
     Assert.assertTrue(show.getLastGuideUpdate().after(SDF.parse("2000-01-01")));
     Assert.assertTrue(show.getLastGuideCheck().after(SDF.parse("2000-01-01")));
     
     Assert.assertNotNull(show.getNextEpisode());
     Assert.assertEquals(ep1, show.getNextEpisode());
     
     Mockito.verify(listener).alertComplete(processor);
     Mockito.verifyNoMoreInteractions(listener);
    
     Mockito.verify(showDao).readAndLock(Long.MAX_VALUE);
     Mockito.verify(showDao).createOrUpdate(show);
     Mockito.verify(showDao).readAllEpisodes(Long.MAX_VALUE);
     Mockito.verifyNoMoreInteractions(showDao);
     
     Mockito.verify(guideDao).info("GUIDEID");
     Mockito.verify(guideDao).episodes("GUIDEID"); 
     Mockito.verifyNoMoreInteractions(guideDao);
   }
  
	@Test
  	public void processSetNextEpCreateTest() throws Exception{
     ProcessorListener listener = Mockito.mock(ProcessorListener.class);
     processor.addListener(listener);
       
     Show show = new Show();
     show.setShowId(Long.MAX_VALUE);
     show.setTimezone("ETC");
     show.setGuideId("GUIDEID");
     show.setLastGuideUpdate(SDF.parse("2000-01-01"));
     show.setLastGuideCheck(SDF.parse("2000-01-01"));
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
     Mockito.when(showDao.readAndLock(Long.MAX_VALUE)).thenReturn(show);
     Mockito.when(showDao.readAllEpisodes(Long.MAX_VALUE)).thenReturn(Arrays.asList(ep2));
     processor.setShowDao(showDao);

     GuideDao guideDao = Mockito.mock(GuideDao.class);
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
     Assert.assertTrue(show.getLastGuideUpdate().after(SDF.parse("2000-01-01")));
     Assert.assertTrue(show.getLastGuideCheck().after(SDF.parse("2000-01-01")));
     
     Assert.assertNotNull(show.getNextEpisode());
     Assert.assertEquals(1, show.getNextEpisode().getSeasonNum());
     Assert.assertEquals(1, show.getNextEpisode().getEpisodeNum());
     Assert.assertEquals("EP1", show.getNextEpisode().getTitle());
     Assert.assertEquals(SDF.parse("2015-01-01"), show.getNextEpisode().getAirDate());
     
     Assert.assertNull(show.getPreviousEpisode());
     
     Mockito.verify(listener).alertComplete(processor);
     Mockito.verifyNoMoreInteractions(listener);
    
     Mockito.verify(showDao).readAndLock(Long.MAX_VALUE);
     Mockito.verify(showDao).createOrUpdate(show);
     Mockito.verify(showDao).readAllEpisodes(Long.MAX_VALUE);
     Mockito.verify(showDao).createOrUpdateEpisodes(ArgumentMatchers.anyList());
     Mockito.verifyNoMoreInteractions(showDao);
     
     Mockito.verify(guideDao).info("GUIDEID");
     Mockito.verify(guideDao).episodes("GUIDEID");  
     Mockito.verifyNoMoreInteractions(guideDao);
   }
  
	@Test
  	public void processEpisodesExceptionTest() throws Exception{
     ProcessorListener listener = Mockito.mock(ProcessorListener.class);
     processor.addListener(listener);
       
     Show show = new Show();
     show.setShowId(Long.MAX_VALUE);
     show.setGuideId("GUIDEID");
     show.setLastGuideUpdate(SDF.parse("2000-01-01"));
     show.setLastGuideCheck(SDF.parse("2000-01-01"));
     show.setAiring(true);
     processor.setShow(show);
     
     ShowDao showDao = Mockito.mock(ShowDao.class);
     Mockito.when(showDao.readAndLock(Long.MAX_VALUE)).thenReturn(show);
     processor.setShowDao(showDao);

     GuideDao guideDao = Mockito.mock(GuideDao.class);
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
     Assert.assertTrue(show.getLastGuideUpdate().equals(SDF.parse("2000-01-01")));
     Assert.assertTrue(show.getLastGuideCheck().equals(SDF.parse("2000-01-01")));
     
     Mockito.verify(listener).alertComplete(processor);
     Mockito.verifyNoMoreInteractions(listener);
    
     Mockito.verify(showDao).readAndLock(Long.MAX_VALUE);
     Mockito.verifyNoMoreInteractions(showDao);
     
     Mockito.verify(guideDao).info("GUIDEID");
     Mockito.verify(guideDao).episodes("GUIDEID");
     Mockito.verifyNoMoreInteractions(guideDao);
   }
  
	@Test
  	public void processReadAllEpisodesExceptionTest() throws Exception{
     ProcessorListener listener = Mockito.mock(ProcessorListener.class);
     processor.addListener(listener);
       
     Show show = new Show();
     show.setShowId(Long.MAX_VALUE);
     show.setGuideId("GUIDEID");
     show.setLastGuideUpdate(SDF.parse("2000-01-01"));
     show.setLastGuideCheck(SDF.parse("2000-01-01"));
     show.setAiring(true);
     processor.setShow(show);
     
     ShowDao showDao = Mockito.mock(ShowDao.class);
     Mockito.when(showDao.readAndLock(Long.MAX_VALUE)).thenReturn(show);
     Mockito.when(showDao.readAllEpisodes(Long.MAX_VALUE)).thenThrow(ShowException.class);
     processor.setShowDao(showDao);

     GuideDao guideDao = Mockito.mock(GuideDao.class);
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
     Assert.assertTrue(show.getLastGuideUpdate().equals(SDF.parse("2000-01-01")));
     Assert.assertTrue(show.getLastGuideCheck().equals(SDF.parse("2000-01-01")));
     
     Mockito.verify(listener).alertComplete(processor);
     Mockito.verifyNoMoreInteractions(listener);
    
     Mockito.verify(showDao).readAndLock(Long.MAX_VALUE);
     Mockito.verify(showDao).readAllEpisodes(Long.MAX_VALUE);
     Mockito.verifyNoMoreInteractions(showDao);
     
     Mockito.verify(guideDao).info("GUIDEID");
     Mockito.verify(guideDao).episodes("GUIDEID"); 
     Mockito.verifyNoMoreInteractions(guideDao);
   }
   
	@Test
  	public void processRemoveResultsPreviousTest() throws Exception{
     ProcessorListener listener = Mockito.mock(ProcessorListener.class);
     processor.addListener(listener);
       
     Show show = new Show();
     show.setShowId(Long.MAX_VALUE);
     show.setTimezone("ETC");
     show.setGuideId("GUIDEID");
     show.setLastGuideUpdate(SDF.parse("2000-01-01"));
     show.setLastGuideCheck(SDF.parse("2000-01-01"));
     show.setAiring(true);
     
     Episode prevEp = new Episode();
     prevEp.setSeasonNum(1);
     prevEp.setEpisodeNum(1);
     prevEp.setTitle("PREV_EP");
     prevEp.setAirDate(SDF.parse("2016-01-28"));
     show.setPreviousEpisode(prevEp);
     
     Episode nextEp = new Episode();
     nextEp.setSeasonNum(1);
     nextEp.setEpisodeNum(2);
     nextEp.setTitle("NEXT_EP");
     nextEp.setAirDate(SDF.parse("2016-01-29"));
     show.setNextEpisode(nextEp);
     processor.setShow(show);
     
     ShowDao showDao = Mockito.mock(ShowDao.class);
     Mockito.when(showDao.readAndLock(Long.MAX_VALUE)).thenReturn(show);
     Mockito.when(showDao.readAllEpisodes(Long.MAX_VALUE)).thenReturn(Arrays.asList(nextEp));
     processor.setShowDao(showDao);

     GuideDao guideDao = Mockito.mock(GuideDao.class);
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
     Assert.assertTrue(show.getLastGuideUpdate().equals(SDF.parse("2000-01-01")));
     Assert.assertTrue(show.getLastGuideCheck().after(SDF.parse("2000-01-01")));
     Assert.assertNull(show.getNextEpisode());

     Mockito.verify(listener).alertComplete(processor);
    
     Mockito.verify(showDao).readAndLock(Long.MAX_VALUE);
     Mockito.verify(showDao).createOrUpdate(show);
     Mockito.verify(showDao).readAllEpisodes(Long.MAX_VALUE);
     Mockito.verify(showDao).deleteEpisodesAfter(prevEp);
     
     Mockito.verify(guideDao).info("GUIDEID");
     Mockito.verify(guideDao).episodes("GUIDEID");
     
     Mockito.verifyNoMoreInteractions(guideDao,showDao,listener);
   }
  
	@Test
  	public void processRemoveResultsNoneTest() throws Exception{
     ProcessorListener listener = Mockito.mock(ProcessorListener.class);
     processor.addListener(listener);
       
     Show show = new Show();
     show.setShowId(Long.MAX_VALUE);
     show.setTimezone("ETC");
     show.setGuideId("GUIDEID");
     show.setLastGuideUpdate(SDF.parse("2000-01-01"));
     show.setLastGuideCheck(SDF.parse("2000-01-01"));
     show.setAiring(true);
     /*
     Episode prevEp = new Episode();
     prevEp.setSeasonNum(1);
     prevEp.setEpisodeNum(1);
     prevEp.setTitle("PREV_EP");
     prevEp.setAirDate(SDF.parse("2016-01-28"));
     show.setPreviousEpisode(prevEp);
     */
     Episode nextEp = new Episode();
     nextEp.setSeasonNum(1);
     nextEp.setEpisodeNum(2);
     nextEp.setTitle("NEXT_EP");
     nextEp.setAirDate(SDF.parse("2016-01-29"));
     show.setNextEpisode(nextEp);
     processor.setShow(show);
     
     
     ShowDao showDao = Mockito.mock(ShowDao.class);
     Mockito.when(showDao.readAndLock(Long.MAX_VALUE)).thenReturn(show);
     //Mockito.when(showDao.readAllEpisodes(Long.MAX_VALUE)).thenReturn(Arrays.asList(nextEp));
     processor.setShowDao(showDao);

     GuideDao guideDao = Mockito.mock(GuideDao.class);
     Mockito.when(guideDao.getGuideSource()).thenReturn(GuideSource.TVDB);
     GuideInfo info = new GuideInfo();
 	  info.setLastUpdated(SDF.parse("2016-01-29"));
     info.setAiring(true);
     Mockito.when(guideDao.info("GUIDEID")).thenReturn(info);
     /*GuideEpisode guideEp1 = new GuideEpisode();
     guideEp1.setSeasonNum(1);
     guideEp1.setEpisodeNum(1);
     guideEp1.setTitle("NEXT_EP");
     guideEp1.setAirDate(SDF.parse("2016-01-29"));
     */
     Mockito.when(guideDao.episodes("GUIDEID")).thenReturn(Collections.<GuideEpisode>emptyList());
     processor.setGuideDao(guideDao);
     
     processor.process();
     
     Assert.assertFalse(processor.isActive());
     Assert.assertTrue(show.getLastGuideUpdate().equals(SDF.parse("2000-01-01")));
     Assert.assertTrue(show.getLastGuideCheck().after(SDF.parse("2000-01-01")));
     Assert.assertNull(show.getNextEpisode());
     Assert.assertNull(show.getPreviousEpisode());

     Mockito.verify(listener).alertComplete(processor);
    
     Mockito.verify(showDao).readAndLock(Long.MAX_VALUE);
     Mockito.verify(showDao).createOrUpdate(show);
     //Mockito.verify(showDao).readAllEpisodes(Long.MAX_VALUE);
     Mockito.verify(showDao).deleteEpisodes(show);
     
     Mockito.verify(guideDao).info("GUIDEID");
     Mockito.verify(guideDao).episodes("GUIDEID");
     
     Mockito.verifyNoMoreInteractions(guideDao,showDao,listener);
   }
  
  	@Test
  	public void toStringTest(){
     	Assert.assertEquals("GuideProcessor_Impl [show=null, config=null, active=false, listeners=[], showDao=null, guideDao=null]", processor.toString());
   }
}
