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
public class ServerCommandHandlerFlags extends CommandHandlerFlags
{
	private boolean isDatabase;
	private boolean isLicensed;
	
	public ServerCommandHandlerFlags(final CommandHandlerFlagsKey key) {
		super(key);
	}
	
	public ServerCommandHandlerFlags setDatabase(final boolean isDatabase) {
		this.isDatabase = isDatabase;
		return this;
	}
	
	public ServerCommandHandlerFlags setLicensed(final boolean isLicensed) {
		this.isLicensed = isLicensed;
		return this;
	}

	public boolean isDatabase() {
		return isDatabase;
	}

	public boolean isLicensed() {
		return isLicensed;
	}
}
