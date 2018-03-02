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
package io.delimeat;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.interceptor.CustomizableTraceInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.delimeat.config.ConfigDao;
import io.delimeat.config.ConfigDao_Impl;
import io.delimeat.config.ConfigService;
import io.delimeat.config.ConfigService_Impl;
import io.delimeat.feed.FeedDataSource;
import io.delimeat.feed.FeedService;
import io.delimeat.feed.FeedService_Impl;
import io.delimeat.feed.LimeTorrentsDataSource_Impl;
import io.delimeat.feed.TorrentDownloadsDataSource_Impl;
import io.delimeat.feed.TorrentProjectDataSource_Impl;
import io.delimeat.feed.ZooqleDataSource_Impl;
import io.delimeat.guide.GuideDataSource;
import io.delimeat.guide.GuideService;
import io.delimeat.guide.GuideService_Impl;
import io.delimeat.guide.TvdbGuideDataSource_Impl;
import io.delimeat.processor.FeedItemProcessor;
import io.delimeat.processor.FeedItemProcessor_Impl;
import io.delimeat.processor.FeedItemReader;
import io.delimeat.processor.FeedItemReader_Impl;
import io.delimeat.processor.GuideItemProcessor;
import io.delimeat.processor.GuideItemProcessor_Impl;
import io.delimeat.processor.GuideItemReader;
import io.delimeat.processor.GuideItemReader_Impl;
import io.delimeat.processor.ProcessorService;
import io.delimeat.processor.ProcessorService_Impl;
import io.delimeat.processor.filter.DailyEpisodeFilter_Impl;
import io.delimeat.processor.filter.ExcludedKeywordFilter_Impl;
import io.delimeat.processor.filter.FeedResultFilter;
import io.delimeat.processor.filter.MiniSeriesEpisodeFilter_Impl;
import io.delimeat.processor.filter.SeasonEpisodeFilter_Impl;
import io.delimeat.processor.filter.TitleFilter_Impl;
import io.delimeat.processor.validation.TorrentCompressedValidator_Impl;
import io.delimeat.processor.validation.TorrentFileTypeValidator_Impl;
import io.delimeat.processor.validation.TorrentFolderValidator_Impl;
import io.delimeat.processor.validation.TorrentSizeValidator_Impl;
import io.delimeat.processor.validation.TorrentValidator;
import io.delimeat.show.EpisodeDao;
import io.delimeat.show.EpisodeDao_Impl;
import io.delimeat.show.EpisodeService;
import io.delimeat.show.EpisodeService_Impl;
import io.delimeat.show.ShowDao;
import io.delimeat.show.ShowDao_Impl;
import io.delimeat.show.ShowService;
import io.delimeat.show.ShowService_Impl;
import io.delimeat.torrent.HttpScrapeRequestHandler_Impl;
import io.delimeat.torrent.HttpTorrentReader_Impl;
import io.delimeat.torrent.MagnetScrapeRequestHandler_Impl;
import io.delimeat.torrent.MagnetTorrentReader_Impl;
import io.delimeat.torrent.ScrapeRequestHandler;
import io.delimeat.torrent.TorrentFileWriter_Impl;
import io.delimeat.torrent.TorrentReader;
import io.delimeat.torrent.TorrentService;
import io.delimeat.torrent.TorrentService_Impl;
import io.delimeat.torrent.TorrentWriter;
import io.delimeat.torrent.UdpScrapeRequestHandler_Impl;

@SpringBootApplication
@EnableScheduling
public class App {

