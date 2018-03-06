/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller.pipeline;

import static com.valloc.framework.NodeType.*;
import static com.valloc.service.CommandDirectory.*;
import static com.valloc.service.ServiceDirectory.*;

import java.util.HashMap;
import java.util.Map;

import com.valloc.framework.NodeType;
import com.valloc.lifecycle.Initializable;

/**
 *
 *
 * @author wstevens
 */
public class CommonCommandHandlerFlagsRepository implements Initializable
{
	static final Map<CommandHandlerFlagsKey, CommandHandlerFlags> commandHandlerFlags = new HashMap<CommandHandlerFlagsKey, CommandHandlerFlags>();
//	static final Map<CommandHandlerFlagsKey, ? super CommandHandlerFlags> commandHandlerFlags = new HashMap<CommandHandlerFlagsKey, CommandHandlerFlags>();
	
	@Override
	public void initialize() {
		for (CommandHandlerFlags flagsObj : commonFlags) {
			commandHandlerFlags.put(flagsObj.id(), flagsObj);
		}
	}
	
	public CommandHandlerFlags getCommandHandlerFlags(final NodeType nodeType, final String serviceName, final String commandName) {
		CommandHandlerFlagsKey key = new CommandHandlerFlagsKey(nodeType, serviceName, commandName);
		return commandHandlerFlags.get(key);
	}
	
	<F extends CommandHandlerFlags> void overrideAndContribute(final F flags) {
		CommandHandlerFlagsKey key = flags.id();
		CommandHandlerFlags commonBasedFlags = commandHandlerFlags.remove(key);
		flags.setConverse(commonBasedFlags.isConverse());
		flags.setTransact(commonBasedFlags.isTransact());
		commandHandlerFlags.put(key, flags);
	}
	
	CommandHandlerFlags newFlags(final NodeType nodeType, final String serviceName, final String commandName) {
		return new CommandHandlerFlags(new CommandHandlerFlagsKey(nodeType, serviceName, commandName));
	}
	
	CommandHandlerFlags[] commonFlags = {
		newFlags(SERVER, USER, LOGIN_DESKTOP).setTransact(true),
		newFlags(SERVER, USER, USER_CREATE_STG_1).setConverse(true).setTransact(true)
	};
	
	
}
