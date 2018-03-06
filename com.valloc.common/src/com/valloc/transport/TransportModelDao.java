/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transport;

import java.util.HashSet;
import java.util.Set;

import com.valloc.domain.system.Agent;
import com.valloc.model.AbstractModelDao;
import com.valloc.model.object.AgentModelObject;

/**
 *
 *
 * @author wstevens
 */
public class TransportModelDao extends AbstractModelDao
{
	public Set<AgentModelObject> activeAgents = new HashSet<AgentModelObject>(); 
	
	public AgentModelObject loadAgentModelObject(final Agent agent) {
		final AgentModelObject agentModelObject = new AgentModelObject(null);
		activeAgents.add(agentModelObject);
		return agentModelObject;
	}
}
