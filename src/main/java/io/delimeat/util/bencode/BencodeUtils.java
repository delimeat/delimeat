package io.delimeat.util.bencode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.EmptyStackException;
import java.util.Stack;

public final class BencodeUtils {

	private static final char DICTIONARY = 'd';
	private static final char LIST = 'l';
	private static final char INTEGER = 'i';
	private static final char END = 'e';
	private static final char NEGATIVE = '-';
	private static final char COLON = ':';
	
	private static void addValue(Stack<BObject> stack, BObject value)
			throws  BencodeException {
		BObject last = stack.peek();
		if (last instanceof BDictionary) {
			BDictionary dict = (BDictionary) last;
			dict.setValue(value);
		} else if (last instanceof BList) {
			BList list = (BList) last;
			list.add(value);
		} else {
			throw new BencodeUnexpectedTypeException("UnexpectedBencodeType " +last.getClass().getName());
		}
	}

	/**
	 * Decode an input stream to a bencoded dictionary
	 *
	 * @param input
	 *            - the input stream to be decoded
	 * @param charset
	 *            - the charset to decode with
	 * @return
	 * @throws BencodeException
	 * @throws IOException
	 */
	public static BDictionary decode(InputStream input)
			throws BencodeException, IOException {
		int c = input.read();
		if (c != DICTIONARY) {
			throw new BencodeUnexpectedCharacterException("ExpectedStartOfDictionary " + (char)c);
		}
		Stack<BObject> stack = new Stack<BObject>();
		BDictionary root = new BDictionary();

		stack.push(root);
		BObject currentValue = null;
		while ((c = input.read()) != -1) {
			if (c == DICTIONARY) {
				currentValue = new BDictionary();
				addValue(stack, currentValue);
				stack.push(currentValue);
			} else if (c == LIST) {
				currentValue = new BList();
				addValue(stack, currentValue);
				stack.push(currentValue);
			} else if (c == INTEGER) {
				currentValue = readInteger(input);
				addValue(stack, currentValue);
			} else if (Character.isDigit((char) c)) {
				currentValue = readString(c, input);
				addValue(stack, currentValue);
			} else if (c == END) {
				BObject last = null;
				try{
					last = stack.peek();
				}catch(EmptyStackException e){
					throw new BencodeException("UnexpectedEndOfCollection");
				}
				if (last instanceof BDictionary) {
					BDictionary dict = (BDictionary) last;
					if (dict.expectingKey() == false) {
						throw new BencodeException("UnexpectedEndOfDictionary");
					}
				} // 90% sure this will never happen
				else if (!(last instanceof BList)) {
					throw new BencodeException("UnexpectedEndOfCollection");
				}
				stack.pop();
			}
		}
		return root;
	}
	/**
	 * 
	 * @param input
	 * @return
	 * @throws BencodeException
	 * @throws IOException
	 */
	public static BDictionary decode(byte[] input) throws BencodeException, IOException{
		ByteArrayInputStream bais = new ByteArrayInputStream(input);
		return decode(bais);
	}

	/**
	 * Encode a BDictionary and output to an output stream
	 *
	 * @param os
	 *            - OutputStream to output to
	 * @param value
	 *            - BDictionary value to encode
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @throws BencodeException
	 */
	public static void encode(OutputStream os, BDictionary value)
			throws IOException, BencodeException {
		encodeDictionary(os, value);
	}
	/**
	 * Encode a BDictionary and return a byte array
	 * @param value
	 * 			 - BDictionary value to encode
	 * @return
	 * @throws IOException
	 * @throws BencodeException
	 */
	public static byte[] encode(BDictionary value) throws IOException, BencodeException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		encodeDictionary(baos, value);
		return baos.toByteArray();
	}

	private static void encodeDictionary(OutputStream os, BDictionary value)
			throws IOException, BencodeException {
		os.write(DICTIONARY);
		for (BString key : value.keySet()) {
			BObject val = value.get(key);
			encodeString(os, key);
			encodeValue(os, val);
		}
		os.write(END);
	}

	private static void encodeInteger(OutputStream os, BInteger value)
			throws IOException {
		os.write(INTEGER);
		os.write(value.toString().getBytes());
		os.write(END);
	}

	private static void encodeList(OutputStream os, BList value)
			throws IOException, BencodeException {
		os.write(LIST);
		for (BObject val : value) {
			encodeValue(os, val);
		}
		os.write(END);
	}

	private static void encodeString(OutputStream os, BString value)
			throws IOException {
		encodeString(os, value.getValue());
	}

	private static void encodeString(OutputStream os, byte[] value)
			throws IOException {
		String result = Integer.toString(value.length);
		os.write(result.getBytes());
		os.write(COLON);
		os.write(value);
	}

	private static void encodeValue(OutputStream os, BObject value)
			throws IOException, BencodeException {

		if (value instanceof BInteger) {
			encodeInteger(os, (BInteger) value);
		} else if (value instanceof BString) {
			encodeString(os, (BString) value);
		} else if (value instanceof BList) {
			encodeList(os, (BList) value);
		} else if (value instanceof BDictionary) {
			encodeDictionary(os, (BDictionary) value);
		} else {
			throw new BencodeException("InvalidBencodingType " + value.getClass().getName());
		}
	}

	private static BInteger readInteger(InputStream stream) throws IOException,
			BencodeException {

		int character = stream.read();
		String value;

		if (character == NEGATIVE) {
			value = "-";
		} else {
			int numeric = Character.getNumericValue((char) character);
			value = numeric + "";
		}

		int next;

		// Read while next character is numeric
		while (true) {

			next = stream.read();

			if (next == -1) {
				throw new BencodeException("UnexpectedEndOfInputInteger");
			} else if ((char) next == END) {
				break;
			} else if (Character.isDigit((char) next)) {
				value = value + Character.getNumericValue((char) next);
			} else {
				throw new BencodeException("UnexpectedInputInteger " + (char)next);
			}
		}
		return new BInteger(value);
	}

	private static BString readString(int firstChar, InputStream input)
			throws IOException, BencodeException {

		int numeric = Character.getNumericValue((char) firstChar);
		String lengthStr = numeric + "";

		int next;

		// Read while next character is numeric
		while (true) {

			next = input.read();

			if (next == -1) {
				throw new BencodeException("UnexpectedEndOfInputString");
			} else if ((char) next == COLON) {
				break;
			} else if (Character.isDigit((char) next)) {
				lengthStr = lengthStr + Character.getNumericValue((char) next);
			} else {
				throw new BencodeException("UnexpectedInputString " + (char) next);
			}
		}

		int length = Integer.parseInt(lengthStr);

		byte[] bytes = new byte[length];

		int totalCharsRead = 0;

		// Need to read everything even if blocking
		while (totalCharsRead < length) {
			int charsRead = input.read(bytes, totalCharsRead, length - totalCharsRead);
			if (charsRead == -1) {
				break;
			}
			totalCharsRead += charsRead;
		}

		// Check that the whole String was read.
		if (totalCharsRead != length) {
			throw new BencodeException("UnexpectedEndOfCharactersString" + new Object[]{totalCharsRead,length});
		}
		return new BString(bytes);
	}
}
