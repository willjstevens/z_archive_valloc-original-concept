/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc;

import java.io.File;
import java.util.concurrent.CountDownLatch;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.valloc.concurrent.request.RequestContainerComponentTestFactory;
import com.valloc.controller.ControllerComponentTestFactory;
import com.valloc.controller.DesktopControllerComponentFactoryBase;
import com.valloc.domain.MockDomainObjectFactory;
import com.valloc.domain.system.ServerHostConfig;
import com.valloc.framework.DesktopFrameworkManager;
import com.valloc.framework.FrameworkComponentTestFactory;
import com.valloc.framework.service.FrameworkControlClient;
import com.valloc.interrupt.InterruptEscapeException;
import com.valloc.interrupt.InterruptType;
import com.valloc.lifecycle.LifecycleChange;
import com.valloc.lifecycle.LifecycleState;
import com.valloc.security.SecuritySupportFactory;
import com.valloc.service.ClientDirectory;
import com.valloc.service.DesktopServiceComponentFactoryBase;
import com.valloc.service.ServiceComponentTestFactory;
import com.valloc.service.ServiceDirectory;
import com.valloc.session.DesktopClientSession;
import com.valloc.session.DesktopSessionComponentFactory;
import com.valloc.session.SessionComponentTestFactory;
import com.valloc.state.MockStateChangeRequester;
import com.valloc.state.StateChangeRequest;
import com.valloc.state.StateChangeResponse;
import com.valloc.transport.TransportComponentTestFactory;
import com.valloc.transport.TransportServerStateMachine;
import com.valloc.user.service.UserService;
import com.valloc.util.Result;
import com.valloc.util.UniqueId;
import com.valloc.util.Util;

/**
 *
 *
 * @author wstevens
 */
public final class UserConnectTest extends AbstractTest
{
	private ControllerComponentTestFactory controllerComponentTestFactory; 
	private ServiceComponentTestFactory serviceComponentTestFactory;
	private FrameworkComponentTestFactory frameworkComponentTestFactory;
	private SessionComponentTestFactory sessionComponentTestFactory;
	private TransportComponentTestFactory transportComponentTestFactory;
	private TransportServerStateMachine transportServerStateMachine;
	private RequestContainerComponentTestFactory requestContainerComponentTestFactory;
	private MockStateChangeRequester stateChangeRequester;
	final MockDomainObjectFactory mockObjFactory = new MockDomainObjectFactory();
	private StateChangeRequest request;

