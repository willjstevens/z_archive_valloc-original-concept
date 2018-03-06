/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc;

import com.valloc.controller.AgentControllerComponentFactory;
import com.valloc.framework.AgentFrameworkComponentFactory;
import com.valloc.service.AgentServiceComponentFactory;
import com.valloc.session.AgentSessionComponentFactory;
import com.valloc.transport.AgentTransportComponentFactory;


/**
 *
 *
 * @author wstevens
 */
public final class AgentResources
{
	private final static AgentResources agentResources = new AgentResources();

	private AgentFrameworkComponentFactory frameworkComponentFactory;
	private AgentTransportComponentFactory transportComponentFactory;
	private AgentSessionComponentFactory sessionComponentFactory;
	private AgentControllerComponentFactory controllerComponentFactory;
	private AgentServiceComponentFactory serviceComponentFactory;

	private AgentResources() {}

	public static AgentResources getInstance() {
		return agentResources;
	}

	public AgentFrameworkComponentFactory getFrameworkComponentFactory() {
		return frameworkComponentFactory;
	}

	public void setFrameworkComponentFactory(final AgentFrameworkComponentFactory frameworkComponentFactory) {
		this.frameworkComponentFactory = frameworkComponentFactory;
	}

	public AgentTransportComponentFactory getTransportComponentFactory() {
		return transportComponentFactory;
	}

	public void setTransportComponentFactory(final AgentTransportComponentFactory transportComponentFactory) {
		this.transportComponentFactory = transportComponentFactory;
	}

	public AgentSessionComponentFactory getSessionComponentFactory() {
		return sessionComponentFactory;
	}

	public void setSessionComponentFactory(final AgentSessionComponentFactory sessionComponentFactory) {
		this.sessionComponentFactory = sessionComponentFactory;
	}

	public AgentControllerComponentFactory getControllerComponentFactory() {
		return controllerComponentFactory;
	}

	public void setControllerComponentFactory(final AgentControllerComponentFactory controllerComponentFactory) {
		this.controllerComponentFactory = controllerComponentFactory;
	}

	public AgentServiceComponentFactory getServiceComponentFactory() {
		return serviceComponentFactory;
	}

	public void setServiceComponentFactory(final AgentServiceComponentFactory serviceComponentFactory) {
		this.serviceComponentFactory = serviceComponentFactory;
	}


}
