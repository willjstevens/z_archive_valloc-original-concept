/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.session;

import com.valloc.domain.system.Agent;
import com.valloc.framework.FrameworkRequest;
import com.valloc.transport.AgentWriteCompleteListener;

/**
 *
 *
 * @author wstevens
 */
public interface AgentServerSession extends ServerSession<Agent>
{
	public void submitPendingWrite(FrameworkRequest agentSpecificRequest, AgentWriteCompleteListener agentWriteCompleteListener);
}
