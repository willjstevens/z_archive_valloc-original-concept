/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transport;

import com.valloc.ApplicationException;
import com.valloc.domain.system.Agent;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public interface AgentWriteCompleteListener
{
	public void writeComplete(UniqueId requestId, Agent agent, ApplicationException applicationException);
}
