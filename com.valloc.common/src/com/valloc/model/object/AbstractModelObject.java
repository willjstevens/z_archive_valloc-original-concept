/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.model.object;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.valloc.ChangeType;
import com.valloc.model.DistributedModelObjectId;
import com.valloc.model.ModelObjectChangeListener;
import com.valloc.model.RemoteModelSubscriber;
import com.valloc.model.dto.ModelDto;

/**
 *
 *
 * @author wstevens
 */
public abstract class AbstractModelObject<T> implements ModelObject<T>
{
	private final DistributedModelObjectId id;
	private T subject;
	private Date timestamp;
	private String relationshipKey;
	private final List<ModelObjectChangeListener<T>> modelChangeListeners = new ArrayList<ModelObjectChangeListener<T>>();
	private final List<RemoteModelSubscriber> remoteModelSubscribers = new ArrayList<RemoteModelSubscriber>();
	
	
	public AbstractModelObject(final DistributedModelObjectId id) {
		this.id = id;
	}

	@Override
	public String getRelationshipKey() {
		return relationshipKey;
	}

	@Override
	public void setRelationshipKey(final String relationshipKey) {
		this.relationshipKey = relationshipKey;
	}

	/* (non-Javadoc)
	 * @see com.valloc.model.ModelObject#getSnapshotTimestamp()
	 */
	@Override
	public Date getSnapshotTimestamp() {
		return timestamp;
	}

	/* (non-Javadoc)
	 * @see com.valloc.model.ModelObject#getSubject()
	 */
	@Override
	public T getSubject() {
		return subject;
	}
	
	/* (non-Javadoc)
	 * @see com.valloc.model.ModelObject#setSubject()
	 */
	@Override
	public void setSubject(final T subject) {
		this.subject = subject;
	}

	/* (non-Javadoc)
	 * @see com.valloc.model.ModelObject#setSnapshotTimestamp(java.util.Date)
	 */
	@Override
	public void setSnapshotTimestamp(final Date timestamp) {
		this.timestamp = timestamp;
	}

	/* (non-Javadoc)
	 * @see com.valloc.model.ModelObject#toModelDto()
	 */
	@Override
	public ModelDto toModelDto() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.valloc.Identifiable#id()
	 */
	@Override
	public DistributedModelObjectId id() {
		return id;
	}

	@Override
	public void notifyModelObjectListeners(final ModelObject<T> modelObject, final ChangeType changeType) {
		for (final ModelObjectChangeListener<T> listener : modelChangeListeners) {
			listener.modelObjectChanged(modelObject, changeType);
		}
	}

	@Override
	public void addModelObjectListener(final ModelObjectChangeListener<T> listener) {
		modelChangeListeners.add(listener);
	}

	@Override
	public void removeModelObjectListener(final ModelObjectChangeListener<T> listener) {
		modelChangeListeners.remove(listener);
	}
	
	@Override
	public void notifyRemoteModelSubscribers(final ModelDto modelDto) {
		for (final RemoteModelSubscriber subscriber : remoteModelSubscribers) {
			subscriber.publishModelDto(modelDto);
		}
	}

	@Override
	public void addRemoteModelSubscriber(final RemoteModelSubscriber subscriber) {
		remoteModelSubscribers.add(subscriber);
	}

	@Override
	public void removeRemoteModelSubscriber(final RemoteModelSubscriber subscriber) {
		remoteModelSubscribers.remove(subscriber);
	}
}
