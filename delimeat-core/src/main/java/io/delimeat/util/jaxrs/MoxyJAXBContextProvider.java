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
