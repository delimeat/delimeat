package io.delimeat.util;

import java.util.List;

import com.google.common.base.MoreObjects;

public class PaginatedResults<T> {

	private final List<T> results;
	private final long count;
	
	/**
	 * Constructor
	 * 
	 * @param results
	 * @param count
	 */
	public PaginatedResults(List<T> results, long count){
		this.results = results;
		this.count = count;
	}
	/**
	 * @return the results
	 */
	public List<T> getResults() {
		return results;
	}

	/**
	 * @return the count
	 */
	public long getCount() {
		return count;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("results", results)
				.add("count", count)  
				.toString();
	}
	
	
	
}
