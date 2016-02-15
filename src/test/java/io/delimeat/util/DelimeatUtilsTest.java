package io.delimeat.util;

import org.junit.Assert;
import org.junit.Test;

import io.delimeat.core.guide.GuideEpisode;
import io.delimeat.core.guide.GuideSource;
import io.delimeat.core.show.ShowGuideSource;
import io.delimeat.core.show.ShowGuideSourcePK;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

//TODO add tests for SHA1 hash and toHex
public class DelimeatUtilsTest {

  @Test
  public void isEmptyStringTest(){
    Assert.assertFalse(DelimeatUtils.isEmpty("TEXT"));
  }
  
  @Test
  public void isEmptyStringNullTest(){
    String value = null;
    Assert.assertTrue(DelimeatUtils.isEmpty(value));
  }
  
  @Test
  public void isEmptyStringEmptyTest(){
    Assert.assertTrue(DelimeatUtils.isEmpty(""));
  }
  
  @Test
  public void isNotEmptyStringTest(){
    Assert.assertTrue(DelimeatUtils.isNotEmpty("TEXT"));
  }
  
  @Test
  public void isNotEmptyStringNullTest(){
    String value = null;
    Assert.assertFalse(DelimeatUtils.isNotEmpty(value));
  }
  
  @Test
  public void isNotEmptyStringEmptyTest(){
    Assert.assertFalse(DelimeatUtils.isNotEmpty(""));
  }

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
  public void findGuideIdNullGuideSourcesTest(){
    Assert.assertNull(DelimeatUtils.findGuideId(null, GuideSource.TVDB));
  }
  
  @Test
  public void findGuideIdEmptyGuideSourcesTest(){
    Assert.assertNull(DelimeatUtils.findGuideId(Collections.<ShowGuideSource>emptyList(), GuideSource.TVDB));
  }
  
  
  @Test
  public void findGuideIdNullIdTest(){
    ShowGuideSource sgs = new ShowGuideSource();
    sgs.setGuideId("GUIDEID");
    Assert.assertNull(DelimeatUtils.findGuideId(Arrays.asList(sgs), GuideSource.TVDB));
  }
  
  @Test
  public void findGuideIdEmptyIdTest(){
    ShowGuideSource sgs = new ShowGuideSource();
    ShowGuideSourcePK id = new ShowGuideSourcePK();
    sgs.setId(id);
    sgs.setGuideId("GUIDEID");
    Assert.assertNull(DelimeatUtils.findGuideId(Arrays.asList(sgs), GuideSource.TVDB));
  }  
  
  @Test
  public void findGuideIdIncorrectSourceTest(){
    ShowGuideSource sgs = new ShowGuideSource();
    ShowGuideSourcePK id = new ShowGuideSourcePK();
    id.setGuideSource(GuideSource.IMDB);
    sgs.setId(id);
    sgs.setGuideId("GUIDEID");
    Assert.assertNull(DelimeatUtils.findGuideId(Arrays.asList(sgs), GuideSource.TVDB));
  }
  
  @Test
  public void findGuideIdCorrectSourceTest(){
    ShowGuideSource sgs = new ShowGuideSource();
    ShowGuideSourcePK id = new ShowGuideSourcePK();
    id.setGuideSource(GuideSource.TVDB);
    sgs.setId(id);
    sgs.setGuideId("GUIDEID");
    Assert.assertEquals("GUIDEID",DelimeatUtils.findGuideId(Arrays.asList(sgs), GuideSource.TVDB));
  }  
  
  @Test
  public void findGuideIdDuplicateSourceTest(){
    ShowGuideSource sgs = new ShowGuideSource();
    ShowGuideSourcePK id = new ShowGuideSourcePK();
    id.setGuideSource(GuideSource.TVDB);
    sgs.setId(id);
    sgs.setGuideId("GUIDEID");
    ShowGuideSource sgs2 = new ShowGuideSource();
    ShowGuideSourcePK id2 = new ShowGuideSourcePK();
    id2.setGuideSource(GuideSource.TVDB);
    sgs2.setId(id2);
    sgs2.setGuideId("GUIDEID2");
    Assert.assertEquals("GUIDEID",DelimeatUtils.findGuideId(Arrays.asList(sgs), GuideSource.TVDB));
  }  
}
