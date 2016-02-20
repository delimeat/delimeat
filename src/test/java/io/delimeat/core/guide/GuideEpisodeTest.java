  package io.delimeat.core.guide;

  import io.delimeat.core.show.Episode;

  import java.text.ParseException;
  import java.text.SimpleDateFormat;

  import org.junit.Assert;
  import org.junit.Before;
  import org.junit.Test;

  public class GuideEpisodeTest {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    private GuideEpisode ep;

    @Before
    public void setUp() {
      ep = new GuideEpisode();
    }

    @Test
    public void setAirDateTest() throws ParseException {
      Assert.assertNull(ep.getAirDate());
      ep.setAirDate(SDF.parse("2005-01-02"));
      Assert.assertEquals("2005-01-02", SDF.format(ep.getAirDate()));

    }

    @Test
    public void setTitleTest() {
      Assert.assertEquals(null, ep.getTitle());
      ep.setTitle("TEST TITLE");
      Assert.assertEquals("TEST TITLE", ep.getTitle());
    }

    @Test
    public void setSeasonNumTest() {
      Assert.assertEquals(0, ep.getSeasonNum().intValue());
      ep.setSeasonNum(12);
      Assert.assertEquals(12, ep.getSeasonNum().intValue());
    }

    @Test
    public void setEpisodeNumTest() {
      Assert.assertEquals(0, ep.getEpisodeNum().intValue());
      ep.setEpisodeNum(55);
      Assert.assertEquals(55, ep.getEpisodeNum().intValue());
    }

    @Test
    public void setProductionNumTest() {
      Assert.assertEquals(0, ep.getProductionNum().intValue());
      ep.setProductionNum(208);
      Assert.assertEquals(208, ep.getProductionNum().intValue());
    }

    @Test
    public void compareToNoAirDateTest() {
      ep.setAirDate(null);

      GuideEpisode ep2 = new GuideEpisode();
      ep2.setAirDate(null);

      Assert.assertEquals(0, ep.compareTo(ep2));
    }

    @Test
    public void compareToOtherNoAirDateTest() throws Exception {
      ep.setAirDate(SDF.parse("2005-01-02"));

      GuideEpisode ep2 = new GuideEpisode();
      ep2.setAirDate(null);

      Assert.assertEquals(1, ep.compareTo(ep2));
    }

    @Test
    public void compareToThisNoAirDateTest() throws Exception {
      ep.setAirDate(null);

      GuideEpisode otherEpisode = new GuideEpisode();
      otherEpisode.setAirDate(SDF.parse("2005-01-02"));

      Assert.assertEquals(-1, ep.compareTo(otherEpisode));
    }

    @Test
    public void compareToAirDateBeforeTest() throws Exception {
      ep.setAirDate(SDF.parse("2005-01-02"));

      GuideEpisode otherEpisode = new GuideEpisode();
      otherEpisode.setAirDate(SDF.parse("2005-02-02"));

      Assert.assertEquals(-1, ep.compareTo(otherEpisode));
    }

    @Test
    public void compareToAirDateAfterTest() throws Exception {
      ep.setAirDate(SDF.parse("2005-03-02"));

      GuideEpisode otherEpisode = new GuideEpisode();
      otherEpisode.setAirDate(SDF.parse("2005-02-02"));

      Assert.assertEquals(1, ep.compareTo(otherEpisode));
    }

    @Test
    public void compareToSeasonAfter() throws Exception {
      ep.setAirDate(SDF.parse("2005-03-02"));
      ep.setSeasonNum(2);
      ep.setEpisodeNum(1);

      GuideEpisode otherEpisode = new GuideEpisode();
      otherEpisode.setAirDate(SDF.parse("2005-03-02"));
      otherEpisode.setSeasonNum(1);
      otherEpisode.setEpisodeNum(1);

      Assert.assertEquals(1, ep.compareTo(otherEpisode));
    }

    @Test
    public void compareToSeasonBefore() throws Exception {
      ep.setAirDate(SDF.parse("2005-03-02"));
      ep.setSeasonNum(1);
      ep.setEpisodeNum(1);

      GuideEpisode otherEpisode = new GuideEpisode();
      otherEpisode.setAirDate(SDF.parse("2005-03-02"));
      otherEpisode.setSeasonNum(2);
      otherEpisode.setEpisodeNum(1);

      Assert.assertEquals(-1, ep.compareTo(otherEpisode));
    }

    @Test
    public void compareToSeasonNull() throws Exception {
      ep.setAirDate(SDF.parse("2005-03-02"));
      ep.setSeasonNum(null);
      ep.setEpisodeNum(1);

      GuideEpisode otherEpisode = new GuideEpisode();
      otherEpisode.setAirDate(SDF.parse("2005-03-02"));
      otherEpisode.setSeasonNum(1);
      otherEpisode.setEpisodeNum(1);

      Assert.assertEquals(-1, ep.compareTo(otherEpisode));
    }

    @Test
    public void compareToEpisodeAfter() throws Exception {
      ep.setAirDate(SDF.parse("2005-03-02"));
      ep.setSeasonNum(2);
      ep.setEpisodeNum(2);

      GuideEpisode otherEpisode = new GuideEpisode();
      otherEpisode.setAirDate(SDF.parse("2005-03-02"));
      otherEpisode.setSeasonNum(1);
      otherEpisode.setEpisodeNum(1);

      Assert.assertEquals(1, ep.compareTo(otherEpisode));
    }

    @Test
    public void compareToEpisodeBefore() throws Exception {
      ep.setAirDate(SDF.parse("2005-03-02"));
      ep.setSeasonNum(2);
      ep.setEpisodeNum(1);

      GuideEpisode otherEpisode = new GuideEpisode();
      otherEpisode.setAirDate(SDF.parse("2005-03-02"));
      otherEpisode.setSeasonNum(2);
      otherEpisode.setEpisodeNum(2);

      Assert.assertEquals(-1, ep.compareTo(otherEpisode));
    }

    @Test
    public void compareToEpisodeNull() throws Exception {
      ep.setAirDate(SDF.parse("2005-03-02"));
      ep.setSeasonNum(1);
      ep.setEpisodeNum(null);    

      GuideEpisode otherEpisode = new GuideEpisode();
      otherEpisode.setAirDate(SDF.parse("2005-03-02"));
      otherEpisode.setSeasonNum(1);
      otherEpisode.setEpisodeNum(1);

      Assert.assertEquals(-1, ep.compareTo(otherEpisode));
    }

    @Test
    public void compareToEpisodeMatch() throws Exception {
      ep.setAirDate(SDF.parse("2005-03-02"));
      ep.setSeasonNum(2);
      ep.setEpisodeNum(2);

      GuideEpisode otherEpisode = new GuideEpisode();
      otherEpisode.setAirDate(SDF.parse("2005-03-02"));
      otherEpisode.setSeasonNum(2);
      otherEpisode.setEpisodeNum(2);

      Assert.assertEquals(0, ep.compareTo(otherEpisode));
    }


    @Test
    public void equalsObjectTest(){
      Assert.assertFalse(ep.equals(new Object())); 
    }

    @Test
    public void equalsGuideEpisodeTest() throws ParseException{
      GuideEpisode otherEp = new GuideEpisode();
      otherEp.setTitle("OTHER");
      otherEp.setAirDate(SDF.parse("2016-01-28"));
      otherEp.setSeasonNum(1);
      otherEp.setEpisodeNum(2);

      ep.setTitle("EP");
      ep.setAirDate(SDF.parse("2000-01-28"));
      ep.setSeasonNum(1);
      ep.setEpisodeNum(2);

      Assert.assertTrue(ep.equals(otherEp));
    }
    @Test
    public void equalsGuideEpisodeSeasonNumTest() throws ParseException{
      GuideEpisode otherEp = new GuideEpisode();
      otherEp.setTitle("OTHER");
      otherEp.setAirDate(SDF.parse("2016-01-28"));
      otherEp.setSeasonNum(2);
      otherEp.setEpisodeNum(2);

      ep.setTitle("EP");
      ep.setAirDate(SDF.parse("2000-01-28"));
      ep.setSeasonNum(1);
      ep.setEpisodeNum(2);

      Assert.assertFalse(ep.equals(otherEp));
    }

    @Test
    public void equalsGuideEpisodeEpisodeNumTest() throws ParseException{
      GuideEpisode otherEp = new GuideEpisode();
      otherEp.setTitle("OTHER");
      otherEp.setAirDate(SDF.parse("2016-01-28"));
      otherEp.setSeasonNum(1);
      otherEp.setEpisodeNum(1);

      ep.setTitle("EP");
      ep.setAirDate(SDF.parse("2000-01-28"));
      ep.setSeasonNum(1);
      ep.setEpisodeNum(2);

      Assert.assertFalse(ep.equals(otherEp));
    }

    @Test
    public void equalsEpisodeTest() throws ParseException{
      Episode otherEp = new Episode();
      otherEp.setTitle("OTHER");
      otherEp.setAirDate(SDF.parse("2016-01-28"));
      otherEp.setSeasonNum(1);
      otherEp.setEpisodeNum(2);
      otherEp.setEpisodeId(Long.MAX_VALUE);
      otherEp.setVersion(Integer.MIN_VALUE);

      ep.setTitle("EP");
      ep.setAirDate(SDF.parse("2000-01-28"));
      ep.setSeasonNum(1);
      ep.setEpisodeNum(2);

      Assert.assertTrue(ep.equals(otherEp));
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

      ep.setTitle("EP");
      ep.setAirDate(SDF.parse("2000-01-28"));
      ep.setSeasonNum(1);
      ep.setEpisodeNum(2);

      Assert.assertFalse(ep.equals(otherEp));
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

      ep.setTitle("EP");
      ep.setAirDate(SDF.parse("2000-01-28"));
      ep.setSeasonNum(1);
      ep.setEpisodeNum(2);

      Assert.assertFalse(ep.equals(otherEp));
    }

    @Test
    public void hashCodeTest() throws ParseException{
      ep.setTitle("EP");
      ep.setAirDate(SDF.parse("2000-01-28"));
      ep.setSeasonNum(1);
      ep.setEpisodeNum(2);  
		
      Assert.assertEquals(-259693325, ep.hashCode());
    }
    

    @Test
    public void toStringTest() throws ParseException{
      ep.setTitle("EP");
      ep.setAirDate(SDF.parse("2000-01-28"));
      ep.setSeasonNum(1);
      ep.setEpisodeNum(2);  
		
      Assert.assertEquals("GuideEpisode{title=EP, airDate=Fri Jan 28 00:00:00 UTC 2000, seasonNum=1, episodeNum=2}", ep.toString());
    }
}
