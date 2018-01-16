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
package io.delimeat.util.jaxrs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.ws.rs.WebApplicationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.delimeat.torrent.bencode.BDictionary;
import io.delimeat.torrent.bencode.BInteger;

public class BencodeBodyReaderTest {

	private BencodeBodyReader reader;
	
	@BeforeEach
	public void setUp() {
		reader = new BencodeBodyReader();
	}
	
	@Test
	public void isReadableTest() {
		Assertions.assertTrue(reader.isReadable(BDictionary.class, null, null, null));
		Assertions.assertTrue(reader.isReadable(Object.class, null, null, null));
	}
	
	@Test
	public void readFromTest() throws WebApplicationException, IOException {
		ByteBuffer buffer = ByteBuffer.allocate(12);
		buffer.put("d5:filesi20e".getBytes());
		
		ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array());
		
		BDictionary dict = reader.readFrom(BDictionary.class, null, null, null, null, bais);
		
		Assertions.assertEquals(1, dict.size());
		Assertions.assertEquals(BInteger.class, dict.get("files").getClass());
		Assertions.assertEquals(new BInteger(20), dict.get("files"));
	}
	
	@Test
	public void readFromExceptionTest() throws WebApplicationException, IOException {		
		ByteBuffer buffer = ByteBuffer.allocate(1);
		buffer.put("e".getBytes());
		
		ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array());
		
		Assertions.assertThrows(WebApplicationException.class, () -> {
			reader.readFrom(BDictionary.class, null, null, null, null, bais);
		});
		
	}
}
