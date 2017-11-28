package io.delimeat.util.http;

import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class JaxbUnmarshaller_ImplTest {

	private JaxbUnmarshaller_Impl<String> unmarshaller;
	
	@BeforeEach
	public void setUp() {
		unmarshaller = new JaxbUnmarshaller_Impl<String>();
	}
	
	@Test
	public void contextTest() {
		Assertions.assertNull(unmarshaller.getContext());
		JAXBContext context = Mockito.mock(JAXBContext.class);
		unmarshaller.setContext(context);
		Assertions.assertEquals(context, unmarshaller.getContext());
	}
	
	@Test
	public void marshallerTest() throws Exception {
		JAXBContext context = Mockito.mock(JAXBContext.class);
		Marshaller marshaller = Mockito.mock(Marshaller.class);
		Mockito.when(context.createMarshaller()).thenReturn(marshaller);
		unmarshaller.setContext(context);
		
		OutputStream outputStream = Mockito.mock(OutputStream.class);
		unmarshaller.marshall(outputStream, "STRING");
		
		Mockito.verify(context).createMarshaller();
		Mockito.verify(marshaller).marshal("STRING",outputStream);
		Mockito.verifyNoMoreInteractions(context,marshaller);
	}
}
