package io.delimeat.config.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;

@Entity
public class Config {

	@Id
	@Column(name="CONFIG_ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CONFIG_SEQ")
	@TableGenerator(name="CONFIG_SEQ", table="SEQUENCE", pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT", pkColumnValue="CONFIG_SEQ")
	@JsonIgnore
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
	
	@ElementCollection
	@CollectionTable(name="IGNORED_FILE_TYPES", joinColumns=@JoinColumn(name="CONFIG_ID"))
	@Column(name="FILE_TYPE")
	private List<String> ignoredFileTypes = new ArrayList<String>();
	
	@ElementCollection
	@CollectionTable(name="EXCLUDED_KEYWORDS", joinColumns=@JoinColumn(name="CONFIG_ID"))
	@Column(name="KEYWORD")
	private List<String> excludedKeywords = new ArrayList<String>();

	@Version
	@JsonIgnore
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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("configId", configId)
				.add("outputDirectory", outputDirectory)
				.add("searchInterval", searchInterval)
				.add("searchDelay", searchDelay)
				.add("preferFiles", preferFiles)
				.add("ignoreFolders", ignoreFolders)
				.add("ignoredFileTypes", ignoredFileTypes)
				.add("excludedKeywords", excludedKeywords)
				.add("version", version)
				.omitNullValues()
				.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (object == null) {
			return false;
		}

		if (this == object) {
			return true;
		}

		if (object instanceof Config) {
			Config other = (Config) object;
			return Objects.equals(this.configId, other.configId)
					&& Objects.equals(this.outputDirectory, other.outputDirectory)
					&& Objects.equals(this.searchInterval, other.searchInterval)
					&& Objects.equals(this.searchDelay, other.searchDelay)
					&& Objects.equals(this.preferFiles, other.preferFiles)
					&& Objects.equals(this.ignoreFolders, other.ignoreFolders)
					&& Objects.equals(this.ignoredFileTypes, other.ignoredFileTypes)
					&& Objects.equals(this.excludedKeywords, other.excludedKeywords)
					&& Objects.equals(this.version, other.version);
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(configId, version);
	}

}
