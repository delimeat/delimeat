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
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.interceptor.CustomizableTraceInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan
@PropertySource({ "classpath:delimeat.properties" })
@EnableJpaRepositories("io.delimeat")
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableScheduling
//@EnableCaching()
public class Application {

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	@Autowired
	private Environment env;

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

	public static void main(String[] args) throws Exception {
		Long appStart = System.currentTimeMillis();
		LOGGER.info("Delimeat starting");

		//@SuppressWarnings("resource")
		try(AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Application.class)){
			ctx.registerShutdownHook();
		}
		

		LOGGER.info("Delimeat started in {} ms", System.currentTimeMillis() - appStart);
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
		factory.setPackagesToScan("io.delimeat.**.domain");
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
		// pointcut.setExpression("execution(public * io.delimeat..*.*(..))");
		return new DefaultPointcutAdvisor(pointcut, customizableTraceInterceptor());
	}
	
	//TODO cache disabled because spring not applying cache from interfaces
	// re-enable once spring works, possible replace with caffeine 
	/*
	@Bean
	public CacheManager cacheManager(){
		SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(
          new ConcurrentMapCache("feed"), 
          new ConcurrentMapCache("guide-info"),
          new ConcurrentMapCache("guide-episodes"),
          new ConcurrentMapCache("guide-search"),
          new ConcurrentMapCache("torrent"),
          new ConcurrentMapCache("scrape")));
        return cacheManager;
	}
	*/

}
