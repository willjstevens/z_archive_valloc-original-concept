/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.session;

import java.util.Collection;

import com.valloc.Identifiable;
import com.valloc.controller.AsynchronousClientController;
import com.valloc.framework.FrameworkManager;
import com.valloc.framework.FrameworkRequest;
import com.valloc.framework.FrameworkResponse;
import com.valloc.localization.Messenger;
import com.valloc.model.dto.ModelDto;
import com.valloc.util.Attributes;

/**
 *
 *
 * @author wstevens
 */
public abstract interface Session extends Identifiable<SessionId>
{
	void setId(SessionId id);
	public String getUsername();
	void setUsername(String username);
	public Attributes getAttributes();
	public boolean isValid();
	public void setIsValid(boolean isValid);
	public void disconnect();
	public Collection<ModelDto> drainModelDtoQueue();
	public void queueModelDtoForTransport(ModelDto modelDto);

	/* Invoked by client code sending the request and waiting/blocking for the response. */
	public void submitSync(FrameworkRequest request, FrameworkResponse response);

	/* Invoked by client code sending a request and will return later to getting the response object. */
	public <C extends AsynchronousClientController> void submitAsync(FrameworkRequest request, FrameworkResponse response, C controller);

	/* Invoked on server or service-side code when a new request is received. */
	public void requestReceived(FrameworkRequest request, FrameworkResponse response);

	/* Invoked on server or service-side code when a request is done processing and process returns response back out. */
	public void returnResponse(FrameworkResponse response);

	/* Invoked on client code side when the response object has returned from server side. */
	public void asyncResponseReceived(FrameworkResponse response);

	public Messenger getMessenger();
	public void setMessenger(Messenger messenger);
	public void setFrameworkManager(FrameworkManager frameworkManager);
}
