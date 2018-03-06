/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller;

import java.util.Set;

import com.valloc.domain.system.ClientNode;
import com.valloc.service.ServerService;

/**
 *
 *
 *
 * @author wstevens
 */
public interface ServerControllerComponentFactory extends ControllerComponentFactory
{
	public SynchronousMultiClientController newMultiClientSyncController(String serviceName, String commandName, ServerService service, Set<ClientNode> clients);
	public AsynchronousMultiClientController newMultiClientAsyncController(String serviceName, String commandName, ServerService service, Set<ClientNode> clients);

}
