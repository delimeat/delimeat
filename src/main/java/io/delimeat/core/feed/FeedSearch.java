package io.delimeat.core.feed;

import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FeedSearch {

	private List<FeedResult> results = new ArrayList<FeedResult>();

	public List<FeedResult> getResults() {
		return results;
	}

	public void setResults(List<FeedResult> results) {
		this.results = results;
	}

	@Override
	public String toString() {
     	return MoreObjects.toStringHelper(this)
              .add("results", results)  
              .toString();
	}

  @Override 
  public int hashCode() {
    return Objects.hash(results);
  }

}
