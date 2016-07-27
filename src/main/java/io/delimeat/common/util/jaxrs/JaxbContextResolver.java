package io.delimeat.common.util.jaxrs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

@Provider
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class JaxbContextResolver implements ContextResolver<JAXBContext> {

  	private static final Logger LOGGER = LoggerFactory.getLogger(JaxbContextResolver.class);
   
  	private Map<String, Object> properties = Collections.<String, Object>emptyMap();
   private Class[] classes;
  
  	public void setClasses(Class... classes){
     	this.classes = classes;
   }
  
  	public Class[] getClasses(){
     return classes;
   }
  
  	public void setProperties(Map<String, Object> properties){
     	this.properties = properties;
   }
  
  	public Map<String, Object> getProperties(){
     	return properties;
   }
  
	@Override
    public JAXBContext getContext(Class<?> type) {
        final Class[] typeArray;
        if (classes != null && classes.length > 0) {
            typeArray = new Class[1 + classes.length];
            System.arraycopy(classes, 0, typeArray, 0, classes.length);
            typeArray[typeArray.length - 1] = type;
        } else {
            typeArray = new Class[]{type};
        }

        try {
            final JAXBContext context = JAXBContext.newInstance(typeArray, properties);
         	LOGGER.info("Using JAXB context " + context);
            return context;
        } catch (JAXBException e) {
         	LOGGER.warn("Unable to create JAXB context.");
            return null;
        }
    }

}
