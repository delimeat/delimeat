package io.delimeat.core.guide;

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
		return "TVDBApiKey [value=" + value + "]";
	}
}
