package io.delimeat.torrent.bencode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.junit.Assert;
import org.junit.Test;

import io.delimeat.torrent.bencode.BDictionary;
import io.delimeat.torrent.bencode.BInteger;
import io.delimeat.torrent.bencode.BList;
import io.delimeat.torrent.bencode.BString;
import io.delimeat.torrent.bencode.BencodeException;
import io.delimeat.torrent.bencode.BencodeUtils;

public class BencodeUtilsTest {

  
	@Test
	public void encodingTest() throws UnsupportedEncodingException,
			IOException, BencodeException {

		// create a root dictionary
		BDictionary dict_1 = new BDictionary();

		// create a list with a string and an integer and add it to the root
		// dictionary
		BList list_1 = new BList();
		list_1.add("STRING_1_VAL");
		list_1.add(2);
		dict_1.put(new BString("LIST_1"), list_1);

		// create a dictionary with a string and integer and add it to the root
		// dictionary
		BDictionary dict_2 = new BDictionary();
		dict_2.put(new BString("STRING_2"), new BString("STRING_2_VAL"));
		dict_2.put(new BString("INTEGER_2"), new BInteger(1));
		dict_1.put(new BString("DICT_2"), dict_2);

		// add a string to the root dictionary
		dict_1.put(new BString("STRING_3"), new BString("STRING_3_VAL"));

		// add an integer to the root dictionary
		dict_1.put(new BString("INTEGER_3"), new BInteger(3));

		// encode the values and verify they are correctly encoded
		byte[] bytes =  BencodeUtils.encode( dict_1);
		String encoded_string = new String(bytes);
		Assert.assertEquals("d6:DICT_2d9:INTEGER_2i1e8:STRING_212:STRING_2_VALe9:INTEGER_3i3e6:LIST_1l12:STRING_1_VALi2ee8:STRING_312:STRING_3_VALe",encoded_string);
	}
   
	@Test
	public void encodingOutputStreamTest() throws UnsupportedEncodingException,
			IOException, BencodeException {

		// create a root dictionary
		BDictionary dict_1 = new BDictionary();

		// create a list with a string and an integer and add it to the root
		// dictionary
		BList list_1 = new BList();
		list_1.add("STRING_1_VAL");
		list_1.add(2);
		dict_1.put(new BString("LIST_1"), list_1);

		// create a dictionary with a string and integer and add it to the root
		// dictionary
		BDictionary dict_2 = new BDictionary();
		dict_2.put(new BString("STRING_2"), new BString("STRING_2_VAL"));
		dict_2.put(new BString("INTEGER_2"), new BInteger(1));
		dict_1.put(new BString("DICT_2"), dict_2);

		// add a string to the root dictionary
		dict_1.put(new BString("STRING_3"), new BString("STRING_3_VAL"));

		// add an integer to the root dictionary
		dict_1.put(new BString("INTEGER_3"), new BInteger(3));

		// encode the values and verify they are correctly encoded
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BencodeUtils.encode(baos, dict_1);
      byte[] bytes =  baos.toByteArray();
		String encoded_string = new String(bytes);
		Assert.assertEquals("d6:DICT_2d9:INTEGER_2i1e8:STRING_212:STRING_2_VALe9:INTEGER_3i3e6:LIST_1l12:STRING_1_VALi2ee8:STRING_312:STRING_3_VALe",encoded_string);
	}
  
	@Test
	/**
	 * check the decoding is correct
	 */
	public void decodingTest() throws Exception {

		// create the bencoded value to be decoded and decode it
		byte[] inputBytes = "d6:DICT_2d9:INTEGER_2i-1e8:STRING_212:STRING_2_VALe9:INTEGER_3i3e6:LIST_1l12:STRING_1_VALi2ee8:STRING_312:STRING_3_VALe"
				.getBytes(Charset.forName("ISO-8859-1"));
		BDictionary dict_1 = BencodeUtils.decode(inputBytes);

		// check that there are four values in the root dictionary
		Assert.assertEquals(4, dict_1.size());

		// check the root dictionary has a list with 1 string and 1 integer in
		// it
		Assert.assertEquals(true,
				dict_1.get(new BString("LIST_1")) instanceof BList);
		BList list_1 = (BList) dict_1.get(new BString("LIST_1"));
		Assert.assertEquals(2, list_1.size());
		Assert.assertEquals(true, list_1.get(0) instanceof BString);
		BString string_1 = (BString) list_1.get(0);
		Assert.assertEquals("STRING_1_VAL", string_1.toString());
		Assert.assertEquals(true, list_1.get(1) instanceof BInteger);
		BInteger integer_1 = (BInteger) list_1.get(1);
		Assert.assertEquals(2, integer_1.longValue());

		// check the root dictionary has a dictionary with a string and an
		// integer in it
		Assert.assertEquals(true,
				dict_1.get(new BString("DICT_2")) instanceof BDictionary);
		BDictionary dict_2 = (BDictionary) dict_1.get(new BString("DICT_2"));
		Assert.assertEquals(2, dict_2.size());
		Assert.assertEquals(true,
				dict_2.get(new BString("STRING_2")) instanceof BString);
		BString string_2 = (BString) dict_2.get(new BString("STRING_2"));
		Assert.assertEquals("STRING_2_VAL", string_2.toString());
		Assert.assertEquals(true,
				dict_2.get(new BString("INTEGER_2")) instanceof BInteger);
		BInteger integer_2 = (BInteger) dict_2.get(new BString("INTEGER_2"));
		Assert.assertEquals(-1, integer_2.longValue());

		// check the root dictionary has a string value in it
		Assert.assertEquals(true,
				dict_1.get(new BString("STRING_3")) instanceof BString);
		BString string_3 = (BString) dict_1.get(new BString("STRING_3"));
		Assert.assertEquals("STRING_3_VAL", string_3.toString());

		// check the root dictionary has an integer value in it
		Assert.assertEquals(true,
				dict_1.get(new BString("INTEGER_3")) instanceof BInteger);
		BInteger integer_3 = (BInteger) dict_1.get(new BString("INTEGER_3"));
		Assert.assertEquals(3, integer_3.longValue());
	}

