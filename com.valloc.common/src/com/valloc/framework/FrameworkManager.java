/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.framework;

import java.util.List;

import com.valloc.concurrent.request.RequestContainer;
import com.valloc.controller.AsynchronousClientController;
import com.valloc.controller.AbstractControllerComponentFactory;
import com.valloc.interrupt.InterruptCompletionListener;
import com.valloc.interrupt.InterruptFuture;
import com.valloc.interrupt.InterruptTracker;
import com.valloc.interrupt.InterruptType;
import com.valloc.util.UniqueId;


/**
 *
 *
 * @author wstevens
 */
public interface FrameworkManager
{
	public void activateRequest(FrameworkRequest request);
	public void deactivateRequest(FrameworkRequest request);
	public List<FrameworkRequest> getAllActiveRequests();
	public void fireRequestReceived(FrameworkRequest request, FrameworkResponse response);

	public void addAsyncController(AsynchronousClientController asyncController);
	public void signalAsyncControllerOfResponse(FrameworkResponse response);
	public RemoteFrameworkRequest toRemoteFrameworkRequest(FrameworkRequest frameworkRequest);

	// registration and query
	public InterruptTracker createAndRegisterInterruptTracker(UniqueId id); 
	public InterruptTracker getInterruptTracker(UniqueId id);
	
	// usage
	public InterruptFuture requestInterrupt(UniqueId interruptId, InterruptType interruptType);	
	public InterruptFuture requestInterrupt(UniqueId interruptId, InterruptType interruptType, InterruptCompletionListener completionListener);
	
	// completion
	public void interruptionComplete(UniqueId interruptId);
	
//	public <I extends UniqueId> void requestInterrupt(I interruptibleId, InterruptType interruptType);
//	public <S extends Interruptible & Service> UniqueId addServiceInterruptible(S service);
//	public void addRequestInterruptible(RequestId requestId, Interruptible interruptible);
//	public void removeRequestInterruptible(RequestId requestId);


//	public void addInterruptListener(InterruptListener<RequestId, String> listener);
//	public void removeInterruptListenersForRequest(RequestId requestId);
//	public void removeInterruptListenerForRequest(RequestId requestId, String listenerTypeId);
//	public void requestInterrupt(RequestId requestId, InterruptType interruptType);

	public void setControllerComponentFactory(AbstractControllerComponentFactory controllerComponentFactory);
	public void setRequestContainer(RequestContainer requestContainer);
}
