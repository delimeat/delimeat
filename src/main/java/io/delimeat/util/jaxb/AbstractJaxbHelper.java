package io.delimeat.util.jaxb;

import io.delimeat.util.UrlHandler;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public abstract class AbstractJaxbHelper {

	protected static final String ENCODING = "UTF-8";

	private Unmarshaller unmarshaller;
	private Marshaller marshaller;
	private UrlHandler urlHandler;

	/**
	 * @return the unmarshaller
	 */
	public Unmarshaller getUnmarshaller() {
		return unmarshaller;
	}

	/**
	 * @param unmarshaller
	 *            the unmarshaller to set
	 */
	public void setUnmarshaller(Unmarshaller unmarshaller) {
		this.unmarshaller = unmarshaller;
	}

	/**
	 * @return the marshaller
	 */
	public Marshaller getMarshaller() {
		return marshaller;
	}

	/**
	 * @param marshaller
	 *            the marshaller to set
	 */
	public void setMarshaller(Marshaller marshaller) {
		this.marshaller = marshaller;
	}

	/**
	 * @return the urlHandler
	 */
	public UrlHandler getUrlHandler() {
		return urlHandler;
	}

	/**
	 * @param urlHandler
	 *            the urlHandler to set
	 */
	public void setUrlHandler(UrlHandler urlHandler) {
		this.urlHandler = urlHandler;
	}

	protected <T> T unmarshal(InputStream input, Class<T> type) throws JAXBException {
		StreamSource source = new StreamSource(input);
		return unmarshaller.unmarshal(source, type).getValue();
	}

	protected void marshal(OutputStream output, Object object) throws JAXBException {
		StreamResult result = new StreamResult(output);
		getMarshaller().marshal(object, result);
	}

}
