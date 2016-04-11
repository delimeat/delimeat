package io.delimeat.core.config;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"outputDirectory", "preferFiles", "ignoreFolders", "ignoredFileTypes", "searchInterval"})
public class Config implements Comparable<Config>{

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
     	return MoreObjects.toStringHelper(this)
              .add("outputDirectory", outputDirectory)
              .add("searchInterval", searchInterval)  
              .add("preferFiles", preferFiles)
              .add("ignoreFolders", ignoreFolders)
              .add("ignoredFileTypes", ignoredFileTypes)
              .toString();
	}
  
    @Override
    public boolean equals(Object object)
    {
      if(object ==null){
        return false;
      }
      
      if(this == object){
        return true;
      }
      
      if (object instanceof Config)
      {
        Config otherConfig = (Config)object;
        return this.compareTo(otherConfig) == 0 ? true : false;
      }
      return false;
    }
  
  @Override
  public int compareTo(Config other){
		return ComparisonChain.start()
                 .compare(this.outputDirectory, other.outputDirectory, Ordering.natural().nullsFirst())
                 .compare(this.searchInterval, other.searchInterval, Ordering.natural().nullsFirst())
                 .compareFalseFirst(this.preferFiles, other.preferFiles)
                 .compareFalseFirst(this.ignoreFolders, other.ignoreFolders)
                 .compare(this.ignoredFileTypes, other.ignoredFileTypes, Ordering.<String>natural().lexicographical().nullsFirst())
                 .result();    
  }

  @Override 
  public int hashCode() {
    return Objects.hash(outputDirectory, searchInterval, preferFiles, ignoreFolders, ignoredFileTypes);
  }


}
