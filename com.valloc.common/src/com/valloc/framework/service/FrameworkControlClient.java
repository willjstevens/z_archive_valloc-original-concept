/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.framework.service;

import com.valloc.interrupt.InterruptType;
import com.valloc.service.CommonService;
import com.valloc.util.Result;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public interface FrameworkControlClient extends CommonService
{
	public Result interruptLocalRequest(UniqueId requestId, InterruptType interruptType);
	public Result interruptRemoteRequest(UniqueId requestId, InterruptType interruptType);
}