	@Before
	public void setUp() throws Exception {
		// build security service factory for SSL support
		final File testKeysFile = buildTestKeysFile();
		final char[] testKeysPassphrase = getTestKeysFilePassphrase();
		final SecuritySupportFactory securitySupportFactory = new SecuritySupportFactory(testKeysFile, testKeysPassphrase);

		// transport layer
		transportComponentTestFactory = new TransportComponentTestFactory();
		transportComponentTestFactory.transportLevelSupport = TestLevelSupport.FULL;
		transportComponentTestFactory.hasDesktopSupport = true; 
		transportComponentTestFactory.hasServerSupport = true;
		transportComponentTestFactory.securityService = securitySupportFactory.newSecuritySupportService();

		// session layer
		sessionComponentTestFactory = new SessionComponentTestFactory();
		sessionComponentTestFactory.sessionLevelSupport = TestLevelSupport.FULL;
		sessionComponentTestFactory.hasDesktopSupport = true;
		sessionComponentTestFactory.hasServerSupport = true;
//		sessionComponentTestFactory.desktopSessionComponentFactory = new DesktopSessionComponentFactoryOverride();
//		sessionComponentTestFactory.serverSessionComponentFactory = new ServerSessionComponentFactoryOverride();

		// controller layer
		controllerComponentTestFactory = new ControllerComponentTestFactory();
		controllerComponentTestFactory.controllerLevelSupport = TestLevelSupport.FULL;
		controllerComponentTestFactory.hasDesktopSupport = true;
		controllerComponentTestFactory.hasServerSupport = true;

		// service layer
		serviceComponentTestFactory = new ServiceComponentTestFactory();
		serviceComponentTestFactory.serviceLevelSupport = TestLevelSupport.FULL;
		serviceComponentTestFactory.hasDesktopSupport = true;
		serviceComponentTestFactory.hasServerSupport = true;

		// framework layer
		frameworkComponentTestFactory = new FrameworkComponentTestFactory();
		frameworkComponentTestFactory.frameworkLevelSupport = TestLevelSupport.FULL;
		frameworkComponentTestFactory.hasDesktopSupport = true;
		frameworkComponentTestFactory.hasServerSupport = true;

		// request container component
		requestContainerComponentTestFactory = new RequestContainerComponentTestFactory();
		requestContainerComponentTestFactory.testLevelSupport = TestLevelSupport.FULL;
		requestContainerComponentTestFactory.hasDesktopSupport = true;
		requestContainerComponentTestFactory.hasServerSupport = true;

		// set mock factory
		transportComponentTestFactory.setDomainObjectFactory(mockObjFactory);
		sessionComponentTestFactory.setDomainObjectFactory(mockObjFactory);
		serviceComponentTestFactory.setDomainObjectFactory(mockObjFactory);
		controllerComponentTestFactory.setDomainObjectFactory(mockObjFactory);
		frameworkComponentTestFactory.setDomainObjectFactory(mockObjFactory);

		// bootstrap all factories to setup actual component factories
		transportComponentTestFactory.bootstrap();
		sessionComponentTestFactory.bootstrap();
		controllerComponentTestFactory.bootstrap();
		serviceComponentTestFactory.bootstrap();
		frameworkComponentTestFactory.bootstrap();
		requestContainerComponentTestFactory.bootstrap();

		// trade references
		requestContainerComponentTestFactory.setFrameworkComponentTestFactory(frameworkComponentTestFactory);
		transportComponentTestFactory.setSessionComponentTestFactory(sessionComponentTestFactory);
		transportComponentTestFactory.setFrameworkComponentTestFactory(frameworkComponentTestFactory);
		sessionComponentTestFactory.setTransportComponentTestFactory(transportComponentTestFactory);
		sessionComponentTestFactory.setFrameworkComponentTestFactory(frameworkComponentTestFactory);
//		frameworkComponentTestFactory.setServiceComponentTestFactory(serviceComponentTestFactory);
		frameworkComponentTestFactory.setControllerComponentTestFactory(controllerComponentTestFactory);
		frameworkComponentTestFactory.setRequestContainer(requestContainerComponentTestFactory);
		controllerComponentTestFactory.setFrameworkComponentTestFactory(frameworkComponentTestFactory);
		controllerComponentTestFactory.setSessionComponentTestFactory(sessionComponentTestFactory);
		controllerComponentTestFactory.setServiceComponentTestFactory(serviceComponentTestFactory);
		serviceComponentTestFactory.setControllerComponentTestFactory(controllerComponentTestFactory);
		serviceComponentTestFactory.setFrameworkComponentTestFactory(frameworkComponentTestFactory);
		serviceComponentTestFactory.setSessionComponentTestFactory(sessionComponentTestFactory);
		serviceComponentTestFactory.setRequestContainer(requestContainerComponentTestFactory);

		// initialize all factories now that setup is complete
		transportComponentTestFactory.initialize();
		sessionComponentTestFactory.initialize();
		controllerComponentTestFactory.initialize();
		frameworkComponentTestFactory.initialize();
		serviceComponentTestFactory.initialize();
		requestContainerComponentTestFactory.initialize();

		// trade any initialize-dependent services

		transportServerStateMachine = transportComponentTestFactory.getTransportServerStateMachine();
		stateChangeRequester = new MockStateChangeRequester();
	}


	@After
	public void tearDown() throws Exception {
		request = new StateChangeRequest(new UniqueId(), LifecycleChange.DESTROY, stateChangeRequester);
		transportServerStateMachine.requestStateChange(request);
	}

