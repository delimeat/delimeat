package io.delimeat.show.domain;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.guide.domain.GuideEpisode;
import io.delimeat.show.domain.Episode;
import io.delimeat.show.domain.EpisodeStatus;
import io.delimeat.show.domain.Show;

public class EpisodeTest {
	
    private Episode episode;

    @Before
    public void setUp() throws Exception {
        episode = new Episode();
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
    public void airDateTest() throws ParseException {
        Assert.assertNull(episode.getAirDate());
        episode.setAirDate(LocalDate.parse("2017-03-31"));
        Assert.assertEquals(LocalDate.parse("2017-03-31"), episode.getAirDate());
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
    

    public void statusTest() {
        Assert.assertEquals(EpisodeStatus.PENDING, episode.getStatus());
        episode.setStatus(EpisodeStatus.SKIPPED);
        Assert.assertEquals(EpisodeStatus.SKIPPED, episode.getStatus());
    }
    
	
	@Test
	public void lastFeedUpdateTest() throws ParseException {
		Assert.assertNull(episode.getLastFeedUpdate());
		Instant instant = Instant.now();
		episode.setLastFeedUpdate(instant);
		Assert.assertEquals(instant, episode.getLastFeedUpdate());
	}
	
	@Test
	public void lastFeedCheckTest() throws ParseException {
		Assert.assertNull(episode.getLastFeedCheck());
		Instant instant = Instant.now();
		episode.setLastFeedCheck(instant);
		Assert.assertEquals(instant, episode.getLastFeedCheck());
	}

    @Test
    public void versionTest() {
        Assert.assertEquals(0, episode.getVersion());
        episode.setVersion(Integer.MAX_VALUE);
        Assert.assertEquals(Integer.MAX_VALUE, episode.getVersion());
    }
    
  @Test
  public void equalsEpisodeTest() throws ParseException{
    Episode otherEp = new Episode();
    otherEp.setTitle("EP");
    otherEp.setAirDate(LocalDate.parse("2016-01-28"));
    otherEp.setSeasonNum(1);
    otherEp.setEpisodeNum(2);
    otherEp.setEpisodeId(Long.MAX_VALUE);
    otherEp.setVersion(Integer.MIN_VALUE);
    
    episode.setTitle("EP");
    episode.setAirDate(LocalDate.parse("2016-01-28"));
    episode.setSeasonNum(1);
    episode.setEpisodeNum(2);
    episode.setEpisodeId(Long.MAX_VALUE);
    episode.setVersion(Integer.MIN_VALUE);
    
    Assert.assertTrue(episode.equals(otherEp));
  }
  
  @Test
  public void equalsEpisodeEpisodeIdTest() throws ParseException{
    Episode otherEp = new Episode();
    otherEp.setTitle("EP");
    otherEp.setAirDate(LocalDate.parse("2016-01-28"));
    otherEp.setSeasonNum(1);
    otherEp.setEpisodeNum(2);
    otherEp.setEpisodeId(Long.MAX_VALUE);
    otherEp.setVersion(Integer.MIN_VALUE);
    
    episode.setTitle("EP");
    episode.setAirDate(LocalDate.parse("2016-01-28"));
    episode.setSeasonNum(1);
    episode.setEpisodeNum(2);
    episode.setEpisodeId(Long.MIN_VALUE);
    episode.setVersion(Integer.MIN_VALUE);
    
    Assert.assertFalse(episode.equals(otherEp));
  }

    @Test
    public void showTest() {
        Assert.assertNull(episode.getShow());
        Show show = new Show();
        episode.setShow(show);
        Assert.assertEquals(show, episode.getShow());
    }

    @Test
    public void equalsNullTest() {
        Assert.assertFalse(episode.equals(null));
    }

    @Test
    public void equalsObjectTest() {
        Assert.assertFalse(episode.equals(new Object()));
    }

    @Test
    public void equalsSelfTest() {
        Assert.assertTrue(episode.equals(episode));
    }

    @Test
    public void equalsGuideEpisodeTest() throws ParseException {
        GuideEpisode otherEp = new GuideEpisode();
        otherEp.setTitle("OTHER");
        otherEp.setAirDate(LocalDate.ofEpochDay(0));
        otherEp.setSeasonNum(2);
        otherEp.setEpisodeNum(3);

		Episode ep = new Episode();
		ep.setSeasonNum(2);
		ep.setEpisodeNum(3);
		
        Assert.assertTrue(ep.equals(otherEp));
    }

    @Test
    public void equalsGuideEpisodeSeasonNumTest() throws ParseException {
        GuideEpisode otherEp = new GuideEpisode();
        otherEp.setTitle("OTHER");
        otherEp.setAirDate(LocalDate.now());
        otherEp.setSeasonNum(2);
        otherEp.setEpisodeNum(2);

		Episode ep = new Episode();
		ep.setSeasonNum(2);
		ep.setEpisodeNum(3);
		
        Assert.assertFalse(episode.equals(otherEp));
    }

    @Test
    public void equalsGuideEpisodeEpisodeNumTest() throws ParseException {
        GuideEpisode otherEp = new GuideEpisode();
        otherEp.setTitle("OTHER");
        otherEp.setAirDate(LocalDate.now());
        otherEp.setSeasonNum(1);
        otherEp.setEpisodeNum(1);

		Episode ep = new Episode();
		ep.setSeasonNum(2);
		ep.setEpisodeNum(3);
		
        Assert.assertFalse(episode.equals(otherEp));
    }

    @Test
    public void equalsEpisodeVersionTest() throws ParseException {
		Episode otherEp = new Episode();
		otherEp.setEpisodeId(1);
		otherEp.setVersion(Integer.MIN_VALUE);
        
		Episode episode = new Episode();
		episode.setEpisodeId(1);
		episode.setVersion(Integer.MAX_VALUE);
		
        Assert.assertFalse(episode.equals(otherEp));
    }

    @Test
    public void hashTest() throws ParseException {
        episode.setEpisodeId(Long.MIN_VALUE);
        episode.setVersion(Integer.MIN_VALUE);

        Assert.assertEquals(961, episode.hashCode());
    }

    @Test
    public void toStringTest() throws ParseException {
        Assert.assertEquals("Episode{episodeId=0, seasonNum=0, episodeNum=0, doubleEp=false, status=PENDING, version=0}", episode.toString());
    }

}
