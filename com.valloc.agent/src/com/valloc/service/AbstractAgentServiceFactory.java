/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.service;

import com.valloc.controller.AgentControllerComponentFactory;
import com.valloc.framework.AgentFrameworkComponentFactory;
import com.valloc.framework.AgentFrameworkManager;
import com.valloc.framework.AgentReferenceKit;

/**
 *
 *
 * @author wstevens
 */
public abstract class AbstractAgentServiceFactory<AS extends AgentService> //, S extends Service<AgentFrameworkManager, AgentControllerComponentFactory>
extends AbstractServiceFactory
	<AgentFrameworkManager, 
	AgentFrameworkComponentFactory, 
	AgentControllerComponentFactory, 
	AS, 
	AgentReferenceKit>
//	AgentReferenceKit<AS>>
//<AgentFrameworkManager, AgentFrameworkComponentFactory, AgentControllerComponentFactory, AS, AgentReferenceKit>
{


}
