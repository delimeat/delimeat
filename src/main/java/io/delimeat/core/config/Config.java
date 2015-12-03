package io.delimeat.core.config;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"outputDirectory", "preferFiles", "ignoreFolders", "ignoredFileTypes", "searchInterval"})
public class Config {

	private String outputDirectory; 
	private int searchInterval = 60*60*1000; // default to 1 hour
	private boolean preferFiles = true;
	private boolean ignoreFolders = false;
	@XmlElement(name="fileType")
	@XmlElementWrapper(name="ignoredFileTypes")
	private List<String> ignoredFileTypes = new ArrayList<String>();

	public int getSearchInterval() {
		return searchInterval;
	}

	public void setSearchInterval(int searchInterval) {
		this.searchInterval = searchInterval;
	}


	public boolean isPreferFiles() {
		return preferFiles;
	}

	public void setPreferFiles(boolean preferFiles) {
		this.preferFiles = preferFiles;
		if(!preferFiles){
			setIgnoreFolders(false);
		}
	}


	public boolean isIgnoreFolders() {
		return ignoreFolders;
	}

	public void setIgnoreFolders(boolean ignoreFolders) {
		this.ignoreFolders = ignoreFolders;
		if(ignoreFolders){
			setPreferFiles(true);
		}
	}

	public void setIgnoredFileTypes(List<String> ignoredFileTypes) {
		this.ignoredFileTypes = ignoredFileTypes;
		
	}

	public List<String> getIgnoredFileTypes() {
		return ignoredFileTypes;
	}

	public String getOutputDirectory() {
		return outputDirectory;
	}

	public void setOutputDirectory(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	@Override
	public String toString() {
		return "Config [outputDirectory=" + outputDirectory
				+ ", searchInterval=" + searchInterval + ", preferFiles="
				+ preferFiles + ", ignoreFolders=" + ignoreFolders
				+ ", ignoredFileTypes=" + ignoredFileTypes + "]";
	}
}
