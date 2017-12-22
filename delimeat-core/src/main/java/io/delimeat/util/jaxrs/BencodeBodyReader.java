package io.delimeat.util.jaxrs;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;

import io.delimeat.torrent.bencode.BDictionary;
import io.delimeat.torrent.bencode.BencodeException;
import io.delimeat.torrent.bencode.BencodeUtils;

public class BencodeBodyReader implements MessageBodyReader<BDictionary> {

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return (type.isAssignableFrom(BDictionary.class));
	}

	@Override
	public BDictionary readFrom(Class<BDictionary> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
		try {
			return BencodeUtils.decode(entityStream);
		} catch (BencodeException e) {
			throw new WebApplicationException(e);
		}
	}

}
