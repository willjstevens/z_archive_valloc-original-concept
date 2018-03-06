/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.model;

/**
 *
 *
 * @author wstevens
 */
public interface ModelDao
{
	public ModelManager getModelManager();
	public void setModelManager(ModelManager modelManager);
	public ParticipantModelRepository getParticipantModelRepository();
	public void setParticipantModelRepository(ParticipantModelRepository participantModelRepository);
	
}
