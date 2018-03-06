/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.framework.service;

import com.valloc.concurrent.request.RequestContainer;
import com.valloc.interrupt.InterruptType;
import com.valloc.service.CommonService;
import com.valloc.util.Result;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public interface FrameworkControlService extends CommonService
{
	public Result interruptRequest(UniqueId requestId, InterruptType interruptType);
	public void setRequestContainer(RequestContainer requestContainer);
}
