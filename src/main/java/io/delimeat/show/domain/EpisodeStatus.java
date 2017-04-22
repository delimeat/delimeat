package io.delimeat.show.domain;

public enum EpisodeStatus {
	
    PENDING(0),
    FOUND(1),
    SKIPPED(2);

    private final int value;

    private EpisodeStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
