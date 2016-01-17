package io.delimeat.core.feed;

import java.util.Arrays;

import io.delimeat.core.service.FeedProcessorService;
import io.delimeat.core.show.Show;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class FeedProcessor_ImplTest {

	private FeedProcessor_Impl processor;
	
	@Before
	public void setUp() throws Exception {
		processor = new FeedProcessor_Impl();
	}
	
	@Test
	public void feedProcessorServiceTest(){
		Assert.assertNull(processor.getService());
		FeedProcessorService service = Mockito.mock(FeedProcessorService.class);
		processor.setService(service);
		Assert.assertEquals(service, processor.getService());
	}
	
	@Test
	public void showTest(){
		Assert.assertNull(processor.getShow());
		Show show = new Show();
		processor.setShow(show);
		Assert.assertEquals(show, processor.getShow());
	}
		
	@Test
	public void foundResultsTest(){
		Assert.assertNull(processor.getFoundResults());
		FeedResult result = new FeedResult();
		processor.setFoundResults(Arrays.asList(result));
		Assert.assertNotNull(processor.getFoundResults());
		Assert.assertEquals(1, processor.getFoundResults().size());
		Assert.assertEquals(result, processor.getFoundResults().get(0));
	}
	
	@Test
	public void selectedResultTest(){
		Assert.assertNull(processor.getSelectedResult());
		FeedResult result = new FeedResult();
		processor.setSelectedResult(result);
		Assert.assertEquals(result, processor.getSelectedResult());	
	}
	
	@Test
	public void runSuccessfulTest() throws Exception{
		Assert.assertEquals(FeedProcessorStatus.PENDING, processor.getStatus());
		
		FeedProcessorService service = Mockito.mock(FeedProcessorService.class);
		Mockito.when(service.process(Mockito.any(FeedProcessor.class), Mockito.any(Show.class))).thenReturn(true);
		processor.setService(service);

		processor.run();
		
		Assert.assertEquals(FeedProcessorStatus.ENDED_SUCCESSFUL, processor.getStatus());
		Mockito.verify(service).process(Mockito.any(FeedProcessor.class), Mockito.any(Show.class));
	}
	
	@Test
	public void runUnsuccessfulTest() throws Exception{
		Assert.assertEquals(FeedProcessorStatus.PENDING, processor.getStatus());
		
		FeedProcessorService service = Mockito.mock(FeedProcessorService.class);
		Mockito.when(service.process(Mockito.any(FeedProcessor.class), Mockito.any(Show.class))).thenReturn(false);
		processor.setService(service);

		processor.run();
		
		Assert.assertEquals(FeedProcessorStatus.ENDED_UNSUCCESSFUL, processor.getStatus());
		Mockito.verify(service).process(Mockito.any(FeedProcessor.class), Mockito.any(Show.class));
	}
	
	@Test
	public void runExceptionTest() throws Exception{
		Assert.assertEquals(FeedProcessorStatus.PENDING, processor.getStatus());
		
		FeedProcessorService service = Mockito.mock(FeedProcessorService.class);
		Mockito.when(service.process(Mockito.any(FeedProcessor.class), Mockito.any(Show.class))).thenThrow(Exception.class);
		processor.setService(service);

		processor.run();
		
		Assert.assertEquals(FeedProcessorStatus.ENDED_ERROR, processor.getStatus());
		Mockito.verify(service).process(Mockito.any(FeedProcessor.class), Mockito.any(Show.class));
	}

}
