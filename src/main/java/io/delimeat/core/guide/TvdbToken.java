package io.delimeat.core.guide;

import java.util.Objects;

import com.google.common.base.MoreObjects;

public class TvdbToken {

    private long time;
    private String value;

    public TvdbToken() {
      time = System.currentTimeMillis();
    }

    /**
      * @return the value
      */
    public String getValue() {
      return value;
    }

    /**
      * @param time
      *            the time to set
      */
    public void setTime(long time) {
      this.time = time;
    }

    /**
      * @param value
      *            the value to set
      */
    public void setValue(String value) {
      this.value = value;
    }

    /**
      * @return the time
      */
    public long getTime() {
      return time;
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
        .add("time",time)
        .toString();
    }

    @Override 
    public int hashCode() {
      return Objects.hash(value,time);
    }
}
