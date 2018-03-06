/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller;

import com.valloc.framework.FrameworkExecutor;
import com.valloc.framework.FrameworkRequest;
import com.valloc.framework.FrameworkResponse;

/**
 *
 *
 * @author wstevens
 */
public interface ServiceController extends Controller, FrameworkExecutor
{

	public FrameworkRequest getFrameworkRequest();
	public void setFrameworkRequest(FrameworkRequest request);
	public FrameworkResponse getFrameworkResponse();
	public void setFrameworkResponse(FrameworkResponse response);
}
