package io.delimeat.core.guide;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import io.delimeat.core.show.Episode;
import io.delimeat.util.jaxb.TvdbDateAdapter;

public class GuideEpisode implements Comparable<GuideEpisode> {

	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

	@XmlJavaTypeAdapter(value=TvdbDateAdapter.class)
	private Date airDate = null;
	private Integer seasonNum = 0;
	private Integer episodeNum = 0;
	private Integer productionNum = 0;
	private String title;

	/**
	 * 
	 * @return Air date for the episode
	 */
	public Date getAirDate() {
		return airDate;
	}

	/**
	 * @return Episode number for the episode
	 */
	public Integer getEpisodeNum() {
		return episodeNum;
	}

	/**
	 * @return Production Number for the episode
	 */
	public Integer getProductionNum() {
		return productionNum;
	}

	/**
	 * @return Season number for the episode
	 */
	public Integer getSeasonNum() {
		return seasonNum;
	}

	/**
	 * @return Title of the episode
	 */
	public String getTitle() {
		return title;
	}

	public void setAirDate(Date airDate) {
		this.airDate = airDate;
	}

	public void setSeasonNum(Integer seasonNum) {
		this.seasonNum = seasonNum;
	}

	public void setEpisodeNum(Integer episodeNum) {
		this.episodeNum = episodeNum;
	}

	public void setProductionNum(Integer productionNum) {
		this.productionNum = productionNum;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public int compareTo(GuideEpisode other) {
		if (this.getAirDate() != null && other.getAirDate() != null) {
			Integer dateCompare = this.getAirDate().compareTo(other.getAirDate());
			if (dateCompare == 0) {
				final Integer thisSeasNum = this.getSeasonNum() != null ? this.getSeasonNum() : new Integer(0);
				final Integer otherSeasNum = other.getSeasonNum() != null ? other.getSeasonNum() : new Integer(0);
				final Integer thisEpNum = this.getEpisodeNum() != null ? this.getEpisodeNum() : new Integer(0);
				final Integer otherEpNum = other.getEpisodeNum() != null ? other.getEpisodeNum() : new Integer(0);
				if ( thisSeasNum > 0 &&  otherSeasNum > 0 && thisEpNum > 0
						&& otherEpNum > 0) {
					Integer seasonCompare = thisSeasNum.compareTo(otherSeasNum);
					if (seasonCompare == 0) {
						Integer episodeCompare = thisEpNum.compareTo(otherEpNum);
						return episodeCompare;
					} else {
						return seasonCompare;
					}
				} else {
					Integer thisProdNum = this.getProductionNum() != null ? this.getProductionNum() : new Integer(0);
					Integer otherProdNum = other.getProductionNum() != null ? other.getProductionNum() : new Integer(0);
					Integer productionCompare = thisProdNum.compareTo(otherProdNum);
					return productionCompare;
				}
			} else {
				return dateCompare;
			}
		} else if (this.getAirDate() != null && other.getAirDate() == null) {
			return 1;
		} else if (this.getAirDate() == null && other.getAirDate() != null) {
			return -1;
		}
		return 0;
	}

	@Override
	public String toString() {
		return "Title: " + title + " date: " + (airDate != null ? SDF.format(airDate) : "") + " s " + seasonNum + " e "
				+ episodeNum + " p " + productionNum;
	}
  
 	@Override
  	public boolean equals(Object object)
  	{
     boolean equal = false;
     if (object != null && object instanceof Episode)
     {
       Episode otherEp = (Episode)object;
       equal = (seasonNum == otherEp.getSeasonNum() && episodeNum == otherEp.getEpisodeNum());
     }else if (object != null && object instanceof GuideEpisode){
       GuideEpisode otherEp = (GuideEpisode)object;
       equal = (seasonNum == otherEp.getSeasonNum() && episodeNum == otherEp.getEpisodeNum());
     }
     return equal;
  }
}
