package io.delimeat.core.processor;

public enum FeedProcessorStatus{
	PENDING,
	STARTED,
	ENDED_SUCCESSFUL,
	ENDED_UNSUCCESSFUL,
	ENDED_ERROR,
	ENDED_ABORT
}
