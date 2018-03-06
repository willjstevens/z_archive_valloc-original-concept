/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.service;

import com.valloc.controller.ServerControllerComponentFactory;
import com.valloc.framework.ServerFrameworkComponentFactory;
import com.valloc.framework.ServerFrameworkManager;
import com.valloc.framework.ServerReferenceKit;

/**
 *
 *
 * @author wstevens
 */
public abstract class AbstractServerServiceFactory<AS extends ServerService> //, S extends Service<ServerFrameworkManager, ServerControllerComponentFactory>
extends AbstractServiceFactory
	<ServerFrameworkManager, 
	ServerFrameworkComponentFactory, 
	ServerControllerComponentFactory, 
	AS, 
	ServerReferenceKit>
//	ServerReferenceKit<AS>>
//<ServerFrameworkManager, ServerFrameworkComponentFactory, ServerControllerComponentFactory, AS, ServerReferenceKit>
{


}