	/**
	 * check that it fails if trying to decode something that is not a
	 * dictionary
	 * 
	 * @throws BencodeException
	 * @throws IOException
	 */
	@Test(expected = BencodeException.class)
	public void NonDictionaryDecodeTest() throws BencodeException, IOException {
     // create the bencoded value to be decoded and decode it
     byte[] inputBytes = "A".getBytes(Charset.forName("ISO-8859-1"));
     BencodeUtils.decode(inputBytes);
	}

	/**
	 * check if it fails when a dictionary key is not a string
	 * 
	 * @throws BencodeException
	 * @throws IOException
	 */
	@Test(expected = BencodeException.class)
	public void InvalidDictionaryKeyDecodeTest() throws BencodeException,
			IOException {
			// create the bencoded value to be decoded and decode it
			byte[] inputBytes = "di1ed9:INTEGER_2e".getBytes(Charset
					.forName("ISO-8859-1"));
			BencodeUtils.decode(inputBytes);
	}

	/**
	 * check if the dictionary has a key/value pair
	 * 
	 * @throws BencodeException
	 * @throws IOException
	 */
	@Test(expected = BencodeException.class)
	public void DictionaryHasKeyButNoValueTest() throws BencodeException,
			IOException {
			// create the bencoded value to be decoded and decode it
			byte[] inputBytes = "d3:keye".getBytes(Charset
					.forName("ISO-8859-1"));
			BencodeUtils.decode(inputBytes);
	}

	/**
	 * check if there is an unexpected end of a collection found, ie: random 'e'
	 * 
	 * @throws BencodeException
	 * @throws IOException
	 */
	@Test(expected = BencodeException.class)
	public void UnexpectedEndOfCollectionTest() throws BencodeException,
			IOException {
			// create the bencoded value to be decoded and decode it
			byte[] inputBytes = "d3:keyi1eee".getBytes(Charset
					.forName("ISO-8859-1"));
			BencodeUtils.decode(inputBytes);
	}

	/**
	 * check if the integer is actually an integer, and has an end, ie 'e'
	 * 
	 * @throws BencodeException
	 * @throws IOException
	 */
	@Test(expected = BencodeException.class)
	public void UnexpectedEndOfIntegerTest() throws BencodeException,
			IOException {
			// create the bencoded value to be decoded and decode it
			byte[] inputBytes = "d3:keyi".getBytes(Charset
					.forName("ISO-8859-1"));
			BencodeUtils.decode(inputBytes);
	}

	/**
	 * check if there is an unexpected character in an integer
	 * 
	 * @throws BencodeException
	 * @throws IOException
	 */
	@Test(expected = BencodeException.class)
	public void UnexpectedCharacterInIntegerTest() throws BencodeException,
			IOException {
			// create the bencoded value to be decoded and decode it
			byte[] inputBytes = "d3:keyi12aee".getBytes(Charset
					.forName("ISO-8859-1"));
			BencodeUtils.decode(inputBytes);
	}

	/**
	 * check if there is a character in the string where it shouldn't be
	 * 
	 * @throws BencodeException
	 * @throws IOException
	 */
	@Test(expected = BencodeException.class)
	public void UnexpectedCharacterInStringTest() throws BencodeException,
			IOException {
			// create the bencoded value to be decoded and decode it
			byte[] inputBytes = "d3r:keyi123ee".getBytes(Charset
					.forName("ISO-8859-1"));
			BencodeUtils.decode(inputBytes);

	}

	/**
	 * check for an unexpected end of the string
	 * 
	 * @throws BencodeException
	 * @throws IOException
	 */
	@Test(expected = BencodeException.class)
	public void UnexpectedEndOfStringTestTwo() throws BencodeException,
			IOException {
			// create the bencoded value to be decoded and decode it
			byte[] inputBytes = "d3:ke".getBytes(Charset.forName("ISO-8859-1"));
			BencodeUtils.decode(inputBytes);

	}

	/**
	 * check for an unexpected end of the string
	 * 
	 * @throws BencodeException
	 * @throws IOException
	 */
	@Test(expected = BencodeException.class)
	public void UnexpectedEndOfStringTestOne() throws BencodeException,
			IOException {
			// create the bencoded value to be decoded and decode it
			byte[] inputBytes = "d3".getBytes(Charset.forName("ISO-8859-1"));
			BencodeUtils.decode(inputBytes);

	}

}
