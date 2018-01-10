package io.delimeat;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
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
import io.delimeat.show.EpisodeDao;
import io.delimeat.show.EpisodeDao_Impl;
import io.delimeat.show.EpisodeService;
import io.delimeat.show.EpisodeService_Impl;
import io.delimeat.show.ShowDao;
import io.delimeat.show.ShowDao_Impl;
import io.delimeat.show.ShowService;
import io.delimeat.show.ShowService_Impl;

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
	
}
