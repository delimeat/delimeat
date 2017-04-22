package io.delimeat.client.domain;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.show.domain.Episode;
import io.delimeat.show.domain.EpisodeStatus;
import io.delimeat.show.domain.Show;
import io.delimeat.show.domain.ShowType;

public class EpisodeDtoTest {
    
	private EpisodeDto dto;
	
	@Before
	public void setUp() throws Exception {
		dto = new EpisodeDto();
	}
	
    @Test
    public void episodeConstructorTest() throws Exception{
		Show show = new Show();
		show.setShowId(2L);
		show.setAirTime(LocalTime.MIDNIGHT);
		show.setTimezone("UTC");
		show.setGuideId("GUIDE_ID");
		show.setTitle("TITLE");
		show.setAiring(true);
		show.setShowType(ShowType.ANIMATED);
		show.setLastGuideCheck(Instant.EPOCH);
		show.setLastGuideUpdate(Instant.EPOCH);
		show.setEnabled(true);
		show.setMinSize(0);
		show.setMaxSize(100);
		show.setVersion(101);
		
		Episode ep = new Episode();
		ep.setEpisodeId(1L);
		ep.setTitle("TITLE");
		ep.setAirDate(LocalDate.parse("1970-01-01"));
		ep.setSeasonNum(2);
		ep.setEpisodeNum(3);
		ep.setDoubleEp(false);
		ep.setStatus(EpisodeStatus.FOUND);
		ep.setLastFeedCheck(Instant.now());
		ep.setLastFeedUpdate(Instant.now());
		ep.setVersion(99);
		ep.setShow(show);
		
        dto = new EpisodeDto(ep);
		Assert.assertEquals("episodeId",1L, dto.getEpisodeId());
		Assert.assertEquals("title","TITLE", dto.getTitle());
		Assert.assertEquals("airDateTime",Instant.EPOCH, dto.getAirDateTime());
		Assert.assertEquals("seasonNum",2, dto.getSeasonNum());
		Assert.assertEquals("episodeNum",3, dto.getEpisodeNum());
		Assert.assertEquals("doubleEp",false, dto.isDoubleEp());
		Assert.assertEquals("status",EpisodeStatus.FOUND, dto.getStatus());
		Assert.assertEquals("version",99, dto.getVersion());
    }	

	@Test
	public void episodeIdTest(){
		Assert.assertEquals(0, dto.getEpisodeId());
		dto.setEpisodeId(99);
		Assert.assertEquals(99, dto.getEpisodeId());
	}
	

    @Test
    public void titleTest() {
        Assert.assertNull(dto.getTitle());
        dto.setTitle("TITLE");
        Assert.assertEquals("TITLE", dto.getTitle());
    }

    @Test
    public void airDateTimeTest() throws ParseException {
        Assert.assertNull(dto.getAirDateTime());
        dto.setAirDateTime(Instant.MIN);
        Assert.assertEquals(Instant.MIN, dto.getAirDateTime());
    }

    @Test
    public void seasonNumTest() {
        Assert.assertEquals(0, dto.getSeasonNum());
        dto.setSeasonNum(Integer.MIN_VALUE);
        Assert.assertEquals(Integer.MIN_VALUE, dto.getSeasonNum());
    }

    @Test
    public void episodeNumTest() {
        Assert.assertEquals(0, dto.getEpisodeNum());
        dto.setEpisodeNum(Integer.MIN_VALUE);
        Assert.assertEquals(Integer.MIN_VALUE, dto.getEpisodeNum());
    }
    
    @Test
    public void doubleEpisodeTest() {
        Assert.assertFalse(dto.isDoubleEp());
        dto.setDoubleEp(true);
        Assert.assertTrue(dto.isDoubleEp());
    }
    

    public void statusTest() {
        Assert.assertEquals(EpisodeStatus.PENDING, dto.getStatus());
        dto.setStatus(EpisodeStatus.SKIPPED);
        Assert.assertEquals(EpisodeStatus.SKIPPED, dto.getStatus());
    }

    @Test
    public void versionTest() {
        Assert.assertEquals(0, dto.getVersion());
        dto.setVersion(Integer.MAX_VALUE);
        Assert.assertEquals(Integer.MAX_VALUE, dto.getVersion());
    }  

    @Test
    public void hashTest() {
        dto.setEpisodeId(Long.MIN_VALUE);
        dto.setVersion(Integer.MIN_VALUE);

        Assert.assertEquals(961, dto.hashCode());
    }


}
