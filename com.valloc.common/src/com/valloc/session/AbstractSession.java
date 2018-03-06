package com.valloc.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.SynchronousQueue;

import com.valloc.CategoryType;
import com.valloc.controller.AsynchronousClientController;
import com.valloc.framework.FrameworkManager;
import com.valloc.framework.FrameworkRequest;
import com.valloc.framework.FrameworkResponse;
import com.valloc.interrupt.InterruptHandler;
import com.valloc.interrupt.InterruptHandlerAdapter;
import com.valloc.interrupt.InterruptTracker;
import com.valloc.interrupt.InterruptType;
import com.valloc.localization.Messenger;
import com.valloc.log.LogManager;
import com.valloc.log.Logger;
import com.valloc.model.dto.ModelDto;
import com.valloc.transport.Connector;
import com.valloc.util.Attributes;
import com.valloc.util.Result;
import com.valloc.util.UniqueId;

abstract class AbstractSession implements Session
{
	private static final Logger logger = LogManager.manager().getLogger(AbstractSession.class, CategoryType.SESSION);
	private SessionId id;
	private String username;
	private boolean isValid;
	private final Attributes attributes = new Attributes();
	private Messenger messenger;
	private FrameworkManager frameworkManager;
	private final SynchronousQueue<ModelDto> modelDtosForTransport = new SynchronousQueue<ModelDto>();
	private final Map<UniqueId, RequestTracker> activeRequests = new HashMap<UniqueId, AbstractSession.RequestTracker>();

	private class RequestTracker {
		FrameworkRequest frameworkRequest;
		FrameworkResponse frameworkResponse;
		InterruptHandler interruptHandler;
		RequestTracker(final FrameworkRequest frameworkRequest, final FrameworkResponse frameworkResponse) {
			this.frameworkRequest = frameworkRequest;
			this.frameworkResponse = frameworkResponse;
		}
	}

	@Override
	public Attributes getAttributes() {
		return attributes;
	}

	@Override
	public SessionId id() {
		return id;
	}

	@Override
	public void setId(final SessionId id) {
		this.id = id;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public void setUsername(final String username) {
		this.username = username;
	}

	@Override
	public boolean isValid() {
		return isValid;
	}

	@Override
	public void setIsValid(final boolean isValid) {
		this.isValid = isValid;
	}

	abstract Connector getConnector();

	@Override
	public void disconnect() {
		getConnector().disconnect();
	}

	@Override
	public void submitSync(final FrameworkRequest request, final FrameworkResponse response) {
		final InterruptTracker interruptTracker = request.getInterruptTracker();
		final RequestTracker requestTracker = new RequestTracker(request, response);
		requestTracker.interruptHandler = new InterruptHandlerAdapter(interruptTracker) {
			@Override public void handleInterrupt(final InterruptType interruptType, final Result result) {
				interruptTracker.unsubscribeInterrputHandler(requestTracker.interruptHandler);
				activeRequests.remove(request.id());
				if (logger.isFine()) {
					logger.fine("Blocking submitSync call was interrupted for session %s.", toString());
				}
			}
		};
		activeRequests.put(request.id(), requestTracker);
		interruptTracker.subscribeInterrputHandler(requestTracker.interruptHandler);

		// this is blocking call so wait for return
		getConnector().submitSync(request, response);

		interruptTracker.unsubscribeInterrputHandler(requestTracker.interruptHandler);
		activeRequests.remove(request.id());
	}

	@Override
	public <C extends AsynchronousClientController> void submitAsync(final FrameworkRequest request, final FrameworkResponse response, final C controller) {

		final InterruptTracker interruptTracker = request.getInterruptTracker();
		final RequestTracker requestTracker = new RequestTracker(request, response);
		requestTracker.interruptHandler = new InterruptHandlerAdapter(interruptTracker) {
			@Override public void handleInterrupt(final InterruptType interruptType, final Result result) {
				interruptTracker.unsubscribeInterrputHandler(requestTracker.interruptHandler);
				activeRequests.remove(request.id());
				if (logger.isFine()) {
					logger.fine("Blocking submitAsync call was interrupted for session %s.", toString());
				}
			}
		};
		activeRequests.put(request.id(), requestTracker);
		interruptTracker.subscribeInterrputHandler(requestTracker.interruptHandler);

		frameworkManager.addAsyncController(controller);
		getConnector().submitAsync(request, response);
	}

	@Override
	public void requestReceived(final FrameworkRequest request, final FrameworkResponse response) {
		frameworkManager.fireRequestReceived(request, response);
	}

	@Override
	public void asyncResponseReceived(final FrameworkResponse response) {
		final UniqueId requestId = response.id();
		final RequestTracker requestTracker = activeRequests.get(requestId);
		final InterruptTracker interruptTracker = requestTracker.frameworkRequest.getInterruptTracker();
		interruptTracker.unsubscribeInterrputHandler(requestTracker.interruptHandler);
		activeRequests.remove(requestId);

		frameworkManager.signalAsyncControllerOfResponse(response);
	}

	@Override
	public void returnResponse(final FrameworkResponse response) {
		getConnector().returnResponse(response);
	}

	@Override
	public void queueModelDtoForTransport(final ModelDto modelDto) {
		// using add(), no wait, just drop and go
		modelDtosForTransport.add(modelDto);
	}

	@Override
	public Collection<ModelDto> drainModelDtoQueue() {
		final Collection<ModelDto> dtosForTransport = new ArrayList<ModelDto>();
		modelDtosForTransport.drainTo(dtosForTransport);
		return dtosForTransport;
	}

	@Override
	public Messenger getMessenger() {
		return messenger;
	}

	@Override
	public void setMessenger(final Messenger messenger) {
		this.messenger = messenger;
	}

	@Override
	public void setFrameworkManager(final FrameworkManager frameworkManager) {
		this.frameworkManager = frameworkManager;
	}

	@Override
	public String toString() {
		return id.toString(); // defer to just SessionId toString
	}
}
