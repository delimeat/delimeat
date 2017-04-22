package io.delimeat.feed.domain;

public enum FeedSource {

    KAT(0),
    TORRENTPROJECT(1),
    LIMETORRENTS(2),
    EXTRATORRENT(3),
    BITSNOOP(4),
    TORRENTDOWNLOADS(5),
    ZOOQLE(6);

    private final int value;

    private FeedSource(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
