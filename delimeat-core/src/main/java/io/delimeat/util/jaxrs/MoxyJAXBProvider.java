package io.delimeat.util.jaxrs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class MoxyJAXBProvider  implements MessageBodyReader<Object>, MessageBodyWriter<Object>{

	private static final Logger LOGGER = LoggerFactory.getLogger(MoxyJAXBProviderTest.class);

	@Context
	protected Providers providers;
	
	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return (mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE) || mediaType.isCompatible(MediaType.APPLICATION_XML_TYPE));
	}

	@Override
	public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
		LOGGER.trace("Reading {}", type);
		ContextResolver<JAXBContext> resolver = providers.getContextResolver(JAXBContext.class, mediaType);
		Object object = null;
		try{
			object = resolver.getContext(type).createUnmarshaller().unmarshal(new StreamSource(entityStream),type).getValue();
			LOGGER.trace("Read {}", object);
		}catch(JAXBException ex) {
			LOGGER.error("Unable to read object", ex);
		}
		return object;
	}

	@Override
	public long getSize(Object entity, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return (mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE) || mediaType.isCompatible(MediaType.APPLICATION_XML_TYPE));
	}

	@Override
	public void writeTo(Object entity, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream outputStream) throws IOException, WebApplicationException {
		LOGGER.trace("Writing {}", entity);
		ContextResolver<JAXBContext> resolver = providers.getContextResolver(JAXBContext.class, mediaType);
		try{
			resolver.getContext(type).createMarshaller().marshal(entity, outputStream);
			LOGGER.trace("Wrote {}", entity);
		}catch(JAXBException ex) {
			LOGGER.error("Unable to read object", ex);
		}
		
	}

}
