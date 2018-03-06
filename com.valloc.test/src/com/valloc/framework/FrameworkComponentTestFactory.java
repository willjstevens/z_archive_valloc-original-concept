/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.framework;

import com.valloc.TestLevelSupport;
import com.valloc.concurrent.request.RequestContainerComponentTestFactory;
import com.valloc.controller.ControllerComponentTestFactory;

/**
 *
 *
 * @author wstevens
 */
public class FrameworkComponentTestFactory extends AbstractTestFrameworkComponentFactory
{
	public boolean hasServerSupport;
	public boolean hasDesktopSupport;
	public TestLevelSupport frameworkLevelSupport;
	private ComponentTestStrategy testStrategy;

	private ServerFrameworkComponentFactory serverFrameworkComponentFactory;
	private DesktopFrameworkComponentFactory desktopFrameworkComponentFactory;

	@Override
	public void bootstrap() {
		switch (frameworkLevelSupport) {
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
				desktopFrameworkComponentFactory = new DesktopFrameworkComponentFactory();
			}
			if (hasServerSupport) {
				serverFrameworkComponentFactory = new ServerFrameworkComponentFactory();
			}
		}
		
		@Override public void initialize() {
			if (hasDesktopSupport) {
				desktopFrameworkComponentFactory.initialize();
			}
			if (hasServerSupport) {
				serverFrameworkComponentFactory.initialize();
			}
		}
	}

	public ServerFrameworkComponentFactory getServerFrameworkComponentFactory() {
		return serverFrameworkComponentFactory;
	}

	public DesktopFrameworkComponentFactory getDesktopFrameworkComponentFactory() {
		return desktopFrameworkComponentFactory;
	}

//	public void setServiceComponentTestFactory(final ServiceComponentTestFactory serviceComponentTestFactory) {
//		if (hasDesktopSupport) {
//			desktopFrameworkComponentFactory.setDesktopServiceComponentFactory(serviceComponentTestFactory.getDesktopServiceComponentFactory());
//		}
//		if (hasServerSupport) {
//			serverFrameworkComponentFactory.setServerServiceComponentFactory(serviceComponentTestFactory.getServerServiceComponentFactory());
//		}
//	}
	
	public void setControllerComponentTestFactory(final ControllerComponentTestFactory controllerComponentTestFactory) {
		if (hasDesktopSupport) {
			desktopFrameworkComponentFactory.setControllerComponentFactory(controllerComponentTestFactory.getDesktopControllerComponentFactory());
		}
		if (hasServerSupport) {
			serverFrameworkComponentFactory.setControllerComponentFactory(controllerComponentTestFactory.getServerControllerComponentFactory());
		}
	}
	
	public void setRequestContainer(final RequestContainerComponentTestFactory requestContainerComponentTestFactory) {
		if (hasDesktopSupport) {
			desktopFrameworkComponentFactory.setRequestContainer(requestContainerComponentTestFactory.getDesktopRequestContainer());
		}
		if (hasServerSupport) {
			serverFrameworkComponentFactory.setRequestContainer(requestContainerComponentTestFactory.getServerRequestContainer());
		}
	}
}
