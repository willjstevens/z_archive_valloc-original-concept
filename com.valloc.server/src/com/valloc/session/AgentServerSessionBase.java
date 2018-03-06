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
public class AgentServerSessionBase extends AbstractServerSession<Agent> implements AgentServerSession
{

	@Override
	public void submitPendingWrite(final FrameworkRequest agentSpecificRequest, final AgentWriteCompleteListener agentWriteCompleteListener) {
	}

}
