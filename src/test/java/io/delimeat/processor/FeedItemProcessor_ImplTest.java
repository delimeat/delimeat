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
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.delimeat.config.ConfigService;
import io.delimeat.config.domain.Config;
import io.delimeat.feed.FeedService;
import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.domain.FeedResultRejection;
import io.delimeat.processor.validation.FeedResultValidator;
import io.delimeat.processor.validation.TorrentValidator;
import io.delimeat.processor.validation.ValidationException;
import io.delimeat.show.EpisodeService;
import io.delimeat.show.domain.Episode;
import io.delimeat.show.domain.Show;
import io.delimeat.show.domain.ShowType;
import io.delimeat.torrent.TorrentService;
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
	public void feedResultValidatorsTest() {
		Assert.assertNotNull(processor.getFeedResultValidators());
		Assert.assertTrue(processor.getFeedResultValidators().isEmpty());
		FeedResultValidator mockedValidator = Mockito
				.mock(FeedResultValidator.class);
		processor.setFeedResultValidators(Arrays.asList(mockedValidator));
		Assert.assertNotNull(processor.getFeedResultValidators());
		Assert.assertFalse(processor.getFeedResultValidators().isEmpty());
		Assert.assertEquals(1, processor.getFeedResultValidators().size());
		Assert.assertEquals(mockedValidator, processor
				.getFeedResultValidators().get(0));
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
	public void validateFeedResultsTest() throws Exception {
		FeedResultValidator validator = Mockito.mock(FeedResultValidator.class);
		processor.setFeedResultValidators(Arrays.asList(validator));
     
		Episode episode = new Episode();

		FeedResult result1 = new FeedResult();
		result1.getFeedResultRejections().add(FeedResultRejection.CONTAINS_COMPRESSED);
		FeedResult result2 = new FeedResult();

		List<FeedResult> outResults = processor.validateFeedResults(Arrays.asList(result1, result2), episode, new Config());
		Assert.assertEquals(1, outResults.size());
		Assert.assertEquals(result2, outResults.get(0));
		
		Mockito.verify(validator).validate(Arrays.asList(result1, result2), episode, new Config());
	}

	@Test(expected = MalformedURLException.class)
	public void fetchTorrentMalformedURLTest() throws Exception {
		FeedResult result = new FeedResult();
		result.setTorrentURL("MALFORMEDURL");

		processor.fetchTorrent(result);
	}

	@Test(expected = URISyntaxException.class)
	public void fetchTorrentURISyntaxTest() throws Exception {
		FeedResult result = new FeedResult();
		result.setTorrentURL("http:URISYNTAXEXCEPTION");

		processor.fetchTorrent(result);
	}

	@Test
	public void fetchTorrentTest() throws Exception {
		FeedResult result = new FeedResult();
		result.setTorrentURL("http://test.com?removed=true");

		TorrentService torrentService = Mockito.mock(TorrentService.class);
		Torrent torrent = new Torrent();
		Mockito.when(torrentService.read(new URI("http://test.com"))).thenReturn(torrent);
		processor.setTorrentService(torrentService);

		Torrent outTorrent = processor.fetchTorrent(result);
		Assert.assertEquals(torrent, outTorrent);
		
		Mockito.verify(torrentService).read(new URI("http://test.com"));
		Mockito.verifyNoMoreInteractions(torrentService);
	}

	@Test(expected = TorrentException.class)
	public void fetchTorrentDaoExceptionTest() throws Exception {
		FeedResult result = new FeedResult();
		result.setTorrentURL("http://test.com?removed=true");

		TorrentService torrentService = Mockito.mock(TorrentService.class);
		Mockito.when(torrentService.read(new URI("http://test.com"))).thenThrow(TorrentException.class);
		processor.setTorrentService(torrentService);

		processor.fetchTorrent(result);
		
		Mockito.verify(torrentService).read(new URI("http://test.com"));
		Mockito.verifyNoMoreInteractions(torrentService);
	}

	@Test
	public void validateTorrentTest() throws Exception {
		Config config = new Config();
		config.setIgnoreFolders(false);
		
		Show show = new Show();
		
		Torrent torrent = new Torrent();
		
		TorrentValidator validator = Mockito.mock(TorrentValidator.class);
		Mockito.when(validator.validate(torrent,show, config))
				.thenReturn(Optional.empty())
				.thenReturn(Optional.of(FeedResultRejection.CONTAINS_COMPRESSED));

		processor.setTorrentValidators(Arrays.asList(validator, validator));

		Optional<FeedResultRejection> result = processor.validateTorrent(torrent, config, show);
		
     	Assert.assertTrue(result.isPresent());
     	Assert.assertEquals(FeedResultRejection.CONTAINS_COMPRESSED, result.get());
		
		Mockito.verify(validator, Mockito.times(2)).validate(torrent, show, config);
		Mockito.verifyNoMoreInteractions(validator);
	}

	@Test(expected = ValidationException.class)
	public void validateTorrentExceptionTest() throws Exception {
		Config config = new Config();
		
		Show show = new Show();
		
		Torrent torrent = new Torrent();
		
		TorrentValidator validator = Mockito.mock(TorrentValidator.class);
		Mockito.when(validator.validate(torrent,show, config)).thenThrow(ValidationException.class);

		processor.setTorrentValidators(Arrays.asList(validator, validator));

		processor.validateTorrent(torrent, config, show);
	}

	@Test
	public void selectBestResultTorrentTest() {
		Torrent torrent = new Torrent();
		FeedResult result1 = new FeedResult();
		result1.setTorrent(torrent);
		
		FeedResult result2 = new FeedResult();

		Torrent result = processor.selectBestResultTorrent(Arrays.asList(result1, result2));

		Assert.assertNotNull(result);
		Assert.assertEquals(torrent, result);
		
	}

	@Test
	public void validatResultTorrentsTest() throws Exception {
		FeedResult result1 = new FeedResult();
		result1.setTorrentURL("http://test.com");
		FeedResult result2 = new FeedResult();
		result2.setTorrentURL(null);
		FeedResult result3 = new FeedResult();
		result3.setTorrentURL("http:URISYNTAXEXCEPTION");
		FeedResult result4 = new FeedResult();
		result4.setTorrentURL("http://test.com");
		FeedResult result5 = new FeedResult();
		result5.setTorrentURL("http://test.com");
		FeedResult result6 = new FeedResult();
		result6.setTorrentURL("http://test.com");
		FeedResult result7 = new FeedResult();
		result7.setTorrentURL("http://test.com");

		TorrentService torrentService = Mockito.mock(TorrentService.class);
		Torrent torrent = new Torrent();
		Mockito.when(torrentService.read(Mockito.any(URI.class)))
        		.thenReturn(torrent)
				.thenThrow(IOException.class)
        		.thenThrow(TorrentException.class)
        		.thenThrow(TorrentNotFoundException.class)
				.thenReturn(torrent);

		processor.setTorrentService(torrentService);

		TorrentValidator validator = Mockito.mock(TorrentValidator.class);
		Mockito.when(validator.validate(Mockito.any(Torrent.class),Mockito.any(Show.class), Mockito.any(Config.class)))
        				.thenReturn(Optional.empty())
        				.thenReturn(Optional.of(FeedResultRejection.CONTAINS_COMPRESSED));
		processor.setTorrentValidators(Arrays.asList(validator));

		Config config = new Config();

		List<FeedResult> results = processor.validateResultTorrents(Arrays.asList(result1, result2, result3, result4, result5, result6, result7),new Show(), config);
		
		Mockito.verify(torrentService, Mockito.times(5)).read(Mockito.any(URI.class));
		Mockito.verify(validator, Mockito.times(2)).validate(Mockito.any(Torrent.class), Mockito.any(Show.class), Mockito.any(Config.class));
		
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(result1, results.get(0));
		Assert.assertTrue(result1.getFeedResultRejections().isEmpty());
		Assert.assertEquals(1, result2.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.UNNABLE_TO_GET_TORRENT, result2.getFeedResultRejections().get(0));
		Assert.assertEquals(1, result3.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.UNNABLE_TO_GET_TORRENT, result3.getFeedResultRejections().get(0));
		Assert.assertEquals(1, result4.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.UNNABLE_TO_GET_TORRENT, result4.getFeedResultRejections().get(0));
		Assert.assertEquals(1, result5.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.UNNABLE_TO_GET_TORRENT, result5.getFeedResultRejections().get(0));
		Assert.assertEquals(1, result6.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.UNNABLE_TO_GET_TORRENT, result6.getFeedResultRejections().get(0));
		Assert.assertEquals(1, result7.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.CONTAINS_COMPRESSED, result7.getFeedResultRejections().get(0));
	}

	@Test(expected = ValidationException.class)
	public void validatResultTorrentsExceptionTest() throws Exception {
		Config config = new Config();
		FeedResult result = new FeedResult();
		result.setTorrentURL("http://test.com");

		TorrentService torrentService = Mockito.mock(TorrentService.class);
		Torrent torrent = new Torrent();
		Mockito.when(torrentService.read(Mockito.any(URI.class)))
        		.thenReturn(torrent);

		processor.setTorrentService(torrentService);

		TorrentValidator validator = Mockito.mock(TorrentValidator.class);
		Mockito.when(validator.validate(Mockito.any(Torrent.class),Mockito.any(Show.class), Mockito.any(Config.class)))
        				.thenThrow(ValidationException.class);
		processor.setTorrentValidators(Arrays.asList(validator));

		processor.validateResultTorrents(Arrays.asList(result), new Show(), config);
     
     	Assert.assertEquals(torrent, result.getTorrent());
     
     	Mockito.verify(torrentService).read(Mockito.any(URI.class));
		Mockito.verify(validator).validate(Mockito.any(Torrent.class),Mockito.any(Show.class), Mockito.any(Config.class));
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
     	
     	FeedResultValidator feedResultValidator = Mockito.mock(FeedResultValidator.class);
     	processor.setFeedResultValidators(Arrays.asList(feedResultValidator));
     
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
     	Mockito.when(torrentValidator.validate(torrent, show, config)).thenReturn(Optional.empty());
     	processor.setTorrentValidators(Arrays.asList(torrentValidator));

     	processor.process(episode);
     	
     	Instant testEnd = Instant.now();
     	
     	Assert.assertEquals(torrent, feedResult.getTorrent());
     	Assert.assertEquals(0, feedResult.getFeedResultRejections().size());
     	Assert.assertTrue(episode.getLastFeedUpdate().toEpochMilli()>= testStart.toEpochMilli());
     	Assert.assertTrue(episode.getLastFeedUpdate().toEpochMilli()<=testEnd.toEpochMilli());
     	Assert.assertTrue(episode.getLastFeedCheck().equals(episode.getLastFeedUpdate()));
 
     	Mockito.verify(service).update(episode);
     	Mockito.verifyNoMoreInteractions(service);
     	
     	Mockito.verify(feedService).read("SHOW_TITLE");
     	Mockito.verifyNoMoreInteractions(feedService);

     	Mockito.verify(feedResultValidator).validate(Arrays.asList(feedResult), episode, config);
     	Mockito.verifyNoMoreInteractions(feedResultValidator);

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
}
