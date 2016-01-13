package io.delimeat.util.bencode;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Test;

public class BDictionaryTest {

	@Test
	public void NullConstructorTest() {
		BDictionary b_dict = new BDictionary();
		Assert.assertEquals(0, b_dict.size());
	}

	@Test
	public void PutAllTest() {
		Map<BString, BObject> values = new TreeMap<BString, BObject>();
		BString b_string_val = new BString();
		BString b_string_key = new BString("key_value");
		values.put(b_string_key, b_string_val);
		BDictionary b_dict = new BDictionary();
		b_dict.putAll(values);
		Assert.assertEquals(1, b_dict.size());
		Assert.assertEquals(b_string_val, b_dict.get(b_string_key));
	}

	@Test
	public void BStringPutTest() {
		BDictionary b_dict = new BDictionary();
		BString b_string_val = new BString();
		BString b_string_key = new BString("key_value");
		Assert.assertEquals(null, b_dict.put(b_string_key, b_string_val));
		Assert.assertEquals(1, b_dict.size());
	}

	@Test
	public void StringPutTest() {
		BDictionary b_dict = new BDictionary();
		BString b_string_val = new BString();
		Assert.assertEquals(null, b_dict.put("key_value", b_string_val));
		Assert.assertEquals(1, b_dict.size());
	}

	@Test
	public void ByteArrayPutTest() {
		BDictionary b_dict = new BDictionary();
		BString b_string_val = new BString();
		Assert.assertEquals(null, b_dict.put(
				"key_value".getBytes(Charset.forName("ISO-8859-1")),
				b_string_val));
		Assert.assertEquals(1, b_dict.size());
	}

	@Test
	public void BStringGetTest() {
		BDictionary b_dict = new BDictionary();
		BString b_string_val = new BString();
		BString b_string_key = new BString("key_value");
		b_dict.put(b_string_key, b_string_val);
		Assert.assertEquals(1, b_dict.size());
		Assert.assertEquals(b_string_val, b_dict.get(b_string_key));
	}

	@Test
	public void StringGetTest() {
		BDictionary b_dict = new BDictionary();
		BString b_string_val = new BString();
		BString b_string_key = new BString("key_value");
		b_dict.put(b_string_key, b_string_val);
		Assert.assertEquals(1, b_dict.size());
		Assert.assertEquals(b_string_val, b_dict.get("key_value"));
	}

	@Test
	public void ByteArrayGetTest() {
		BDictionary b_dict = new BDictionary();
		BString b_string_val = new BString();
		BString b_string_key = new BString("key_value");
		b_dict.put(b_string_key, b_string_val);
		Assert.assertEquals(1, b_dict.size());
		Assert.assertEquals(b_string_val,
				b_dict.get("key_value".getBytes(Charset.forName("ISO-8859-1"))));
	}

	@Test
	public void BStringRemoveTest() {
		BDictionary b_dict = new BDictionary();
		BString b_string_val = new BString();
		BString b_string_key = new BString("key_value");
		b_dict.put(b_string_key, b_string_val);
		Assert.assertEquals(1, b_dict.size());
		Assert.assertEquals(b_string_val, b_dict.remove(b_string_key));
		Assert.assertEquals(0, b_dict.size());
	}

	@Test
	public void StringRemoveTest() {
		BDictionary b_dict = new BDictionary();
		BString b_string_val = new BString();
		BString b_string_key = new BString("key_value");
		b_dict.put(b_string_key, b_string_val);
		Assert.assertEquals(1, b_dict.size());
		Assert.assertEquals(b_string_val, b_dict.remove("key_value"));
		Assert.assertEquals(0, b_dict.size());
	}

	@Test
	public void ByteArrayRemoveTest() {
		BDictionary b_dict = new BDictionary();
		BString b_string_val = new BString();
		BString b_string_key = new BString("key_value");
		b_dict.put(b_string_key, b_string_val);
		Assert.assertEquals(1, b_dict.size());
		Assert.assertEquals(b_string_val, b_dict.remove("key_value"
				.getBytes(Charset.forName("ISO-8859-1"))));
		Assert.assertEquals(0, b_dict.size());
	}

	@Test
	public void SetValueTest() throws BencodeException {
		BDictionary b_dict = new BDictionary();
		Assert.assertEquals(true, b_dict.expectingKey());
		Assert.assertEquals(0, b_dict.size());
		BString b_string_key = new BString("key_value");
		b_dict.setValue(b_string_key);
		Assert.assertEquals(false, b_dict.expectingKey());
		Assert.assertEquals(0, b_dict.size());
		BInteger b_integer_val = new BInteger(1);
		b_dict.setValue(b_integer_val);
		Assert.assertEquals(true, b_dict.expectingKey());
		Assert.assertEquals(1, b_dict.size());
	}

	@Test(expected = BencodeException.class)
	public void InvalidSetValueTest() throws BencodeException {
		BDictionary b_dict = new BDictionary();
		Assert.assertEquals(true, b_dict.expectingKey());
		BInteger b_integer_val = new BInteger(1);
		b_dict.setValue(b_integer_val);
	}
}
