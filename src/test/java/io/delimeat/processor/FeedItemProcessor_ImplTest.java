/*
 * Copyright 2013-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.delimeat.processor;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.delimeat.config.ConfigService;
import io.delimeat.config.domain.Config;
import io.delimeat.feed.FeedService;
import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.domain.FeedSource;
import io.delimeat.processor.domain.FeedProcessUnit;
import io.delimeat.processor.domain.FeedProcessUnitRejection;
import io.delimeat.processor.filter.FeedResultFilter;
import io.delimeat.processor.validation.TorrentValidator;
import io.delimeat.show.EpisodeService;
import io.delimeat.show.domain.Episode;
import io.delimeat.show.domain.EpisodeStatus;
import io.delimeat.show.domain.Show;
import io.delimeat.show.domain.ShowType;
import io.delimeat.torrent.TorrentService;
import io.delimeat.torrent.domain.InfoHash;
import io.delimeat.torrent.domain.ScrapeResult;
import io.delimeat.torrent.domain.Torrent;
import io.delimeat.torrent.domain.TorrentInfo;
import io.delimeat.torrent.exception.TorrentException;

public class FeedItemProcessor_ImplTest {

	private FeedItemProcessor_Impl processor;
	
	@Before
	public void setUp(){
		processor = new FeedItemProcessor_Impl();
	}

	@Test
	public void toStringTest(){
		Assert.assertEquals("FeedItemProcessor_Impl [torrentValidators=[], ]", processor.toString());
	}
	@Test
	public void configServiceTest() {
		Assert.assertNull(processor.getConfigService());
		ConfigService configService = Mockito.mock(ConfigService.class);
		processor.setConfigService(configService);
		Assert.assertEquals(configService, processor.getConfigService());
	}
	
	@Test
	public void episodeServiceTest() {
		Assert.assertNull(processor.getEpisodeService());
		EpisodeService service = Mockito.mock(EpisodeService.class);
		processor.setEpisodeService(service);
		Assert.assertEquals(service, processor.getEpisodeService());
	}

	@Test
	public void feedServiceTest() {
		Assert.assertNull(processor.getFeedService());
		FeedService service = Mockito.mock(FeedService.class);
		processor.setFeedService(service);
		Assert.assertEquals(service, processor.getFeedService());
	}

	@Test
	public void torrentServiceTest() {
		Assert.assertNull(processor.getTorrentService());
		TorrentService torrentService = Mockito.mock(TorrentService.class);
		processor.setTorrentService(torrentService);
		Assert.assertEquals(torrentService, processor.getTorrentService());
	}

	@Test
	public void torrentValidatorsTest() {
		Assert.assertNotNull(processor.getTorrentValidators());
		Assert.assertTrue(processor.getTorrentValidators().isEmpty());
		TorrentValidator mockedValidator = Mockito.mock(TorrentValidator.class);
		processor.setTorrentValidators(Arrays.asList(mockedValidator));
		Assert.assertNotNull(processor.getTorrentValidators());
		Assert.assertFalse(processor.getTorrentValidators().isEmpty());
		Assert.assertEquals(1, processor.getTorrentValidators().size());
		Assert.assertEquals(mockedValidator, processor.getTorrentValidators().get(0));
	}
	
	@Test
	public void feedResultFiltersTest(){
		Assert.assertNotNull(processor.getFeedResultFilters());
		Assert.assertTrue(processor.getFeedResultFilters().isEmpty());
		FeedResultFilter filter = Mockito.mock(FeedResultFilter.class);
		processor.setFeedResultFilters(Arrays.asList(filter));
		Assert.assertNotNull(processor.getFeedResultFilters());
		Assert.assertFalse(processor.getFeedResultFilters().isEmpty());
		Assert.assertEquals(1, processor.getFeedResultFilters().size());
		Assert.assertEquals(filter, processor.getFeedResultFilters().get(0));
	}

	@Test
	public void fetchTorrentUriTest() throws Exception {
		FeedProcessUnit result = new FeedProcessUnit();
		result.setDownloadUri(new URI("http://test.com"));
		result.setInfoHash(null);

		TorrentService torrentService = Mockito.mock(TorrentService.class);
		Torrent torrent = new Torrent();
		TorrentInfo info = new TorrentInfo();
		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		info.setInfoHash(infoHash);
		torrent.setInfo(info);
		Mockito.when(torrentService.read(new URI("http://test.com"))).thenReturn(torrent);
		processor.setTorrentService(torrentService);

		processor.fetchTorrent(result);
		
		Assert.assertEquals(torrent, result.getTorrent());
		Assert.assertEquals(new InfoHash("INFO_HASH".getBytes()), result.getInfoHash());
		Assert.assertEquals(0, result.getRejections().size());
		
		Mockito.verify(torrentService).read(new URI("http://test.com"));
		Mockito.verifyNoMoreInteractions(torrentService);
	}
	
	@Test
	public void fetchTorrentUriDaoExceptionTest() throws Exception {
		FeedProcessUnit result = new FeedProcessUnit();
		result.setDownloadUri(new URI("http://test.com"));
		result.setInfoHash(null);

		TorrentService torrentService = Mockito.mock(TorrentService.class);
		Mockito.when(torrentService.read(new URI("http://test.com"))).thenThrow(TorrentException.class);
		processor.setTorrentService(torrentService);

		processor.fetchTorrent(result);
		
		Assert.assertNull(result.getTorrent());
		Assert.assertEquals(1, result.getRejections().size());
		Assert.assertEquals(FeedProcessUnitRejection.UNNABLE_TO_GET_TORRENT, result.getRejections().get(0));
		
		Mockito.verify(torrentService).read(new URI("http://test.com"));
		Mockito.verifyNoMoreInteractions(torrentService);
	}
	
	@Test
	public void fetchTorrentInfoHashTest() throws Exception {
		FeedProcessUnit result = new FeedProcessUnit();
		result.setDownloadUri(null);
		result.setInfoHash(new InfoHash("INFO_HASH".getBytes()));


		TorrentService torrentService = Mockito.mock(TorrentService.class);
		Torrent torrent = new Torrent();
		TorrentInfo info = new TorrentInfo();
		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		info.setInfoHash(infoHash);
		torrent.setInfo(info);
		Mockito.when(torrentService.read(new InfoHash("INFO_HASH".getBytes()))).thenReturn(torrent);
		processor.setTorrentService(torrentService);

		processor.fetchTorrent(result);
		
		Assert.assertEquals(torrent, result.getTorrent());
		Assert.assertEquals(new InfoHash("INFO_HASH".getBytes()), result.getInfoHash());
		Assert.assertEquals(0, result.getRejections().size());
		
		Mockito.verify(torrentService).read(new InfoHash("INFO_HASH".getBytes()));
		Mockito.verifyNoMoreInteractions(torrentService);
	}
	
	@Test
	public void fetchTorrentInfoHashDaoExceptionTest() throws Exception {
		FeedProcessUnit result = new FeedProcessUnit();
		result.setDownloadUri(null);
		result.setInfoHash(new InfoHash("INFO_HASH".getBytes()));

		TorrentService torrentService = Mockito.mock(TorrentService.class);
		Mockito.when(torrentService.read(new InfoHash("INFO_HASH".getBytes()))).thenThrow(TorrentException.class);
		processor.setTorrentService(torrentService);

		processor.fetchTorrent(result);
		
		Assert.assertNull(result.getTorrent());
		Assert.assertEquals(1, result.getRejections().size());
		Assert.assertEquals(FeedProcessUnitRejection.UNNABLE_TO_GET_TORRENT, result.getRejections().get(0));
		
		Mockito.verify(torrentService).read(new InfoHash("INFO_HASH".getBytes()));
		Mockito.verifyNoMoreInteractions(torrentService);
	}
	
	@Test
	public void fetchTorrentNoUriNoInfoHashTest() throws Exception {
		FeedProcessUnit result = new FeedProcessUnit();
		result.setDownloadUri(null);
		result.setInfoHash(null);
		
		TorrentService torrentService = Mockito.mock(TorrentService.class);
		processor.setTorrentService(torrentService);
		
		processor.fetchTorrent(result);
		Assert.assertNull(result.getTorrent());
		Assert.assertEquals(1, result.getRejections().size());
		Assert.assertEquals(FeedProcessUnitRejection.UNNABLE_TO_GET_TORRENT, result.getRejections().get(0));
		
		Mockito.verifyZeroInteractions(torrentService);

	}
	
	@Test
	public void validateTorrentOkTest() throws Exception {
		Config config = new Config();
		config.setIgnoreFolders(false);
		
		Show show = new Show();
		
		Torrent torrent = new Torrent();
		FeedProcessUnit processUnit = new FeedProcessUnit();
		processUnit.setTorrent(torrent);
		
		TorrentValidator validator = Mockito.mock(TorrentValidator.class);
		Mockito.when(validator.validate(torrent,show, config)).thenReturn(true);

		processor.setTorrentValidators(Arrays.asList(validator));

		processor.validateTorrent(processUnit, show, config);
		
     	Assert.assertEquals(0, processUnit.getRejections().size());
		
		Mockito.verify(validator).validate(torrent, show, config);
		Mockito.verifyNoMoreInteractions(validator);
	}
	
	@Test
	public void validateTorrentInvalidTest() throws Exception {
		Config config = new Config();
		config.setIgnoreFolders(false);
		
		Show show = new Show();
		
		Torrent torrent = new Torrent();
		FeedProcessUnit processUnit = new FeedProcessUnit();
		processUnit.setTorrent(torrent);
		
		TorrentValidator validator = Mockito.mock(TorrentValidator.class);
		Mockito.when(validator.validate(torrent,show, config)).thenReturn(false);
		Mockito.when(validator.getRejection()).thenReturn(FeedProcessUnitRejection.CONTAINS_COMPRESSED);

		processor.setTorrentValidators(Arrays.asList(validator));

		processor.validateTorrent(processUnit, show, config);
		
     	Assert.assertEquals(1, processUnit.getRejections().size());
     	Assert.assertEquals(FeedProcessUnitRejection.CONTAINS_COMPRESSED, processUnit.getRejections().get(0));
		
		Mockito.verify(validator).validate(torrent, show, config);
		Mockito.verify(validator).getRejection();
		Mockito.verifyNoMoreInteractions(validator);
	}

	@Test
	public void selectBestResultTest() {
		Torrent torrent = new Torrent();
		FeedProcessUnit result1 = new FeedProcessUnit();
		result1.setScrape(new ScrapeResult(100,0));
		result1.setTorrent(torrent);
		
		FeedProcessUnit result2 = new FeedProcessUnit();
		result2.setScrape(new ScrapeResult(50,0));

		
		FeedProcessUnit result3 = new FeedProcessUnit();
		result3.setScrape(new ScrapeResult(200,0));

		result3.getRejections().add(FeedProcessUnitRejection.CONTAINS_COMPRESSED);

		FeedProcessUnit result = processor.selectBestResultTorrent(Arrays.asList(result2, result1));

		Assert.assertEquals(result, result1);		
	}
  
  	@Test
  	public void processTest() throws Exception{
  		Instant testStart = Instant.now();
     
     	Show show = new Show();
     	show.setTitle("SHOW_TITLE");
     	show.setShowType(ShowType.SEASON);
     	Episode episode = new Episode();
     	episode.setTitle("EPISODE_TITLE");
     	episode.setSeasonNum(1);
     	episode.setEpisodeNum(2);
     	episode.setEpisodeId(1L);
     	episode.setLastFeedCheck(Instant.EPOCH);
     	episode.setLastFeedUpdate(Instant.EPOCH);
     	episode.setShow(show);

		ConfigService configService = Mockito.mock(ConfigService.class);
		Config config = new Config();
		Mockito.when(configService.read()).thenReturn(config);
		processor.setConfigService(configService);
		
		EpisodeService service = Mockito.mock(EpisodeService.class);
     	Mockito.when(service.read(1L)).thenReturn(episode);
		processor.setEpisodeService(service);
     
     	FeedService feedService = Mockito.mock(FeedService.class);
     	FeedResult feedResult = new FeedResult();
     	feedResult.setSource(FeedSource.BITSNOOP);
     	feedResult.setTorrentURL("http://test.com");
     	feedResult.setInfoHashHex(null);
     	Mockito.when(feedService.read("SHOW_TITLE")).thenReturn(Arrays.asList(feedResult));
     	processor.setFeedService(feedService);
     
		TorrentService torrentService = Mockito.mock(TorrentService.class);
     	Torrent torrent = new Torrent();
     	TorrentInfo info = new TorrentInfo();
     	InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
     	info.setInfoHash(infoHash);
     	torrent.setInfo(info);
		Mockito.when(torrentService.read(Mockito.any(URI.class))).thenReturn(torrent);
		ScrapeResult scrapeResult = new ScrapeResult(100,200);
		Mockito.when(torrentService.scrape(torrent)).thenReturn(scrapeResult);
		processor.setTorrentService(torrentService);
     
     	TorrentValidator torrentValidator = Mockito.mock(TorrentValidator.class);
     	Mockito.when(torrentValidator.validate(torrent, show, config)).thenReturn(true);
     	processor.setTorrentValidators(Arrays.asList(torrentValidator));

     	processor.process(episode);
     	
     	Instant testEnd = Instant.now();
     	
     	Assert.assertTrue(episode.getLastFeedUpdate().toEpochMilli()>= testStart.toEpochMilli());
     	Assert.assertTrue(episode.getLastFeedUpdate().toEpochMilli()<=testEnd.toEpochMilli());
     	Assert.assertTrue(episode.getLastFeedCheck().equals(episode.getLastFeedUpdate()));
     	Assert.assertEquals(EpisodeStatus.FOUND, episode.getStatus());
 
     	Mockito.verify(service).update(episode);
     	Mockito.verifyNoMoreInteractions(service);
     	
     	Mockito.verify(feedService).read("SHOW_TITLE");
     	Mockito.verifyNoMoreInteractions(feedService);

    	Mockito.verify(torrentValidator).validate(torrent, show, config);
     	Mockito.verifyNoMoreInteractions(torrentValidator);

     	Mockito.verify(torrentService).read(Mockito.any(URI.class));
     	Mockito.verify(torrentService).write("BITSNOOP_SHOW_TITLE_01x02_EPISODE_TITLE.torrent",torrent, config);
     	Mockito.verify(torrentService).scrape(torrent);
     	Mockito.verifyNoMoreInteractions(torrentService);
     	
		Mockito.verify(configService).read();
		Mockito.verifyNoMoreInteractions(configService);
   }
  	
  	@Test
  	public void generateTorrentFileNameDailyShowTest(){
  		Show show = new Show();
  		show.setShowType(ShowType.DAILY);
  		show.setTitle("SHOW_TITLE");
  		Episode episode = new Episode();
  		episode.setAirDate(LocalDate.ofEpochDay(0));
  		episode.setTitle("EPISODE_TITLE");
  		episode.setShow(show);
  		
  		Assert.assertEquals("BITSNOOP_SHOW_TITLE_1970-01-01_EPISODE_TITLE.torrent", processor.generateTorrentFileName(FeedSource.BITSNOOP, episode));
  		
  	}
  	
  	@Test
  	public void generateTorrentFileNameSeasonShowTest(){
  		Show show = new Show();
  		show.setShowType(ShowType.SEASON);
  		show.setTitle("SHOW_TITLE");
  		Episode episode = new Episode();
  		episode.setSeasonNum(10);
  		episode.setEpisodeNum(9);
  		episode.setTitle("EPISODE_TITLE");
  		episode.setShow(show);
  		
  		Assert.assertEquals("BITSNOOP_SHOW_TITLE_10x09_EPISODE_TITLE.torrent", processor.generateTorrentFileName(FeedSource.BITSNOOP, episode));	
  	}
  	
  	@Test
  	public void convertFeedResultsTest() throws Exception{
  		FeedResult feedResult = new FeedResult();
  		feedResult.setSource(FeedSource.BITSNOOP);
  		feedResult.setContentLength(1);
  		feedResult.setTitle("TITLE_1");
  		feedResult.setSeeders(99);
  		feedResult.setLeechers(100);
  		feedResult.setTorrentURL("http://download_1.url");
  		feedResult.setInfoHashHex("df706cf16f45e8c0fd226223509c7e97b4ffec13");
  		
  		FeedProcessUnit processUnit = processor.convertFeedResult(feedResult);
  		Assert.assertEquals(FeedSource.BITSNOOP,processUnit.getSource());
  		Assert.assertEquals("TITLE_1", processUnit.getTitle());
  		Assert.assertEquals(99, processUnit.getScrape().getSeeders());
  		Assert.assertEquals(100, processUnit.getScrape().getLeechers());
  		Assert.assertEquals(new URI("http://download_1.url"), processUnit.getDownloadUri());
  		Assert.assertEquals("df706cf16f45e8c0fd226223509c7e97b4ffec13", processUnit.getInfoHash().getHex());	
  	}
  	
  	@Test
  	public void convertFeedResultsNoDownloadUriTest() throws Exception{
  		FeedResult feedResult = new FeedResult();
  		feedResult.setContentLength(1);
  		feedResult.setTitle("TITLE_1");
  		feedResult.setSeeders(99);
  		feedResult.setLeechers(100);
  		feedResult.setTorrentURL(null);
  		feedResult.setInfoHashHex("df706cf16f45e8c0fd226223509c7e97b4ffec13");
  	  		  		
  		FeedProcessUnit processUnit = processor.convertFeedResult(feedResult);
  		Assert.assertEquals("TITLE_1", processUnit.getTitle());
  		Assert.assertEquals(99, processUnit.getScrape().getSeeders());
  		Assert.assertEquals(100, processUnit.getScrape().getLeechers());
  		Assert.assertNull(processUnit.getDownloadUri());
  		Assert.assertEquals("df706cf16f45e8c0fd226223509c7e97b4ffec13", processUnit.getInfoHash().getHex());
  	}
  	
  	@Test
  	public void convertFeedResultsNoInfoHashTest() throws Exception{
  		FeedResult feedResult = new FeedResult();
  		feedResult.setContentLength(1);
  		feedResult.setTitle("TITLE_1");
  		feedResult.setSeeders(99);
  		feedResult.setLeechers(100);
  		feedResult.setTorrentURL("http://download_1.url");
  		feedResult.setInfoHashHex(null);
  		
  		FeedProcessUnit processUnit = processor.convertFeedResult(feedResult);
  		Assert.assertEquals("TITLE_1", processUnit.getTitle());
  		Assert.assertEquals(99, processUnit.getScrape().getSeeders());
  		Assert.assertEquals(100, processUnit.getScrape().getLeechers());
  		Assert.assertEquals(new URI("http://download_1.url"), processUnit.getDownloadUri());
  		Assert.assertNull(processUnit.getInfoHash());
  	}
  	
  	@Test
  	public void convertFeedResultsInvalidDownloadUriTest() throws Exception{
  		FeedResult feedResult = new FeedResult();
  		feedResult.setContentLength(1);
  		feedResult.setTitle("TITLE_1");
  		feedResult.setSeeders(99);
  		feedResult.setLeechers(100);
  		feedResult.setTorrentURL("//\\");
  		feedResult.setInfoHashHex(null);
  		
  		FeedProcessUnit processUnit = processor.convertFeedResult(feedResult);
  		Assert.assertEquals("TITLE_1", processUnit.getTitle());
  		Assert.assertEquals(99, processUnit.getScrape().getSeeders());
  		Assert.assertEquals(100, processUnit.getScrape().getLeechers());
  		Assert.assertNull(processUnit.getDownloadUri());
  		Assert.assertNull( processUnit.getInfoHash());	
  	}
  	
  	@Test
  	public void scrapeTorrentTest(){
  		FeedProcessUnit processUnit = new FeedProcessUnit();
  		Torrent torrent = new Torrent();
  		processUnit.setTorrent(torrent);

  		TorrentService torrentService = Mockito.mock(TorrentService.class);
		ScrapeResult scrapeResult = new ScrapeResult(100,200);
		Mockito.when(torrentService.scrape(torrent)).thenReturn(scrapeResult);
		processor.setTorrentService(torrentService); 
		
		processor.scrapeTorrent(processUnit);
		
		Assert.assertEquals(0,processUnit.getRejections().size());
		Assert.assertEquals(100,processUnit.getScrape().getSeeders());
		Assert.assertEquals(200, processUnit.getScrape().getLeechers());
		
		Mockito.verify(torrentService).scrape(torrent);
		Mockito.verifyNoMoreInteractions(torrentService);		
  	}
  	
  	@Test
  	public void scrapeTorrentBelowMinTest(){
  		FeedProcessUnit processUnit = new FeedProcessUnit();
  		Torrent torrent = new Torrent();
  		processUnit.setTorrent(torrent);

  		TorrentService torrentService = Mockito.mock(TorrentService.class);
		ScrapeResult scrapeResult = new ScrapeResult(19,18);
		Mockito.when(torrentService.scrape(torrent)).thenReturn(scrapeResult);
		processor.setTorrentService(torrentService); 
		
		processor.scrapeTorrent(processUnit);
		
		Assert.assertEquals(1,processUnit.getRejections().size());
		Assert.assertEquals(FeedProcessUnitRejection.INSUFFICENT_SEEDERS, processUnit.getRejections().get(0));
		Assert.assertEquals(19,processUnit.getScrape().getSeeders());
		Assert.assertEquals(18, processUnit.getScrape().getLeechers());
		
		Mockito.verify(torrentService).scrape(torrent);
		Mockito.verifyNoMoreInteractions(torrentService); 		
  	}
  	
  	@Test
  	public void scrpateTorrentNotReturnedTest(){
  		FeedProcessUnit processUnit = new FeedProcessUnit();
  		Torrent torrent = new Torrent();
  		processUnit.setTorrent(torrent);

  		TorrentService torrentService = Mockito.mock(TorrentService.class);
		Mockito.when(torrentService.scrape(torrent)).thenReturn(null);
		processor.setTorrentService(torrentService); 
		
		processor.scrapeTorrent(processUnit);
		
		Assert.assertEquals(1,processUnit.getRejections().size());
		Assert.assertEquals(FeedProcessUnitRejection.INSUFFICENT_SEEDERS, processUnit.getRejections().get(0));
		Assert.assertNull(processUnit.getScrape());
		
		Mockito.verify(torrentService).scrape(torrent);
		Mockito.verifyNoMoreInteractions(torrentService);   		
  	}
}