	@Test
	public void connect() {
		final StateChangeRequest request = new StateChangeRequest(new UniqueId(), LifecycleChange.START, stateChangeRequester);
		transportServerStateMachine.requestStateChange(request);
		final StateChangeResponse response = stateChangeRequester.getResponse();
		Assert.assertEquals(response.getResultingState(), LifecycleState.ACTIVE);

		final DesktopSessionComponentFactory desktopSessionComponentFactory = sessionComponentTestFactory.getDesktopSessionComponentFactory();
		final DesktopClientSession clientSession = desktopSessionComponentFactory.createAndRegisterDesktopSession("wstevens");
		final ServerHostConfig serverHostConfiig = mockObjFactory.newServerHostConfig();

		final MessageSummary messageSummary = clientSession.connect(serverHostConfiig);
		Assert.assertEquals(true, messageSummary.isSuccess());

//		final DesktopServiceComponentFactory<UserService> desktopServiceComponentFactory = serviceComponentTestFactory.getDesktopServiceComponentFactory();
		final DesktopServiceComponentFactoryBase desktopServiceComponentFactory = serviceComponentTestFactory.getDesktopServiceComponentFactory();
		
		// sync local call
		final UserService userService1 = desktopServiceComponentFactory.newClientService(ServiceDirectory.USER);
		final Result result1 = userService1.syncLocalMeth("Hello from syncLocalMeth");
		final MessageSummary loginSummary1 = result1.getMessageSummary();
		Assert.assertTrue(loginSummary1.isSuccess());

		// sync remote call
		final UserService userService2 = desktopServiceComponentFactory.newClientService(ServiceDirectory.USER);
		final Result result2 = userService2.login("wstevens", "bad-password".toCharArray());
		final MessageSummary loginSummary2 = result2.getMessageSummary();
		Assert.assertTrue(loginSummary2.isSuccess());

		// async remote call
		final UserService userService3 = desktopServiceComponentFactory.newClientService(ServiceDirectory.USER);
		final Result result3 = userService3.asyncRemoteMeth("Hello from asyncRemote");
		final MessageSummary loginSummary3 = result3.getMessageSummary();
		Assert.assertTrue(loginSummary3.isSuccess());

		// async local call
		final UserService userService4 = desktopServiceComponentFactory.newClientService(ServiceDirectory.USER);
		final Result result4 = userService4.asyncLocalMeth("Hello from LOCAL meth.");
		final MessageSummary loginSummary4 = result4.getMessageSummary();
		Assert.assertTrue(loginSummary4.isSuccess());

		clientSession.disconnect();
	}

	@Test
	public void interrupt() {
		final StateChangeRequest request = new StateChangeRequest(new UniqueId(), LifecycleChange.START, stateChangeRequester);
		transportServerStateMachine.requestStateChange(request);
		final StateChangeResponse response = stateChangeRequester.getResponse();
		Assert.assertEquals(response.getResultingState(), LifecycleState.ACTIVE);

		final DesktopSessionComponentFactory desktopSessionComponentFactory = sessionComponentTestFactory.getDesktopSessionComponentFactory();
		final DesktopClientSession clientSession = desktopSessionComponentFactory.createAndRegisterDesktopSession("wstevens");
		final ServerHostConfig serverHostConfiig = mockObjFactory.newServerHostConfig();

		final MessageSummary messageSummary = clientSession.connect(serverHostConfiig); 
		Assert.assertEquals(true, messageSummary.isSuccess());

		final DesktopServiceComponentFactoryBase desktopServiceComponentFactory = serviceComponentTestFactory.getDesktopServiceComponentFactory();
		final InterruptServiceClient client = new InterruptServiceClient();
		final CountDownLatch terminateLatch = new CountDownLatch(1);
		client.terminateLatch = terminateLatch;
		client.isSync = true;
		new Thread(client).start();
		try {
			client.latch.await();
		} catch (final InterruptedException e) { e.printStackTrace(); }
		Util.quietSecondsSleep(2);
		final UniqueId serviceReqId = client.userService1.id();

		final FrameworkControlClient<DesktopFrameworkManager, DesktopControllerComponentFactoryBase> frameworkControlService =
//			desktopServiceComponentFactory.newCommonService(ClientDirectory.FRAMEWORK_CONTROL);
			desktopServiceComponentFactory.newClientService(ClientDirectory.FRAMEWORK_CONTROL);
 
//		final UserService userSerrr = frameworkComponentTestFactory.getDesktopFrameworkComponentFactory().newService("");
		
		final Result result1 = frameworkControlService.interruptRemoteRequest(serviceReqId, InterruptType.USER_CANCELLED);

		final MessageSummary interruptSummary = result1.getMessageSummary();
		Assert.assertTrue(interruptSummary.isSuccess());

		try {
			terminateLatch.await();
		} catch (final InterruptedException e) { e.printStackTrace(); }
		clientSession.disconnect();
	}

