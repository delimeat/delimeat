package io.delimeat.util.bencode;

import org.junit.Assert;
import org.junit.Test;

public class BIntegerTest {

	@Test
	public void NullConstructorTest() {
		BInteger b_integer_one = new BInteger();
		Assert.assertEquals(0, b_integer_one.getValue());
	}

	@Test
	public void LongConstructorTest() {
		BInteger b_integer_one = new BInteger(1234567890);
		Assert.assertEquals(1234567890, b_integer_one.getValue());
	}

	@Test
	public void LongSetTest() {
		BInteger b_integer_one = new BInteger();
		b_integer_one.setValue(1234567890);
		Assert.assertEquals(1234567890, b_integer_one.getValue());
	}

	@Test
	public void BooleanTrueTest() {
		BInteger b_integer_one = new BInteger();
		b_integer_one.setValue(1);
		Assert.assertEquals(BInteger.BOOLEAN_TRUE, b_integer_one.getValue());
	}

	@Test
	public void BooleanFalseTest() {
		BInteger integer = new BInteger();
		integer.setValue(0);
		Assert.assertEquals(BInteger.BOOLEAN_FALSE, integer.getValue());
	}

	@Test
	public void NullBIntegerComparableTest() {
		BInteger b_integer_one = new BInteger();
		BInteger b_integer_two = new BInteger(1234567890);
		Assert.assertEquals(true, b_integer_two.compareTo(b_integer_one) != 0);
	}

	@Test
	public void SameBIntegerComparableTest() {
		BInteger b_integer_one = new BInteger(1234567890);
		BInteger b_integer_two = new BInteger(1234567890);
		Assert.assertEquals(0, b_integer_one.compareTo(b_integer_two));
	}

	@Test
	public void SmallerBIntegerComparableTest() {
		BInteger b_integer_one = new BInteger(987654321);
		BInteger b_integer_two = new BInteger(1234567890);
		Assert.assertEquals(true, b_integer_one.compareTo(b_integer_two) < 0);
	}

	@Test
	public void LargerBIntegerComparableTest() {
		BInteger b_integer_one = new BInteger(1234567890);
		BInteger b_integer_two = new BInteger(987654321);
		Assert.assertEquals(true, b_integer_one.compareTo(b_integer_two) > 0);
	}

	@Test
	public void NullComparableTest() {
		BInteger b_integer_one = new BInteger(987654321);
		Assert.assertEquals(true, b_integer_one.compareTo(null) != 0);
	}

	@Test
	public void NullBIntegerEqualsTest() {
		BInteger b_integer_one = new BInteger();
		BInteger b_integer_two = new BInteger(1234567890);
		Assert.assertEquals(false, b_integer_two.equals(b_integer_one));
	}

	@Test
	public void EqualBIntegerEqualsTest() {
		BInteger b_integer_one = new BInteger(1234567890);
		BInteger b_integer_two = new BInteger(1234567890);
		Assert.assertEquals(true, b_integer_one.equals(b_integer_two));
	}

	@Test
	public void NotEqualBIntegerEqualsTest() {
		BInteger b_integer_one = new BInteger(987654321);
		BInteger b_integer_two = new BInteger(1234567890);
		Assert.assertEquals(false, b_integer_one.equals(b_integer_two));
	}

	@Test
	public void EqualIntegerEqualsTest() {
		Integer integer_one = new Integer(1234567890);
		BInteger b_integer_one = new BInteger(1234567890);
		Assert.assertEquals(true, b_integer_one.equals(integer_one));
	}

	@Test
	public void NotEqualIntegerEqualsTest() {
		Integer integer_one = new Integer(987654321);
		BInteger b_integer_one = new BInteger(1234567890);
		Assert.assertEquals(false, b_integer_one.equals(integer_one));
	}

	@Test
	public void EqualLongEqualsTest() {
		Long long_one = new Long(1234567890L);
		BInteger b_integer_one = new BInteger(1234567890);
		Assert.assertEquals(true, b_integer_one.equals(long_one));
	}

	@Test
	public void NotEqualLongEqualsTest() {
		Long long_one = new Long(987654321L);
		BInteger b_integer_one = new BInteger(1234567890);
		Assert.assertEquals(false, b_integer_one.equals(long_one));
	}
}
