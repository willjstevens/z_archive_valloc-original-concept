/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transport;

import com.valloc.AbstractComponentFactory;
import com.valloc.domain.Serializer;
import com.valloc.domain.system.ServerHostConfig;
import com.valloc.framework.AbstractFrameworkComponentFactory;
import com.valloc.security.SecuritySupportService;

/**
 * 
 * 
 * @author wstevens
 */
abstract class AbstractTransportComponentFactory extends AbstractComponentFactory
{
	private ServerHostConfig serverHostConfig;
	private Serializer serializer;
	private SecuritySupportService securityService;
	private AbstractFrameworkComponentFactory abstractFrameworkComponentFactory;

	NettyPipelineFactory newBaseNettyPipelineFactory() {
		final NettyPipelineFactory pipelineFactory = new NettyPipelineFactory();
		pipelineFactory.setSecurityService(securityService);
		pipelineFactory.setSerializer(serializer);
		return pipelineFactory;
	}

	public void setSerializer(final Serializer serializer) {
		this.serializer = serializer;
	}

	public void setSecurityService(final SecuritySupportService securityService) {
		this.securityService = securityService;
	}

	public void setServerHostConfig(final ServerHostConfig serverHostConfig) {
		this.serverHostConfig = serverHostConfig;
	}

	ServerHostConfig getServerHostConfig() {
		return serverHostConfig;
	}

	Serializer getSerializer() {
		return serializer;
	}

	SecuritySupportService getSecurityService() {
		return securityService;
	}

	public AbstractFrameworkComponentFactory getFrameworkComponentFactory() {
		return abstractFrameworkComponentFactory;
	}

	public void setFrameworkComponentFactory(final AbstractFrameworkComponentFactory abstractFrameworkComponentFactory) {
		this.abstractFrameworkComponentFactory = abstractFrameworkComponentFactory;
	}
}
