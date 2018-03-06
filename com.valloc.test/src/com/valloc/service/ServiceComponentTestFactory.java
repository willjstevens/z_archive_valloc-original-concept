/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.service;

import com.valloc.TestLevelSupport;
import com.valloc.concurrent.request.RequestContainerComponentTestFactory;
import com.valloc.controller.ControllerComponentTestFactory;
import com.valloc.framework.AbstractTestFrameworkComponentFactory;
import com.valloc.framework.ComponentTestStrategy;
import com.valloc.framework.FrameworkComponentTestFactory;
import com.valloc.session.SessionComponentTestFactory;

/**
 *
 *
 * @author wstevens
 */
public class ServiceComponentTestFactory extends AbstractTestFrameworkComponentFactory
{
	public boolean hasServerSupport;
	public boolean hasDesktopSupport;
	public TestLevelSupport serviceLevelSupport;
	private ComponentTestStrategy testStrategy;

	private ServerServiceComponentFactory serverServiceComponentFactory;
	private DesktopServiceComponentFactoryBase desktopServiceComponentFactory;

	@Override
	public void bootstrap() {
		switch (serviceLevelSupport) {
//		case FAKE:		testStrategy = new FakeSessionSupport();	break;
//		case CUSTOM:	testStrategy = new CustomSessionSupport();	break;
		default: 		testStrategy = new FullSessionSupport();
		}
		testStrategy.bootstrap();
	}

	@Override
	public void initialize() {
		testStrategy.initialize();
	}

	private class FullSessionSupport implements ComponentTestStrategy {
		@Override public void bootstrap() {
			if (hasServerSupport) {
				serverServiceComponentFactory = new ServerServiceComponentFactory();
			}
			if (hasDesktopSupport) {
				desktopServiceComponentFactory = new DesktopServiceComponentFactoryBase();
			}
		}

		@Override public void initialize() {
			if (hasServerSupport) {
				serverServiceComponentFactory.initialize();
			}
			if (hasDesktopSupport) {
				desktopServiceComponentFactory.initialize();
			}
		}
	}

	public ServerServiceComponentFactory getServerServiceComponentFactory() {
		return serverServiceComponentFactory;
	}

	public DesktopServiceComponentFactoryBase getDesktopServiceComponentFactory() {
		return desktopServiceComponentFactory;
	}

	public void setControllerComponentTestFactory(final ControllerComponentTestFactory controllerComponentTestFactory) {
		if (hasDesktopSupport) {
			desktopServiceComponentFactory.setControllerComponentFactory(controllerComponentTestFactory.getDesktopControllerComponentFactory());
		}
		if (hasServerSupport) {
			serverServiceComponentFactory.setControllerComponentFactory(controllerComponentTestFactory.getServerControllerComponentFactory());
		}
	}

	public void setFrameworkComponentTestFactory(final FrameworkComponentTestFactory frameworkComponentTestFactory) {
		if (hasDesktopSupport) {
			desktopServiceComponentFactory.setFrameworkComponentFactory(frameworkComponentTestFactory.getDesktopFrameworkComponentFactory());
		}
		if (hasServerSupport) {
			serverServiceComponentFactory.setFrameworkComponentFactory(frameworkComponentTestFactory.getServerFrameworkComponentFactory());
		}
	}

	public void setSessionComponentTestFactory(final SessionComponentTestFactory sessionComponentTestFactory) {
		if (hasDesktopSupport) {
			desktopServiceComponentFactory.setSessionComponentFactory(sessionComponentTestFactory.getDesktopSessionComponentFactory());
		}
		if (hasServerSupport) {
			serverServiceComponentFactory.setSessionComponentFactory(sessionComponentTestFactory.getServerSessionComponentFactory());
		}
	}

	public void setRequestContainer(final RequestContainerComponentTestFactory requestContainerComponentTestFactory) {
		if (hasDesktopSupport) {
			desktopServiceComponentFactory.setRequestContainer(requestContainerComponentTestFactory.getDesktopRequestContainer());
		}
		if (hasServerSupport) {
			serverServiceComponentFactory.setRequestContainer(requestContainerComponentTestFactory.getServerRequestContainer());
		}
	}
}
