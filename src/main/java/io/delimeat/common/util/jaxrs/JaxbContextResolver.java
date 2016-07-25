package io.delimeat.common.util.jaxrs;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

@Provider
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class JaxbContextResolver implements ContextResolver<JAXBContext> {

	private JAXBContext context;
	private List<Class<?>> classes = new ArrayList<Class<?>>();

	@Override
	public JAXBContext getContext(Class<?> type) {
		return context;
	}

	/**
	 * @param context
	 *            the context to set
	 */
	public void setContext(JAXBContext context) {
		this.context = context;
	}

	/**
	 * @return the classes
	 */
	public List<Class<?>> getClasses() {
		return classes;
	}

	/**
	 * @param classes
	 *            the classes to set
	 */
	public void setClasses(List<Class<?>> classes) {
		this.classes = classes;
	}
}
