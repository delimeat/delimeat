package io.delimeat.core.feed;

public enum FeedProcessorStatus{
	PENDING,
	STARTED,
	ENDED_SUCCESSFUL,
	ENDED_UNSUCCESSFUL,
	ENDED_ERROR,
	ENDED_ABORT
}
