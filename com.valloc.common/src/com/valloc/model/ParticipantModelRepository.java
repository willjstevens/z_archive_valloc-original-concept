/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.model;

import java.util.HashMap;
import java.util.Map;

import com.valloc.Constants;
import com.valloc.Nameable;
import com.valloc.framework.NodeType;
import com.valloc.model.object.ModelObject;

/**
 *
 *
 * @author wstevens
 */
//public class ParticipantModelRepository implements Identifiable<String>
public class ParticipantModelRepository implements Nameable
{
	private final String participantId;
	private final NodeType nodeType;
	private final String id;
	private final Map<String, ModelObject<?>> modelObjects = new HashMap<String, ModelObject<?>>();

	ParticipantModelRepository(final String participantId, final NodeType nodeType) {
		this.participantId = participantId;
		this.nodeType = nodeType;
		id = createId(nodeType, participantId);
	}

	public static String createId(final NodeType nodeType, final String participantId) {
		return nodeType.name().toLowerCase() + Constants.DASH + participantId;
	}

	@Override
	public String name() {
		return id;
	}

	public void addHeadModelObject(final ModelObject<?> modelObject) {
		modelObjects.put(modelObject.getRelationshipKey(), modelObject);
	}

	public void removeHeadModelObject(final ModelObject<?> modelObject) {
		modelObjects.remove(modelObject);
	}

	public <T> ModelObject<T> getHeadModelObject(final String relationshipKey) {
		@SuppressWarnings(Constants.UNCHECKED)
		final ModelObject<T> retval = (ModelObject<T>) modelObjects.get(relationshipKey);
		return retval;
	}

	public String getParticipantId() {
		return participantId;
	}

	public NodeType getParticipantType() {
		return nodeType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		if (!(obj instanceof ParticipantModelRepository)) {
			return false;
		}
		final ParticipantModelRepository other = (ParticipantModelRepository) obj;
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
