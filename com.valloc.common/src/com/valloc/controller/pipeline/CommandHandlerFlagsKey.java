/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller.pipeline;

import com.valloc.framework.NodeType;

/**
 *
 *
 * @author wstevens
 */
public final class CommandHandlerFlagsKey
{
	private final NodeType nodeType;
	private final String serviceName;
	private final String commandName;
	
	public CommandHandlerFlagsKey(final NodeType nodeType, final String serviceName, final String commandName) {
		this.nodeType = nodeType;
		this.serviceName = serviceName;
		this.commandName = commandName;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + commandName.hashCode();
		result = prime * result + nodeType.hashCode();
		result = prime * result + serviceName.hashCode();
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommandHandlerFlagsKey other = (CommandHandlerFlagsKey) obj;
		if (!commandName.equals(other.commandName))
			return false;
		if (nodeType != other.nodeType)
			return false;
		if (!serviceName.equals(other.serviceName))
			return false;
		return true;
	}

	public NodeType getNodeType() {
		return nodeType;
	}

	public String getServiceName() {
		return serviceName;
	}

	public String getCommandName() {
		return commandName;
	}
}