	@Test
	public void interruptAsync() {
		final StateChangeRequest request = new StateChangeRequest(new UniqueId(), LifecycleChange.START, stateChangeRequester);
		transportServerStateMachine.requestStateChange(request);
		final StateChangeResponse response = stateChangeRequester.getResponse();
		Assert.assertEquals(response.getResultingState(), LifecycleState.ACTIVE);
		final DesktopSessionComponentFactory desktopSessionComponentFactory = sessionComponentTestFactory.getDesktopSessionComponentFactory();
		final DesktopClientSession clientSession = desktopSessionComponentFactory.createAndRegisterDesktopSession("wstevens");
		final ServerHostConfig serverHostConfiig = mockObjFactory.newServerHostConfig();
		final MessageSummary messageSummary = clientSession.connect(serverHostConfiig);
		Assert.assertEquals(true, messageSummary.isSuccess());

		final DesktopServiceComponentFactoryBase desktopServiceComponentFactory = serviceComponentTestFactory.getDesktopServiceComponentFactory();
		final InterruptServiceClient client = new InterruptServiceClient();
		final CountDownLatch terminateLatch = new CountDownLatch(1);
		client.terminateLatch = terminateLatch;
		client.isSync = false;
		new Thread(client).start();
		try {
			client.latch.await();
		} catch (final InterruptedException e) { e.printStackTrace(); }
		Util.quietSecondsSleep(2);
		final UniqueId serviceReqId = client.userService1.id();

//		final FrameworkControlClient frameworkControlService = desktopServiceComponentFactory.newClientService(ClientDirectory.FRAMEWORK_CONTROL);
		final FrameworkControlClient<DesktopFrameworkManager, DesktopControllerComponentFactoryBase> frameworkControlService = 
//			desktopServiceComponentFactory.newCommonService(ClientDirectory.FRAMEWORK_CONTROL);
			desktopServiceComponentFactory.newClientService(ClientDirectory.FRAMEWORK_CONTROL);
		
//		final UserService clientUserService = desktopServiceComponentFactory.newClientService(ServiceDirectory.USER);
//		final DesktopControllerComponentFactory desktopControllerComponentFactory = clientUserService.getControllerComponentFactory();
		
		final Result result1 = frameworkControlService.interruptRemoteRequest(serviceReqId, InterruptType.USER_CANCELLED);

		final MessageSummary interruptSummary = result1.getMessageSummary(); 
		Assert.assertTrue(interruptSummary.isSuccess());

		try {
			terminateLatch.await();
		} catch (final InterruptedException e) { e.printStackTrace(); }
		clientSession.disconnect();
	}

	private class InterruptServiceClient implements Runnable {
		UserService userService1 = null;
		CountDownLatch latch = new CountDownLatch(1);
		CountDownLatch terminateLatch = null;
		boolean isSync;
		@Override public void run() {
			final DesktopServiceComponentFactoryBase desktopServiceComponentFactory = serviceComponentTestFactory.getDesktopServiceComponentFactory();
			userService1 = desktopServiceComponentFactory.newClientService(ServiceDirectory.USER);
			latch.countDown();
			try {
				userService1.interruptMe(isSync);
				Assert.fail("Should fail if here");
			} catch (final InterruptEscapeException e) {
				System.out.println("SUCCESSFULLY RECEIVED AND HANDLED INTERRUPT.");
			} finally {
				terminateLatch.countDown();
			}
		}
	}
}
