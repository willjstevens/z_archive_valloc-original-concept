/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller.pipeline;

import static com.valloc.framework.NodeType.*;
import static com.valloc.service.CommandDirectory.*;
import static com.valloc.service.ServiceDirectory.*;

import com.valloc.framework.NodeType;

/**
 *
 *
 * @author wstevens
 */
public class DesktopCommandHandlerFlagsRepository extends CommonCommandHandlerFlagsRepository
{

	@Override
	public void initialize() {
		super.initialize();
		
		for (DesktopCommandHandlerFlags desktopFlagsObj : serverFlags) {
			CommandHandlerFlagsKey key = desktopFlagsObj.id();
			if (!commandHandlerFlags.containsKey(key)) {
				// here no previous object exists so just put server-exclusive flags object in...
				commandHandlerFlags.put(key, desktopFlagsObj);
			} else { // else there is already a commonly shared flags object which needs to be 
					 //	wrapped/decorated with the new server exclusive flags
				// TODO: Should this be decorator pattern instead of transferring values plain old way??
				overrideAndContribute(desktopFlagsObj);
			}			
		}
	}
	
	@Override
	DesktopCommandHandlerFlags newFlags(final NodeType nodeType, final String serviceName, final String commandName) {
		return new DesktopCommandHandlerFlags(new CommandHandlerFlagsKey(nodeType, serviceName, commandName));
	}
	 
	DesktopCommandHandlerFlags[] serverFlags = {
		newFlags(SERVER, USER, LOGIN_DESKTOP).setPreferences(true)
	};
}
