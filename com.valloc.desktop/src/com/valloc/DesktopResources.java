/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc;

import com.valloc.controller.DesktopControllerComponentFactoryBase;
import com.valloc.framework.DesktopFrameworkComponentFactory;
import com.valloc.service.DesktopServiceComponentFactoryBase;
import com.valloc.session.DesktopSessionComponentFactory;
import com.valloc.transport.DesktopTransportComponentFactory;


/**
 *
 *
 * @author wstevens
 */
public final class DesktopResources
{
	private final static DesktopResources desktopResources = new DesktopResources();

	private DesktopFrameworkComponentFactory frameworkComponentFactory;
	private DesktopTransportComponentFactory transportComponentFactory;
	private DesktopSessionComponentFactory sessionComponentFactory;
	private DesktopControllerComponentFactoryBase controllerComponentFactory;
	private DesktopServiceComponentFactoryBase serviceComponentFactory;
	
	private DesktopResources() {}
	
	public static DesktopResources getInstance() {
		return desktopResources;
	}

	public DesktopFrameworkComponentFactory getFrameworkComponentFactory() {
		return frameworkComponentFactory;
	}

	public void setFrameworkComponentFactory(final DesktopFrameworkComponentFactory frameworkComponentFactory) {
		this.frameworkComponentFactory = frameworkComponentFactory;
	}

	public DesktopTransportComponentFactory getTransportComponentFactory() {
		return transportComponentFactory;
	}

	public void setTransportComponentFactory(final DesktopTransportComponentFactory transportComponentFactory) {
		this.transportComponentFactory = transportComponentFactory;
	}

	public DesktopSessionComponentFactory getSessionComponentFactory() {
		return sessionComponentFactory;
	}

	public void setSessionComponentFactory(final DesktopSessionComponentFactory sessionComponentFactory) {
		this.sessionComponentFactory = sessionComponentFactory;
	}

	public DesktopControllerComponentFactoryBase getControllerComponentFactory() {
		return controllerComponentFactory;
	}

	public void setControllerComponentFactory(final DesktopControllerComponentFactoryBase controllerComponentFactory) {
		this.controllerComponentFactory = controllerComponentFactory;
	}

	public DesktopServiceComponentFactoryBase getServiceComponentFactory() {
		return serviceComponentFactory;
	}

	public void setServiceComponentFactory(final DesktopServiceComponentFactoryBase serviceComponentFactory) {
		this.serviceComponentFactory = serviceComponentFactory;
	}

	
}
