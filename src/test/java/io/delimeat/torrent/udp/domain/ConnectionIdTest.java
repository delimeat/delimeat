package io.delimeat.torrent.udp.domain;

import java.net.InetSocketAddress;
import java.time.Instant;

import org.junit.Assert;
import org.junit.Test;

public class ConnectionIdTest {

	@Test
	public void constructorTest() throws Exception {
		ConnectionId id = new ConnectionId(Long.MAX_VALUE, new InetSocketAddress("host", 80), Instant.EPOCH);

		Assert.assertEquals(Long.MAX_VALUE, id.getValue());
		Assert.assertEquals(new InetSocketAddress("host", 80), id.getFromAddress());
		Assert.assertEquals(Instant.EPOCH, id.getExpiry());
	}

	@Test
	public void hashCodeTest() {
		ConnectionId id = new ConnectionId(Long.MAX_VALUE, new InetSocketAddress("host", 80), Instant.EPOCH);

		Assert.assertEquals(-2047984281, id.hashCode());
	}

	@Test
	public void equalsSelfTest() {
		ConnectionId id = new ConnectionId(Long.MAX_VALUE, new InetSocketAddress("host", 80), Instant.EPOCH);

		Assert.assertTrue(id.equals(id));
	}

	@Test
	public void equalsNullTest() {
		ConnectionId id = new ConnectionId(Long.MAX_VALUE, new InetSocketAddress("host", 80), Instant.EPOCH);

		Assert.assertFalse(id.equals(null));
	}

	@Test
	public void equalsObjectTest() {
		ConnectionId id = new ConnectionId(Long.MAX_VALUE, new InetSocketAddress("host", 80), Instant.EPOCH);

		Assert.assertFalse(id.equals(new Object()));
	}

	@Test
	public void equalsTest() {
		ConnectionId id = new ConnectionId(Long.MAX_VALUE, new InetSocketAddress("host", 80), Instant.EPOCH);
		ConnectionId other = new ConnectionId(Long.MAX_VALUE, new InetSocketAddress("host", 80), Instant.EPOCH);

		Assert.assertTrue(id.equals(other));
	}

	@Test
	public void equalsValueTest() {
		ConnectionId id = new ConnectionId(Long.MAX_VALUE, new InetSocketAddress("host", 80), Instant.EPOCH);
		ConnectionId other = new ConnectionId(Long.MIN_VALUE, new InetSocketAddress("host", 80), Instant.EPOCH);

		Assert.assertFalse(id.equals(other));
	}

	@Test
	public void equalsExpiryTest() {
		ConnectionId id = new ConnectionId(Long.MAX_VALUE, new InetSocketAddress("host", 80), Instant.EPOCH);
		ConnectionId other = new ConnectionId(Long.MAX_VALUE, new InetSocketAddress("host", 80), Instant.MIN);

		Assert.assertFalse(id.equals(other));
	}
	
	@Test
	public void equalsExpiryNullTest() {
		ConnectionId id = new ConnectionId(Long.MAX_VALUE, new InetSocketAddress("host", 80), null);
		ConnectionId other = new ConnectionId(Long.MAX_VALUE, new InetSocketAddress("host", 80), Instant.MIN);

		Assert.assertFalse(id.equals(other));
	}

	@Test
	public void equalsFromAddressTest() {
		ConnectionId id = new ConnectionId(Long.MAX_VALUE, new InetSocketAddress("host", 80), Instant.EPOCH);
		ConnectionId other = new ConnectionId(Long.MAX_VALUE, new InetSocketAddress("test", 80), Instant.EPOCH);

		Assert.assertFalse(id.equals(other));
	}
	
	@Test
	public void equalsFromAddressNullTest() {
		ConnectionId id = new ConnectionId(Long.MAX_VALUE, null, Instant.EPOCH);
		ConnectionId other = new ConnectionId(Long.MAX_VALUE, new InetSocketAddress("test", 80), Instant.EPOCH);

		Assert.assertFalse(id.equals(other));
	}

	@Test
	public void toStringTest() {
		ConnectionId id = new ConnectionId(Long.MAX_VALUE, new InetSocketAddress("host", 80), Instant.EPOCH);

		Assert.assertEquals(
				"ConnectionId [value=9223372036854775807, fromAddress=host:80, expiry=1970-01-01T00:00:00Z]",
				id.toString());
	}
}
