/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.framework;

import com.valloc.Constants;
import com.valloc.Priority;

/**
 *
 *
 * @author wstevens
 */
public class DesktopFrameworkComponentFactory extends AbstractFrameworkComponentFactory
{
	private DesktopFrameworkManager frameworkManager;

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
//	public FrameworkManager getFrameworkManager() {\
	public <FM extends FrameworkManager> FM getFrameworkManager() {
		@SuppressWarnings(Constants.UNCHECKED)
		final FM retval = (FM) getDesktopFrameworkManager();
		return retval;
	}

//	@Override
//	public FrameworkManager getFrameworkManager() {
//		return getDesktopFrameworkManager();
//	}

	public DesktopFrameworkManager getDesktopFrameworkManager() {
		if (frameworkManager == null) {
			throw new NullPointerException("Framework manager should be initialized by now.");
		}
		return frameworkManager;
	}

	DesktopFrameworkManager newFrameworkManager() {
		final DesktopFrameworkManager frameworkManager = new DesktopFrameworkManagerBase();
		frameworkManager.setControllerComponentFactory(getControllerComponentFactory());
		frameworkManager.setRequestContainer(getRequestContainer());
		return frameworkManager;
	}
}
