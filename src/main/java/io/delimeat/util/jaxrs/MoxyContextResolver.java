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

import java.util.Collections;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@SuppressWarnings("rawtypes")
public class MoxyContextResolver implements ContextResolver<JAXBContext> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MoxyContextResolver.class);

	private Map<String, Object> properties = Collections.emptyMap();
	private Class[] classes;

	/**
	 * Set classes
	 * 
	 * @param classes
	 */
	public void setClasses(Class... classes) {
		this.classes = classes;
	}

	/**
	 * @return classes
	 */
	public Class[] getClasses() {
		return classes;
	}

	/**
	 * Set properties
	 * 
	 * @param properties
	 */
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	/**
	 * @return properties
	 */
	public Map<String, Object> getProperties() {
		return properties;
	}

	/* (non-Javadoc)
	 * @see javax.ws.rs.ext.ContextResolver#getContext(java.lang.Class)
	 */
	@Override
	public JAXBContext getContext(Class<?> type) {
		final Class[] typeArray;
		if (classes != null && classes.length > 0) {
			typeArray = new Class[1 + classes.length];
			System.arraycopy(classes, 0, typeArray, 0, classes.length);
			typeArray[typeArray.length - 1] = type;
		} else {
			typeArray = new Class[] { type };
		}
		try {
			final JAXBContext context = JAXBContextFactory.createContext(typeArray, properties);
			LOGGER.info("Using JAXB context " + context);
			return context;
		} catch (Exception  e) {
			LOGGER.warn("Unable to create JAXB context.", e);
			return null;
		}
	}

}
