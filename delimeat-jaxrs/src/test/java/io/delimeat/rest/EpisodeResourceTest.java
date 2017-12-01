package io.delimeat.rest;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.delimeat.show.EpisodeService;
import io.delimeat.show.entity.Episode;
import io.delimeat.show.exception.ShowException;

public class EpisodeResourceTest {
	
	private EpisodeResource resource;
	
	@BeforeEach
	public void setUp() {
		resource = new EpisodeResource();
	}
	
	@Test
	public void readTest() throws ShowException {
		resource.service = Mockito.mock(EpisodeService.class);
		Episode episode = new Episode();
		Mockito.when(resource.service.findAllPending()).thenReturn(Arrays.asList(episode));
		
		List<Episode> results = resource.read();
		Assertions.assertEquals(1, results.size());
		Assertions.assertEquals(episode, results.get(0));
		
		Mockito.verify(resource.service).findAllPending();
		Mockito.verifyNoMoreInteractions(resource.service);
	}
	
	@Test
	public void updateTest() throws ShowException {
		resource.service = Mockito.mock(EpisodeService.class);
		Episode episode = new Episode();
		Mockito.when(resource.service.update(episode)).thenReturn(episode);
		
		Episode result = resource.update(1L, episode);
		Assertions.assertEquals(episode, result);
		
		Mockito.verify(resource.service).update(episode);
		Mockito.verifyNoMoreInteractions(resource.service);
	}
}
