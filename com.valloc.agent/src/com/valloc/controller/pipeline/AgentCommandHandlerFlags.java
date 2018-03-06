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
public class AgentCommandHandlerFlags extends CommandHandlerFlags
{
	private boolean isPreferences;
	
	public AgentCommandHandlerFlags(final CommandHandlerFlagsKey key) {
		super(key);
	}
	
	public AgentCommandHandlerFlags setPreferences(final boolean isPreferences) {
		this.isPreferences = isPreferences;
		return this;
	}

	public boolean isPreferences() {
		return isPreferences;
	}
}
