package io.delimeat.core.feed;

public enum FeedSource {

    KAT(0),
    STRIKE(1);

    private final int value;

    private FeedSource(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
