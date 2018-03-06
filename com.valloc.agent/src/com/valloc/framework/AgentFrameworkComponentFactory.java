/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.framework;

import com.valloc.Priority;


/**
 *
 *
 * @author wstevens
 */
public class AgentFrameworkComponentFactory extends AbstractFrameworkComponentFactory
{
	private AgentFrameworkManager frameworkManager;

	@Override
	public void initialize() {
		frameworkManager = newFrameworkManager();
		
		setInitialized(true);
	}

	@Override
	public Priority getNodeDefaultPriority() {
		return Priority.USER_STANDARD;
	}

	@Override
	public FrameworkManager getFrameworkManager() {
		return getAgentFrameworkManager();
	}

	public AgentFrameworkManager getAgentFrameworkManager() {
		if (frameworkManager == null) {
			throw new NullPointerException("Framework manager should be initialized by now.");
		}
		return frameworkManager;
	}
	
	AgentFrameworkManager newFrameworkManager() {
		final AgentFrameworkManager frameworkManager = new AgentFrameworkManagerBase();
		frameworkManager.setControllerComponentFactory(getControllerComponentFactory());
		frameworkManager.setRequestContainer(getRequestContainer());
		return frameworkManager;
	}
}