	@Autowired
	private Environment env;
	
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);  
		
	}
	
	/**
	 * @return the env
	 */
	public Environment getEnv() {
		return env;
	}

	/**
	 * @param env the env to set
	 */
	public void setEnv(Environment env) {
		this.env = env;
	}

	@Bean(name="io.delimeat.torrent.udpAddress")
	public InetSocketAddress udpScrapeSocketAddress(){
		String address = env.getProperty("io.delimeat.torrent.udp.address");
		int port = Integer.valueOf(env.getProperty("io.delimeat.torrent.udp.port"));
		return new InetSocketAddress(address,port);
	}

	@Bean
	public CustomizableTraceInterceptor customizableTraceInterceptor() {
		CustomizableTraceInterceptor cti = new CustomizableTraceInterceptor();
		cti.setUseDynamicLogger(true);
		cti.setHideProxyClassNames(true);
		cti.setEnterMessage("Entering $[targetClassName].$[methodName]($[arguments])");
		cti.setExitMessage("Leaving $[targetClassName].$[methodName](): $[returnValue]");
		cti.setExceptionMessage("Exception $[targetClassName].$[methodName]($[arguments]): $[exception]");
		return cti;
	}

	@Bean
	public Advisor traceInteceptorAdvisor() {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression("within(io.delimeat..*)");
		return new DefaultPointcutAdvisor(pointcut, customizableTraceInterceptor());
	}
	
	@Bean
	public ConfigDao configDao() {
		return new ConfigDao_Impl();
	}
	
	@Bean
	public ConfigService configService(ConfigDao configDao) {
		ConfigService_Impl service = new ConfigService_Impl();
		service.setConfigDao(configDao);
		service.setDefaultOutputDir(env.getProperty("io.delimeat.config.defaultOutputDir"));
		return service;
	}	
	
	@Bean
	public GuideDataSource guideDataSource() throws URISyntaxException {
		TvdbGuideDataSource_Impl dataSource = new TvdbGuideDataSource_Impl();
		dataSource.setBaseUri(new URI(env.getProperty("io.delimeat.guide.tvdb.baseUri")));
		return dataSource;
	}
	
	@Bean
	public GuideService guideService(GuideDataSource guideDataSource) {
		GuideService_Impl service = new GuideService_Impl();
		service.setGuideDataSource(guideDataSource);
		return service;
	}
	
	@Bean 
	public FeedDataSource limetorrentsDataSource() throws URISyntaxException {
		LimeTorrentsDataSource_Impl dataSource = new LimeTorrentsDataSource_Impl();
		dataSource.setBaseUri(new URI(env.getProperty("io.delimeat.feed.limetorrents.baseUri")));
		return dataSource;
	}	
	
	@Bean 
	public FeedDataSource torrentDownloadsDataSource() throws URISyntaxException {
		TorrentDownloadsDataSource_Impl dataSource = new TorrentDownloadsDataSource_Impl();
		dataSource.setBaseUri(new URI(env.getProperty("io.delimeat.feed.torrentdownloads.baseUri")));
		return dataSource;
	}
	
	@Bean 
	public FeedDataSource torrentProjectDataSource() throws URISyntaxException {
		TorrentProjectDataSource_Impl dataSource = new TorrentProjectDataSource_Impl();
		dataSource.setBaseUri(new URI(env.getProperty("io.delimeat.feed.torrentproject.baseUri")));
		return dataSource;
	}
	
	@Bean 
	public FeedDataSource zooqleDataSource() throws URISyntaxException {
		ZooqleDataSource_Impl dataSource = new ZooqleDataSource_Impl();
		dataSource.setBaseUri(new URI(env.getProperty("io.delimeat.feed.zooqle.baseUri")));
		return dataSource;
	}
	
	@Bean
	public FeedService feedService(List<FeedDataSource> feedDataSources) {
		FeedService_Impl service = new FeedService_Impl();
		service.setFeedDataSources(feedDataSources);
		return service;			
	}
	
	@Bean
	public EpisodeDao episodeDao() {
		return new EpisodeDao_Impl();
	}
	
	@Bean
	public EpisodeService episodeService(EpisodeDao episodeDao) {
		EpisodeService_Impl service = new EpisodeService_Impl();
		service.setEpisodeDao(episodeDao);
		return service;
	}
	
	@Bean
	public ShowDao showDao() {
		return new ShowDao_Impl();
	}
	
	@Bean
	public ShowService showService(ShowDao showDao, EpisodeService episodeService, GuideService guideService) {
		ShowService_Impl service = new ShowService_Impl();
		service.setShowDao(showDao);
		service.setEpisodeService(episodeService);
		service.setGuideService(guideService);
		return service;
	}
	
	@Bean
	public FeedItemProcessor feedItemProcessor(ConfigService configService, EpisodeService episodeService, FeedService feedService, TorrentService torrentService, List<TorrentValidator> torrentValidators, List<FeedResultFilter> feedResultFilters) {
		FeedItemProcessor_Impl processor = new FeedItemProcessor_Impl();
		processor.setConfigService(configService);
		processor.setEpisodeService(episodeService);
		processor.setFeedService(feedService);
		processor.setTorrentService(torrentService);
		processor.setTorrentValidators(torrentValidators);
		processor.setFeedResultFilters(feedResultFilters);
		return processor;
	}
	
	@Bean
	public FeedItemReader feedItemReader(EpisodeService episodeService, ConfigService configService) {
		FeedItemReader_Impl reader = new FeedItemReader_Impl();
		reader.setConfigService(configService);
		reader.setEpisodeService(episodeService);
		return reader;
	}
	
	@Bean 
	public GuideItemProcessor guideItemProcessor(ShowService showService, EpisodeService episodeService, GuideService guideService) {
		GuideItemProcessor_Impl processor = new GuideItemProcessor_Impl();
		processor.setEpisodeService(episodeService);
		processor.setGuideService(guideService);
		processor.setShowService(showService);
		return processor;
	}
	
	@Bean
	public GuideItemReader guideItemReader(ShowService showService) {
		GuideItemReader_Impl reader = new GuideItemReader_Impl();
		reader.setShowService(showService);
		return reader;
	}
	
	@Bean
	public ProcessorService processorService(FeedItemProcessor feedItemProcessor, FeedItemReader feedItemReader, GuideItemProcessor guideItemProcessor, GuideItemReader guideItemReader) {
		ProcessorService_Impl service = new ProcessorService_Impl();
		service.setFeedItemProcessor(feedItemProcessor);
		service.setFeedItemReader(feedItemReader);
		service.setGuideItemProcessor(guideItemProcessor);
		service.setGuideItemReader(guideItemReader);
		return service;
	}
	
	@Bean
	public TorrentService torrentService(Map<String,TorrentReader> torrentReaders, TorrentWriter torrentWriter, Map<String, ScrapeRequestHandler> scrapeRequestHandlers) {
		TorrentService_Impl service = new TorrentService_Impl();
		service.setReaders(torrentReaders);
		service.setWriter(torrentWriter);
		service.setScrapeRequestHandlers(scrapeRequestHandlers);
		service.setMagnetUriTemplate("magnet:?xt=urn:btih:%s");
		return service;
	}
	
	@Bean
	public TorrentReader httpTorrentReader() {
		return new HttpTorrentReader_Impl();
	}
	
	@Bean
	public TorrentReader magnetTorrentReader() {
		MagnetTorrentReader_Impl reader = new MagnetTorrentReader_Impl();
		reader.setDownloadUriTemplate(env.getProperty("io.delimeat.torrent.magnet.defaultDownloadTemplate"));
		return reader;
	}
	
	@Bean
	public Map<String, TorrentReader> torrentReaders(TorrentReader httpTorrentReader, TorrentReader magnetTorrentReader){
		Map<String,TorrentReader> map = new HashMap<>();
		map.put("HTTP", httpTorrentReader);
		map.put("HTTPS", httpTorrentReader);
		map.put("MAGNET", magnetTorrentReader);
		return map;
	}
	
	@Bean
	public TorrentWriter torrentWriter() {
		return new TorrentFileWriter_Impl();
	}
	
	@Bean
	public ScrapeRequestHandler httpScrapeRequestHandler() {
		return new HttpScrapeRequestHandler_Impl();
	}
	
	@Bean
	public ScrapeRequestHandler magnetScrapeRequestHandler() throws URISyntaxException {
		MagnetScrapeRequestHandler_Impl handler = new MagnetScrapeRequestHandler_Impl();
		handler.setDefaultTracker(new URI(env.getProperty("io.delimeat.torrent.magnet.defaultTrackerUri")));
		return handler;
	}
	
	@Bean
	public ScrapeRequestHandler udpScrapeRequestHandler(InetSocketAddress udpScrapeSocketAddress) {
		UdpScrapeRequestHandler_Impl handler = new UdpScrapeRequestHandler_Impl();
		handler.setAddress(udpScrapeSocketAddress);
		return handler;
	}	
	
	@Bean
	public Map<String, ScrapeRequestHandler> scrapeRequestHandlers(ScrapeRequestHandler httpScrapeRequestHandler, ScrapeRequestHandler magnetScrapeRequestHandler, ScrapeRequestHandler udpScrapeRequestHandler){
		Map<String,ScrapeRequestHandler> map = new HashMap<>();
		map.put("HTTP", httpScrapeRequestHandler);
		map.put("HTTPS", httpScrapeRequestHandler);
		map.put("MAGNET", magnetScrapeRequestHandler);
		map.put("UDP", udpScrapeRequestHandler);
		return map;
	}
	
	@Bean
	public TorrentValidator torrentSizeValidator() {
		return new TorrentSizeValidator_Impl();
	}
	
	@Bean
	public TorrentValidator torrentCompressedValidator() {
		return new TorrentCompressedValidator_Impl ();
	}
	
	@Bean
	public TorrentValidator torrentFileTypeValidator() {
		return new TorrentFileTypeValidator_Impl  ();
	}
	
	@Bean
	public TorrentValidator torrentFolderValidator() {
		return new TorrentFolderValidator_Impl();
	}

	
	@Bean
	public FeedResultFilter feedResultTitleFilter() {
		return new TitleFilter_Impl();
	}
	
	@Bean
	public FeedResultFilter feedResultKeywordFilter() {
		return new ExcludedKeywordFilter_Impl ();
	}
	
	@Bean
	public FeedResultFilter feedResultSeasonFilter() {
		return new SeasonEpisodeFilter_Impl ();
	}
	
	@Bean
	public FeedResultFilter feedResultDailyFilter() {
		return new DailyEpisodeFilter_Impl ();
	}
	
	@Bean
	public FeedResultFilter feedResultMiniSeriesFilter() {
		return new MiniSeriesEpisodeFilter_Impl ();
	}
	
}
