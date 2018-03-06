/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller;

import com.valloc.MessageSummary;
import com.valloc.domain.system.ClientNode;
import com.valloc.framework.FrameworkRequest;
import com.valloc.framework.FrameworkResponse;
import com.valloc.framework.MultiClientRequest;


/**
 *
 *
 * @author wstevens
 */
public abstract interface MultiClientController<C extends ClientNode> extends ClientController
{
	public void addArgLast(C clientParticipant, Object arg);

	public boolean hasReturnValue(C client);
	public Object getReturnValue(C client);
	public MessageSummary getMessageSummary(C client);

	public MultiClientRequest<C> getMultiClientRequest();
	public void setMultiClientRequest(MultiClientRequest<C> multiClientRequest);
	public FrameworkRequest getClientFrameworkRequest(C client);
	public FrameworkResponse getFrameworkResponse(C client);
}


/*
public abstract interface MultiClientController<C extends ClientNode> extends ClientController
{
	public void addArgLast(C clientParticipant, Object arg);

	public boolean hasReturnValue(C client);
	public Object getReturnValue(C client);
	public MessageSummary getMessageSummary(C client);

	public MultiClientRequest<C> getMultiClientRequest();
	public void setMultiClientRequest(MultiClientRequest<C> multiClientRequest);
	public FrameworkRequest getClientFrameworkRequest(C client);
	public FrameworkResponse getFrameworkResponse(C client);
}

public abstract interface MultiClientController extends ClientController
{
	public void addArgLast(ClientNode clientNode, Object arg);

	public boolean hasReturnValue(ClientNode client);
	public Object getReturnValue(ClientNode client);
	public MessageSummary getMessageSummary(ClientNode client);

	public MultiClientRequest<ClientNode> getMultiClientRequest();
	public void setMultiClientRequest(MultiClientRequest<ClientNode> multiClientRequest);
	public FrameworkRequest getClientFrameworkRequest(ClientNode client);
	public FrameworkResponse getFrameworkResponse(ClientNode client);
}

*/
