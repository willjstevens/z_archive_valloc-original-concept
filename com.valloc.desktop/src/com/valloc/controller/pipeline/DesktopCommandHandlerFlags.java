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
public class DesktopCommandHandlerFlags extends CommandHandlerFlags
{
	private boolean isPreferences;
	
	public DesktopCommandHandlerFlags(final CommandHandlerFlagsKey key) {
		super(key);
	}
	
	public DesktopCommandHandlerFlags setPreferences(final boolean isPreferences) {
		this.isPreferences = isPreferences;
		return this;
	}

	public boolean isPreferences() {
		return isPreferences;
	}
}
