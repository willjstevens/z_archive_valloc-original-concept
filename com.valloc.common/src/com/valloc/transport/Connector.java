/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transport;

import com.valloc.MessageSummary;
import com.valloc.framework.FrameworkManager;
import com.valloc.framework.FrameworkRequest;
import com.valloc.framework.FrameworkResponse;
import com.valloc.session.Session;

/**
 * A transport mechanism to connect to the another <code>Connector</code> across the network. A connector allows two-way communication for both sides
 * to initiate requests and communication.
 *
 * @author wstevens
 */
public interface Connector extends NettyConnector
{
	public <S extends Session> S getSession();
	public <S extends Session> void setSession(S session);

	/**
	 * A connector on either side of the network can issue a disconnect which the other side will receive a notification to.
	 */
	public MessageSummary disconnect();

	/* Invoked by client code sending the request and waiting/blocking for the response. */
	public void submitSync(FrameworkRequest request, FrameworkResponse response);
	
	/* Invoked by client code sending a request and will return later to getting the response object. */
	public void submitAsync(FrameworkRequest request, FrameworkResponse response);
	
	/* Invoked on server or service-side code when a new request is received. */
	public void requestReceived(FrameworkRequest request, FrameworkResponse response);

	/* Invoked on server or service-side code when a request is done processing and process returns response back out. */
	public void returnResponse(FrameworkResponse response);

	/* Invoked on client code side when the response object has returned from server side. */
	public void responseReceived(FrameworkResponse response);

	public void setFrameworkManager(FrameworkManager frameworkManager);
}
