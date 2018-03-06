/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.model;

import com.valloc.framework.NodeType;

/**
 *
 *
 * @author wstevens
 */
public class DistributedModelObjectId
{
	private final String participantId;
	private final NodeType nodeType;
	private final int distributedId;
	
	public DistributedModelObjectId(final String participantId, final NodeType nodeType, final int distributedId) {
		this.participantId = participantId;
		this.nodeType = nodeType;
		this.distributedId = distributedId;
	}

	public String getParticipantId() {
		return participantId;
	}

	public NodeType getParticipantType() {
		return nodeType;
	}

	public int getDistributedId() {
		return distributedId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + distributedId;
		result = prime * result + ((participantId == null) ? 0 : participantId.hashCode());
		result = prime * result + ((nodeType == null) ? 0 : nodeType.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DistributedModelObjectId)) {
			return false;
		}
		final DistributedModelObjectId other = (DistributedModelObjectId) obj;
		if (distributedId != other.distributedId) {
			return false;
		}
		if (participantId == null) {
			if (other.participantId != null) {
				return false;
			}
		} else if (!participantId.equals(other.participantId)) {
			return false;
		}
		if (nodeType == null) {
			if (other.nodeType != null) {
				return false;
			}
		} else if (!nodeType.equals(other.nodeType)) {
			return false;
		}
		return true;
	}
}
