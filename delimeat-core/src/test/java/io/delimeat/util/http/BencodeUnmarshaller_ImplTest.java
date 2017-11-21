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
package io.delimeat.util.http;

import java.io.ByteArrayInputStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.delimeat.torrent.bencode.BDictionary;
import io.delimeat.torrent.bencode.BInteger;

public class BencodeUnmarshaller_ImplTest {

	private BencodeUnmarshaller_Impl unmarshaller;
	
	@BeforeEach
	public void setUp(){
		unmarshaller = new BencodeUnmarshaller_Impl();
	}
	
	@Test
	public void unmarshallTest() throws Exception{
		BDictionary dict = unmarshaller.unmarshall(new ByteArrayInputStream("d7:INTEGERi2ee".getBytes()), BDictionary.class);
		Assertions.assertEquals(new BInteger(2), dict.get("INTEGER"));
	}
}
