package io.delimeat;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.interceptor.CustomizableTraceInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

import io.delimeat.config.ConfigDao;
import io.delimeat.config.ConfigDao_Impl;
import io.delimeat.config.ConfigService;
import io.delimeat.config.ConfigService_Impl;
import io.delimeat.feed.FeedDataSource;
import io.delimeat.feed.FeedService;
import io.delimeat.feed.FeedService_Impl;
import io.delimeat.feed.LimeTorrentsDataSource_Impl;
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
@ImportResource("classpath:spring/application-context.xml")
//@Configuration
//@ComponentScan
@ConfigurationProperties 
@PropertySource({ "classpath:delimeat.properties" })
//@EnableJpaRepositories("io.delimeat")
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true)
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
	
	@Bean
	public DataSource dataSource() {
		HikariDataSource ds = new HikariDataSource();
		ds.setJdbcUrl(env.getProperty("io.delimeat.show.jdbcUrl"));
		return ds;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,
			JpaVendorAdapter jpaVendorAdapter) {
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(dataSource);
		factory.setJpaVendorAdapter(jpaVendorAdapter);
		Properties jpaProperties = new Properties();
		jpaProperties.put("eclipselink.weaving", "false");
		jpaProperties.put("eclipselink.logging.level", "OFF");
		jpaProperties.put("eclipselink.ddl-generation", "create-or-extend-tables");
		jpaProperties.put("eclipselink.ddl-generation.output-mode", "database");
		factory.setJpaProperties(jpaProperties);
		factory.setPackagesToScan("io.delimeat.**.entity");
		return factory;
	}
	
	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		return new EclipseLinkJpaVendorAdapter();
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);
		return transactionManager;
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
	public TorrentService torrentService(Map<String,TorrentReader> torrentReaders, TorrentWriter torrentWriter, Map<String, ScrapeRequestHandler> scrapeRequestHandlers) {
		TorrentService_Impl service = new TorrentService_Impl();
		service.setReaders(torrentReaders);
		service.setWriter(torrentWriter);
		service.setScrapeRequestHandlers(scrapeRequestHandlers);
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
	public List<TorrentValidator> torrentValidators(TorrentValidator torrentSizeValidator, TorrentValidator torrentCompressedValidator, TorrentValidator torrentFileTypeValidator, TorrentValidator torrentFolderValidator){
		List<TorrentValidator> validators = new ArrayList<>();
		validators.add(torrentSizeValidator);
		validators.add(torrentCompressedValidator);
		validators.add(torrentFileTypeValidator);
		validators.add(torrentFolderValidator);
		return validators;
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
	
	@Bean
	public List<FeedResultFilter> feedResultFilters(FeedResultFilter feedResultTitleFilter, FeedResultFilter feedResultKeywordFilter, FeedResultFilter feedResultSeasonFilter, FeedResultFilter feedResultDailyFilter, FeedResultFilter feedResultMiniSeriesFilter){
		List<FeedResultFilter> filters = new ArrayList<>();
		filters.add(feedResultTitleFilter);
		filters.add(feedResultKeywordFilter);
		filters.add(feedResultSeasonFilter);
		filters.add(feedResultDailyFilter);
		filters.add(feedResultMiniSeriesFilter);
		return filters;
	}
	
}
