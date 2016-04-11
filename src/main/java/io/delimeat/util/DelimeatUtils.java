package io.delimeat.util;

import org.apache.commons.codec.digest.DigestUtils;

import io.delimeat.core.guide.GuideEpisode;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DelimeatUtils {

  	DelimeatUtils(){
     	//do nothing
   }
  
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
	 public static boolean isEmpty(Collection<?> value){
		 return value == null || value.isEmpty();
	 }
  
    /**
     * Returns true if the collection is not null or empty.
     *
     * @param value
     * @return true if collection is null or empty
     */  
	 public static boolean isNotEmpty(Collection<?> value){
		 return !isEmpty(value);
	 }

    /**
     * Calculates the SHA1 of the byte array.
     *
     * @param bytes
     * @return sha1 of the byte array
     */
    public static byte[] getSHA1(byte[] bytes) {
        MessageDigest md = DigestUtils.getSha1Digest();
        md.update(bytes, 0, bytes.length);
        return  md.digest();
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
  
  /**
     * Returns the clean list of guide episodes.
     * 	episodes must have a airing date, and not be a special episode (no season or season == 0)
     *
     * @param episodes
     * @return clean episodes
     */  
  public static List<GuideEpisode> cleanEpisodes(List<GuideEpisode> episodes){
    List<GuideEpisode> cleanEps = new ArrayList<GuideEpisode>();
    if(isNotEmpty(episodes) == true){
      for(GuideEpisode ep: episodes){
        if(ep.getSeasonNum() != null && ep.getSeasonNum() != 0 && ep.getAirDate() != null ){
          cleanEps.add(ep);
        }

      }
    }
    return cleanEps;
  }
  	
}
