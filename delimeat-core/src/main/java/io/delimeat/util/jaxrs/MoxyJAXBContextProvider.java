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
package io.delimeat.util.jaxrs;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;
import javax.xml.bind.JAXBContext;

import org.eclipse.persistence.exceptions.JAXBException;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class MoxyJAXBContextProvider implements ContextResolver<JAXBContext> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MoxyJAXBContextProvider.class);
	
	@Context
	protected Providers providers;

	/**
	 * @return the providers
	 */
	public Providers getProviders() {
		return providers;
	}

	/**
	 * @param providers the providers to set
	 */
	public void setProviders(Providers providers) {
		this.providers = providers;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public JAXBContext getContext(Class<?> type) {
		LOGGER.trace("Context requested for {}", type);
		final ContextResolver<MoxyJAXBConfig> resolver = providers.getContextResolver(MoxyJAXBConfig.class, MediaType.APPLICATION_JSON_TYPE);
		final MoxyJAXBConfig config = resolver.getContext(type);

		Class[] classArray = new Class[config.getClasses().size()];
		config.getClasses().toArray(classArray);
		JAXBContext context = null;
		try {
			context =  JAXBContextFactory.createContext(classArray, config.getProperties());
			LOGGER.trace("Context created");
		} catch (JAXBException | javax.xml.bind.JAXBException e) {
			LOGGER.error("Unable to create context",e);
		}	
		return context;
	}

}
