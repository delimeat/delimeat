package io.delimeat.torrent;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SchemeComparatorTest {

	private SchemeComparator comparator;
	
	@Before
	public void setUp(){
		comparator = new SchemeComparator();
	}
  	
  	@Test
  	public void determineSchemeTest(){
  		Assert.assertEquals("udp", comparator.determineScheme("udp:tracker"));
  	}
  	
  	@Test
  	public void determineSchemeNoColonTest(){
  		Assert.assertNull( comparator.determineScheme("udptracker"));
  	}
  	
  	@Test
  	public void determineSchemeNoSchemeTest(){
  		Assert.assertNull( comparator.determineScheme(":tracker"));
  	}
  	
  	@Test
  	public void compareUdpUdpTest(){
  		Assert.assertEquals(0, comparator.compare("udp:tracker", "udp:tracker"));
  	}
  	
  	@Test
  	public void compareUdpHttpsTest(){
  		Assert.assertEquals(-1, comparator.compare("udp:tracker", "https:tracker"));
  	}
  	
  	@Test
  	public void compareUdpHttpTest(){
  		Assert.assertEquals(-1, comparator.compare("udp:tracker", "http:tracker"));
  	}
  	
  	@Test
  	public void compareUdpMagnetTest(){
  		Assert.assertEquals(-1, comparator.compare("udp:tracker", "magnet:tracker"));
  	}
  	
  	@Test
  	public void compareUdpNullTest(){
  		Assert.assertEquals(-1, comparator.compare("udp:tracker", ":tracker"));
  	}
  	
  	@Test
  	public void compareHttpsUdpTest(){
  		Assert.assertEquals(1, comparator.compare("https:tracker", "udp:tracker"));
  	}
  	
  	@Test
  	public void compareHttpsHttpsTest(){
  		Assert.assertEquals(0, comparator.compare("https:tracker", "https:tracker"));
  	}
  	
  	@Test
  	public void compareHttpsHttpTest(){
  		Assert.assertEquals(-1, comparator.compare("https:tracker", "http:tracker"));
  	}
  	
  	@Test
  	public void compareHttpsMagnetTest(){
  		Assert.assertEquals(-1, comparator.compare("https:tracker", "magnet:tracker"));
  	}
  	
  	@Test
  	public void compareHttpsNullTest(){
  		Assert.assertEquals(-1, comparator.compare("https:tracker", ":tracker"));
  	}
  	
  	@Test
  	public void compareHttpUdpTest(){
  		Assert.assertEquals(1, comparator.compare("http:tracker", "udp:tracker"));
  	}
  	
  	@Test
  	public void compareHttpHttpsTest(){
  		Assert.assertEquals(1, comparator.compare("http:tracker", "https:tracker"));
  	}
  	
  	@Test
  	public void compareHttpHttpTest(){
  		Assert.assertEquals(0, comparator.compare("http:tracker", "http:tracker"));
  	}
  	
  	@Test
  	public void compareHttpMagnetTest(){
  		Assert.assertEquals(-1, comparator.compare("http:tracker", "magnet:tracker"));
  	}
  	
  	@Test
  	public void compareHttpNullTest(){
  		Assert.assertEquals(-1, comparator.compare("http:tracker", ":tracker"));
  	}
  	
  	@Test
  	public void compareMagnetUdpTest(){
  		Assert.assertEquals(1, comparator.compare("magnet:tracker", "udp:tracker"));
  	}
  	
  	@Test
  	public void compareMagnetHttpsTest(){
  		Assert.assertEquals(1, comparator.compare("magnet:tracker", "https:tracker"));
  	}
  	
  	@Test
  	public void compareMagnetHttpTest(){
  		Assert.assertEquals(1, comparator.compare("magnet:tracker", "http:tracker"));
  	}
  	
  	@Test
  	public void compareMagnetMagnetTest(){
  		Assert.assertEquals(0, comparator.compare("magnet:tracker", "magnet:tracker"));
  	}
  	
  	@Test
  	public void compareMagnetNullTest(){
  		Assert.assertEquals(-1, comparator.compare("magnet:tracker", ":tracker"));
  	}
  	
  	@Test
  	public void compareNullUdpTest(){
  		Assert.assertEquals(1, comparator.compare(null, "udp:tracker"));
  	}
  	
  	@Test
  	public void compareNullHttpsTest(){
  		Assert.assertEquals(1, comparator.compare(null, "https:tracker"));
  	}
  	
  	@Test
  	public void compareNullHttpTest(){
  		Assert.assertEquals(1, comparator.compare(null, "http:tracker"));
  	}
  	
  	@Test
  	public void compareNullMagnetTest(){
  		Assert.assertEquals(1, comparator.compare(null, "magnet:tracker"));
  	}
  	
  	@Test
  	public void compareNullNullTest(){
  		Assert.assertEquals(0, comparator.compare(null, ":tracker"));
  	}
  	
}
