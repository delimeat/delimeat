package io.delimeat.core.torrent;

import io.delimeat.util.UrlHandler;

import java.io.ByteArrayInputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;

public class HttpScraper_ImplTest {

	private HttpScrapeRequestHandler_Impl scraper;
	
	@Before
	public void createScraper(){
		scraper = new HttpScrapeRequestHandler_Impl();
	}
	
	@Test
	public void UrlHandlerTest(){
		Assert.assertNull(scraper.getUrlHandler());
		UrlHandler mockedHandler= Mockito.mock(UrlHandler.class);
		scraper.setUrlHandler(mockedHandler);
		Assert.assertEquals(mockedHandler, scraper.getUrlHandler());
	}
	
	@Test(expected=UnhandledScrapeException.class)
	public void invalidGenerateScrapeURITest() throws Exception{
		URI announceURI = new URI("http://test.com/a");
		byte[] infoHash = "INFO_HASH".getBytes();
		scraper.generateScrapeURL(announceURI, infoHash);
	}
	
	@Test
	public void validGenerateScrapeURIAnnounceTest() throws Exception{
		URI announceURI = new URI("http://test/announce");
		byte[] infoHash = "INFO_HASH".getBytes();
		URL scrapeURL = scraper.generateScrapeURL(announceURI, infoHash);
		Assert.assertEquals("http://test/scrape?info_hash=INFO_HASH", scrapeURL.toString());
	}
	
	@Test
	public void validGenerateScrapeURITest() throws Exception{
		URI announceURI = new URI("http://test/scrape");
		byte[] infoHash = "INFO_HASH".getBytes();
		URL scrapeURL = scraper.generateScrapeURL(announceURI, infoHash);
		Assert.assertEquals("http://test/scrape?info_hash=INFO_HASH", scrapeURL.toString());
	}
	
	@Test
	public void validGenerateScrapeURIIncludesInfoHashTest() throws Exception{
		URI announceURI = new URI("http://test/scrape?info_hash=INFO_HASH");
		byte[] infoHash = "INFO_HASH".getBytes();
		URL scrapeURL = scraper.generateScrapeURL(announceURI, infoHash);
		Assert.assertEquals("http://test/scrape?info_hash=INFO_HASH", scrapeURL.toString());
	}
	
	@Test
	public void validGenerateScrapeURIIncludesQueryTest() throws Exception{
		URI announceURI = new URI("http://test/scrape?test=true");
		byte[] infoHash = "INFO_HASH".getBytes();
		URL scrapeURL = scraper.generateScrapeURL(announceURI, infoHash);
		Assert.assertEquals("http://test/scrape?test=true&info_hash=INFO_HASH", scrapeURL.toString());
	}
	
	@Test
	public void scrapeTest() throws URISyntaxException, Exception{
		UrlHandler mockedHandler= Mockito.mock(UrlHandler.class);
		String scrapeResult = "d5:filesd20:....................d8:completei5e10:downloadedi50e10:incompletei10eeee";
      HttpURLConnection mockedConnection = Mockito.mock(HttpURLConnection.class);
      Mockito.when(mockedConnection.getResponseCode()).thenReturn(200);
      Mockito.when(mockedConnection.getInputStream()).thenReturn(new ByteArrayInputStream(scrapeResult.getBytes()));
		Mockito.when(mockedHandler.openUrlConnection(Mockito.any(URL.class))).thenReturn(mockedConnection);
		
		scraper.setUrlHandler(mockedHandler);
		ScrapeResult result = scraper.scrape(new URI("http://test/announce?test=true"), "....................".getBytes());
		
		Assert.assertEquals(5, result.getSeeders());
		Assert.assertEquals(10, result.getLeechers());
	}
  
	@Test(expected=TorrentException.class)
	public void scrapeNotHTTPTest() throws URISyntaxException, Exception{
		scraper.scrape(new URI("udp://test.com"), "....................".getBytes());
	}
  
	@Test(expected=TorrentException.class)
	public void scrapeNotOKTest() throws Exception{
		UrlHandler mockedHandler= Mockito.mock(UrlHandler.class);
      HttpURLConnection mockedConnection = Mockito.mock(HttpURLConnection.class);
      Mockito.when(mockedConnection.getResponseCode()).thenReturn(204);
		Mockito.when(mockedHandler.openUrlConnection(Mockito.any(URL.class))).thenReturn(mockedConnection);
		
		scraper.setUrlHandler(mockedHandler);
		scraper.scrape(new URI("http://test/announce?test=true"), "....................".getBytes());
	}
}
