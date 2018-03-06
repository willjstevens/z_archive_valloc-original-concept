/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.model;

import com.valloc.lifecycle.Initializable;

/**
 *
 *
 * @author wstevens
 */
public interface ModelManager extends Initializable
{
	public void addParticipantModelRepository(ParticipantModelRepository participantModelRepository);
	public void removeParticipantModelRepository(ParticipantModelRepository participantModelRepository);
	public ParticipantModelRepository getParticipantModelRepository(DistributedModelObjectId distributedId);
	public void setModelComponentFactory(ModelComponentFactory modelComponentFactory);
	
}
