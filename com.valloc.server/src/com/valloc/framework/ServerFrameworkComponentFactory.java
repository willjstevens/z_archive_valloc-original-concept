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
public class ServerFrameworkComponentFactory extends AbstractFrameworkComponentFactory
{

	private ServerFrameworkManager frameworkManager;

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
		return getServerFrameworkManager();
	}

	public ServerFrameworkManager getServerFrameworkManager() {
		if (frameworkManager == null) {
			throw new NullPointerException("Framework manager should be initialized by now.");
		}
		return frameworkManager;
	}
	
	ServerFrameworkManager newFrameworkManager() {
		final ServerFrameworkManager frameworkManager = new ServerFrameworkManagerBase();
		frameworkManager.setControllerComponentFactory(getControllerComponentFactory());
		frameworkManager.setRequestContainer(getRequestContainer());
		return frameworkManager;
	}
}
