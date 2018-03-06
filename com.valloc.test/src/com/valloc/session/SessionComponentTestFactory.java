/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.session;

import com.valloc.TestLevelSupport;
import com.valloc.framework.AbstractTestFrameworkComponentFactory;
import com.valloc.framework.ComponentTestStrategy;
import com.valloc.framework.FrameworkComponentTestFactory;
import com.valloc.transport.TransportComponentTestFactory;

/**
 *
 *
 * @author wstevens
 */
public class SessionComponentTestFactory extends AbstractTestFrameworkComponentFactory
{
	public boolean hasServerSupport;
	public boolean hasDesktopSupport;
	public TestLevelSupport sessionLevelSupport;
	private ComponentTestStrategy testStrategy;

	private ServerSessionComponentFactory serverSessionComponentFactory;
	private DesktopSessionComponentFactory desktopSessionComponentFactory;

	@Override
	public void bootstrap() {
		switch (sessionLevelSupport) {
		case FAKE:		testStrategy = new FakeSessionSupport();	break;
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
				desktopSessionComponentFactory = new DesktopSessionComponentFactory();
			}
			if (hasServerSupport) {
				serverSessionComponentFactory = new ServerSessionComponentFactory();
			}
		}
		
		@Override public void initialize() {
			if (hasDesktopSupport) {
				desktopSessionComponentFactory.initialize();
			}
			if (hasServerSupport) {
				serverSessionComponentFactory.initialize();
			}
		}
	}

	private class FakeSessionSupport implements ComponentTestStrategy {		
		@Override public void bootstrap() {
			if (hasDesktopSupport) {
				desktopSessionComponentFactory = new DesktopSessionComponentFactory();
			}
			if (hasServerSupport) {
				serverSessionComponentFactory = new ServerSessionComponentFactory();
			}
		}
		
		@Override public void initialize() {
			if (hasDesktopSupport) {
				desktopSessionComponentFactory.initialize();
			}
			if (hasServerSupport) {
				serverSessionComponentFactory.initialize();
			}
		}
	}

	public void setServerSessionComponentFactory(final ServerSessionComponentFactory serverSessionComponentFactory) {
		this.serverSessionComponentFactory = serverSessionComponentFactory;
	}

	public void setDesktopSessionComponentFactory(final DesktopSessionComponentFactory desktopSessionComponentFactory) {
		this.desktopSessionComponentFactory = desktopSessionComponentFactory;
	}

	public ServerSessionComponentFactory getServerSessionComponentFactory() {
		return serverSessionComponentFactory;
	}

	public DesktopSessionComponentFactory getDesktopSessionComponentFactory() {
		return desktopSessionComponentFactory;
	}

	public void setTransportComponentTestFactory(final TransportComponentTestFactory transportComponentTestFactory) {
		if (hasDesktopSupport) {
			desktopSessionComponentFactory.setDesktopTransportComponentFactory(transportComponentTestFactory.getDesktopTransportComponentFactory());
		}
	}
	
	public void setFrameworkComponentTestFactory(final FrameworkComponentTestFactory frameworkComponentTestFactory) {
		if (hasDesktopSupport) {
			desktopSessionComponentFactory.setDesktopFrameworkComponentFactory(frameworkComponentTestFactory.getDesktopFrameworkComponentFactory());
		}
		if (hasServerSupport) {
			serverSessionComponentFactory.setServerFrameworkComponentFactory(frameworkComponentTestFactory.getServerFrameworkComponentFactory());
		}
	}
	
}
