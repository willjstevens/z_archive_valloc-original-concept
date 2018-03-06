/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transport;

import com.valloc.domain.system.Agent;
import com.valloc.framework.FrameworkRequest;


/**
 *
 *
 * @author wstevens
 */
public interface AgentServerConnector extends ServerConnector<Agent>
{
	public void submitPendingWrite(FrameworkRequest agentSpecificRequest, AgentWriteCompleteListener agentWriteCompleteListener);
}
