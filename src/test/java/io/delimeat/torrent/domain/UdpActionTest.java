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
package io.delimeat.torrent.domain;

import org.junit.Assert;
import org.junit.Test;

public class UdpActionTest {

	@Test
	public void connectTest(){
		Assert.assertEquals(0, UdpAction.CONNECT.value());
	}
	
	@Test
	public void announceTest(){
		Assert.assertEquals(1, UdpAction.ANNOUNCE.value());
	}
	
	@Test
	public void scrapeTest(){
		Assert.assertEquals(2, UdpAction.SCRAPE.value());
	}
	
	@Test
	public void errorTest(){
		Assert.assertEquals(3, UdpAction.ERROR.value());
	}
	
	@Test
	public void getSuccessTest(){
		Assert.assertEquals(UdpAction.CONNECT, UdpAction.get(0));
	}
	
	@Test
	public void getFailTest(){
		Assert.assertNull( UdpAction.get(Integer.MAX_VALUE));
	}
}
