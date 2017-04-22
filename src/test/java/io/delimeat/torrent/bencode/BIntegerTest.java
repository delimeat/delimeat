package io.delimeat.torrent.bencode;

import org.junit.Assert;
import org.junit.Test;

import io.delimeat.torrent.bencode.BInteger;

public class BIntegerTest {

	@Test
	public void LongConstructorTest() {
		BInteger b_integer_one = new BInteger(1234567890);
		Assert.assertEquals(1234567890, b_integer_one.longValue());
	}

}
