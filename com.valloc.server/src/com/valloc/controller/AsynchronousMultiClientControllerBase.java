/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller;

import com.valloc.domain.system.ClientNode;
import com.valloc.interrupt.InterruptType;
import com.valloc.util.Result;

/**
 *
 *
 * @author wstevens
 */
final class AsynchronousMultiClientControllerBase<C extends ClientNode> extends AbstractMultiClientController<C> implements AsynchronousMultiClientController<C>
{

	@Override
	public void submitAsync() {
	}

	@Override
	public void blockForCompletion() {
	}

	@Override
	public boolean isBlocking() {
		return false;
	}

	@Override
	public void signalCompletion() {
	}

	@Override
	public void handleInterrupt(final InterruptType interruptType, final Result result) {
	}

}
