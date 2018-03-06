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
public class AgentServerConnectorBase extends AbstractServerConnector<Agent> implements AgentServerConnector
{

	@Override
	public void submitPendingWrite(final FrameworkRequest agentSpecificRequest, final AgentWriteCompleteListener agentWriteCompleteListener) {
	}
}