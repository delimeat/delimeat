package io.delimeat.util;

import io.delimeat.core.guide.GuideEpisode;
import io.delimeat.core.guide.GuideSource;
import io.delimeat.core.show.Episode;
import io.delimeat.core.show.ShowGuideSource;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
  
    public static Episode toEpisode(GuideEpisode episode){
      Episode newEp = new Episode();
      newEp.setTitle(episode.getTitle());
      newEp.setAirDate(episode.getAirDate());
      newEp.setSeasonNum(episode.getSeasonNum());
      newEp.setEpisodeNum(episode.getEpisodeNum());
      return newEp;
    }
  
	public static List<GuideEpisode> cleanEpisodes(List<GuideEpisode> episodes){
		List<GuideEpisode> cleanEps = new ArrayList<GuideEpisode>();
     	Iterator<GuideEpisode> it = episodes.iterator();
     	while(it.hasNext()){
        GuideEpisode ep = it.next();
        if(ep.getSeasonNum() != null && ep.getSeasonNum() != 0 && ep.getAirDate() != null ){
          cleanEps.add(ep);
        }
			
      }
     	return cleanEps;
   }
  
  public static int compare(Episode episode, GuideEpisode guideEpisode){
		Integer epSeason = episode.getSeasonNum();
      int seasonCompare = epSeason.compareTo(guideEpisode.getSeasonNum());
      if(seasonCompare == 0){
        Integer epEpisode = episode.getEpisodeNum();
        return epEpisode.compareTo(guideEpisode.getEpisodeNum());
      }
      return seasonCompare;
  }
  
  public static String findGuideId(List<ShowGuideSource> guideSources, GuideSource guideSource){
      String guideId = null;
      for(ShowGuideSource source: guideSources){
        if(source.getId().getGuideSource() == guideSource){
          guideId = source.getGuideId();
          break;
        }
      }
      return guideId;
  }
  	
}
