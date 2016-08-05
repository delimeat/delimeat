package io.delimeat.util.jaxrs;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.eclipse.persistence.jaxb.rs.MOXyJsonProvider;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomMOXyJsonProvider extends MOXyJsonProvider {

	  /**
	   * hack to resovle issue using Jersey GenericType to return a root array
	   * MOXyJsonProvider only calls context resovlers if there is 1 domain class
	   * Jersey will return two classes when using GenericType with a Collection & a Generic Type
	   */
	  @Override
	  public JAXBContext getJAXBContext(Set<Class<?>> domainClasses,
	                         Annotation[] annotations,
	                         MediaType mediaType,
	                         MultivaluedMap<String,?> httpHeaders)
	                              throws JAXBException{
		if (domainClasses.size() > 1) {
			Iterator<Class<?>> it = domainClasses.iterator();
			while (it.hasNext()) {
				Class<?> domainClass = it.next();
				if (Collection.class.isAssignableFrom(domainClass)) {
					it.remove();
				}
			}
		}
		return super.getJAXBContext(domainClasses, annotations, mediaType,httpHeaders);
	  }
}
