/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.framework;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.valloc.Priority;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public class Request extends Message
{
	private final NodeType nodeType;
	private final String serviceName;
	private final String commandName;
	private final CallType callType;
	private final Priority priority;
	private final List<Object> commandArgs = new ArrayList<Object>();
	private final Date startTimestamp;
	private Date endTimestamp;

	public Request(final UniqueId requestId, final NodeType nodeType, final String serviceName, final String commandName, final CallType callType, final Priority priority, final Date startTimestamp) {
		super(requestId);
		this.nodeType = nodeType;
		this.serviceName = serviceName;
		this.commandName = commandName;
		this.callType = callType;
		this.priority = priority;
		this.startTimestamp = startTimestamp;
	}

	public void addCommandArgLast(final Object arg) {
		commandArgs.add(arg);
	}

	public void setEndTimestamp(final Date endTimestamp) {
		this.endTimestamp = endTimestamp;
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

	public CallType getCallType() {
		return callType;
	}

	public Priority getPriority() {
		return priority;
	}

	public List<Object> getCommandArgs() {
		return commandArgs;
	}

	public Date getStartTimestamp() {
		return startTimestamp;
	}

	public Date getEndTimestamp() {
		return endTimestamp;
	}
}
