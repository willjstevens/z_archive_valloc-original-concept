/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.valloc.concurrent.request.RequestContainer;
import com.valloc.controller.AsynchronousClientController;
import com.valloc.controller.AbstractControllerComponentFactory;
import com.valloc.controller.ServiceController;
import com.valloc.interrupt.InterruptCompletionListener;
import com.valloc.interrupt.InterruptFuture;
import com.valloc.interrupt.InterruptTracker;
import com.valloc.interrupt.InterruptType;
import com.valloc.util.UniqueId;
import com.valloc.util.Util;


/**
 *
 *
 * @author wstevens
 */
public abstract class AbstractFrameworkManager implements FrameworkManager
{
	private AbstractControllerComponentFactory controllerComponentFactory;
	private RequestContainer requestContainer;
	private static final Set<FrameworkRequest> activeRequests = new HashSet<FrameworkRequest>();
	private final Map<UniqueId, AsynchronousClientController> asyncControllers = new HashMap<UniqueId, AsynchronousClientController>();
	private final Map<UniqueId, InterruptTracker> interruptTrackers = new HashMap<UniqueId, InterruptTracker>();

	@Override
	public void activateRequest(final FrameworkRequest request) {
		activeRequests.add(request);
	}

	@Override
	public void deactivateRequest(final FrameworkRequest request) {
		activeRequests.remove(request);
	}

	@Override
	public List<FrameworkRequest> getAllActiveRequests() {
		return new ArrayList<FrameworkRequest>(activeRequests);
	}

	@Override
	public InterruptTracker createAndRegisterInterruptTracker(final UniqueId id) {
		final FrameworkManager frameworkManager = this;
		final InterruptTracker tracker = new InterruptTracker(id, frameworkManager);
		interruptTrackers.put(id, tracker);
		return tracker;
	}

	@Override
	public InterruptTracker getInterruptTracker(final UniqueId id) {
		return interruptTrackers.get(id);
	}

	@Override
	public InterruptFuture requestInterrupt(final UniqueId id, final InterruptType interruptType) {
		return requestInterrupt(id, interruptType, null);
	}

	@Override
	public InterruptFuture requestInterrupt(final UniqueId id, final InterruptType interruptType, final InterruptCompletionListener completionListener) {
		final InterruptTracker tracker = interruptTrackers.get(id);
		if (tracker == null) {
			throw new IllegalArgumentException("No existing and known request or interrupt ID to interrupt against.");
		}
		final InterruptFuture future = tracker.requestInterrupt(interruptType, completionListener);
		return future;
	}

	@Override
	public void interruptionComplete(final UniqueId interruptId) {
		interruptTrackers.remove(interruptId);
	}

	@Override
	public void fireRequestReceived(final FrameworkRequest request, final FrameworkResponse response) {
		final ServiceController serviceController = controllerComponentFactory.newServiceController(request, response);
		requestContainer.queueForExecution(serviceController);
	}

	@Override
	public void addAsyncController(final AsynchronousClientController asyncController) {
		final UniqueId requestId = asyncController.getFrameworkRequest().id();
		asyncControllers.put(requestId, asyncController);
	}

	@Override
	public RemoteFrameworkRequest toRemoteFrameworkRequest(final FrameworkRequest frameworkRequest) {
		final Request request = frameworkRequest.getRequest();
		final RemoteFrameworkRequest remoteFrameworkRequest = new RemoteFrameworkRequest(request);
		// these could be null but will be set flat ID values anyway
		remoteFrameworkRequest.setSessionId(Util.toId(frameworkRequest.getSession()));
		remoteFrameworkRequest.setConversationId(Util.toId(frameworkRequest.getConversation()));
		remoteFrameworkRequest.setTransactionId(Util.toId(frameworkRequest.getTransaction()));
		return remoteFrameworkRequest;
	}

	@Override
	public void signalAsyncControllerOfResponse(final FrameworkResponse response) {
		final UniqueId requestId = response.id();
		final AsynchronousClientController asyncController = asyncControllers.remove(requestId);
		asyncController.setFrameworkResponse(response);
		asyncController.signalCompletion();
	}

	@Override
	public void setControllerComponentFactory(final AbstractControllerComponentFactory controllerComponentFactory) {
		this.controllerComponentFactory = controllerComponentFactory;
	}

	@Override
	public void setRequestContainer(final RequestContainer requestContainer) {
		this.requestContainer = requestContainer;
	}
}
