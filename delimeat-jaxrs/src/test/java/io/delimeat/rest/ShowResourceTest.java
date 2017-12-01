package io.delimeat.rest;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.delimeat.show.ShowService;
import io.delimeat.show.entity.Episode;
import io.delimeat.show.entity.Show;
import io.delimeat.show.exception.ShowException;

public class ShowResourceTest {
	
	private ShowResource resource;
	
	@BeforeEach
	public void setUp() {
		resource = new ShowResource();
	}
	
	@Test
	public void readAllTest() throws ShowException {
		resource.service = Mockito.mock(ShowService.class);
		Show show = new Show();
		Mockito.when(resource.service.readAll()).thenReturn(Arrays.asList(show));

		List<Show> results = resource.readAll();
		Assertions.assertEquals(1, results.size());
		Assertions.assertEquals(show, results.get(0));

		Mockito.verify(resource.service).readAll();
		Mockito.verifyNoMoreInteractions(resource.service);
	}
	
	@Test
	public void createTest() throws ShowException {
		resource.service = Mockito.mock(ShowService.class);
		Show show = new Show();
		
		Show result = resource.create(show);
		
		Assertions.assertEquals(show, result);
		
		Mockito.verify(resource.service).create(show);
		Mockito.verifyNoMoreInteractions(resource.service);
	}
	
	@Test
	public void readTest() throws ShowException {
		resource.service = Mockito.mock(ShowService.class);
		Show show = new Show();
		Mockito.when(resource.service.read(1L)).thenReturn(show);

		
		Show result = resource.read(1L);
		
		Assertions.assertEquals(show, result);
		
		Mockito.verify(resource.service).read(1L);
		Mockito.verifyNoMoreInteractions(resource.service);
	}
	
	@Test
	public void updateTest() throws ShowException {
		resource.service = Mockito.mock(ShowService.class);
		Show show = new Show();
		Mockito.when(resource.service.update(show)).thenReturn(show);

		
		Show result = resource.update(1L, show);
		
		Assertions.assertEquals(show, result);
		
		Mockito.verify(resource.service).update(show);
		Mockito.verifyNoMoreInteractions(resource.service);
	}
	
	@Test
	public void deleteTest() throws ShowException {
		resource.service = Mockito.mock(ShowService.class);
	
		resource.delete(1L);
		
		Mockito.verify(resource.service).delete(1L);
		Mockito.verifyNoMoreInteractions(resource.service);
	}
	
	@Test
	public void episodesTest() throws ShowException {
		resource.service = Mockito.mock(ShowService.class);
		Show show = new Show();
		Episode episode = new Episode();
		show.setEpisodes(Arrays.asList(episode));
		Mockito.when(resource.service.read(1L)).thenReturn(show);
		
		List<Episode> results = resource.episodes(1L);
		
		Assertions.assertEquals(1, results.size());
		Assertions.assertEquals(episode, results.get(0));
		
		Mockito.verify(resource.service).read(1L);
		Mockito.verifyNoMoreInteractions(resource.service);		
	}
}
