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
package io.delimeat.guide.entity;

import java.time.LocalDate;
import java.util.Objects;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import io.delimeat.util.jaxb.LocalDateAdapter;

@XmlRootElement
public class GuideSearchResult {

	private String description;
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	private LocalDate firstAired;
	private String guideId;
	private String title;
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the firstAired
	 */
	public LocalDate getFirstAired() {
		return firstAired;
	}
	/**
	 * @param firstAired the firstAired to set
	 */
	public void setFirstAired(LocalDate firstAired) {
		this.firstAired = firstAired;
	}
	/**
	 * @return the guideId
	 */
	public String getGuideId() {
		return guideId;
	}
	/**
	 * @param guideId the guideId to set
	 */
	public void setGuideId(String guideId) {
		this.guideId = guideId;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(description, firstAired, guideId, title);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GuideSearchResult other = (GuideSearchResult) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (firstAired == null) {
			if (other.firstAired != null)
				return false;
		} else if (!firstAired.equals(other.firstAired))
			return false;
		if (guideId == null) {
			if (other.guideId != null)
				return false;
		} else if (!guideId.equals(other.guideId))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GuideSearchResult [" + (description != null ? "description=" + description + ", " : "")
				+ (firstAired != null ? "firstAired=" + firstAired + ", " : "")
				+ (guideId != null ? "guideId=" + guideId + ", " : "") + (title != null ? "title=" + title : "") + "]";
	}

}
