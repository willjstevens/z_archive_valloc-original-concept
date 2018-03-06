/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transport;

import java.net.InetSocketAddress;

import com.valloc.TestLevelSupport;
import com.valloc.domain.Serializer;
import com.valloc.domain.SerializerFactory;
import com.valloc.domain.system.ServerHostConfig;
import com.valloc.framework.AbstractTestFrameworkComponentFactory;
import com.valloc.framework.ComponentTestStrategy;
import com.valloc.framework.FrameworkComponentTestFactory;
import com.valloc.security.SecuritySupportService;
import com.valloc.session.DesktopSessionComponentFactory;
import com.valloc.session.ServerSessionComponentFactory;
import com.valloc.session.SessionComponentTestFactory;

/**
 * 
 * 
 * @author wstevens
 */
public class TransportComponentTestFactory extends AbstractTestFrameworkComponentFactory
{

	public static final int DEFAULT_SERVER_PORT = 5959;

	public boolean hasServerSupport;
	public boolean hasDesktopSupport;
	public TestLevelSupport transportLevelSupport;
	private ComponentTestStrategy testStrategy;
	
	private ServerTransportComponentFactory serverTransportComponentFactory;
	private DesktopTransportComponentFactory desktopTransportComponentFactory;
	public ServerSessionComponentFactory serverSessionComponentFactory;
	public DesktopSessionComponentFactory desktopSessionComponentFactory;

	public SecuritySupportService securityService;
	public Serializer serializer;
	public int serverPort = DEFAULT_SERVER_PORT;
	public InetSocketAddress serverAddress;
	public ServerHostConfig serverHostConfig;
	public boolean frameworkObjectsInitialized;

	@Override
	public void bootstrap() {
		switch (transportLevelSupport) {
		case FAKE:		testStrategy = new FakeTransportSupport();		break;
		case CUSTOM:	testStrategy = new CustomTransportSupport();	break;
		default:		testStrategy = new FullTransportSupport();
		}
		testStrategy.bootstrap();
	}

	@Override
	public void initialize() {
		testStrategy.initialize();
	}

	private class FullTransportSupport implements ComponentTestStrategy {		
		@Override public void bootstrap() {
			serializer = SerializerFactory.getSerializerFactory().getSerializer();
			serverAddress = new InetSocketAddress(serverPort);
			serverHostConfig = new ServerHostConfig(serverAddress);
			if (hasDesktopSupport) {
				desktopTransportComponentFactory = new DesktopTransportComponentFactory();
				desktopTransportComponentFactory.setServerHostConfig(serverHostConfig);
				desktopTransportComponentFactory.setSecurityService(securityService);
				desktopTransportComponentFactory.setSerializer(serializer);
			}
			if (hasServerSupport) {
				serverTransportComponentFactory = new ServerTransportComponentFactory();
				serverTransportComponentFactory.setServerHostConfig(serverHostConfig);
				serverTransportComponentFactory.setSecurityService(securityService);
				serverTransportComponentFactory.setSerializer(serializer);
			}
		}
		
		@Override public void initialize() {
			if (hasDesktopSupport) {
				desktopTransportComponentFactory.initialize();
			}
			if (hasServerSupport) {
				serverTransportComponentFactory.initialize();
			}
		}
	}

	private class FakeTransportSupport implements ComponentTestStrategy {		
		@Override public void bootstrap() {
			if (hasDesktopSupport) {
				desktopTransportComponentFactory = new DesktopTransportComponentFactory();
			}
			if (hasServerSupport) {
				serverTransportComponentFactory = new ServerTransportComponentFactory();
			}
		}
		
		@Override public void initialize() {
			if (hasDesktopSupport) {
				desktopTransportComponentFactory.initialize();
			}
			if (hasServerSupport) {
				serverTransportComponentFactory.initialize();
			}
		}
	}

	private class CustomTransportSupport implements ComponentTestStrategy {		
		@Override public void bootstrap() {
			if (hasDesktopSupport) {
				desktopTransportComponentFactory = new DesktopTransportComponentFactory();
			}
			if (hasServerSupport) {
				serverTransportComponentFactory = new ServerTransportComponentFactory();
			}
		}
		
		@Override public void initialize() {
			if (hasDesktopSupport) {
				desktopTransportComponentFactory.initialize();
			}
			if (hasServerSupport) {
				serverTransportComponentFactory.initialize();
			}
		}
	}

	public TransportServer getTransportServer() {
		return serverTransportComponentFactory.getTransportServer();
	}

	public TransportServerStateMachine getTransportServerStateMachine() {
		return serverTransportComponentFactory.getServerStateMachine();
	}

	public ServerTransportManager getTransportServerManager() {
		return serverTransportComponentFactory.getTransportManager();
	}

	public ServerTransportComponentFactory getServerTransportComponentFactory() {
		return serverTransportComponentFactory;
	}

	public DesktopTransportComponentFactory getDesktopTransportComponentFactory() {
		return desktopTransportComponentFactory;
	}

	public void setSessionComponentTestFactory(final SessionComponentTestFactory sessionComponentTestFactory) {
		if (hasServerSupport) {
			serverTransportComponentFactory.setServerSessionComponentFactory(sessionComponentTestFactory.getServerSessionComponentFactory());
		}
	}
	
	public void setFrameworkComponentTestFactory(final FrameworkComponentTestFactory frameworkComponentTestFactory) {
		if (hasDesktopSupport) {
			desktopTransportComponentFactory.setFrameworkComponentFactory(frameworkComponentTestFactory.getDesktopFrameworkComponentFactory());
		}
		if (hasServerSupport) {
			serverTransportComponentFactory.setFrameworkComponentFactory(frameworkComponentTestFactory.getServerFrameworkComponentFactory());
		}
	}
}
