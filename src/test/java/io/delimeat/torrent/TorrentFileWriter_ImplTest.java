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
package io.delimeat.torrent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.config.domain.Config;
import io.delimeat.torrent.exception.TorrentException;

public class TorrentFileWriter_ImplTest {

	private TorrentFileWriter_Impl writer;
	
	@Before
	public void setUp(){
		writer = new TorrentFileWriter_Impl();
	}
	
	@Test
	public void buildUrlDirectoryTest() throws IOException, TorrentException{
		Config config = new Config();
		config.setOutputDirectory("OUTPUT");
		
		Assert.assertEquals(new URL("file:OUTPUT/FILENAME"), writer.buildUrl("FILENAME", config));
	}
	
	@Test
	public void buildUrlNullDirectoryTest() throws IOException, TorrentException{
		Config config = new Config();
		config.setOutputDirectory(null);
		
		String dir = System.getProperty("user.home");
		
		Assert.assertEquals(new URL("file:"+dir+"/FILENAME"), writer.buildUrl("FILENAME", config));
	}
	
	@Test
	public void buildUrlEmptyDirectoryTest() throws IOException, TorrentException{
		Config config = new Config();
		config.setOutputDirectory("");
		
		String dir = System.getProperty("user.home");
		
		Assert.assertEquals(new URL("file:"+dir+"/FILENAME"), writer.buildUrl("FILENAME", config));
	}
	
	@Test
	public void writeToStreamTest() throws Exception{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		writer.writeToStream(baos, "TEST".getBytes());
		Assert.assertEquals("TEST", new String(baos.toByteArray()));
	}
	
	@Test(expected=TorrentException.class)
	public void writeTest() throws TorrentException{
		writer.write("","JIBBERISH".getBytes(), new Config());
		Assert.fail();
	}
	
}
