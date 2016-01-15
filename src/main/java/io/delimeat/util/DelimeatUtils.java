package io.delimeat.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

public class DelimeatUtils {

    /**
     * Returns true if the string is null or empty.
     *
     * @param value
     * @return true if string is null or empty
     */
    public static boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }
  
    /**
     * Returns true if the string is not null or empty.
     *
     * @param value
     * @return true if string is not null or empty
     */
    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }
  
    /**
     * Returns true if the collection is null or empty.
     *
     * @param value
     * @return true if collection is null or empty
     */  
	 public static boolean isCollectionEmpty(Collection<?> value){
		 return value == null || value.isEmpty();
	 }

    /**
     * Calculates the SHA1 of the byte array.
     *
     * @param bytes
     * @return sha1 of the byte array
     */
    public static String getSHA1(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(bytes, 0, bytes.length);
            byte[] digest = md.digest();
            return toHex(digest);
        } catch (NoSuchAlgorithmException t) {
            throw new RuntimeException(t);
        }
    }

    /**
     * Returns the hex representation of the byte array.
     *
     * @param bytes
     * @return byte array as hex string
     */
    public static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            if ((bytes[i] & 0xff) < 0x10) {
                sb.append('0');
            }
            sb.append(Long.toString(bytes[i] & 0xff, 16));
        }
        return sb.toString();
    }
}
