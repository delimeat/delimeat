package io.delimeat.guide.domain;

import com.google.common.base.MoreObjects;

public class GuideError {

	private String message;

	/**
	 * Set the error message
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Get the error message
	 * 
	 * @return error message
	 */
	public String getMessage() {
		return message;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
	      return MoreObjects.toStringHelper(this)
	    	        .add("message", message)
	    	        .omitNullValues()
	    	        .toString();
	}

}
