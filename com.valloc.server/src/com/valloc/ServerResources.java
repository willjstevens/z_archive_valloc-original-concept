/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc;

import com.valloc.controller.ServerControllerComponentFactoryBase;
import com.valloc.framework.ServerFrameworkComponentFactory;
import com.valloc.service.ServerServiceComponentFactory;
import com.valloc.session.ServerSessionComponentFactory;
import com.valloc.transport.ServerTransportComponentFactory;

/**
 *
 *
 * @author wstevens
 */
public final class ServerResources
{
	private final static ServerResources serverResources = new ServerResources();

	private ServerFrameworkComponentFactory frameworkComponentFactory;
	private ServerTransportComponentFactory transportComponentFactory;
	private ServerSessionComponentFactory sessionComponentFactory;
	private ServerControllerComponentFactoryBase controllerComponentFactory;
	private ServerServiceComponentFactory serviceComponentFactory;
	
	private ServerResources() {}
	
	public static ServerResources getInstance() {
		return serverResources;
	}

	public ServerFrameworkComponentFactory getFrameworkComponentFactory() {
		return frameworkComponentFactory;
	}

	public void setFrameworkComponentFactory(final ServerFrameworkComponentFactory frameworkComponentFactory) {
		this.frameworkComponentFactory = frameworkComponentFactory;
	}

	public ServerTransportComponentFactory getTransportComponentFactory() {
		return transportComponentFactory;
	}

	public void setTransportComponentFactory(final ServerTransportComponentFactory transportComponentFactory) {
		this.transportComponentFactory = transportComponentFactory;
	}

	public ServerSessionComponentFactory getSessionComponentFactory() {
		return sessionComponentFactory;
	}

	public void setSessionComponentFactory(final ServerSessionComponentFactory sessionComponentFactory) {
		this.sessionComponentFactory = sessionComponentFactory;
	}

	public ServerControllerComponentFactoryBase getControllerComponentFactory() {
		return controllerComponentFactory;
	}

	public void setControllerComponentFactory(final ServerControllerComponentFactoryBase controllerComponentFactory) {
		this.controllerComponentFactory = controllerComponentFactory;
	}

	public ServerServiceComponentFactory getServiceComponentFactory() {
		return serviceComponentFactory;
	}

	public void setServiceComponentFactory(final ServerServiceComponentFactory serviceComponentFactory) {
		this.serviceComponentFactory = serviceComponentFactory;
	}

	
}
