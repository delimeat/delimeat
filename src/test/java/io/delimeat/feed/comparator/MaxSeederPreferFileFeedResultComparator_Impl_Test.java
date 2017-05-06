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
package io.delimeat.feed.comparator;

import io.delimeat.feed.comparator.MaxSeederPreferFileFeedResultComparator_Impl;
import io.delimeat.feed.domain.FeedResult;
import io.delimeat.torrent.domain.Torrent;
import io.delimeat.torrent.domain.TorrentFile;
import io.delimeat.torrent.domain.TorrentInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MaxSeederPreferFileFeedResultComparator_Impl_Test {

	private MaxSeederPreferFileFeedResultComparator_Impl comparator;
	private List<FeedResult> results;
	
	@Before
	public void setUp(){
		comparator = new MaxSeederPreferFileFeedResultComparator_Impl();
		results = new ArrayList<FeedResult>();
	}
	
	@Test
	public void selectSortedTest() throws Exception{
		FeedResult result1 = new FeedResult();
		result1.setSeeders(100);
		Torrent torrent1 = new Torrent();
		TorrentInfo info1 = new TorrentInfo();
		torrent1.setInfo(info1);
		result1.setTorrent(torrent1);
		results.add(result1);
		
		FeedResult result2 = new FeedResult();
		result2.setSeeders(20);
		Torrent torrent2 = new Torrent();
		TorrentInfo info2 = new TorrentInfo();
		torrent2.setInfo(info2);
		result2.setTorrent(torrent2);
		results.add(result2);

		
		FeedResult result3 = new FeedResult();
		result3.setSeeders(200);
		Torrent torrent3 = new Torrent();
		TorrentInfo info3 = new TorrentInfo();
		TorrentFile file1 = new TorrentFile();
		info3.getFiles().add(file1);
		TorrentFile file2 = new TorrentFile();
		info3.getFiles().add(file2);
		torrent3.setInfo(info3);
		result3.setTorrent(torrent3);
		results.add(result3);
		
		Collections.sort(results, comparator);
		
		Assert.assertEquals("0",result1, results.get(0));
		Assert.assertEquals("1",result2, results.get(1));
		Assert.assertEquals("2",result3, results.get(2));
	}
}
