/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller;

import com.valloc.MessageSummary;
import com.valloc.framework.FrameworkRequest;
import com.valloc.framework.FrameworkResponse;
import com.valloc.util.Result;


/**
 *
 *
 * @author wstevens
 */
public abstract interface SingleClientController extends ClientController
{
	public void addArgLast(Object arg);

	public boolean hasReturnValue();
	public Object getReturnValue();
	public MessageSummary getMessageSummary();
	public Result summarizeToResult();

	public FrameworkRequest getFrameworkRequest();
	public void setFrameworkRequest(FrameworkRequest request);
	public FrameworkResponse getFrameworkResponse();
	public void setFrameworkResponse(FrameworkResponse response);
}
