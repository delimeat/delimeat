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
package io.delimeat.torrent.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TorrentFileTest {

	private TorrentFile torrentFile;

	@BeforeEach
	public void setUp() {
		torrentFile = new TorrentFile();
	}

	@Test
	public void lengthTest() {
		Assertions.assertEquals(0, torrentFile.getLength());
		torrentFile.setLength(Long.MAX_VALUE);
		Assertions.assertEquals(Long.MAX_VALUE, torrentFile.getLength());
	}

	@Test
	public void nameTest() {
		Assertions.assertNull(torrentFile.getName());
		torrentFile.setName("NAME");
		Assertions.assertEquals("NAME", torrentFile.getName());
	}

	@Test
	public void toStringTest() {
		Assertions.assertEquals("TorrentFile [length=0, ]", torrentFile.toString());
	}
  	
	@Test
  	public void hashCodeTest(){
		torrentFile.setName("NAME");
		torrentFile.setLength(Long.MAX_VALUE);
     	Assertions.assertEquals(-2145094068, torrentFile.hashCode());
   }
	
	@Test
	public void equalsTest() {
		torrentFile.setLength(Long.MAX_VALUE);
		torrentFile.setName("NAME");
		TorrentFile other = new TorrentFile();
		other.setLength(Long.MAX_VALUE);
		other.setName("NAME");

		Assertions.assertTrue(torrentFile.equals(other));
	}

	@Test
	public void equalsSelfTest() {
		Assertions.assertTrue(torrentFile.equals(torrentFile));
	}

	@Test
	public void equalsNullTest() {
		Assertions.assertFalse(torrentFile.equals(null));
	}

	@Test
	public void equalsOtherClassTest() {
		Assertions.assertFalse(torrentFile.equals(new Object()));
	}
	
	@Test
	public void equalsNameNullTest() {
		torrentFile.setLength(Long.MAX_VALUE);
		torrentFile.setName(null);
		TorrentFile other = new TorrentFile();
		other.setLength(Long.MAX_VALUE);
		other.setName("NAME");

		Assertions.assertFalse(torrentFile.equals(other));
	}
	
	@Test
	public void equalsLengthTest() {
		torrentFile.setLength(Long.MAX_VALUE);
		torrentFile.setName("NAME");
		TorrentFile other = new TorrentFile();
		other.setLength(Long.MIN_VALUE);
		other.setName("NAME");

		Assertions.assertFalse(torrentFile.equals(other));
	}
	
	@Test
	public void equalsNameTest() {
		torrentFile.setLength(Long.MAX_VALUE);
		torrentFile.setName("NAME_OTHER");
		TorrentFile other = new TorrentFile();
		other.setLength(Long.MAX_VALUE);
		other.setName("NAME");

		Assertions.assertFalse(torrentFile.equals(other));
	}
	
}
