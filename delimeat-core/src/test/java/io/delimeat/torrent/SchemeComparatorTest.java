package io.delimeat.torrent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SchemeComparatorTest {

	private SchemeComparator comparator;

	@BeforeEach
	public void setUp() {
		comparator = new SchemeComparator();
	}

	@Test
	public void determineSchemeTest() {
		Assertions.assertEquals("udp", comparator.determineScheme("udp:tracker"));
	}

	@Test
	public void determineSchemeNoColonTest() {
		Assertions.assertNull(comparator.determineScheme("udptracker"));
	}

	@Test
	public void determineSchemeNoSchemeTest() {
		Assertions.assertNull(comparator.determineScheme(":tracker"));
	}

	@Test
	public void compareUdpUdpTest() {
		Assertions.assertEquals(0, comparator.compare("udp:tracker", "udp:tracker"));
	}

	@Test
	public void compareUdpHttpsTest() {
		Assertions.assertEquals(-1, comparator.compare("udp:tracker", "https:tracker"));
	}

	@Test
	public void compareUdpHttpTest() {
		Assertions.assertEquals(-1, comparator.compare("udp:tracker", "http:tracker"));
	}

	@Test
	public void compareUdpMagnetTest() {
		Assertions.assertEquals(-1, comparator.compare("udp:tracker", "magnet:tracker"));
	}

	@Test
	public void compareUdpNullTest() {
		Assertions.assertEquals(-1, comparator.compare("udp:tracker", ":tracker"));
	}

	@Test
	public void compareHttpsUdpTest() {
		Assertions.assertEquals(1, comparator.compare("https:tracker", "udp:tracker"));
	}

	@Test
	public void compareHttpsHttpsTest() {
		Assertions.assertEquals(0, comparator.compare("https:tracker", "https:tracker"));
	}

	@Test
	public void compareHttpsHttpTest() {
		Assertions.assertEquals(-1, comparator.compare("https:tracker", "http:tracker"));
	}

	@Test
	public void compareHttpsMagnetTest() {
		Assertions.assertEquals(-1, comparator.compare("https:tracker", "magnet:tracker"));
	}

	@Test
	public void compareHttpsNullTest() {
		Assertions.assertEquals(-1, comparator.compare("https:tracker", ":tracker"));
	}

	@Test
	public void compareHttpUdpTest() {
		Assertions.assertEquals(1, comparator.compare("http:tracker", "udp:tracker"));
	}

	@Test
	public void compareHttpHttpsTest() {
		Assertions.assertEquals(1, comparator.compare("http:tracker", "https:tracker"));
	}

	@Test
	public void compareHttpHttpTest() {
		Assertions.assertEquals(0, comparator.compare("http:tracker", "http:tracker"));
	}

	@Test
	public void compareHttpMagnetTest() {
		Assertions.assertEquals(-1, comparator.compare("http:tracker", "magnet:tracker"));
	}

	@Test
	public void compareHttpNullTest() {
		Assertions.assertEquals(-1, comparator.compare("http:tracker", ":tracker"));
	}

	@Test
	public void compareMagnetUdpTest() {
		Assertions.assertEquals(1, comparator.compare("magnet:tracker", "udp:tracker"));
	}

	@Test
	public void compareMagnetHttpsTest() {
		Assertions.assertEquals(1, comparator.compare("magnet:tracker", "https:tracker"));
	}

	@Test
	public void compareMagnetHttpTest() {
		Assertions.assertEquals(1, comparator.compare("magnet:tracker", "http:tracker"));
	}

	@Test
	public void compareMagnetMagnetTest() {
		Assertions.assertEquals(0, comparator.compare("magnet:tracker", "magnet:tracker"));
	}

	@Test
	public void compareMagnetNullTest() {
		Assertions.assertEquals(-1, comparator.compare("magnet:tracker", ":tracker"));
	}

	@Test
	public void compareNullUdpTest() {
		Assertions.assertEquals(1, comparator.compare(null, "udp:tracker"));
	}

	@Test
	public void compareNullHttpsTest() {
		Assertions.assertEquals(1, comparator.compare(null, "https:tracker"));
	}

	@Test
	public void compareNullHttpTest() {
		Assertions.assertEquals(1, comparator.compare(null, "http:tracker"));
	}

	@Test
	public void compareNullMagnetTest() {
		Assertions.assertEquals(1, comparator.compare(null, "magnet:tracker"));
	}

	@Test
	public void compareNullNullTest() {
		Assertions.assertEquals(0, comparator.compare(null, ":tracker"));
	}

}
