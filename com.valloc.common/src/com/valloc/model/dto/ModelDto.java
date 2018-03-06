/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.model.dto;

import java.util.Date;

import com.valloc.ChangeType;
import com.valloc.Identifiable;
import com.valloc.model.DistributedModelObjectId;

/**
 *
 *
 * @author wstevens
 */
public class ModelDto implements Identifiable<DistributedModelObjectId>
{
	private final DistributedModelObjectId id;
	private final ChangeType changeType;
	private final Date timestamp;
	private final String daoId;
	private final String commandId;

	ModelDto(final DistributedModelObjectId id, final ChangeType changeType, final Date timestamp, final String daoId, final String commandId) {
		this.id = id;
		this.changeType = changeType;
		this.timestamp = timestamp;
		this.daoId = daoId;
		this.commandId = commandId;
	}

	@Override
	public DistributedModelObjectId id() {
		return id;
	}

	public ChangeType getChangeType() {
		return changeType;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getDaoId() {
		return daoId;
	}

	public String getCommandId() {
		return commandId;
	}
	
//	
}
