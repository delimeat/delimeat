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

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TorrentTest {

	private Torrent torrent;

	@Before
	public void setUp() {
		torrent = new Torrent();
	}

	@Test
	public void trackerTest() {
		Assert.assertNull(torrent.getTracker());
		torrent.setTracker("TRACKER");
		Assert.assertEquals("TRACKER", torrent.getTracker());
	}

	@Test
	public void trackersTest() {
		Assert.assertEquals(0, torrent.getTrackers().size());

		torrent.setTrackers(Arrays.asList("TRACKER_1", "TRACKER_2"));

		Assert.assertEquals(2, torrent.getTrackers().size());
		Assert.assertEquals("TRACKER_1", torrent.getTrackers().get(0));
		Assert.assertEquals("TRACKER_2", torrent.getTrackers().get(1));
	}

	@Test
	public void infoTest() {
		Assert.assertNull(torrent.getInfo());

		TorrentInfo info = new TorrentInfo();
		torrent.setInfo(info);

		Assert.assertEquals(info, torrent.getInfo());
	}

	@Test
	public void bytesTest() {
		Assert.assertNull(torrent.getBytes());
		torrent.setBytes("BYTES".getBytes());
		Assert.assertEquals("BYTES", new String(torrent.getBytes()));
	}

	@Test
	public void toStringTest() {
		Assert.assertEquals("Torrent [trackers=[], ]", torrent.toString());
	}

	@Test
	public void hashCodeTest() {
		torrent.setTracker("TRACKER");
		torrent.setTrackers(Arrays.asList("TRACKER_1", "TRACKER_2"));
		torrent.setInfo(new TorrentInfo());
		torrent.setBytes("BYTES".getBytes());
		Assert.assertEquals(-453204695, torrent.hashCode());
	}
	
	@Test
	public void equalsTest() {
		torrent.setTracker("TRACKER");
		torrent.setTrackers(Arrays.asList("TRACKER_1", "TRACKER_2"));
		torrent.setInfo(new TorrentInfo());
		Torrent other = new Torrent();
		other.setTracker("TRACKER");
		other.setTrackers(Arrays.asList("TRACKER_1", "TRACKER_2"));
		other.setInfo(new TorrentInfo());

		Assert.assertTrue(torrent.equals(other));
	}

	@Test
	public void equalsSelfTest() {
		Assert.assertTrue(torrent.equals(torrent));
	}

	@Test
	public void equalsNullTest() {
		Assert.assertFalse(torrent.equals(null));
	}

	@Test
	public void equalsOtherClassTest() {
		Assert.assertFalse(torrent.equals("STRING"));
	}
	
	@Test
	public void equalsInfoNullTest() {
		torrent.setTracker("TRACKER");
		torrent.setTrackers(Arrays.asList("TRACKER_1", "TRACKER_2"));
		torrent.setInfo(null);
		Torrent other = new Torrent();
		other.setTracker("TRACKER");
		other.setTrackers(Arrays.asList("TRACKER_1", "TRACKER_2"));
		other.setInfo(new TorrentInfo());

		Assert.assertFalse(torrent.equals(other));
	}
	
	@Test
	public void equalsTrackerNullTest() {
		torrent.setTracker(null);
		torrent.setTrackers(Arrays.asList("TRACKER_1", "TRACKER_2"));
		torrent.setInfo(new TorrentInfo());
		Torrent other = new Torrent();
		other.setTracker("TRACKER");
		other.setTrackers(Arrays.asList("TRACKER_1", "TRACKER_2"));
		other.setInfo(new TorrentInfo());

		Assert.assertFalse(torrent.equals(other));
	}
	
	@Test
	public void equalsTrackersNullTest() {
		torrent.setTracker("TRACKER");
		torrent.setTrackers(null);
		torrent.setInfo(new TorrentInfo());
		Torrent other = new Torrent();
		other.setTracker("TRACKER");
		other.setTrackers(Arrays.asList("TRACKER_1", "TRACKER_2"));
		other.setInfo(new TorrentInfo());

		Assert.assertFalse(torrent.equals(other));
	}
	
	@Test
	public void equalsInfoTest() {
		torrent.setTracker("TRACKER");
		torrent.setTrackers(Arrays.asList("TRACKER_1", "TRACKER_2"));
		TorrentInfo info = new TorrentInfo();
		info.setInfoHash(new InfoHash("INFO".getBytes()));
		torrent.setInfo(info);
		Torrent other = new Torrent();
		other.setTracker("TRACKER");
		other.setTrackers(Arrays.asList("TRACKER_1", "TRACKER_2"));
		other.setInfo(new TorrentInfo());

		Assert.assertFalse(torrent.equals(other));
	}
	
	@Test
	public void equalsTrackerTest() {
		torrent.setTracker(null);
		torrent.setTrackers(Arrays.asList("TRACKER_1", "TRACKER_2"));
		torrent.setInfo(new TorrentInfo());
		Torrent other = new Torrent();
		other.setTracker("TRACKER");
		other.setTrackers(Arrays.asList("TRACKER_1", "TRACKER_2"));
		other.setInfo(new TorrentInfo());

		Assert.assertFalse(torrent.equals(other));
	}
	
	@Test
	public void equalsTrackersTest() {
		torrent.setTracker("TRACKER");
		torrent.setTrackers(Arrays.asList("TRACKER_1"));
		torrent.setInfo(new TorrentInfo());
		Torrent other = new Torrent();
		other.setTracker("TRACKER");
		other.setTrackers(Arrays.asList("TRACKER_1", "TRACKER_2"));
		other.setInfo(new TorrentInfo());

		Assert.assertFalse(torrent.equals(other));
	}
}
