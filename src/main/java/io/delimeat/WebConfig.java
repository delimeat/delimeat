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

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.inject.Inject;
import javax.servlet.Filter;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.interceptor.CustomizableTraceInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class WebConfig  {

	@Inject Environment env;

	@Bean
	public InetAddress udpScrapeBindAddress() throws UnknownHostException{
		return InetAddress.getByName(env.getProperty("io.delimeat.torrent.udp.address"));
	}
	
	@Bean
	public DatagramSocket updScrapeDatagramSocket() throws NumberFormatException, SocketException, UnknownHostException{
		return new DatagramSocket(Integer.valueOf(env.getProperty("io.delimeat.torrent.udp.port")), udpScrapeBindAddress());
	}
	
	@Bean
	public Filter shallowEtagHeaderFilter() {
		return new ShallowEtagHeaderFilter();
	}
	
	@Bean("processorExecutorId")
	public ThreadPoolTaskExecutor processorExecutor(){
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(1);
		executor.setMaxPoolSize(10);
		executor.setQueueCapacity(25);
		return executor;
	}
	/*
	  @Bean
	  public Jackson2ObjectMapperBuilder objectMapperBuilder(){
		  return new Jackson2ObjectMapperBuilder()
				  		.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				  		.featuresToDisable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
				  		.featuresToDisable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);
	  }
	 */

	@Bean
	public CustomizableTraceInterceptor customizableTraceInterceptor() {
		CustomizableTraceInterceptor cti = new CustomizableTraceInterceptor();
		cti.setUseDynamicLogger(true);
		cti.setEnterMessage("Entering $[methodName]($[arguments])");
		cti.setExitMessage("Entering $[methodName]($[arguments]), Returned $[returnValue]");
		cti.setExceptionMessage("Exception $[methodName]($[arguments]): $[exception]");
		return cti;
	}

	@Bean
	public Advisor jpaRepositoryAdvisor() {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression("within(io.delimeat..*)");
		return new DefaultPointcutAdvisor(pointcut, customizableTraceInterceptor());
	}

}
