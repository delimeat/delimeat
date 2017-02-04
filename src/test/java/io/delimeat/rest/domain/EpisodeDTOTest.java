package io.delimeat.rest.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.core.show.Episode;
import io.delimeat.core.show.EpisodeStatus;
import io.delimeat.core.show.Show;
import io.delimeat.core.show.ShowType;

public class EpisodeDTOTest {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
    static {
        SDF.setTimeZone(TimeZone.getTimeZone("UTC"));
    }
    
	private EpisodeDTO dto;
	
	@Before
	public void setUp() throws Exception {
		dto = new EpisodeDTO();
	}
	
    @Test
    public void episodeConstructorTest() throws Exception{
		Show show = new Show(2L,0,"UTC","GUIDE_ID","TITLE",true,ShowType.ANIMATED,new Date(),new Date(),new Date(),new Date(),true,false,0,100,101);
		Episode ep = new Episode(1L,"TITLE",SDF.parse("1970-01-01"),2,3,false,EpisodeStatus.FOUND,99,show);
		
        dto = new EpisodeDTO(ep);
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
