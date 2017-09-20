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

import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

public class ApplicationTest {

	private Application application;
	
	@Before
	public void setUp(){
		application = new Application();
	}
	
	@Test
	public void environmentTest(){
		Assert.assertNull(application.getEnv());
		Environment env = Mockito.mock(Environment.class);
		application.setEnv(env);	
		Assert.assertEquals(env, application.getEnv());
	}
	
	@Test
	public void datasourceTest(){
		Environment env = Mockito.mock(Environment.class);
		Mockito.when(env.getProperty("io.delimeat.show.jdbcUrl")).thenReturn("url");
		application.setEnv(env);
		
		DataSource dataSource = application.dataSource();
		Assert.assertEquals(HikariDataSource.class, dataSource.getClass());
		HikariDataSource hikariDataSource = (HikariDataSource)dataSource;
		Assert.assertEquals("url", hikariDataSource.getJdbcUrl());
		
		hikariDataSource.close();
		
		Mockito.verify(env).getProperty("io.delimeat.show.jdbcUrl");
	}
	
	@Test
	public void entityManagerFactoryTest(){
		DataSource dataSource = Mockito.mock(DataSource.class);
		JpaVendorAdapter adapter = Mockito.mock(JpaVendorAdapter.class);
		LocalContainerEntityManagerFactoryBean emf = application.entityManagerFactory(dataSource, adapter);
		
		Assert.assertEquals(dataSource, emf.getDataSource());
		Assert.assertEquals(adapter, emf.getJpaVendorAdapter());
		Assert.assertEquals(4, emf.getJpaPropertyMap().size());
		Assert.assertEquals("database", emf.getJpaPropertyMap().get("eclipselink.ddl-generation.output-mode"));
		Assert.assertEquals("create-or-extend-tables", emf.getJpaPropertyMap().get("eclipselink.ddl-generation"));
		Assert.assertEquals("OFF", emf.getJpaPropertyMap().get("eclipselink.logging.level"));
		Assert.assertEquals("false", emf.getJpaPropertyMap().get("eclipselink.weaving"));

	}
	
	@Test
	public void jpaVendorAdapter(){
		JpaVendorAdapter adapter = application.jpaVendorAdapter();
		
		Assert.assertEquals(EclipseLinkJpaVendorAdapter.class, adapter.getClass());
	}
	
	@Test
	public void transactionManagerTest(){
		EntityManagerFactory emf = Mockito.mock(EntityManagerFactory.class);
		PlatformTransactionManager transactionManager = application.transactionManager(emf);
		
		Assert.assertEquals(JpaTransactionManager.class, transactionManager.getClass());
		JpaTransactionManager jpaTransactionManager = (JpaTransactionManager)transactionManager;
		Assert.assertEquals(emf,jpaTransactionManager.getEntityManagerFactory());
	}
	
	@Test
	public void udpScrapeSocketAddressTest() throws UnknownHostException{
		Environment env = Mockito.mock(Environment.class);
		Mockito.when(env.getProperty("io.delimeat.torrent.udp.address")).thenReturn("0.0.0.0");
		Mockito.when(env.getProperty("io.delimeat.torrent.udp.port")).thenReturn("1234");
		application.setEnv(env);
		
		InetSocketAddress address = application.udpScrapeSocketAddress();
		
		Assert.assertEquals(Inet4Address.class, address.getAddress().getClass());
		Assert.assertEquals("0.0.0.0", address.getAddress().getHostAddress());
		Assert.assertEquals(1234, address.getPort());
		
		Mockito.verify(env).getProperty("io.delimeat.torrent.udp.address");
		Mockito.verify(env).getProperty("io.delimeat.torrent.udp.port");
	}
	
	@Test
	public void traceInteceptorAdvisorTest(){
		Advisor advisor = application.traceInteceptorAdvisor();
		Assert.assertEquals(DefaultPointcutAdvisor.class, advisor.getClass());
		
		//TODO better testing

	}
}
