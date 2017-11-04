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
package io.delimeat.util.http;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.transform.stream.StreamSource;

public class JaxbUnmarshaller_Impl<T> implements BodyUnmarshaller<T>, BodyMarshaller {

	private JAXBContext context;
	
	/**
	 * @return the context
	 */
	public JAXBContext getContext() {
		return context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(JAXBContext context) {
		this.context = context;
	}
	
	/* (non-Javadoc)
	 * @see io.delimeat.util.rest.BodyUnmarshaller#handle(java.io.InputStream, java.lang.Class)
	 */
	@Override
	public T unmarshall(InputStream input, Class<T> responseClass) throws Exception {
		StreamSource source = new StreamSource(input);
		return getContext().createUnmarshaller().unmarshal(source, responseClass).getValue();
	}

	/* (non-Javadoc)
	 * @see io.delimeat.util.rest.BodyMarshaller#marshall(java.io.OutputStream, java.lang.Object)
	 */
	@Override
	public <S> void marshall(OutputStream output, S requestObject) throws Exception {
		getContext().createMarshaller().marshal(requestObject, output);
	}

}
