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
final class SynchronousMultiClientControllerBase<C extends ClientNode> extends AbstractMultiClientController<C> implements SynchronousMultiClientController<C>
{

	@Override
	public void submitSync() {

	}

	@Override
	public void handleInterrupt(final InterruptType interruptType, final Result result) {
	}

}
