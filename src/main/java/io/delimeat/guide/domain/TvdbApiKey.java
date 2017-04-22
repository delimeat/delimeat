package io.delimeat.guide.domain;

import java.util.Objects;

import com.google.common.base.MoreObjects;

public class TvdbApiKey {

	private String value;

	/**
	 * Set the API Key value
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the api key value
	 */
	public String getValue() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
							.add("value", value)
							.omitNullValues()
							.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
}
