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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.core.env.Environment;

public class AppTest {

	private App application;
	
	@BeforeEach
	public void setUp(){
		application = new App();
	}
	
	@Test
	public void environmentTest(){
		Assertions.assertNull(application.getEnv());
		Environment env = Mockito.mock(Environment.class);
		application.setEnv(env);	
		Assertions.assertEquals(env, application.getEnv());
	}
	
	@Test
	public void udpScrapeSocketAddressTest() throws UnknownHostException{
		Environment env = Mockito.mock(Environment.class);
		Mockito.when(env.getProperty("io.delimeat.torrent.udp.address")).thenReturn("0.0.0.0");
		Mockito.when(env.getProperty("io.delimeat.torrent.udp.port")).thenReturn("1234");
		application.setEnv(env);
		
		InetSocketAddress address = application.udpScrapeSocketAddress();
		
		Assertions.assertEquals(Inet4Address.class, address.getAddress().getClass());
		Assertions.assertEquals("0.0.0.0", address.getAddress().getHostAddress());
		Assertions.assertEquals(1234, address.getPort());
		
		Mockito.verify(env).getProperty("io.delimeat.torrent.udp.address");
		Mockito.verify(env).getProperty("io.delimeat.torrent.udp.port");
	}
	
	@Test
	public void traceInteceptorAdvisorTest(){
		Advisor advisor = application.traceInteceptorAdvisor();
		Assertions.assertEquals(DefaultPointcutAdvisor.class, advisor.getClass());
		
		//TODO better testing

	}
}
