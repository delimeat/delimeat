package io.delimeat.core.guide;

import com.google.common.base.MoreObjects;

public class GuideIdentifier {

	private GuideSource source;
	private String value;

	/**
	 * @return the source
	 */
	public GuideSource getSource() {
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(GuideSource source) {
		this.source = source;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
     	return MoreObjects.toStringHelper(this)
              .add("source", source)
              .add("value", value)  
              .toString();
	}
}
