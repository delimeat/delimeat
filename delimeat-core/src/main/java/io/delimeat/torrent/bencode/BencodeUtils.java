/*
 * Copyright 2013-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.delimeat.torrent.bencode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.EmptyStackException;
import java.util.Stack;

public final class BencodeUtils {

	private static final char DICTIONARY = 'd';
	private static final char LIST = 'l';
	private static final char INTEGER = 'i';
	private static final char END = 'e';
	private static final char NEGATIVE = '-';
	private static final char COLON = ':';

	public static BDictionary decode(InputStream input)
			throws IOException, BencodeException {

           int c = input.read();
           if (c != DICTIONARY) {
             throw new BencodeException("Expected start of dictionary got " + (char)c);
           }
           final Stack<BCollection> stack = new Stack<BCollection>();
           final BDictionary root = new BDictionary();

           stack.push(root);
           BObject currentValue = null;
           while ((c = input.read()) != -1) {
             if (c == DICTIONARY) {
               currentValue = new BDictionary();
               stack.peek().addValue(currentValue);
               stack.push((BCollection)currentValue);
             } else if (c == LIST) {
               currentValue = new BList();
               stack.peek().addValue(currentValue);
               stack.push((BCollection)currentValue);
             } else if (c == INTEGER) {
               currentValue = readInteger(input);
               stack.peek().addValue(currentValue);
             } else if (Character.isDigit((char) c)) {
               int numericValue = Character.getNumericValue((char) c);
               currentValue = readString(numericValue, input);
               stack.peek().addValue(currentValue);
             } else if (c == END) {
               try{
                 final BCollection last = stack.peek();

                 if (last instanceof BDictionary && ((BDictionary)last).getKey() != null) {
                   throw new BencodeException("Unexpected end of dictionary");
                 }

                 stack.pop();

               }catch(EmptyStackException e){
                 throw new BencodeException("Unexpected end of collection encountered");
               }

             }
           }
           return root; 
	}

	public static BDictionary decode(byte[] input) throws IOException, BencodeException {
		return decode(new ByteArrayInputStream(input));
	}
  

  	public static void encode(OutputStream os, BDictionary value)
    throws IOException, BencodeException {
      encodeDictionary(os, value);
   }

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
		final byte[] length = Integer.toString(value.length).getBytes();
		os.write(length);
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
		}
	}

	private static BInteger readInteger(InputStream stream) throws IOException,
			BencodeException {

		final int character = stream.read();
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
				throw new BencodeException("Unexpected EndOfInputInteger");
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
  
	private static BString readString(int firstCharNumeric, InputStream input)
			throws IOException, BencodeException {

		String lengthStr = firstCharNumeric + "";

		// Read while next character is numeric
		int next;
      while (true) {

			next = input.read();

			if (next == -1) {
				throw new BencodeException("Unexpected end of string length");
			} else if ((char) next == COLON) {
				break;
			} else if (Character.isDigit((char) next)) {
				lengthStr = lengthStr + Character.getNumericValue((char) next);
			} else {
				throw new BencodeException("Unexpected character in string length " + (char) next);
			}
		}

		final int expectedLength = Integer.parseInt(lengthStr);
		final byte[] bytes = new byte[expectedLength];
		int actualLength = 0;

		// Need to read everything even if blocking
		while (actualLength < expectedLength) {
			int charsRead = input.read(bytes, actualLength, expectedLength - actualLength);
			if (charsRead == -1) {
				break;
			}
			actualLength += charsRead;
		}

		// Check that the whole String was read.
		if (actualLength != expectedLength) {
			throw new BencodeException("Unexpected End Of Characters String expected "+ expectedLength + " read " + actualLength);
		}
		return new BString(bytes);
	}
}
