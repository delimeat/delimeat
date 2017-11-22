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

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TorrentTest {

	private Torrent torrent;

	@BeforeEach
	public void setUp() {
		torrent = new Torrent();
	}

	@Test
	public void trackerTest() {
		Assertions.assertNull(torrent.getTracker());
		torrent.setTracker("TRACKER");
		Assertions.assertEquals("TRACKER", torrent.getTracker());
	}

	@Test
	public void trackersTest() {
		Assertions.assertEquals(0, torrent.getTrackers().size());

		torrent.setTrackers(Arrays.asList("TRACKER_1", "TRACKER_2"));

		Assertions.assertEquals(2, torrent.getTrackers().size());
		Assertions.assertEquals("TRACKER_1", torrent.getTrackers().get(0));
		Assertions.assertEquals("TRACKER_2", torrent.getTrackers().get(1));
	}

	@Test
	public void infoTest() {
		Assertions.assertNull(torrent.getInfo());

		TorrentInfo info = new TorrentInfo();
		torrent.setInfo(info);

		Assertions.assertEquals(info, torrent.getInfo());
	}

	@Test
	public void bytesTest() {
		Assertions.assertNull(torrent.getBytes());
		torrent.setBytes("BYTES".getBytes());
		Assertions.assertEquals("BYTES", new String(torrent.getBytes()));
	}

	@Test
	public void toStringTest() {
		Assertions.assertEquals("Torrent [trackers=[], ]", torrent.toString());
	}

	@Test
	public void hashCodeTest() {
		torrent.setTracker("TRACKER");
		torrent.setTrackers(Arrays.asList("TRACKER_1", "TRACKER_2"));
		torrent.setInfo(new TorrentInfo());
		torrent.setBytes("BYTES".getBytes());
		Assertions.assertEquals(-453204695, torrent.hashCode());
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

		Assertions.assertTrue(torrent.equals(other));
	}

	@Test
	public void equalsSelfTest() {
		Assertions.assertTrue(torrent.equals(torrent));
	}

	@Test
	public void equalsNullTest() {
		Assertions.assertFalse(torrent.equals(null));
	}

	@Test
	public void equalsOtherClassTest() {
		Assertions.assertFalse(torrent.equals(new Object()));
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

		Assertions.assertFalse(torrent.equals(other));
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

		Assertions.assertFalse(torrent.equals(other));
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

		Assertions.assertFalse(torrent.equals(other));
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

		Assertions.assertFalse(torrent.equals(other));
	}

	@Test
	public void equalsTrackerTest() {
		torrent.setTracker("OTHER_TRACKER");
		torrent.setTrackers(Arrays.asList("TRACKER_1", "TRACKER_2"));
		torrent.setInfo(new TorrentInfo());
		Torrent other = new Torrent();
		other.setTracker("TRACKER");
		other.setTrackers(Arrays.asList("TRACKER_1", "TRACKER_2"));
		other.setInfo(new TorrentInfo());

		Assertions.assertFalse(torrent.equals(other));
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

		Assertions.assertFalse(torrent.equals(other));
	}

}
