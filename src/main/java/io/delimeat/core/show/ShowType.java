package io.delimeat.core.show;

public enum ShowType {

    UNKNOWN(0), 
    ANIMATED(1), 
    DAILY(2), 
    MINI_SERIES(3),
    SEASON(4);

    private final int value;

    private ShowType(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }
}
