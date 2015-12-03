package io.delimeat.core.show;

import io.delimeat.util.jaxb.ShowGuideSourcePKAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class ShowGuideSource {

	@XmlElement(name = "guideSource")
	@XmlJavaTypeAdapter(value = ShowGuideSourcePKAdapter.class)
	private ShowGuideSourcePK id;
	private String guideId;
	private int version;

	@XmlTransient
	private Show show;

	/**
	 * @return the id
	 */
	public ShowGuideSourcePK getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(ShowGuideSourcePK id) {
		this.id = id;
	}

	/**
	 * @return the show
	 */
	public Show getShow() {
		return show;
	}

	/**
	 * @param show
	 *            the show to set
	 */
	public void setShow(Show show) {
		this.show = show;
	}

	/**
	 * @return the guideId
	 */
	public String getGuideId() {
		return guideId;
	}

	/**
	 * @param guideId
	 *            the guideId to set
	 */
	public void setGuideId(String guideId) {
		this.guideId = guideId;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ShowGuideSource [pk=" + id + ", guideId=" + guideId + ", version=" + version + "]";
	}

}
