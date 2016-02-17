package io.delimeat.core.torrent;

import io.delimeat.util.UrlHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;

public class HttpScrapeRequestHandler_ImplTest {

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
		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		scraper.generateScrapeURL(announceURI, infoHash);
	}
	
	@Test
	public void validGenerateScrapeURIAnnounceTest() throws Exception{
		URI announceURI = new URI("http://test/announce");
		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
     	URL scrapeURL = scraper.generateScrapeURL(announceURI, infoHash);
		Assert.assertEquals("http://test/scrape?info_hash=%60%14%3F%3FT%3FT%0E%3F%12%3F5%3F%3F%3F%3F%3F%3F%3F", scrapeURL.toString());
	}
	
	@Test
	public void validGenerateScrapeURITest() throws Exception{
		URI announceURI = new URI("http://test/scrape");
		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		URL scrapeURL = scraper.generateScrapeURL(announceURI, infoHash);
		Assert.assertEquals("http://test/scrape?info_hash=%60%14%3F%3FT%3FT%0E%3F%12%3F5%3F%3F%3F%3F%3F%3F%3F", scrapeURL.toString());
	}
	
	@Test
	public void validGenerateScrapeURIIncludesInfoHashTest() throws Exception{
		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
    	URI announceURI = new URI("http://test/scrape?info_hash=%60%14%3F%3FT%3FT%0E%3F%12%3F5%3F%3F%3F%3F%3F%3F%3F");
		URL scrapeURL = scraper.generateScrapeURL(announceURI, infoHash);
		Assert.assertEquals("http://test/scrape?info_hash=%60%14%3F%3FT%3FT%0E%3F%12%3F5%3F%3F%3F%3F%3F%3F%3F", scrapeURL.toString());
	}
	
	@Test
	public void validGenerateScrapeURIIncludesQueryTest() throws Exception{
		URI announceURI = new URI("http://test/scrape?test=true");
		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		URL scrapeURL = scraper.generateScrapeURL(announceURI, infoHash);
		Assert.assertEquals("http://test/scrape?test=true&info_hash=%60%14%3F%3FT%3FT%0E%3F%12%3F5%3F%3F%3F%3F%3F%3F%3F", scrapeURL.toString());
	}
	
	@Test
	public void scrapeTest() throws URISyntaxException, Exception{
		UrlHandler mockedHandler= Mockito.mock(UrlHandler.class);
      InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());

     	ByteArrayOutputStream baos = new ByteArrayOutputStream();
     	baos.write("d5:filesd20:".getBytes());
     	baos.write(infoHash.getBytes());
     	baos.write("d8:completei5e10:downloadedi50e10:incompletei10eeee".getBytes());
      byte[] scrapeResult = baos.toByteArray();

      HttpURLConnection mockedConnection = Mockito.mock(HttpURLConnection.class);
      Mockito.when(mockedConnection.getResponseCode()).thenReturn(200);
      Mockito.when(mockedConnection.getInputStream()).thenReturn(new ByteArrayInputStream(scrapeResult));
		Mockito.when(mockedHandler.openUrlConnection(Mockito.any(URL.class))).thenReturn(mockedConnection);
     	Mockito.when(mockedHandler.openInput(Mockito.any(URLConnection.class))).thenReturn(new ByteArrayInputStream(scrapeResult));
		
		scraper.setUrlHandler(mockedHandler);

		ScrapeResult result = scraper.scrape(new URI("http://test/announce?test=true"), infoHash);
		
		Assert.assertEquals(5, result.getSeeders());
		Assert.assertEquals(10, result.getLeechers());
	}
  
	@Test(expected=TorrentException.class)
	public void scrapeNotHTTPTest() throws URISyntaxException, Exception{
     InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		scraper.scrape(new URI("udp://test.com"), infoHash);
	}
  
	@Test(expected=TorrentException.class)
	public void scrapeNotOKTest() throws Exception{
		UrlHandler mockedHandler= Mockito.mock(UrlHandler.class);
      HttpURLConnection mockedConnection = Mockito.mock(HttpURLConnection.class);
      Mockito.when(mockedConnection.getResponseCode()).thenReturn(204);
		Mockito.when(mockedHandler.openUrlConnection(Mockito.any(URL.class))).thenReturn(mockedConnection);
		
		scraper.setUrlHandler(mockedHandler);
      InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());

		scraper.scrape(new URI("http://test/announce?test=true"), infoHash);
	}
}
