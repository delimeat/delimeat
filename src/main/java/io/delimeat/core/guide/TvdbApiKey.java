package io.delimeat.core.guide;

import java.util.Objects;

import com.google.common.base.MoreObjects;

public class TvdbApiKey {

	private String value;

	public void setValue(String value) {
		this.value = value;
	}

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
        .toString();
	}

    @Override 
    public int hashCode() {
      return Objects.hash(value);
    }
}
