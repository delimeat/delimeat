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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.delimeat.config.ConfigService;
import io.delimeat.config.domain.Config;
import io.delimeat.feed.FeedService;
import io.delimeat.feed.domain.FeedResult;
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
import io.delimeat.torrent.domain.Torrent;
import io.delimeat.torrent.domain.TorrentInfo;
import io.delimeat.torrent.exception.TorrentException;
import io.delimeat.torrent.exception.TorrentNotFoundException;

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
	public void downloadUriTemplateTest(){
		Assert.assertNull(processor.getDownloadUriTemplate());
		processor.setDownloadUriTemplate("TEMPLATE");
		Assert.assertEquals("TEMPLATE", processor.getDownloadUriTemplate());
	}
	
	@Test
	public void readEpisodeTest() throws Exception {
		Episode episode = new Episode();
		
		Config config = new Config();

		FeedResultFilter filter = Mockito.mock(FeedResultFilter.class);
		processor.setFeedResultFilters(Arrays.asList(filter));
		
		processor.filterFeedResults(new ArrayList<FeedResult>(), episode, config);
		
		Mockito.verify(filter).filter(Mockito.any(),Mockito.any(),Mockito.any());
		Mockito.verifyNoMoreInteractions(filter);

	}

	@Test
	public void fetchTorrentMalformedURLTest() throws Exception {
		FeedProcessUnit result = new FeedProcessUnit();
		result.setDownloadUri(new URI("test:BLAH"));

		processor.fetchTorrent(result);
		
		Assert.assertNull(result.getTorrent());
		Assert.assertEquals(1, result.getRejections().size());
		Assert.assertEquals(FeedProcessUnitRejection.UNNABLE_TO_GET_TORRENT, result.getRejections().get(0));
		
	}

	@Test
	public void fetchTorrentURISyntaxTest() throws Exception {
		FeedProcessUnit result = new FeedProcessUnit();
		result.setDownloadUri(new URI("http:BLAH"));

		processor.fetchTorrent(result);
		
		Assert.assertNull(result.getTorrent());
		Assert.assertEquals(1, result.getRejections().size());
		Assert.assertEquals(FeedProcessUnitRejection.UNNABLE_TO_GET_TORRENT, result.getRejections().get(0));
	}

	@Test
	public void fetchTorrentTest() throws Exception {
		FeedProcessUnit result = new FeedProcessUnit();
		result.setDownloadUri(new URI("http://test.com?removed=true"));


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
	public void fetchTorrentDaoExceptionTest() throws Exception {
		FeedProcessUnit result = new FeedProcessUnit();
		result.setDownloadUri(new URI("http://test.com?removed=true"));

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
	public void validateTorrentTest() throws Exception {
		Config config = new Config();
		config.setIgnoreFolders(false);
		
		Show show = new Show();
		
		Torrent torrent = new Torrent();
		FeedProcessUnit processUnit = new FeedProcessUnit();
		processUnit.setTorrent(torrent);
		
		TorrentValidator validator = Mockito.mock(TorrentValidator.class);
		Mockito.when(validator.validate(torrent,show, config))
				.thenReturn(true)
				.thenReturn(false);
		Mockito.when(validator.getRejection()).thenReturn(FeedProcessUnitRejection.CONTAINS_COMPRESSED);

		processor.setTorrentValidators(Arrays.asList(validator, validator));

		processor.validateTorrent(processUnit, show, config);
		
     	Assert.assertEquals(1, processUnit.getRejections().size());
     	Assert.assertEquals(FeedProcessUnitRejection.CONTAINS_COMPRESSED, processUnit.getRejections().get(0));
		
		Mockito.verify(validator, Mockito.times(2)).validate(torrent, show, config);
		Mockito.verify(validator).getRejection();
		Mockito.verifyNoMoreInteractions(validator);
	}

	@Test
	public void selectBestResultTest() {
		Torrent torrent = new Torrent();
		FeedProcessUnit result1 = new FeedProcessUnit();
		result1.setSeeders(100);
		result1.setTorrent(torrent);
		
		FeedProcessUnit result2 = new FeedProcessUnit();
		result2.setSeeders(50);

		FeedProcessUnit result = processor.selectBestResultTorrent(Arrays.asList(result2, result1));

		Assert.assertEquals(result,result1);		
	}

	@Test
	public void validatResultTorrentsTest() throws Exception {
		FeedProcessUnit result1 = new FeedProcessUnit();
		result1.setDownloadUri(new URI("http://test1.com"));
		FeedProcessUnit result2 = new FeedProcessUnit();
		result2.setDownloadUri(null);
		FeedProcessUnit result3 = new FeedProcessUnit();
		result3.setDownloadUri(new URI("http:URISYNTAXEXCEPTION"));
		FeedProcessUnit result4 = new FeedProcessUnit();
		result4.setDownloadUri(new URI("http://test2.com"));
		FeedProcessUnit result5 = new FeedProcessUnit();
		result5.setDownloadUri(new URI("http://test3.com"));
		FeedProcessUnit result6 = new FeedProcessUnit();
		result6.setDownloadUri(new URI("http://test4.com"));
		FeedProcessUnit result7 = new FeedProcessUnit();
		result7.setDownloadUri(new URI("http://test5.com"));

		TorrentService torrentService = Mockito.mock(TorrentService.class);
		Torrent torrent = new Torrent();
		TorrentInfo info = new TorrentInfo();
		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		info.setInfoHash(infoHash);
		torrent.setInfo(info);
		Mockito.when(torrentService.read(Mockito.any(URI.class)))
        		.thenReturn(torrent)
				.thenThrow(IOException.class)
        		.thenThrow(TorrentException.class)
        		.thenThrow(TorrentNotFoundException.class)
				.thenReturn(torrent);

		processor.setTorrentService(torrentService);

		TorrentValidator validator = Mockito.mock(TorrentValidator.class);
		Mockito.when(validator.validate(Mockito.any(Torrent.class),Mockito.any(Show.class), Mockito.any(Config.class)))
        				.thenReturn(true)
        				.thenReturn(false);
		Mockito.when(validator.getRejection()).thenReturn(FeedProcessUnitRejection.CONTAINS_COMPRESSED);
		processor.setTorrentValidators(Arrays.asList(validator));

		Config config = new Config();

		processor.validateResultTorrents(Arrays.asList(result1, result2, result3, result4, result5, result6, result7),new Show(), config);
		
		Mockito.verify(torrentService, Mockito.times(5)).read(Mockito.any(URI.class));
		Mockito.verify(validator, Mockito.times(2)).validate(Mockito.any(Torrent.class), Mockito.any(Show.class), Mockito.any(Config.class));
		
		Assert.assertTrue(result1.getRejections().isEmpty());
		Assert.assertEquals(1, result2.getRejections().size());
		Assert.assertEquals(FeedProcessUnitRejection.UNNABLE_TO_GET_TORRENT, result2.getRejections().get(0));
		Assert.assertEquals(1, result3.getRejections().size());
		Assert.assertEquals(FeedProcessUnitRejection.UNNABLE_TO_GET_TORRENT, result3.getRejections().get(0));
		Assert.assertEquals(1, result4.getRejections().size());
		Assert.assertEquals(FeedProcessUnitRejection.UNNABLE_TO_GET_TORRENT, result4.getRejections().get(0));
		Assert.assertEquals(1, result5.getRejections().size());
		Assert.assertEquals(FeedProcessUnitRejection.UNNABLE_TO_GET_TORRENT, result5.getRejections().get(0));
		Assert.assertEquals(1, result6.getRejections().size());
		Assert.assertEquals(FeedProcessUnitRejection.UNNABLE_TO_GET_TORRENT, result6.getRejections().get(0));
		Assert.assertEquals(1, result7.getRejections().size());
		Assert.assertEquals(FeedProcessUnitRejection.CONTAINS_COMPRESSED, result7.getRejections().get(0));
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
     	feedResult.setTorrentURL("http://test.com");
     	Mockito.when(feedService.read("SHOW_TITLE")).thenReturn(Arrays.asList(feedResult));
     	processor.setFeedService(feedService);
     
		TorrentService torrentService = Mockito.mock(TorrentService.class);
     	Torrent torrent = new Torrent();
     	torrent.setBytes("BYTES".getBytes());
     	TorrentInfo info = new TorrentInfo();
     	info.setName("TORRENT");
     	torrent.setInfo(info);
		Mockito.when(torrentService.read(Mockito.any(URI.class)))
        		.thenReturn(torrent);
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
     	Mockito.verify(torrentService).write("SHOW_TITLE_1x2_EPISODE_TITLE.torrent",torrent, config);
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
  		
  		Assert.assertEquals("SHOW_TITLE_1970-01-01_EPISODE_TITLE.torrent", processor.generateTorrentFileName(episode));
  		
  	}
  	
  	@Test
  	public void generateTorrentFileNameSeasonShowTest(){
  		Show show = new Show();
  		show.setShowType(ShowType.SEASON);
  		show.setTitle("SHOW_TITLE");
  		Episode episode = new Episode();
  		episode.setSeasonNum(100);
  		episode.setEpisodeNum(99);
  		episode.setTitle("EPISODE_TITLE");
  		episode.setShow(show);
  		
  		Assert.assertEquals("SHOW_TITLE_100x99_EPISODE_TITLE.torrent", processor.generateTorrentFileName(episode));	
  	}
  	
  	@Test
  	public void buildDownloadUriTest() throws Exception{
  		processor.setDownloadUriTemplate("http:%s");
  		URI uri = processor.buildDownloadUri(new InfoHash("INFO_HASH".getBytes()));
  		Assert.assertEquals(new URI("http:494E464F5F48415348"), uri);		
  	}
  	
  	@Test(expected=URISyntaxException.class)
  	public void buildDownloadUriExceptionTest() throws Exception{
  		processor.setDownloadUriTemplate("JIBBERISH %s");
  		processor.buildDownloadUri(new InfoHash("INFO_HASH".getBytes()));		
  	}
  	
  	@Test
  	public void buildInfoHashFromMagnetTest() throws Exception{
  		InfoHash infoHash = processor.buildInfoHashFromMagnet("magnet:?xt=urn:btih:df706cf16f45e8c0fd226223509c7e97b4ffec13&tr=udp://tracker.coppersurfer.tk:6969/announce");
  		Assert.assertEquals("df706cf16f45e8c0fd226223509c7e97b4ffec13", infoHash.getHex());
  	}
  	
  	@Test
  	public void buildInfoHashFromMagnetNoMatchTest() throws Exception{
  		Assert.assertNull(processor.buildInfoHashFromMagnet("magnet:?xt=urn:btih:"));
  	}
  	
  	@Test
  	public void convertFeedResultsTest() throws Exception{
  		FeedResult feedResult = new FeedResult();
  		feedResult.setContentLength(1);
  		feedResult.setTitle("TITLE_1");
  		feedResult.setSeeders(99);
  		feedResult.setLeechers(100);
  		feedResult.setTorrentURL("http://download_1.url");
  		feedResult.setMagnetUri("magnet:?xt=urn:btih:df706cf16f45e8c0fd226223509c7e97b4ffec13&tr=udp://tracker.coppersurfer.tk:6969/announce");
  		feedResult.setInfoHashHex("df706cf16f45e8c0fd226223509c7e97b4ffec13");
  		
  		FeedProcessUnit processUnit = processor.convertFeedResult(feedResult);
  		Assert.assertEquals(1, processUnit.getContentLength());
  		Assert.assertEquals("TITLE_1", processUnit.getTitle());
  		Assert.assertEquals(99, processUnit.getSeeders());
  		Assert.assertEquals(100, processUnit.getLeechers());
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
  		feedResult.setMagnetUri("magnet:?xt=urn:btih:df706cf16f45e8c0fd226223509c7e97b4ffec13&tr=udp://tracker.coppersurfer.tk:6969/announce");
  		feedResult.setInfoHashHex("df706cf16f45e8c0fd226223509c7e97b4ffec13");
  		
  		processor.setDownloadUriTemplate("https://itorrents.org/torrent/%s.torrent");
  		  		
  		FeedProcessUnit processUnit = processor.convertFeedResult(feedResult);
  		Assert.assertEquals(1, processUnit.getContentLength());
  		Assert.assertEquals("TITLE_1", processUnit.getTitle());
  		Assert.assertEquals(99, processUnit.getSeeders());
  		Assert.assertEquals(100, processUnit.getLeechers());
  		Assert.assertEquals(new URI("https://itorrents.org/torrent/DF706CF16F45E8C0FD226223509C7E97B4FFEC13.torrent"), processUnit.getDownloadUri());
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
  		feedResult.setMagnetUri("magnet:?xt=urn:btih:df706cf16f45e8c0fd226223509c7e97b4ffec13&tr=udp://tracker.coppersurfer.tk:6969/announce");
  		feedResult.setInfoHashHex(null);
  		
  		FeedProcessUnit processUnit = processor.convertFeedResult(feedResult);
  		Assert.assertEquals(1, processUnit.getContentLength());
  		Assert.assertEquals("TITLE_1", processUnit.getTitle());
  		Assert.assertEquals(99, processUnit.getSeeders());
  		Assert.assertEquals(100, processUnit.getLeechers());
  		Assert.assertEquals(new URI("http://download_1.url"), processUnit.getDownloadUri());
  		Assert.assertEquals("df706cf16f45e8c0fd226223509c7e97b4ffec13", processUnit.getInfoHash().getHex());
  	}
  	
  	@Test
  	public void convertFeedResultsInvalidDownloadUriTest() throws Exception{
  		FeedResult feedResult = new FeedResult();
  		feedResult.setContentLength(1);
  		feedResult.setTitle("TITLE_1");
  		feedResult.setSeeders(99);
  		feedResult.setLeechers(100);
  		feedResult.setTorrentURL("//\\");
  		feedResult.setMagnetUri(null);
  		feedResult.setInfoHashHex(null);
  		
  		FeedProcessUnit processUnit = processor.convertFeedResult(feedResult);
  		Assert.assertEquals(1, processUnit.getContentLength());
  		Assert.assertEquals("TITLE_1", processUnit.getTitle());
  		Assert.assertEquals(99, processUnit.getSeeders());
  		Assert.assertEquals(100, processUnit.getLeechers());
  		Assert.assertNull(processUnit.getDownloadUri());
  		Assert.assertNull( processUnit.getInfoHash());	
  	}
  	
  	@Test
  	public void convertFeedResultsInvalidMagnetUriTest() throws Exception{
  		FeedResult feedResult = new FeedResult();
  		feedResult.setContentLength(1);
  		feedResult.setTitle("TITLE_1");
  		feedResult.setSeeders(99);
  		feedResult.setLeechers(100);
  		feedResult.setTorrentURL("http://download_1.url");
  		feedResult.setMagnetUri("//\\");
  		feedResult.setInfoHashHex(null);
  		
  		FeedProcessUnit processUnit = processor.convertFeedResult(feedResult);
  		Assert.assertEquals(1, processUnit.getContentLength());
  		Assert.assertEquals("TITLE_1", processUnit.getTitle());
  		Assert.assertEquals(99, processUnit.getSeeders());
  		Assert.assertEquals(100, processUnit.getLeechers());
  		Assert.assertEquals(new URI("http://download_1.url"), processUnit.getDownloadUri());
  		Assert.assertNull(processUnit.getInfoHash());	
  	}
}
