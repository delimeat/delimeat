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
package io.delimeat.config.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OrderColumn;
import javax.persistence.Version;

@Entity
public class Config {

	@Id
	@Column(name="CONFIG_ID")
	private Long configId;
	
	@Column(name="OUTPUT_DIR",nullable=false)
	@Basic(optional=false)
	private String outputDirectory;
	
	@Column(name="SEARCH_INTERVAL",nullable=false)
	@Basic(optional=false)
	private int searchInterval;
	
	@Column(name="SEARCH_DELAY",nullable=false)
	@Basic(optional=false)
	private int searchDelay;
	
	@Column(name="PREFER_FILES",nullable=false)
	@Basic(optional=false)
	private boolean preferFiles;
	
	@Column(name="IGNORE_FOLDERS",nullable=false)
	@Basic(optional=false)
	private boolean ignoreFolders;
	
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name="IGNORED_FILE_TYPES", joinColumns=@JoinColumn(name="CONFIG_ID"))
	@Column(name="FILE_TYPE")
	@OrderColumn
	private List<String> ignoredFileTypes = new ArrayList<String>();
	
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name="EXCLUDED_KEYWORDS", joinColumns=@JoinColumn(name="CONFIG_ID"))
	@Column(name="KEYWORD")
	@OrderColumn
	private List<String> excludedKeywords = new ArrayList<String>();

	@Version
	private Long version;

	/**
	 * @return the configId
	 */
	public Long getConfigId() {
		return configId;
	}

	/**
	 * @param configId the configId to set
	 */
	public void setConfigId(Long configId) {
		this.configId = configId;
	}

	/**
	 * @return the outputDirectory
	 */
	public String getOutputDirectory() {
		return outputDirectory;
	}

	/**
	 * @param outputDirectory the outputDirectory to set
	 */
	public void setOutputDirectory(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	/**
	 * @return the searchInterval
	 */
	public int getSearchInterval() {
		return searchInterval;
	}

	/**
	 * @param searchInterval the searchInterval to set
	 */
	public void setSearchInterval(int searchInterval) {
		this.searchInterval = searchInterval;
	}

	/**
	 * @return the searchDelay
	 */
	public int getSearchDelay() {
		return searchDelay;
	}

	/**
	 * @param searchDelay the searchDelay to set
	 */
	public void setSearchDelay(int searchDelay) {
		this.searchDelay = searchDelay;
	}

	/**
	 * @return the preferFiles
	 */
	public boolean isPreferFiles() {
		return preferFiles;
	}

	/**
	 * @param preferFiles the preferFiles to set
	 */
	public void setPreferFiles(boolean preferFiles) {
		this.preferFiles = preferFiles;
	}

	/**
	 * @return the ignoreFolders
	 */
	public boolean isIgnoreFolders() {
		return ignoreFolders;
	}

	/**
	 * @param ignoreFolders the ignoreFolders to set
	 */
	public void setIgnoreFolders(boolean ignoreFolders) {
		this.ignoreFolders = ignoreFolders;
	}

	/**
	 * @return the ignoredFileTypes
	 */
	public List<String> getIgnoredFileTypes() {
		return ignoredFileTypes;
	}

	/**
	 * @param ignoredFileTypes the ignoredFileTypes to set
	 */
	public void setIgnoredFileTypes(List<String> ignoredFileTypes) {
		this.ignoredFileTypes = ignoredFileTypes;
	}

	/**
	 * @return the excludedKeywords
	 */
	public List<String> getExcludedKeywords() {
		return excludedKeywords;
	}

	/**
	 * @param excludedKeywords the excludedKeywords to set
	 */
	public void setExcludedKeywords(List<String> excludedKeywords) {
		this.excludedKeywords = excludedKeywords;
	}

	/**
	 * @return the version
	 */
	public Long getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(Long version) {
		this.version = version;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(configId, version);
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
		Config other = (Config) obj;
		if (configId == null) {
			if (other.configId != null)
				return false;
		} else if (!configId.equals(other.configId))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Config [" + (configId != null ? "configId=" + configId + ", " : "")
				+ (outputDirectory != null ? "outputDirectory=" + outputDirectory + ", " : "") + "searchInterval="
				+ searchInterval + ", searchDelay=" + searchDelay + ", preferFiles=" + preferFiles + ", ignoreFolders="
				+ ignoreFolders + ", " + (ignoredFileTypes != null ? "ignoredFileTypes=" + ignoredFileTypes + ", " : "")
				+ (excludedKeywords != null ? "excludedKeywords=" + excludedKeywords + ", " : "")
				+ (version != null ? "version=" + version : "") + "]";
	}
	
}