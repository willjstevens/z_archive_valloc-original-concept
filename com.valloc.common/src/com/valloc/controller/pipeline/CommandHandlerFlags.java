/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller.pipeline;

import com.valloc.Identifiable;


/**
 *
 *
 * @author wstevens
 */
public class CommandHandlerFlags implements Identifiable<CommandHandlerFlagsKey>
{
	private final CommandHandlerFlagsKey key;
	private boolean isTransact;
	private boolean isConverse;
	
	public CommandHandlerFlags(final CommandHandlerFlagsKey key) {
		this.key = key;
	}
	
	@Override
	public CommandHandlerFlagsKey id() {
		return key;
	}

	public CommandHandlerFlags setTransact(final boolean isTransact) {
		this.isTransact = isTransact;
		return this;
	}

	public CommandHandlerFlags setConverse(final boolean isConverse) {
		this.isConverse = isConverse;
		return this;
	}

	public boolean isTransact() {
		return isTransact;
	}

	public boolean isConverse() {
		return isConverse;
	}
}
