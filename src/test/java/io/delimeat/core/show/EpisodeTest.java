package io.delimeat.core.show;

import io.delimeat.core.guide.GuideEpisode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EpisodeTest {

	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	static{
		SDF.setTimeZone(TimeZone.getTimeZone("UTC"));
	}
  
	private Episode episode;

	@Before
	public void setUp() throws Exception {
		episode = new Episode();
	}
  
  @Test
  public void guideEpisodeConstructorTest() throws ParseException{
    GuideEpisode guideEp = new GuideEpisode();
    guideEp.setAirDate(SDF.parse("2016-01-28"));
    guideEp.setEpisodeNum(2);
    guideEp.setSeasonNum(1);
    guideEp.setTitle("TITLE");
    episode = new Episode(guideEp);
    Assert.assertEquals("2016-01-28",SDF.format(episode.getAirDate()));
    Assert.assertEquals(2, episode.getEpisodeNum());
    Assert.assertEquals(1, episode.getSeasonNum());
    Assert.assertEquals("TITLE",episode.getTitle());
  }

	@Test
	public void episodeIdTest() {
		Assert.assertEquals(0, episode.getEpisodeId());
		episode.setEpisodeId(Long.MAX_VALUE);
		Assert.assertEquals(Long.MAX_VALUE, episode.getEpisodeId());
	}

	@Test
	public void titleTest() {
		Assert.assertNull(episode.getTitle());
		episode.setTitle("TITLE");
		Assert.assertEquals("TITLE", episode.getTitle());
	}

	@Test
	public void airDateTimeTest() throws ParseException {
		Assert.assertNull(episode.getAirDate());
		episode.setAirDate(SDF.parse("2015-11-06"));
		Assert.assertEquals("2015-11-06", SDF.format(episode.getAirDate()));
	}

	@Test
	public void seasonNumTest() {
		Assert.assertEquals(0, episode.getSeasonNum());
		episode.setSeasonNum(Integer.MIN_VALUE);
		Assert.assertEquals(Integer.MIN_VALUE, episode.getSeasonNum());
	}

	@Test
	public void episodeNumTest() {
		Assert.assertEquals(0, episode.getEpisodeNum());
		episode.setEpisodeNum(Integer.MIN_VALUE);
		Assert.assertEquals(Integer.MIN_VALUE, episode.getEpisodeNum());
	}

	@Test
	public void doubleEpisodeTest() {
		Assert.assertFalse(episode.isDoubleEp());
		episode.setDoubleEp(true);
		Assert.assertTrue(episode.isDoubleEp());
	}

	@Test
	public void versionTest() {
		Assert.assertEquals(0, episode.getVersion());
		episode.setVersion(Integer.MAX_VALUE);
		Assert.assertEquals(Integer.MAX_VALUE, episode.getVersion());
	}

	@Test
	public void showTest() {
		Assert.assertNull(episode.getShow());
		Show show = new Show();
		episode.setShow(show);
		Assert.assertEquals(show, episode.getShow());
	}

  	@Test
  	public void equalsNullTest(){
   	Assert.assertFalse(episode.equals(null)); 
  	}
  
  	@Test
  	public void equalsObjectTest(){
   	Assert.assertFalse(episode.equals(new Object())); 
  	}
  
  	@Test
  	public void equalsSelfTest(){
   	Assert.assertTrue(episode.equals(episode)); 
  	}
  
  @Test
  public void equalsGuideEpisodeTest() throws ParseException{
    GuideEpisode otherEp = new GuideEpisode();
    otherEp.setTitle("OTHER");
    otherEp.setAirDate(SDF.parse("2016-01-28"));
    otherEp.setSeasonNum(1);
    otherEp.setEpisodeNum(2);
    
    episode.setTitle("EP");
    episode.setAirDate(SDF.parse("2000-01-28"));
    episode.setSeasonNum(1);
    episode.setEpisodeNum(2);
    episode.setEpisodeId(Long.MIN_VALUE);
    episode.setVersion(Integer.MAX_VALUE);
    
    Assert.assertTrue(episode.equals(otherEp));
  }
  @Test
  public void equalsGuideEpisodeSeasonNumTest() throws ParseException{
    GuideEpisode otherEp = new GuideEpisode();
    otherEp.setTitle("OTHER");
    otherEp.setAirDate(SDF.parse("2016-01-28"));
    otherEp.setSeasonNum(2);
    otherEp.setEpisodeNum(2);
    
    episode.setTitle("EP");
    episode.setAirDate(SDF.parse("2000-01-28"));
    episode.setSeasonNum(1);
    episode.setEpisodeNum(2);
    episode.setEpisodeId(Long.MIN_VALUE);
    episode.setVersion(Integer.MAX_VALUE);
    
    Assert.assertFalse(episode.equals(otherEp));
  }
  
  @Test
  public void equalsGuideEpisodeEpisodeNumTest() throws ParseException{
    GuideEpisode otherEp = new GuideEpisode();
    otherEp.setTitle("OTHER");
    otherEp.setAirDate(SDF.parse("2016-01-28"));
    otherEp.setSeasonNum(1);
    otherEp.setEpisodeNum(1);
    
    episode.setTitle("EP");
    episode.setAirDate(SDF.parse("2000-01-28"));
    episode.setSeasonNum(1);
    episode.setEpisodeNum(2);
    episode.setEpisodeId(Long.MIN_VALUE);
    episode.setVersion(Integer.MAX_VALUE);
    
    Assert.assertFalse(episode.equals(otherEp));
  }

  @Test
  public void equalsEpisodeTest() throws ParseException{
    Episode otherEp = new Episode();
    otherEp.setTitle("EP");
    otherEp.setAirDate(SDF.parse("2016-01-28"));
    otherEp.setSeasonNum(1);
    otherEp.setEpisodeNum(2);
    otherEp.setEpisodeId(Long.MAX_VALUE);
    otherEp.setVersion(Integer.MIN_VALUE);
    
    episode.setTitle("EP");
    episode.setAirDate(SDF.parse("2016-01-28"));
    episode.setSeasonNum(1);
    episode.setEpisodeNum(2);
    episode.setEpisodeId(Long.MAX_VALUE);
    episode.setVersion(Integer.MIN_VALUE);
    
    Assert.assertTrue(episode.equals(otherEp));
  }
  @Test
  public void equalsEpisodeSeasonNumTest() throws ParseException{
    Episode otherEp = new Episode();
    otherEp.setTitle("OTHER");
    otherEp.setAirDate(SDF.parse("2016-01-28"));
    otherEp.setSeasonNum(2);
    otherEp.setEpisodeNum(2);
    otherEp.setEpisodeId(Long.MAX_VALUE);
    otherEp.setVersion(Integer.MIN_VALUE);
    
    episode.setTitle("EP");
    episode.setAirDate(SDF.parse("2016-01-28"));
    episode.setSeasonNum(1);
    episode.setEpisodeNum(2);
    episode.setEpisodeId(Long.MAX_VALUE);
    episode.setVersion(Integer.MIN_VALUE);
    
    Assert.assertFalse(episode.equals(otherEp));
  }
  
  @Test
  public void equalsEpisodeEpisodeNumTest() throws ParseException{
    Episode otherEp = new Episode();
    otherEp.setTitle("OTHER");
    otherEp.setAirDate(SDF.parse("2016-01-28"));
    otherEp.setSeasonNum(1);
    otherEp.setEpisodeNum(1);
    otherEp.setEpisodeId(Long.MAX_VALUE);
    otherEp.setVersion(Integer.MIN_VALUE);
    
    episode.setTitle("EP");
    episode.setAirDate(SDF.parse("2016-01-28"));
    episode.setSeasonNum(1);
    episode.setEpisodeNum(2);
    episode.setEpisodeId(Long.MAX_VALUE);
    episode.setVersion(Integer.MIN_VALUE);
    
    Assert.assertFalse(episode.equals(otherEp));
  }
  
  @Test
  public void equalsEpisodeTitleTest() throws ParseException{
    Episode otherEp = new Episode();
    otherEp.setTitle("OTHER");
    otherEp.setAirDate(SDF.parse("2016-01-28"));
    otherEp.setSeasonNum(1);
    otherEp.setEpisodeNum(2);
    otherEp.setEpisodeId(Long.MAX_VALUE);
    otherEp.setVersion(Integer.MIN_VALUE);
    
    episode.setTitle("EP");
    episode.setAirDate(SDF.parse("2016-01-28"));
    episode.setSeasonNum(1);
    episode.setEpisodeNum(2);
    episode.setEpisodeId(Long.MAX_VALUE);
    episode.setVersion(Integer.MIN_VALUE);
    
    Assert.assertFalse(episode.equals(otherEp));
  }
  
  
  @Test
  public void equalsEpisodeEpisodeIdTest() throws ParseException{
    Episode otherEp = new Episode();
    otherEp.setTitle("EP");
    otherEp.setAirDate(SDF.parse("2016-01-28"));
    otherEp.setSeasonNum(1);
    otherEp.setEpisodeNum(2);
    otherEp.setEpisodeId(Long.MAX_VALUE);
    otherEp.setVersion(Integer.MIN_VALUE);
    
    episode.setTitle("EP");
    episode.setAirDate(SDF.parse("2016-01-28"));
    episode.setSeasonNum(1);
    episode.setEpisodeNum(2);
    episode.setEpisodeId(Long.MIN_VALUE);
    episode.setVersion(Integer.MIN_VALUE);
    
    Assert.assertFalse(episode.equals(otherEp));
  }

  @Test
  public void equalsEpisodeAirDateNullTest() throws ParseException{
    Episode otherEp = new Episode();
    otherEp.setTitle("EP");
    otherEp.setAirDate(SDF.parse("2016-01-28"));
    otherEp.setSeasonNum(1);
    otherEp.setEpisodeNum(2);
    otherEp.setEpisodeId(Long.MAX_VALUE);
    otherEp.setVersion(Integer.MIN_VALUE);
    
    episode.setTitle("EP");
    episode.setAirDate(null);
    episode.setSeasonNum(1);
    episode.setEpisodeNum(2);
    episode.setEpisodeId(Long.MIN_VALUE);
    episode.setVersion(Integer.MIN_VALUE);
    
    Assert.assertFalse(episode.equals(otherEp));
  }
  
  @Test
  public void equalsEpisodeOtherAirDateNullTest() throws ParseException{
    Episode otherEp = new Episode();
    otherEp.setTitle("EP");
    otherEp.setAirDate(null);
    otherEp.setSeasonNum(1);
    otherEp.setEpisodeNum(2);
    otherEp.setEpisodeId(Long.MAX_VALUE);
    otherEp.setVersion(Integer.MIN_VALUE);
    
    episode.setTitle("EP");
    episode.setAirDate(SDF.parse("2016-01-28"));
    episode.setSeasonNum(1);
    episode.setEpisodeNum(2);
    episode.setEpisodeId(Long.MIN_VALUE);
    episode.setVersion(Integer.MIN_VALUE);
    
    Assert.assertFalse(episode.equals(otherEp));
  }
  
  @Test
  public void equalsEpisodeAirDateTest() throws ParseException{
    Episode otherEp = new Episode();
    otherEp.setTitle("EP");
    otherEp.setAirDate(SDF.parse("2016-01-29"));
    otherEp.setSeasonNum(1);
    otherEp.setEpisodeNum(2);
    otherEp.setEpisodeId(Long.MAX_VALUE);
    otherEp.setVersion(Integer.MIN_VALUE);
    
    episode.setTitle("EP");
    episode.setAirDate(SDF.parse("2016-01-28"));
    episode.setSeasonNum(1);
    episode.setEpisodeNum(2);
    episode.setEpisodeId(Long.MIN_VALUE);
    episode.setVersion(Integer.MIN_VALUE);
    
    Assert.assertFalse(episode.equals(otherEp));
  }
  
  	@Test
  	public void hashTest() throws ParseException{
     episode.setTitle("EP");
     episode.setAirDate(SDF.parse("2016-01-28"));
     episode.setSeasonNum(1);
     episode.setEpisodeNum(2);
     episode.setEpisodeId(Long.MIN_VALUE);
     episode.setVersion(Integer.MIN_VALUE);
     
     Assert.assertEquals(961,episode.hashCode());
     
   }
  
    @Test
    public void toStringTest() throws ParseException{
     episode.setTitle("EP");
     episode.setAirDate(SDF.parse("2016-01-28"));
     episode.setSeasonNum(1);
     episode.setEpisodeNum(2);
     episode.setEpisodeId(Long.MIN_VALUE);
     episode.setVersion(Integer.MIN_VALUE);  
		
      Assert.assertEquals("Episode{episodeId=-9223372036854775808, title=EP, airDate=Thu Jan 28 00:00:00 UTC 2016, seasonNum=1, episodeNum=2, doubleEp=false, showId=null, version=-2147483648}", episode.toString());
    }

}
