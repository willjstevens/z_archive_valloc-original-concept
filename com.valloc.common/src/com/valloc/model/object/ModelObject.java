/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.model.object;

import java.util.Date;

import com.valloc.ChangeType;
import com.valloc.Identifiable;
import com.valloc.model.DistributedModelObjectId;
import com.valloc.model.ModelObjectChangeListener;
import com.valloc.model.RemoteModelSubscriber;
import com.valloc.model.dto.ModelDto;

/**
 *
 *
 * @author wstevens
 */
public interface ModelObject<T> extends Identifiable<DistributedModelObjectId>
{
	public String getRelationshipKey();
	public void setRelationshipKey(String relationshipKey);
	public T getSubject();
	public void setSubject(T subject);
	public Date getSnapshotTimestamp();
	public void setSnapshotTimestamp(Date timestamp);
	
	public void notifyModelObjectListeners(ModelObject<T> modelObject, ChangeType changeType);	
	public void addModelObjectListener(ModelObjectChangeListener<T> listener);
	public void removeModelObjectListener(ModelObjectChangeListener<T> listener);
	
	public void notifyRemoteModelSubscribers(ModelDto modelDto);
	public void addRemoteModelSubscriber(RemoteModelSubscriber subscriber);
	public void removeRemoteModelSubscriber(RemoteModelSubscriber subscriber);
	
	public ModelDto toModelDto();	
}
