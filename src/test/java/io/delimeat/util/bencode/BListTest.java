package io.delimeat.util.bencode;

import org.junit.Assert;
import org.junit.Test;

public class BListTest {

	@Test
	public void NullConstructorTest() {
		BList b_list_one = new BList();
		Assert.assertEquals(0, b_list_one.size());
	}

	@Test
	public void BValueAddTest() {
		BList b_list_one = new BList();
		BString b_string = new BString();
		b_list_one.add(b_string);
		Assert.assertEquals(1, b_list_one.size());
		Assert.assertEquals(b_string, b_list_one.get(0));
	}

	@Test
	public void LongAddTest() {
		BList b_list_one = new BList();
		b_list_one.add(1234567890);
		Assert.assertEquals(1, b_list_one.size());
		Assert.assertEquals(true, b_list_one.get(0) instanceof BInteger);
		Assert.assertEquals(1234567890,
				((BInteger) b_list_one.get(0)).longValue());
	}

	@Test
	public void StringAddTest() {
		BList b_list_one = new BList();
		b_list_one.add("string_value");
		Assert.assertEquals(1, b_list_one.size());
		Assert.assertEquals(true, b_list_one.get(0) instanceof BString);
		Assert.assertEquals("string_value",
				((BString) b_list_one.get(0)).toString());
	}

	@Test
	public void ByteArrayAddTest() {
		BList b_list_one = new BList();
		b_list_one.add("string_value".getBytes());
		Assert.assertEquals(1, b_list_one.size());
		Assert.assertEquals(true, b_list_one.get(0) instanceof BString);
		Assert.assertEquals("string_value",
				new String(((BString) b_list_one.get(0)).getValue()));
	}

	@Test
	public void BValueRemoveTest() {
		BString b_string = new BString();
		BInteger b_integer = new BInteger(1);
		BList b_list_one = new BList();
		b_list_one.add(b_string);
		b_list_one.add(b_integer);
		Assert.assertEquals(2, b_list_one.size());
		Assert.assertEquals(true, b_list_one.remove(b_string));
		Assert.assertEquals(1, b_list_one.size());
		Assert.assertEquals(b_integer, b_list_one.get(0));
	}

	@Test
	public void IndexRemoveTest() {
		BString b_string = new BString();
		BInteger b_integer = new BInteger(1);
		BList b_list_one = new BList();
		b_list_one.add(b_string);
		b_list_one.add(b_integer);
		Assert.assertEquals(2, b_list_one.size());
		Assert.assertEquals(b_integer, b_list_one.remove(1));
		Assert.assertEquals(1, b_list_one.size());
		Assert.assertEquals(b_string, b_list_one.get(0));
	}
}
