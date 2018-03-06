/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.request;

import com.valloc.TestLevelSupport;
import com.valloc.framework.AbstractTestFrameworkComponentFactory;
import com.valloc.framework.ComponentTestStrategy;
import com.valloc.framework.FrameworkComponentTestFactory;
import com.valloc.lifecycle.LifecycleChange;
import com.valloc.state.MockStateChangeRequester;
import com.valloc.state.StateChangeRequest;
import com.valloc.thread.BaseThreadManager;
import com.valloc.thread.ThreadManager;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public class RequestContainerComponentTestFactory extends AbstractTestFrameworkComponentFactory
{
	public boolean hasServerSupport;
	public boolean hasDesktopSupport;
	public TestLevelSupport testLevelSupport;
	private ComponentTestStrategy testStrategy;

	private ThreadManager threadManager;
	private RequestContainer desktopRequestContainer;
	private RequestContainerConfiguration desktopConfig;
	private RequestContainerStateMachine desktopStateMachine;
	private RequestContainer serverRequestContainer;
	private RequestContainerConfiguration serverConfig;
	private RequestContainerStateMachine serverStateMachine;
	private MockStateChangeRequester requester;
	private FrameworkComponentTestFactory frameworkComponentTestFactory;

	@Override
	public void bootstrap() {
		switch (testLevelSupport) {
		// case FAKE: testStrategy = new FakeSessionSupport(); break;
		default:	testStrategy = new FullSessionSupport();
		}
		testStrategy.bootstrap();
	}

	@Override
	public void initialize() {
		testStrategy.initialize();
	}

	private class FullSessionSupport implements ComponentTestStrategy
	{
		@Override public void bootstrap() {
			threadManager = BaseThreadManager.getInstance();
			threadManager.purge();
			if (hasDesktopSupport) {
				desktopConfig = new DesktopRequestContainerConfiguration();
				desktopRequestContainer = new BaseRequestContainer(desktopConfig, threadManager);
				desktopStateMachine = new RequestContainerStateMachine(desktopRequestContainer);
			}
			if (hasServerSupport) {
				serverConfig = new ServerRequestContainerConfiguration();
				serverRequestContainer = new BaseRequestContainer(serverConfig, threadManager);
				serverStateMachine = new RequestContainerStateMachine(serverRequestContainer);
			}
			requester = new MockStateChangeRequester();
		}

		@Override public void initialize() {
			if (hasDesktopSupport) {
				desktopRequestContainer.setFrameworkManager(frameworkComponentTestFactory.getDesktopFrameworkComponentFactory().getFrameworkManager());
				desktopStateMachine.requestStateChange(new StateChangeRequest(new UniqueId(), LifecycleChange.START, requester));
			}
			if (hasServerSupport) {
				serverRequestContainer.setFrameworkManager(frameworkComponentTestFactory.getServerFrameworkComponentFactory().getFrameworkManager());
				serverStateMachine.requestStateChange(new StateChangeRequest(new UniqueId(), LifecycleChange.START, requester));
			}
		}
	}

	public RequestContainer getDesktopRequestContainer() {
		return desktopRequestContainer;
	}

	public RequestContainer getServerRequestContainer() {
		return serverRequestContainer;
	}
	
	public void setFrameworkComponentTestFactory(final FrameworkComponentTestFactory frameworkComponentTestFactory) {
		this.frameworkComponentTestFactory = frameworkComponentTestFactory;
	}
}
