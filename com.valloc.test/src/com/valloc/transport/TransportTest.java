/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transport;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.valloc.AbstractTest;
import com.valloc.MessageSummary;
import com.valloc.TestLevelSupport;
import com.valloc.domain.MockDomainObjectFactory;
import com.valloc.framework.FrameworkRequest;
import com.valloc.framework.FrameworkResponse;
import com.valloc.lifecycle.LifecycleChange;
import com.valloc.security.SecuritySupportFactory;
import com.valloc.session.DesktopClientSession;
import com.valloc.session.DesktopServerSession;
import com.valloc.session.DesktopServerSessionBase;
import com.valloc.session.DesktopSessionComponentFactory;
import com.valloc.session.ServerSessionComponentFactory;
import com.valloc.session.SessionComponentTestFactory;
import com.valloc.state.MockStateChangeRequester;
import com.valloc.state.StateChangeRequest;
import com.valloc.state.StateChangeResponse;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public final class TransportTest extends AbstractTest
{
	private TransportComponentTestFactory transportComponentTestFactory;
	private SessionComponentTestFactory sessionComponentTestFactory;
	private TransportServer transportServer;
	private DesktopClientConnector desktopConnector;
	private TransportServerStateMachine serverStateMachine;
	private MockStateChangeRequester stateChangeRequester;

	@Before
	public void setUp() throws Exception {
		// build security service factory for SSL support
		final File testKeysFile = buildTestKeysFile();
		final char[] testKeysPassphrase = getTestKeysFilePassphrase();
		final SecuritySupportFactory securitySupportFactory = new SecuritySupportFactory(testKeysFile, testKeysPassphrase);

		// now setup factories and references
		transportComponentTestFactory = new TransportComponentTestFactory();
		transportComponentTestFactory.transportLevelSupport = TestLevelSupport.FULL;
		transportComponentTestFactory.hasDesktopSupport = true;
		transportComponentTestFactory.hasServerSupport = true;
		transportComponentTestFactory.setDomainObjectFactory(new MockDomainObjectFactory());
		transportComponentTestFactory.securityService = securitySupportFactory.newSecuritySupportService();

		sessionComponentTestFactory = new SessionComponentTestFactory();
		sessionComponentTestFactory.sessionLevelSupport = TestLevelSupport.CUSTOM;
		sessionComponentTestFactory.hasDesktopSupport = true;
		sessionComponentTestFactory.hasServerSupport = true;
		sessionComponentTestFactory.setDesktopSessionComponentFactory(new DesktopSessionComponentFactoryOverride());
		sessionComponentTestFactory.setServerSessionComponentFactory(new ServerSessionComponentFactoryOverride());
		transportComponentTestFactory.setSessionComponentTestFactory(sessionComponentTestFactory);

		transportComponentTestFactory.initialize();
		sessionComponentTestFactory.initialize();

		transportServer = transportComponentTestFactory.getTransportServer();
		serverStateMachine = transportComponentTestFactory.getTransportServerStateMachine();
		stateChangeRequester = new MockStateChangeRequester();

		sessionComponentTestFactory.getDesktopSessionComponentFactory().createAndRegisterDesktopSession("wstevens");
		desktopConnector = transportComponentTestFactory.getDesktopTransportComponentFactory().getTransportManager().getDesktopConnector();
	}

	@Test
	public void basicFunctionality() {
		StateChangeRequest request = new StateChangeRequest(new UniqueId(), LifecycleChange.START, stateChangeRequester);
		serverStateMachine.requestStateChange(request);
		final StateChangeResponse response = stateChangeRequester.getResponse();

		Assert.assertTrue(transportServer.isBound());
		Assert.assertEquals(MessageSummary.SUCCESS, response.getMessageSummary());

		desktopConnector.connect(transportComponentTestFactory.serverHostConfig);
		Assert.assertTrue(desktopConnector.isConnected());

		final FrameworkRequest req = transportComponentTestFactory.newCannedWhateverFrameworkRequest();
		final FrameworkResponse resp = transportComponentTestFactory.newCannedWhateverFrameworkResponse(req);
		desktopConnector.submitSync(req, resp);

		desktopConnector.disconnect();
		request = new StateChangeRequest(new UniqueId(), LifecycleChange.DESTROY, stateChangeRequester);
		serverStateMachine.requestStateChange(request);
	}

	private final class ServerSessionComponentFactoryOverride extends ServerSessionComponentFactory
	{
		@Override
		protected DesktopServerSession newDesktopServerSession() {
			return new DesktopServerSessionBase() {
				@Override
				public void requestReceived(final FrameworkRequest request, final FrameworkResponse response) {
					// super.requestReceived(request);
					// logic here to catch request and throw back an expected response; the response should
					// be sent out via the returnResponse method which should make direct reference
					// to the included connector object
				}
			};
		}
	}

	private final class DesktopSessionComponentFactoryOverride extends DesktopSessionComponentFactory
	{
		@Override
		protected DesktopClientSession newDesktopClientSession() {
			return super.newDesktopClientSession();
		}
	}
}
