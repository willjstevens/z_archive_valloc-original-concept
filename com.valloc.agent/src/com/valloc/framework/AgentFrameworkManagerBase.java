/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.framework;

import com.valloc.interrupt.InterruptTracker;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public class AgentFrameworkManagerBase extends AbstractFrameworkManager implements AgentFrameworkManager
{


	@Override
	public InterruptTracker createAndRegisterInterruptTracker() {
		final UniqueId id = new UniqueId();
		return createAndRegisterInterruptTracker(id);
	}

}
