/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller;

import java.util.LinkedList;

import com.valloc.CommonResources;
import com.valloc.CommonResourcesAdapter;
import com.valloc.DesktopResources;
import com.valloc.controller.pipeline.CommandHandlerFlags;
import com.valloc.controller.pipeline.ControllerPipelineHandlerFactory;
import com.valloc.controller.pipeline.ControllerPipelineProcessor;
import com.valloc.controller.pipeline.DesktopCommandHandlerFlags;
import com.valloc.controller.pipeline.DesktopCommandHandlerFlagsRepository;
import com.valloc.framework.CallType;
import com.valloc.framework.FrameworkRequest;
import com.valloc.framework.FrameworkResponse;
import com.valloc.framework.NodeType;
import com.valloc.interrupt.InterruptTracker;
import com.valloc.service.Service;
import com.valloc.session.DesktopSessionComponentFactory;
import com.valloc.session.Session;
import com.valloc.util.DependencyItem;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public class DesktopControllerComponentFactoryBase extends AbstractControllerComponentFactory implements DesktopControllerComponentFactory
{
	private DesktopCommandHandlerFlagsRepository desktopCommandHandlerFlagsRepository;
	private DesktopResources desktopResources;
	private CommonResourcesAdapter commonResourcesAdapter;

	@Override
	public void initialize() {
		desktopCommandHandlerFlagsRepository = new DesktopCommandHandlerFlagsRepository();
		desktopCommandHandlerFlagsRepository.initialize();

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
	public SynchronousClientController newClientLocalSyncController(final String serviceName, final String commandName, final Service service) {
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
	public AsynchronousClientController newClientLocalAsyncController(final String serviceName, final String commandName, final Service service) {
		//	public AsynchronousClientController newClientLocalAsyncController(final String serviceName, final String commandName, final DesktopService service) {
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
	public SynchronousClientController newClientRemoteSyncController(final String serviceName, final String commandName, final Service service) {
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
	public AsynchronousClientController newClientRemoteAsyncController(final String serviceName, final String commandName, final Service service) {
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

	@Override
	FrameworkRequest newFrameworkRequest(final UniqueId uniqueId, final CallType callType, final String serviceName, final String commandName, final InterruptTracker interruptTracker) {
		final FrameworkRequest frameworkRequest =
			.newFrameworkRequest(uniqueId, NodeType.DESKTOP, serviceName, commandName, callType);
		frameworkRequest.setInterruptTracker(interruptTracker);

		if (callType == CallType.REMOTE_SYNCHRONOUS || callType == CallType.REMOTE_ASYNCHRONOUS) {
			final DesktopSessionComponentFactory desktopSessionComponentFactory = (DesktopSessionComponentFactory) sessionComponentFactory;
			final Session session = desktopSessionComponentFactory.getSessionManager().getServerSession();
			frameworkRequest.setSession(session);
		}

		return frameworkRequest;
	}

	@Override
	void addOptionalHandlers(final NodeType nodeType, final String serviceName, final String commandName, final LinkedList<DependencyItem<String, ControllerPipelineHandlerFactory>> handlerList) {
		final CommandHandlerFlags flagsObj = desktopCommandHandlerFlagsRepository.getCommandHandlerFlags(nodeType, serviceName, commandName);
		if (flagsObj != null) { // could be null if nothing was added
			if (flagsObj instanceof DesktopCommandHandlerFlags) {
				final DesktopCommandHandlerFlags desktopFlagsObj = (DesktopCommandHandlerFlags) flagsObj;
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
	CommonResources getCommonResources() {
		return commonResourcesAdapter;
	}

	@Override
	public DesktopResources getDesktopResources() {
		return desktopResources;
	}

	@Override
	public void setDesktopResources(final DesktopResources desktopResources) {
		this.desktopResources = desktopResources;
		commonResourcesAdapter = new CommonResourcesAdapter(desktopResources);
	}

}
