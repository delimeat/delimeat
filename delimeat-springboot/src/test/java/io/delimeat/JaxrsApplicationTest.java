package io.delimeat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.delimeat.rest.ConfigResource;
import io.delimeat.rest.EpisodeResource;
import io.delimeat.rest.GuideResource;
import io.delimeat.rest.ShowResource;

public class JaxrsApplicationTest {

	private JaxrsApplication app;
	
	@BeforeEach
	public void setUp() {
		app = new JaxrsApplication();
	}
	
	@Test
	public void resourcesTest() {
		Assertions.assertEquals(4, app.getClasses().size());
		Assertions.assertTrue(app.getClasses().contains(ConfigResource.class));
		Assertions.assertTrue(app.getClasses().contains(EpisodeResource.class));
		Assertions.assertTrue(app.getClasses().contains(GuideResource.class));
		Assertions.assertTrue(app.getClasses().contains(ShowResource.class));
	}
}
