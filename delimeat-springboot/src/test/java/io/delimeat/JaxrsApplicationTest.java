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
