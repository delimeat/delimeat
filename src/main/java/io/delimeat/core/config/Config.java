package io.delimeat.core.config;

import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"outputDirectory", "preferFiles", "ignoreFolders", "ignoredFileTypes","excludedKeywords", "searchInterval", "searchDelay"})
public class Config {

    private String outputDirectory;
    private int searchInterval = 4 * 60 * 60 * 1000; // default to 4 hours
    private int searchDelay = 60 * 60 * 1000; // default to 1 hour
    private boolean preferFiles = true;
    private boolean ignoreFolders = false;
    @XmlElement(name = "fileType")
    @XmlElementWrapper(name = "ignoredFileTypes")
    private List<String> ignoredFileTypes = new ArrayList<String>();
    @XmlElement(name = "keyword")
    @XmlElementWrapper(name = "excludedKeywords")
  	 private List<String> excludedKeywords = new ArrayList<String>();

    public int getSearchInterval() {
        return searchInterval;
    }

    public void setSearchInterval(int searchInterval) {
        this.searchInterval = searchInterval;
    }

    public int getSearchDelay() {
        return searchDelay;
    }

    public void setSearchDelay(int searchDelay) {
        this.searchDelay = searchDelay;
    }

    public boolean isPreferFiles() {
        return preferFiles;
    }

    public void setPreferFiles(boolean preferFiles) {
        this.preferFiles = preferFiles;
        if (!preferFiles) {
            setIgnoreFolders(false);
        }
    }


    public boolean isIgnoreFolders() {
        return ignoreFolders;
    }

    public void setIgnoreFolders(boolean ignoreFolders) {
        this.ignoreFolders = ignoreFolders;
        if (ignoreFolders) {
            setPreferFiles(true);
        }
    }

    public void setIgnoredFileTypes(List<String> ignoredFileTypes) {
        this.ignoredFileTypes = ignoredFileTypes;

    }

    public List<String> getIgnoredFileTypes() {
        return ignoredFileTypes;
    }
  
  	 public void setExcludedKeywords(List<String> excludedKeywords){
      	this.excludedKeywords = excludedKeywords;
    }
  	 
    public List<String> getExcludedKeywords(){
      	return excludedKeywords;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
          				.add("outputDirectory", outputDirectory)
          				.add("searchInterval", searchInterval)
                     .add("searchDelay", searchDelay)
          				.add("preferFiles", preferFiles)
          				.add("ignoreFolders", ignoreFolders)
                     .add("ignoredFileTypes", ignoredFileTypes)
                     .add("excludedKeywords", excludedKeywords)
          				.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }

        if (this == object) {
            return true;
        }

        if (object instanceof Config) {
            Config other = (Config)object;
            return Objects.equals(this.outputDirectory, other.outputDirectory)
              				&& Objects.equals(this.searchInterval, other.searchInterval)
                   		&& Objects.equals(this.searchDelay, other.searchDelay)
              				&& Objects.equals(this.preferFiles, other.preferFiles)
                   		&& Objects.equals(this.ignoreFolders, other.ignoreFolders)
                   		&& Objects.equals(this.ignoredFileTypes, other.ignoredFileTypes)
          					&& Objects.equals(this.excludedKeywords, other.excludedKeywords);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(outputDirectory, searchInterval, searchDelay, preferFiles, ignoreFolders, ignoredFileTypes, excludedKeywords);
    }


}
