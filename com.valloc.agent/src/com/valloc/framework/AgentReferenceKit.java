/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.framework;

import com.valloc.controller.AgentControllerComponentFactory;
import com.valloc.session.AgentSessionManager;

/**
 *
 *
 * @author wstevens
 */
//public class AgentReferenceKit extends ReferenceKit
//public class AgentReferenceKit extends ReferenceKit<AgentFrameworkManager, AgentFrameworkComponentFactory, AgentControllerComponentFactory, AgentService>
//public class AgentReferenceKit<S extends Service<AgentFrameworkManager, AgentControllerComponentFactory>> 
//extends ReferenceKit<AgentFrameworkManager, AgentFrameworkComponentFactory, AgentControllerComponentFactory, S>
public class AgentReferenceKit 
extends ReferenceKit<AgentFrameworkManager, AgentFrameworkComponentFactory, AgentControllerComponentFactory>
{
	public AgentControllerComponentFactory controllerComponentFactory;
	public AgentSessionManager sessionManager;
	public AgentFrameworkManager frameworkManager;
	public AgentFrameworkComponentFactory agentFrameworkComponentFactory;
}
