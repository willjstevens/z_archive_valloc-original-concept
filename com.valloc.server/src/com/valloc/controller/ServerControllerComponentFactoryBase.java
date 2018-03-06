/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller;

import java.util.LinkedList;
import java.util.Set;

import com.valloc.controller.pipeline.CommandHandlerFlags;
import com.valloc.controller.pipeline.ControllerPipelineHandlerFactory;
import com.valloc.controller.pipeline.ControllerPipelineProcessor;
import com.valloc.controller.pipeline.ServerCommandHandlerFlags;
import com.valloc.controller.pipeline.ServerCommandHandlerFlagsRepository;
import com.valloc.domain.system.ClientNode;
import com.valloc.framework.CallType;
import com.valloc.framework.FrameworkRequest;
import com.valloc.framework.FrameworkResponse;
import com.valloc.framework.NodeType;
import com.valloc.interrupt.InterruptTracker;
import com.valloc.service.ServerService;
import com.valloc.service.Service;
import com.valloc.util.DependencyItem;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public class ServerControllerComponentFactoryBase extends AbstractControllerComponentFactory implements ServerControllerComponentFactory
{
	private ServerCommandHandlerFlagsRepository serverCommandHandlerFlagsRepository;

	@Override
	public void initialize() {
		serverCommandHandlerFlagsRepository = new ServerCommandHandlerFlagsRepository();
		serverCommandHandlerFlagsRepository.initialize();

		super.initialize();
	}

	@Override
	void initializeDependencies() {
		super.initializeDependencies();
		// server specific handler mappings here
	}

	@Override
	void initializeHandlerFactoryMappings() {
		super.initializeHandlerFactoryMappings();
	}

	@Override
	public SynchronousClientController newClientLocalSyncController(final String serviceName,
			final String commandName, final Service service) {
		return null;
	}

	@Override
	public AsynchronousClientController newClientLocalAsyncController(final String serviceName,
			final String commandName, final Service service) {
		return null;
	}

	@Override
	public SynchronousClientController newClientRemoteSyncController(final String serviceName, final String commandName, final Service service) {
		return null;
	}

	@Override
	public AsynchronousClientController newClientRemoteAsyncController(final String serviceName,
			final String commandName, final Service service) {
		return null;
	}

	@Override
	public SynchronousMultiClientController newMultiClientSyncController(final String serviceName,
			final String commandName, final ServerService service, final Set<ClientNode> clients) {
		return null;
	}

	@Override
	public AsynchronousMultiClientController newMultiClientAsyncController(final String serviceName,
			final String commandName, final ServerService service, final Set<ClientNode> clients) {
		return null;
	}

	@Override
	FrameworkRequest newFrameworkRequest(final UniqueId uniqueId, final CallType callType, final String serviceName, final String commandName, final InterruptTracker interruptTracker) {
		final FrameworkRequest frameworkRequest =
			abstractFrameworkComponentFactory.newFrameworkRequest(uniqueId, NodeType.DESKTOP, serviceName, commandName, callType);
		frameworkRequest.setInterruptTracker(interruptTracker);
		return frameworkRequest;
	}

	@Override
	public ServiceController newServiceController(final FrameworkRequest request, final FrameworkResponse response) {
		final ServiceController serviceController = new ServiceControllerBase();

		// retrieve handler list and set definite handlers
		final LinkedList<DependencyItem<String, ControllerPipelineHandlerFactory>> handlerList = newHandlerList();
		insertFactoryToHandlerList(serviceInvokerControllerPipelineHandlerFactory.name(), handlerList);

		// now assemble pipeline processor and initialize
		final CallType callType = request.getCallType();
		if (callType == CallType.LOCAL_ASYNCHRONOUS) {
			insertFactoryToHandlerList(localReceiverControllerPipelineHandlerFactory.name(), handlerList);
		} else if (callType == CallType.REMOTE_SYNCHRONOUS || callType == CallType.REMOTE_ASYNCHRONOUS) {
			insertFactoryToHandlerList(remoteReceiverControllerPipelineHandlerFactory.name(), handlerList);
		}
		final InterruptTracker interruptTracker = request.getInterruptTracker();
		final ControllerPipelineProcessor pipelineProcessor = newControllerPipelineProcessor(handlerList, interruptTracker);
		serviceController.setControllerPipelineProcessor(pipelineProcessor);
		serviceController.setFrameworkRequest(request);
		serviceController.setFrameworkResponse(response);
		serviceController.setInterruptTracker(interruptTracker);
		pipelineProcessor.initialize();

		return serviceController;
	}

	@Override
	void addOptionalHandlers(final NodeType nodeType, final String serviceName, final String commandName, final LinkedList<DependencyItem<String, ControllerPipelineHandlerFactory>> handlerList) {
		final CommandHandlerFlags flagsObj = serverCommandHandlerFlagsRepository.getCommandHandlerFlags(nodeType, serviceName, commandName);
		if (flagsObj != null) { // could be null if nothing was added
			if (flagsObj instanceof ServerCommandHandlerFlags) {
				final ServerCommandHandlerFlags serverFlagsObj = (ServerCommandHandlerFlags) flagsObj;
				if (serverFlagsObj.isDatabase()) {
					// TODO: Add preferences handler here
					//					insertFactoryToHandlerList(databaseHandlerFactory.id(), handlerList);
				}
				if (serverFlagsObj.isLicensed()) {
					// TODO: Add preferences handler here
					//					insertFactoryToHandlerList(licenseHandlerFactory.id(), handlerList);
				}
			}
			// common flags here
			if (flagsObj.isConverse()) {
				// TODO: Add conversation handler here
				//				insertFactoryToHandlerList(conversationHandlerFactory.id(), handlerList);
			}
			if (flagsObj.isTransact()) {
				// TODO: Add transacton handler here
				//				insertFactoryToHandlerList(transactionHandlerFactory.id(), handlerList);
			}
		}
	}
}
