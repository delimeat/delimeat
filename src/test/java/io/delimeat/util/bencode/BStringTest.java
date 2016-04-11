package io.delimeat.util.bencode;

import org.junit.Assert;
import org.junit.Test;

public class BStringTest {

	@Test
	public void stringConstructorTest() {
		BString b_string = new BString("string_value");
		Assert.assertEquals("string_value", b_string.toString());
	}
  

	@Test
	public void byteArrayConstructorTest() {
		BString b_string = new BString("string_value".getBytes());
		Assert.assertEquals("string_value", b_string.toString());
	}	
  
  	@Test
  	public void nullConstructorTest(){
     	byte[] bytes = null;
   	BString b_string = new BString(bytes);
		Assert.assertEquals("",b_string.toString());
   }

	@Test
	public void sameComparableTest() {
		BString b_string_one = new BString("match");
		BString b_string_two = new BString("match");
		Assert.assertEquals(true, b_string_one.compareTo(b_string_two) == 0);
	}

	@Test
	public void notSameComparableTest() {
		BString b_string_one = new BString("match");
		BString b_string_two = new BString("no match");
		Assert.assertEquals(true, b_string_one.compareTo(b_string_two) != 0);
	}

	@Test
	public void nullComparableTest() {
		BString b_string_one = new BString("match");
		Assert.assertEquals(true, b_string_one.compareTo(null) != 0);
	}

	@Test
	public void emptyEqualsTest() {
		BString b_string_one = new BString("");
		BString b_string_two = new BString("equals");
		Assert.assertEquals(false, b_string_two.equals(b_string_one));
	}

	@Test
	public void equalEqualsTest() {
		BString b_string_one = new BString("match");
		BString b_string_two = new BString("match");
		Assert.assertEquals(true, b_string_one.equals(b_string_two));
	}

	@Test
	public void notEqualBStringEqualsTest() {
		BString b_string_one = new BString("match");
		BString b_string_two = new BString("no match");
		Assert.assertEquals(false, b_string_one.equals(b_string_two));
	}

	@Test
	public void nullEqualsTest() {
		BString b_string_one = new BString("match");
		Assert.assertEquals(false, b_string_one.equals(null));
	}

	@Test
	public void equalStringEqualsTest() {
		String string_one = "match";
		BString b_string_one = new BString("match");
		Assert.assertEquals(true, b_string_one.equals(string_one));
	}

	@Test
	public void notEqaulStringEqualsTest() {
		String string_one = "no match";
		BString b_string_one = new BString("match");
		Assert.assertEquals(false, b_string_one.equals(string_one));
	}
  
  	@Test
  	public void equalsByteArrayTest(){
     	BString b_string_one = new BString("match");
		Assert.assertEquals(true, b_string_one.equals("match".getBytes()));
   }

}
