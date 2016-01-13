package io.delimeat.core.torrent;

import io.delimeat.util.UrlHandler;
import io.delimeat.util.bencode.BDictionary;
import io.delimeat.util.bencode.BList;
import io.delimeat.util.bencode.BString;
import io.delimeat.util.bencode.BencodeException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TorrentDao_Impl_Test {

	private String sep = System.getProperty("file.separator");
	private TorrentDao_Impl dao;

	@Before
	public void setUp() {
		dao = new TorrentDao_Impl();
	}

	@Test
	public void UrlHandlerTest() {
		UrlHandler mockedUrlHandler = Mockito.mock(UrlHandler.class);
		Assert.assertNull(dao.getUrlHandler());
		dao.setUrlHandler(mockedUrlHandler);
		Assert.assertEquals(mockedUrlHandler, dao.getUrlHandler());
	}

	@Test
	public void ScaperTestTest() {
		ScrapeRequestHandler mockedScraper1 = Mockito
				.mock(ScrapeRequestHandler.class);
		ScrapeRequestHandler mockedScraper2 = Mockito
				.mock(ScrapeRequestHandler.class);
		Assert.assertEquals(0, dao.getScrapeReqeustHandlers().size());
		dao.getScrapeReqeustHandlers().put("HTTP", mockedScraper1);
		dao.getScrapeReqeustHandlers().put("UDP", mockedScraper2);
		Assert.assertEquals(2, dao.getScrapeReqeustHandlers().size());
		Assert.assertEquals(mockedScraper1,
				dao.getScrapeReqeustHandlers().get("HTTP"));
		Assert.assertEquals(mockedScraper2,
				dao.getScrapeReqeustHandlers().get("UDP"));
	}

	@Test
	public void parseTorrentFileTest() {
		BDictionary fileDict = new BDictionary();
		fileDict.put("length", 1234567890L);
		BList pathList = new BList();
		pathList.add("part1");
		pathList.add("part2");
		pathList.add("part3");
		pathList.add("fileName");
		fileDict.put("path", pathList);

		TorrentFile file = dao.parseTorrentFile(fileDict);
		Assert.assertEquals(1234567890L, file.getLength());
		Assert.assertEquals("part1" + sep + "part2" + sep + "part3" + sep
				+ "fileName", file.getName());
	}

	@Test
	public void parseInfoDictionaryTest() throws IOException, BencodeException,
			NoSuchAlgorithmException {
		BDictionary infoDict = new BDictionary();
		infoDict.put("name", "NAME");
		infoDict.put("length", 987654321L);
		BList filesList = new BList();
		BDictionary fileDict1 = new BDictionary();
		BList pathList1 = new BList();
		pathList1.add("1_part_1");
		pathList1.add("1_file_name");
		fileDict1.put("length", 1234);
		fileDict1.put("path", pathList1);
		filesList.add(fileDict1);
		BDictionary fileDict2 = new BDictionary();
		BList pathList2 = new BList();
		pathList2.add("2_part_1");
		pathList2.add("2_file_name");
		fileDict2.put("length", 56789);
		fileDict2.put("path", pathList2);
		filesList.add(fileDict2);
		infoDict.put("files", filesList);
		TorrentInfo info = dao.parseInfoDictionary(infoDict);
		Assert.assertEquals(987654321L, info.getLength());
		Assert.assertEquals("NAME", info.getName());
		Assert.assertEquals(2, info.getFiles().size());
		Assert.assertEquals("1_part_1" + sep + "1_file_name", info.getFiles()
				.get(0).getName());
		Assert.assertEquals(1234, info.getFiles().get(0).getLength());
		Assert.assertEquals("2_part_1" + sep + "2_file_name", info.getFiles()
				.get(1).getName());
		Assert.assertEquals(56789, info.getFiles().get(1).getLength());
		Assert.assertEquals("��^�&�Mm��x�Q�(�",
				new String(info.getInfoHash()));
	}

	@Test
	public void parseAnnounceListTest() {
		BList announceList = new BList();
		BList tier1 = new BList();
		tier1.add(new BString("1_tracker_1"));
		tier1.add(new BString("1_tracker_2"));
		announceList.add(tier1);
		BList tier2 = new BList();
		tier2.add(new BString("2_tracker_1"));
		tier2.add(new BString("2_tracker_2"));
		announceList.add(tier2);

		List<String> trackers = dao.parseAnnounceList(announceList);
		Assert.assertEquals(4, trackers.size());
		Assert.assertEquals("1_tracker_1", trackers.get(0));
		Assert.assertEquals("1_tracker_2", trackers.get(1));
		Assert.assertEquals("2_tracker_1", trackers.get(2));
		Assert.assertEquals("2_tracker_2", trackers.get(3));
	}

	@Test
	public void parseRootDictionaryTest() throws IOException, BencodeException,
			NoSuchAlgorithmException {
		BDictionary root = new BDictionary();
		root.put("announce", "TRACKER_1");

		BList announceList = new BList();
		BList tier1 = new BList();
		tier1.add(new BString("1_tracker_1"));
		tier1.add(new BString("1_tracker_2"));
		announceList.add(tier1);
		BList tier2 = new BList();
		tier2.add(new BString("2_tracker_1"));
		tier2.add(new BString("2_tracker_2"));
		announceList.add(tier2);
		root.put("announce-list", announceList);

		BDictionary infoDict = new BDictionary();
		infoDict.put("name", "NAME");
		infoDict.put("length", 987654321L);
		BList filesList = new BList();
		BDictionary fileDict1 = new BDictionary();
		BList pathList1 = new BList();
		pathList1.add("1_part_1");
		pathList1.add("1_file_name");
		fileDict1.put("length", 1234);
		fileDict1.put("path", pathList1);
		filesList.add(fileDict1);
		BDictionary fileDict2 = new BDictionary();
		BList pathList2 = new BList();
		pathList2.add("2_part_1");
		pathList2.add("2_file_name");
		fileDict2.put("length", 56789);
		fileDict2.put("path", pathList2);
		filesList.add(fileDict2);
		infoDict.put("files", filesList);
		root.put("info", infoDict);

		Torrent torrent = dao.parseRootDictionary(root);
		Assert.assertEquals("TRACKER_1", torrent.getTracker());
		Assert.assertEquals(4, torrent.getTrackers().size());
		Assert.assertEquals("1_tracker_1", torrent.getTrackers().get(0));
		Assert.assertEquals("1_tracker_2", torrent.getTrackers().get(1));
		Assert.assertEquals("2_tracker_1", torrent.getTrackers().get(2));
		Assert.assertEquals("2_tracker_2", torrent.getTrackers().get(3));
		Assert.assertEquals(987654321L, torrent.getInfo().getLength());
		Assert.assertEquals("NAME", torrent.getInfo().getName());
		Assert.assertEquals(2, torrent.getInfo().getFiles().size());
		Assert.assertEquals("1_part_1" + sep + "1_file_name", torrent.getInfo()
				.getFiles().get(0).getName());
		Assert.assertEquals(1234, torrent.getInfo().getFiles().get(0)
				.getLength());
		Assert.assertEquals("2_part_1" + sep + "2_file_name", torrent.getInfo()
				.getFiles().get(1).getName());
		Assert.assertEquals(56789, torrent.getInfo().getFiles().get(1)
				.getLength());
		Assert.assertEquals("��^�&�Mm��x�Q�(�", new String(torrent.getInfo()
				.getInfoHash()));
		String bytesVal = "d8:announce9:TRACKER_113:announce-listll11:1_tracker_111:1_tracker_2el11:2_tracker_111:2_tracker_2ee4:infod5:filesld6:lengthi1234e4:pathl8:1_part_111:1_file_nameeed6:lengthi56789e4:pathl8:2_part_111:2_file_nameeee6:lengthi987654321e4:name4:NAMEee";
		Assert.assertEquals(bytesVal, new String(torrent.getBytes()));
	}

	@Test
	public void readTest() throws Exception {
		String bytesVal = "d8:announce9:TRACKER_113:announce-listll11:1_tracker_111:1_tracker_2el11:2_tracker_111:2_tracker_2ee4:infod5:filesld6:lengthi1234e4:pathl8:1_part_111:1_file_nameeed6:lengthi56789e4:pathl8:2_part_111:2_file_nameeee6:lengthi987654321e4:name4:NAMEee";
		ByteArrayInputStream bais = new ByteArrayInputStream(
				bytesVal.getBytes());
		UrlHandler mockedUrlHandler = Mockito.mock(UrlHandler.class);
		Mockito.when(mockedUrlHandler.openInput(Mockito.any(URL.class),Mockito.any(Map.class)))
				.thenReturn(bais);
		dao.setUrlHandler(mockedUrlHandler);

		URI uri = new URI("http://test.com/");

		Torrent torrent = dao.read(uri);
		Assert.assertEquals("TRACKER_1", torrent.getTracker());
		Assert.assertEquals(4, torrent.getTrackers().size());
		Assert.assertEquals("1_tracker_1", torrent.getTrackers().get(0));
		Assert.assertEquals("1_tracker_2", torrent.getTrackers().get(1));
		Assert.assertEquals("2_tracker_1", torrent.getTrackers().get(2));
		Assert.assertEquals("2_tracker_2", torrent.getTrackers().get(3));
		Assert.assertEquals(987654321L, torrent.getInfo().getLength());
		Assert.assertEquals("NAME", torrent.getInfo().getName());
		Assert.assertEquals(2, torrent.getInfo().getFiles().size());
		Assert.assertEquals("1_part_1" + sep + "1_file_name", torrent.getInfo()
				.getFiles().get(0).getName());
		Assert.assertEquals(1234, torrent.getInfo().getFiles().get(0)
				.getLength());
		Assert.assertEquals("2_part_1" + sep + "2_file_name", torrent.getInfo()
				.getFiles().get(1).getName());
		Assert.assertEquals(56789, torrent.getInfo().getFiles().get(1)
				.getLength());
		Assert.assertEquals("��^�&�Mm��x�Q�(�", new String(torrent.getInfo()
				.getInfoHash()));
		Assert.assertEquals(bytesVal, new String(torrent.getBytes()));
	}

	@Test(expected = UnhandledScrapeException.class)
	public void scrapeUnhandledTest() throws Exception {
		ScrapeRequestHandler mockedScraper = Mockito
				.mock(ScrapeRequestHandler.class);
		dao.getScrapeReqeustHandlers().put("http", mockedScraper);

		URI scrapeUri = new URI("udp://scrape.me:8080");
		dao.scrape(scrapeUri, "INFO_HASH".getBytes());
	}

	@Test
	public void scrapeTest() throws Exception {
		ScrapeResult mockedResult = Mockito.mock(ScrapeResult.class);
		ScrapeRequestHandler mockedScraper = Mockito
				.mock(ScrapeRequestHandler.class);
		Mockito.when(
				mockedScraper.scrape(Mockito.any(URI.class),
						Mockito.any(byte[].class))).thenReturn(mockedResult);
		dao.getScrapeReqeustHandlers().put("HTTP", mockedScraper);

		URI uri = new URI("http://scrape.me.com");
		Assert.assertEquals(mockedResult,
				dao.scrape(uri, "INFO_HASH".getBytes()));
	}

}
