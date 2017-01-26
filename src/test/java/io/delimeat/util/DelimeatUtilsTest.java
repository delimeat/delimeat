package io.delimeat.util;

import org.junit.Assert;
import org.junit.Test;

import io.delimeat.core.guide.GuideEpisode;
import io.delimeat.util.DelimeatUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DelimeatUtilsTest {

  @Test
  public void isEmptyCollectionTest(){
    Assert.assertFalse(DelimeatUtils.isEmpty(Arrays.asList("VALUE")));
  }
  
  @Test
  public void isEmptyCollectionNullTest(){
    List<String> value = null;
    Assert.assertTrue(DelimeatUtils.isEmpty(value));
  }
  
  @Test
  public void isEmptyCollectionEmptyTest(){
    Assert.assertTrue(DelimeatUtils.isEmpty(Collections.EMPTY_LIST));
  }
  
  @Test
  public void isNotEmptyCollectionTest(){
    Assert.assertTrue(DelimeatUtils.isNotEmpty(Arrays.asList("VALUE")));
  }
  
  @Test
  public void isNotEmptyCollectionNullTest(){
    List<String> value = null;
    Assert.assertFalse(DelimeatUtils.isNotEmpty(value));
  }
  
  @Test
  public void isNotEmptyCollectionEmptyTest(){
    Assert.assertFalse(DelimeatUtils.isNotEmpty(Collections.EMPTY_SET));
  }
  
  @Test
  public void cleanEpisodesNullTest(){
    List<GuideEpisode> results = DelimeatUtils.cleanEpisodes(null);
    Assert.assertNotNull(results);
    Assert.assertTrue(results.isEmpty());
  }
  
  @Test
  public void cleanEpisodesEmptyTest(){
    List<GuideEpisode> results = DelimeatUtils.cleanEpisodes(Collections.<GuideEpisode>emptyList());
    Assert.assertNotNull(results);
    Assert.assertTrue(results.isEmpty());
  }
  
  @Test
  public void cleanEpisodesNullSeasonTest(){
    GuideEpisode ep = new GuideEpisode();
    ep.setSeasonNum(null);
    ep.setAirDate(new Date());
    List<GuideEpisode> results = DelimeatUtils.cleanEpisodes(Arrays.asList(ep));
    Assert.assertNotNull(results);
    Assert.assertTrue(results.isEmpty());
  }
  
  @Test
  public void cleanEpisodesZeroSeasonTest(){
    GuideEpisode ep = new GuideEpisode();
    ep.setSeasonNum(0);
    ep.setAirDate(new Date());
    List<GuideEpisode> results = DelimeatUtils.cleanEpisodes(Arrays.asList(ep));
    Assert.assertNotNull(results);
    Assert.assertTrue(results.isEmpty());
  }
  
  @Test
  public void cleanEpisodesNullAirDateTest(){
    GuideEpisode ep = new GuideEpisode();
    ep.setSeasonNum(1);
    ep.setAirDate(null);
    List<GuideEpisode> results = DelimeatUtils.cleanEpisodes(Arrays.asList(ep));
    Assert.assertNotNull(results);
    Assert.assertTrue(results.isEmpty());
  } 
  
  
  @Test
  public void cleanEpisodesTest(){
    GuideEpisode ep = new GuideEpisode();
    ep.setSeasonNum(1);
    ep.setAirDate(new Date());
    List<GuideEpisode> results = DelimeatUtils.cleanEpisodes(Arrays.asList(ep));
    Assert.assertNotNull(results);
    Assert.assertEquals(1, results.size());
    Assert.assertEquals(ep, results.get(0));
  }
  
  
  @Test
  public void sha1HashTest(){
    byte[] expectedBytes = new byte[]{50,82,79,-89,112,-96,70,-88,123,28,107,-61,15,99,-84,52,18,-84,-125,37};
    Assert.assertTrue(Arrays.equals(expectedBytes,DelimeatUtils.getSHA1("byes".getBytes())));
  }
  
  @Test
  public void toHexTest(){
    Assert.assertEquals("6279746573", DelimeatUtils.toHex("bytes".getBytes()));
  }

}
