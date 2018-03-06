/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller;

import com.valloc.TestLevelSupport;
import com.valloc.framework.AbstractTestFrameworkComponentFactory;
import com.valloc.framework.ComponentTestStrategy;
import com.valloc.framework.FrameworkComponentTestFactory;
import com.valloc.service.ServiceComponentTestFactory;
import com.valloc.session.SessionComponentTestFactory;

/**
 *
 *
 * @author wstevens
 */
public class ControllerComponentTestFactory extends AbstractTestFrameworkComponentFactory
{
	public boolean hasServerSupport;
	public boolean hasDesktopSupport;
	public TestLevelSupport controllerLevelSupport;
	private ComponentTestStrategy testStrategy;

	private ServerControllerComponentFactoryBase serverControllerComponentFactory;
	private DesktopControllerComponentFactoryBase desktopControllerComponentFactory;

	@Override
	public void bootstrap() {
		switch (controllerLevelSupport) {
//		case FAKE:		testStrategy = new FakeSessionSupport();	break;
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
			if (hasDesktopSupport) {
				desktopControllerComponentFactory = new DesktopControllerComponentFactoryBase();
			}
			if (hasServerSupport) {
				serverControllerComponentFactory = new ServerControllerComponentFactoryBase();
			}
		}

		@Override public void initialize() {
			if (hasDesktopSupport) {
				desktopControllerComponentFactory.initialize();
			}
			if (hasServerSupport) {
				serverControllerComponentFactory.initialize();
			}
		}
	}

	public ServerControllerComponentFactoryBase getServerControllerComponentFactory() {
		return serverControllerComponentFactory;
	}

	public DesktopControllerComponentFactoryBase getDesktopControllerComponentFactory() {
		return desktopControllerComponentFactory;
	}

	public void setFrameworkComponentTestFactory(final FrameworkComponentTestFactory frameworkComponentTestFactory) {
		if (hasDesktopSupport) {
			desktopControllerComponentFactory.frameworkComponentFactory = frameworkComponentTestFactory.getDesktopFrameworkComponentFactory();
		}
		if (hasServerSupport) {
			serverControllerComponentFactory.abstractFrameworkComponentFactory = frameworkComponentTestFactory.getServerFrameworkComponentFactory();
		}
	}

	public void setSessionComponentTestFactory(final SessionComponentTestFactory sessionComponentTestFactory) {
		if (hasDesktopSupport) {
			desktopControllerComponentFactory.sessionComponentFactory = sessionComponentTestFactory.getDesktopSessionComponentFactory();
		}
		if (hasServerSupport) {
			serverControllerComponentFactory.sessionComponentFactory = sessionComponentTestFactory.getServerSessionComponentFactory();
		}
	}

	public void setServiceComponentTestFactory(final ServiceComponentTestFactory serviceComponentTestFactory) {
		if (hasDesktopSupport) {
			desktopControllerComponentFactory.serviceComponentFactory = serviceComponentTestFactory.getDesktopServiceComponentFactory();
		}
		if (hasServerSupport) {
			serverControllerComponentFactory.serviceComponentFactory = serviceComponentTestFactory.getServerServiceComponentFactory();
		}
	}
}
