/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller;

import java.util.LinkedList;

import com.valloc.controller.pipeline.AgentCommandHandlerFlags;
import com.valloc.controller.pipeline.AgentCommandHandlerFlagsRepository;
import com.valloc.controller.pipeline.CommandHandlerFlags;
import com.valloc.controller.pipeline.ControllerPipelineHandlerFactory;
import com.valloc.controller.pipeline.ControllerPipelineProcessor;
import com.valloc.framework.CallType;
import com.valloc.framework.FrameworkManager;
import com.valloc.framework.FrameworkRequest;
import com.valloc.framework.FrameworkResponse;
import com.valloc.framework.NodeType;
import com.valloc.interrupt.InterruptTracker;
import com.valloc.service.Service;
import com.valloc.session.AgentSessionComponentFactory;
import com.valloc.session.Session;
import com.valloc.util.DependencyItem;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public class AgentControllerComponentFactory extends AbstractControllerComponentFactory
{
	private AgentCommandHandlerFlagsRepository agentCommandHandlerFlagsRepository;

	@Override
	public void initialize() {
		agentCommandHandlerFlagsRepository = new AgentCommandHandlerFlagsRepository();
		agentCommandHandlerFlagsRepository.initialize();

		super.initialize();
	}

	@Override
	void initializeHandlerFactoryMappings() {
		super.initializeHandlerFactoryMappings();
		// desktop specific handler mappings here
	}

	@Override
	void initializeDependencies() {
		super.initializeDependencies();
	}

	@Override
	//	public <CCF extends ControllerComponentFactory> SynchronousClientController newClientLocalSyncController(final String serviceName, final String commandName, final Service<CCF> service) {
	public <S extends Service<? extends FrameworkManager, ? extends AbstractControllerComponentFactory>> SynchronousClientController newClientLocalSyncController(final String serviceName, final String commandName, final S service) {
		final SynchronousClientController controller = new SynchronousClientControllerBase();
		final UniqueId inheritedId = service.id();
		final InterruptTracker interruptTracker = service.getInterruptTracker();
		controller.setService(service);
		controller.setInterruptTracker(interruptTracker);

		final LinkedList<DependencyItem<String, ControllerPipelineHandlerFactory>> handlerList = newHandlerList();
		insertFactoryToHandlerList(frameworkControllerPipelineHandlerFactory.name(), handlerList);
		insertFactoryToHandlerList(serviceInvokerControllerPipelineHandlerFactory.name(), handlerList);

		addOptionalHandlers(NodeType.DESKTOP, serviceName, commandName, handlerList);

		// now build request with accessible information
		final FrameworkRequest frameworkRequest = newFrameworkRequest(inheritedId, CallType.LOCAL_SYNCHRONOUS, serviceName, commandName, interruptTracker);
		final FrameworkResponse frameworkResponse = newFrameworkResponse(inheritedId);

		// now assemble pipeline processor and initialize
		final ControllerPipelineProcessor pipelineProcessor = newControllerPipelineProcessor(handlerList, interruptTracker);
		controller.setControllerPipelineProcessor(pipelineProcessor);
		controller.setFrameworkRequest(frameworkRequest);
		controller.setFrameworkResponse(frameworkResponse);
		pipelineProcessor.initialize();

		return controller;
	}

	@Override
	//	public <CCF extends ControllerComponentFactory> AsynchronousClientController newClientLocalAsyncController(final String serviceName, final String commandName, final Service<CCF> service) {
	public <S extends Service<? extends FrameworkManager, ? extends AbstractControllerComponentFactory>> AsynchronousClientController newClientLocalAsyncController(final String serviceName, final String commandName, final S service) {
		final AsynchronousClientController controller = new AsynchronousClientControllerBase();
		final UniqueId inheritedId = service.id();
		final InterruptTracker interruptTracker = service.getInterruptTracker();
		controller.setService(service);
		controller.setInterruptTracker(interruptTracker);

		final LinkedList<DependencyItem<String, ControllerPipelineHandlerFactory>> handlerList = newHandlerList();
		insertFactoryToHandlerList(frameworkControllerPipelineHandlerFactory.name(), handlerList);
		insertFactoryToHandlerList(localSenderControllerPipelineHandlerFactory.name(), handlerList);

		addOptionalHandlers(NodeType.DESKTOP, serviceName, commandName, handlerList);

		// now build request with accessible information
		final FrameworkRequest frameworkRequest = newFrameworkRequest(inheritedId, CallType.LOCAL_ASYNCHRONOUS, serviceName, commandName, interruptTracker);
		final FrameworkResponse frameworkResponse = newFrameworkResponse(inheritedId);

		// now assemble pipeline processor and initialize
		final ControllerPipelineProcessor pipelineProcessor = newControllerPipelineProcessor(handlerList, interruptTracker);
		controller.setControllerPipelineProcessor(pipelineProcessor);
		controller.setFrameworkRequest(frameworkRequest);
		controller.setFrameworkResponse(frameworkResponse);
		pipelineProcessor.initialize();

		return controller;
	}

	@Override
	//	public <CCF extends ControllerComponentFactory> SynchronousClientController newClientRemoteSyncController(final String serviceName, final String commandName, final Service<CCF> service) {
	public <S extends Service<? extends FrameworkManager, ? extends AbstractControllerComponentFactory>> SynchronousClientController newClientRemoteSyncController(final String serviceName, final String commandName, final S service) {
		final SynchronousClientController controller = new SynchronousClientControllerBase();
		final UniqueId inheritedId = service.id();
		final InterruptTracker interruptTracker = service.getInterruptTracker();
		controller.setService(service);
		controller.setInterruptTracker(interruptTracker);

		final LinkedList<DependencyItem<String, ControllerPipelineHandlerFactory>> handlerList = newHandlerList();
		insertFactoryToHandlerList(frameworkControllerPipelineHandlerFactory.name(), handlerList);
		insertFactoryToHandlerList(remoteSenderControllerPipelineHandlerFactory.name(), handlerList);

		addOptionalHandlers(NodeType.SERVER, serviceName, commandName, handlerList);

		// now build request with accessible information
		final FrameworkRequest frameworkRequest = newFrameworkRequest(inheritedId, CallType.REMOTE_SYNCHRONOUS, serviceName, commandName, interruptTracker);
		final FrameworkResponse frameworkResponse = newFrameworkResponse(inheritedId);

		// now assemble pipeline processor and initialize
		final ControllerPipelineProcessor pipelineProcessor = newControllerPipelineProcessor(handlerList, interruptTracker);
		controller.setControllerPipelineProcessor(pipelineProcessor);
		controller.setFrameworkRequest(frameworkRequest);
		controller.setFrameworkResponse(frameworkResponse);
		pipelineProcessor.initialize();

		return controller;
	}

	@Override
	//	public <CCF extends ControllerComponentFactory> AsynchronousClientController newClientRemoteAsyncController(final String serviceName, final String commandName, final Service<CCF> service) {
	public <S extends Service<? extends FrameworkManager, ? extends AbstractControllerComponentFactory>> AsynchronousClientController newClientRemoteAsyncController(final String serviceName, final String commandName, final S service) {
		final AsynchronousClientController controller = new AsynchronousClientControllerBase();
		final UniqueId inheritedId = service.id();
		final InterruptTracker interruptTracker = service.getInterruptTracker();
		controller.setService(service);
		controller.setInterruptTracker(interruptTracker);

		final LinkedList<DependencyItem<String, ControllerPipelineHandlerFactory>> handlerList = newHandlerList();
		insertFactoryToHandlerList(frameworkControllerPipelineHandlerFactory.name(), handlerList);
		insertFactoryToHandlerList(remoteSenderControllerPipelineHandlerFactory.name(), handlerList);

		addOptionalHandlers(NodeType.SERVER, serviceName, commandName, handlerList);

		// now build request with accessible information
		final FrameworkRequest frameworkRequest = newFrameworkRequest(inheritedId, CallType.REMOTE_ASYNCHRONOUS, serviceName, commandName, interruptTracker);
		final FrameworkResponse frameworkResponse = newFrameworkResponse(inheritedId);

		// now assemble pipeline processor and initialize
		final ControllerPipelineProcessor pipelineProcessor = newControllerPipelineProcessor(handlerList, interruptTracker);
		controller.setControllerPipelineProcessor(pipelineProcessor);
		controller.setFrameworkRequest(frameworkRequest);
		controller.setFrameworkResponse(frameworkResponse);
		pipelineProcessor.initialize();

		return controller;
	}

	@Override
	FrameworkRequest newFrameworkRequest(final UniqueId uniqueId, final CallType callType, final String serviceName, final String commandName, final InterruptTracker interruptTracker) {
		final FrameworkRequest frameworkRequest =
			abstractFrameworkComponentFactory.newFrameworkRequest(uniqueId, NodeType.DESKTOP, serviceName, commandName, callType);
		frameworkRequest.setInterruptTracker(interruptTracker);

		if (callType == CallType.REMOTE_SYNCHRONOUS || callType == CallType.REMOTE_ASYNCHRONOUS) {
			final AgentSessionComponentFactory desktopSessionComponentFactory = (AgentSessionComponentFactory) sessionComponentFactory;
			final Session session = desktopSessionComponentFactory.getSessionManager().getServerSession();
			frameworkRequest.setSession(session);
		}

		return frameworkRequest;
	}

	@Override
	void addOptionalHandlers(final NodeType nodeType, final String serviceName, final String commandName, final LinkedList<DependencyItem<String, ControllerPipelineHandlerFactory>> handlerList) {
		final CommandHandlerFlags flagsObj = agentCommandHandlerFlagsRepository.getCommandHandlerFlags(nodeType, serviceName, commandName);
		if (flagsObj != null) { // could be null if nothing was added
			if (flagsObj instanceof AgentCommandHandlerFlags) {
				final AgentCommandHandlerFlags desktopFlagsObj = (AgentCommandHandlerFlags) flagsObj;
				if (desktopFlagsObj.isPreferences()) {
					// TODO: Add preferences handler here
					//					insertFactoryToHandlerList(preferencesHandlerFactory.id(), handlerList);
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
		pipelineProcessor.initialize();

		return serviceController;
	}
}
