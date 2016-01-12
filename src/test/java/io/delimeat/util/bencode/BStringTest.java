package io.delimeat.util.bencode;

import org.junit.Assert;
import org.junit.Test;

public class BStringTest {

	@Test
	public void NullConstructorTest() {
		BString b_string = new BString();
		Assert.assertNull(b_string.getValue());
	}

	@Test
	public void StringConstructorTest() {
		BString b_string = new BString("string_value");
		Assert.assertEquals("string_value", b_string.toString());
	}

	@Test
	public void ByteArraySetTest() {
		BString b_string = new BString();
		b_string.setValue("string_value".getBytes());
		Assert.assertEquals("string_value", new String(b_string.getValue()));
	}

	@Test
	public void StringSetTest() {
		BString b_string = new BString();
		b_string.setValue("string_value");
		Assert.assertEquals("string_value", b_string.toString());
	}

	@Test
	public void NullBStringComparableTest() {
		BString b_string_one = new BString();
		BString b_string_two = new BString("compare");
		Assert.assertEquals(true, b_string_two.compareTo(b_string_one) != 0);
	}

	@Test
	public void SameBStringComparableTest() {
		BString b_string_one = new BString("match");
		BString b_string_two = new BString("match");
		Assert.assertEquals(true, b_string_one.compareTo(b_string_two) == 0);
	}

	@Test
	public void NotSameBStringComparableTest() {
		BString b_string_one = new BString("match");
		BString b_string_two = new BString("no match");
		Assert.assertEquals(true, b_string_one.compareTo(b_string_two) != 0);
	}

	@Test
	public void NullComparableTest() {
		BString b_string_one = new BString("match");
		Assert.assertEquals(true, b_string_one.compareTo(null) != 0);
	}

	@Test
	public void NullBStringEqualsTest() {
		BString b_string_one = new BString();
		BString b_string_two = new BString("equals");
		Assert.assertEquals(false, b_string_two.equals(b_string_one));
	}

	@Test
	public void EqualBStringEqualsTest() {
		BString b_string_one = new BString("match");
		BString b_string_two = new BString("match");
		Assert.assertEquals(true, b_string_one.equals(b_string_two));
	}

	@Test
	public void NotEqualBStringEqualsTest() {
		BString b_string_one = new BString("match");
		BString b_string_two = new BString("no match");
		Assert.assertEquals(false, b_string_one.equals(b_string_two));
	}

	@Test
	public void NullEqualsTest() {
		BString b_string_one = new BString("match");
		Assert.assertEquals(false, b_string_one.equals(null));
	}

	@Test
	public void EqualStringEqualsTest() {
		String string_one = "match";
		BString b_string_one = new BString("match");
		Assert.assertEquals(true, b_string_one.equals(string_one));
	}

	@Test
	public void NotEqaulStringEqualsTest() {
		String string_one = "no match";
		BString b_string_one = new BString("match");
		Assert.assertEquals(false, b_string_one.equals(string_one));
	}

}
