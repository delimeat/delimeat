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
package io.delimeat.util;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.persistence.jaxb.JAXBContextFactory;

public class JsonUtils {

	public static String toJson(Object object) throws JAXBException {
		if(object == null){ 
			return null; 
		}
		
		List<Class<?>> classList = new ArrayList<Class<?>>();
		if(Collection.class.isAssignableFrom(object.getClass())){
			Collection<?> collection = (Collection<?>)object;
			collection.forEach(p->{
				classList.add(p.getClass());
			});
		}else{
			classList.add(object.getClass());
		}
		Class<?>[] classes = new Class[classList.size()];
		classList.toArray(classes);
		
		JAXBContext jc = createContext(classes);
		StringWriter sw = new StringWriter();
		jc.createMarshaller().marshal(object, sw);
		return sw.toString();
	}
	
	public static <T> T fromJson(byte[] body, Class<T> responseClass) throws JAXBException{
		JAXBContext jc = createContext(responseClass);
		StreamSource source = new StreamSource(new ByteArrayInputStream(body));
		return jc.createUnmarshaller().unmarshal(source, responseClass).getValue();
	}
	
	public static JAXBContext createContext(Class<?>... classes) throws JAXBException{
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("eclipselink.media-type", "application/json");
		properties.put("eclipselink.json.include-root", false);
		return JAXBContextFactory.createContext(classes, properties);
	}
}
