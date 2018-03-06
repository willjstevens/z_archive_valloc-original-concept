/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc;


/**
 * 
 * 
 * @author wstevens
 */
public abstract class AbstractComponentFactory implements ComponentFactory
{
	private boolean isInitialized;

	protected void setInitialized(final boolean isInitialized) {
		this.isInitialized = isInitialized;
	}

	protected boolean isInitialized() {
		return isInitialized;
	}

	protected void validate() throws IllegalStateException {
		if (!isInitialized) {
			throw new IllegalStateException(getClass().getSimpleName() + " has not been initialized for use.");
		}
	}
}
