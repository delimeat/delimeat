package io.delimeat.util.jaxrs;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
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
	
	private final Map<String, Object> properties;
	private List<Class<?>> classes;

	private JAXBContext context = null;
	
	public MoxyJAXBContextProvider(Map<String, Object> properties, List<Class<?>> classes) {
		this.properties = properties;
		this.classes = classes;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public JAXBContext getContext(Class<?> type) {
		LOGGER.trace("Context requested for {}", type);
		if(context == null) {
			Class[] classArray = new Class[classes.size()];
			classes.toArray(classArray);
			try {
				context =  JAXBContextFactory.createContext(classArray, properties);
				LOGGER.trace("Context created");
			} catch (JAXBException | javax.xml.bind.JAXBException e) {
				LOGGER.error("Unable to create context",e);
			}	
		}
		return context;
		
	}

}
