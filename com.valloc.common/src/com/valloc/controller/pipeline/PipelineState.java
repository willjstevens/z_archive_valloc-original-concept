/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller.pipeline;

/**
 *
 *
 * @author wstevens
 */
class PipelineState
{
	private boolean isComplete;

	boolean isComplete() {
		return isComplete;
	}

	void setComplete(final boolean isComplete) {
		this.isComplete = isComplete;
	}
}
