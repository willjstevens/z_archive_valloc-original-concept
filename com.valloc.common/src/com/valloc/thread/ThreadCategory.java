/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.thread;

/**
 *
 *
 * @author wstevens
 */
public enum ThreadCategory
{
	FRAMEWORK_REQUEST						(25),
	FRAMEWORK_REQUEST_COMPLETION_SERVICE	(1),
	REQUEST_REJECT							(10),
	BACKGROUND_UTILITY						(5),
	QUEUE_MESSAGE_SPINNER					(5);
	
	private int expectedThreadCount;
	
	private ThreadCategory(final int expectedThreadCount) {
		this.expectedThreadCount = expectedThreadCount;
	}
	
	public int expectedThreadCount() {
		return expectedThreadCount;
	}
}
