/*
 * Copyright 2013-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.delimeat.show;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Test;

import io.delimeat.guide.domain.GuideEpisode;
import io.delimeat.show.ShowUtils;
import io.delimeat.show.domain.Episode;

public class ShowUtilsTest {
    
    @Test
    public void determineAirTimeLocalDateTest(){
    	//2016-12-27 22:00 EST = 2016-12-28 03:00 UTC
    	
    	Instant result = ShowUtils.determineAirTime(LocalDate.parse("2016-12-27"), LocalTime.parse("22:00"),"US/Eastern");
    	Assert.assertEquals(Instant.parse("2016-12-28T03:00:00Z"), result);
    	
    }
    

    @Test
    public void guideEpisodeConstructorTest() throws ParseException {
        GuideEpisode guideEp = new GuideEpisode();
        guideEp.setAirDate(LocalDate.ofEpochDay(0));
        guideEp.setEpisodeNum(2);
        guideEp.setSeasonNum(1);
        guideEp.setTitle("TITLE");
        Episode episode = ShowUtils.fromGuideEpisode(guideEp);
        Assert.assertEquals(0, episode.getEpisodeId());
        Assert.assertEquals(LocalDate.parse("1970-01-01"), episode.getAirDate());
        Assert.assertEquals(2, episode.getEpisodeNum());
        Assert.assertEquals(1, episode.getSeasonNum());
        Assert.assertEquals("TITLE", episode.getTitle());
        Assert.assertEquals(0, episode.getVersion());
    }
    
    @Test
    public void guideEpisodeNullSeasonEpisodeConstructorTest() throws ParseException {
        GuideEpisode guideEp = new GuideEpisode();
        guideEp.setAirDate(LocalDate.ofEpochDay(0));
        guideEp.setEpisodeNum(null);
        guideEp.setSeasonNum(null);
        guideEp.setTitle("TITLE");
        Episode episode = ShowUtils.fromGuideEpisode(guideEp);
        Assert.assertEquals(0, episode.getEpisodeId());
        Assert.assertEquals(LocalDate.parse("1970-01-01"), episode.getAirDate());
        Assert.assertEquals(0, episode.getEpisodeNum());
        Assert.assertEquals(0, episode.getSeasonNum());
        Assert.assertEquals("TITLE", episode.getTitle());
        Assert.assertEquals(0, episode.getVersion());
    }
   
}
